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
        new JobInfoPage(writer, json);
      }
    } else if (path.startsWith("/task_")) {
      String jobId = path.substring(6);
      String result = getTLSRest(tlsAddress + TLJPATH + "/" + jobId, writer);
      if (result == null) {
        return;
      }
      JSON jobJson = new JSON(result);
      JobInfo info = jobJson.getMRv2JobInfo();
      Integer totalTasks = info.getMapTasks() + info.getReduceTasks();
      Long startTime = info.getSubmitTime();
      Long finishTime = info.getFinishTime();
      String windowQuery = setWindowQuery(startTime, finishTime);
      
      String idFilter = jobId.substring(4);
      JSON json = null;
      boolean success = false;
      while (startTime <= finishTime) {
        String taskResult = getTLSRest(tlsAddress + TLTPATH + windowQuery, writer);
        if (taskResult == null) {
          break;
        }
        if (json == null) {
          json = new JSON(taskResult, jobId);
          json.setTaskEntities(idFilter);
        } else {
          json.addTaskEntities(taskResult, idFilter);
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
