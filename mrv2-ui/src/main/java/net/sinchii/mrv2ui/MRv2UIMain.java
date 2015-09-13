package net.sinchii.mrv2ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sinchii.mrv2ui.web.ErrorPage;
import net.sinchii.mrv2ui.web.JobInfoPage;
import net.sinchii.mrv2ui.web.MainPage;
import net.sinchii.mrv2ui.web.TaskInfoPage;

import org.apache.http.HttpEntity;
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
        new JobInfoPage(writer, json);
      }
    } else if (path.startsWith("/task_")) {
      //TODO
      String windowQuery = "";
      String sessionJobId = "";
      HttpSession session = req.getSession();
      if (session != null) {
        sessionJobId = (String) session.getAttribute(JOBID);
        String wStart = (String) session.getAttribute(WINDOWSTART);
        String wEnd = (String) session.getAttribute(WINDOWEND);
        windowQuery = "?windowStart=" + wStart + "&windowEnd=" + wEnd;
      } else {
        sessionJobId = path.substring(5);
      }
      
      String result = getTLSRest(tlsAddress + TLTPATH + windowQuery, writer);
      if (result != null) {
        JSON json = new JSON(result, sessionJobId);
        new TaskInfoPage(writer, json);
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
      if (code != 200) {
        System.out.println(uri);
        new ErrorPage(writer, code);
        return null;
      }
      HttpEntity entity = response.getEntity();
      str = EntityUtils.toString(entity);
    } catch (Exception e) {
      e.printStackTrace();
      new ErrorPage(writer, 500);
      return null;
    }
    return str;
  }
}
