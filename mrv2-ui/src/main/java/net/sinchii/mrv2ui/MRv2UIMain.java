/**
 * Copyright (c) 2015 Shinichi YAMASHITA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sinchii.mrv2ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sinchii.mrv2ui.dao.JobInfo;
import net.sinchii.mrv2ui.web.ErrorPage;
import net.sinchii.mrv2ui.web.JobInfoCSV;
import net.sinchii.mrv2ui.web.JobInfoPage;
import net.sinchii.mrv2ui.web.MainPage;
import net.sinchii.mrv2ui.web.TaskInfoPage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@WebServlet(name="MRv2UIMain", urlPatterns={"/m/*"})
public class MRv2UIMain extends HttpServlet {

  private String tlsAddress;
  private static final String TLJPATH = "/ws/v1/timeline/MAPREDUCE_JOB";
  private static final String TLTPATH = "/ws/v1/timeline/MAPREDUCE_TASK";
  
  private static final String JOBID = "JOBID";
  private static final String WINDOWSTART = "windowStart";
  private static final String WINDOWEND = "windowEnd";
  private static final String TOTALTASKS = "totalTasks";
  
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    
    init();
    res.setContentType("text/html; charset=UTF-8");
    
    PrintWriter writer = res.getWriter();
    contentsHandler(req, writer);
    writer.close();
  }
  
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doGet(req, res);
  }
  
  private void contentsHandler(HttpServletRequest req, PrintWriter writer) {
    String path = req.getPathInfo();
    
    if (path == null || path.equals("/")) {
      String result = getTLSRest(tlsAddress + TLJPATH, writer);
      if (result != null) {
        JSON json = new JSON(result);
        new MainPage(writer, json);
      }
    } else if (path.startsWith("/job_")) {
      String result = getTLSRest(tlsAddress + TLJPATH + path, writer);
      if (result != null) {
        JSON json = new JSON(result);
        HttpSession session = req.getSession();
        session.setAttribute(JOBID,
            json.getMRv2JobInfo().getJobId());
        session.setAttribute(WINDOWSTART,
            Long.toString(json.getMRv2JobInfo().getSubmitTime()));
        session.setAttribute(WINDOWEND,
            Long.toString(json.getMRv2JobInfo().getFinishTime()));
        Integer totalTasks =
            json.getMRv2JobInfo().getMapTasks() + json.getMRv2JobInfo().getReduceTasks();
        session.setAttribute(TOTALTASKS, totalTasks);
        new JobInfoPage(writer, json);
      }
    } else if (path.startsWith("/task_")) {
      String windowQuery = "";
      String sessionJobId = "";
      Long startTime = Long.MIN_VALUE;
      Long finishTime = Long.MAX_VALUE;
      Integer totalTasks = -1;
      HttpSession session = req.getSession();
      if (session != null) {
        sessionJobId = (String) session.getAttribute(JOBID);
        startTime =
            Long.parseLong((String) session.getAttribute(WINDOWSTART));
        finishTime =
            Long.parseLong((String) session.getAttribute(WINDOWEND));
        totalTasks = (Integer) session.getAttribute(TOTALTASKS);
        windowQuery = setWindowQuery(startTime, finishTime);
      } else {
        sessionJobId = path.substring(5);
        String result =
            getTLSRest(tlsAddress + TLJPATH + sessionJobId, writer);
        if (result == null) {
          return;
        }
        JSON jobJson = new JSON(result);
        JobInfo info = jobJson.getMRv2JobInfo();
        totalTasks = info.getMapTasks() + info.getReduceTasks();
        startTime = info.getSubmitTime();
        finishTime = info.getFinishTime();
        windowQuery = setWindowQuery(startTime, finishTime);
      }
      String idFilter = sessionJobId.substring(4);
      JSON json = null;
      boolean success = false;
      while (startTime <= finishTime) {
        String result = getTLSRest(tlsAddress + TLTPATH + windowQuery, writer);
        if (result == null) {
          break;
        }
        if (json == null) {
          json = new JSON(result, sessionJobId);
          json.setTaskEntities(idFilter);
        } else {
          json.addTaskEntities(result, idFilter);
        }
        if (totalTasks == json.getNumEvents()) {
          success = true;
          break;
        }
        finishTime = json.getTaskMinimumTime();
        windowQuery = setWindowQuery(startTime, finishTime);
      }
      if (!success) {
        new ErrorPage(writer, HttpStatus.SC_INTERNAL_SERVER_ERROR);
      } else {
        new TaskInfoPage(writer, json);
      }
    } else if (path.startsWith("/csv")) {
      String result = getTLSRest(tlsAddress + TLJPATH, writer);
      if (result != null) {
        JSON json = new JSON(result);
        new JobInfoCSV(writer, json);
      }
    }
  }
  
  public void init() {
    tlsAddress = getServletConfig().getServletContext()
        .getInitParameter(MRv2UIConfig.TLS_HTTP_ADDRESS);
    if (tlsAddress == null) {
      tlsAddress = MRv2UIConfig.DEFAULT_TLS_HTTP_ADDRESS;
    }
  }
  
  private String getTLSRest(String uri, PrintWriter writer) {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(uri);

    String str = null;
    try {
      CloseableHttpResponse response = client.execute(httpGet);
      int code = response.getStatusLine().getStatusCode();
      if (code != HttpStatus.SC_OK) {
        System.out.println(uri);
        new ErrorPage(writer, code);
        return null;
      }
      HttpEntity entity = response.getEntity();
      str = EntityUtils.toString(entity);
    } catch (Exception e) {
      e.printStackTrace();
      new ErrorPage(writer, HttpStatus.SC_INTERNAL_SERVER_ERROR);
      return null;
    }
    return str;
  }

  private String setWindowQuery(Long start, Long finish) {
    return "?windowStart=" + start + "&windowEnd=" + finish;
  }
}
