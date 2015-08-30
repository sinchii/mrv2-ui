package net.sinchii.mrv2ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sinchii.mrv2ui.web.ErrorPage;
import net.sinchii.mrv2ui.web.JobInfoPage;
import net.sinchii.mrv2ui.web.MainPage;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@WebServlet(name="MRv2UIMain", urlPatterns={"/m/*"})
public class MRv2UIMain extends HttpServlet {

  private String initPath;
  private static final String TLPATH = "/ws/v1/timeline/MAPREDUCE_JOB";
  
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    
    init();
    res.setContentType("text/html; charset=UTF-8");
    
    PrintWriter writer = res.getWriter();
    contentsHandler(req.getPathInfo(), writer);
    writer.close();
  }
  
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doGet(req, res);
  }
  
  private void contentsHandler(String path, PrintWriter writer) {
    if (path == null) {
      String result = getTLSRest(initPath, writer);
      if (result != null) {
        new MainPage(writer, result);
      }
    } else if (path.startsWith("/job_")) {
      String result = getTLSRest(initPath + path, writer);
      if (result != null) {
        new JobInfoPage(writer, result);
      }
    }
  }
  
  public void init() {
    String tlsAddress = getServletConfig().getServletContext()
        .getInitParameter(MRv2UIConfig.TLS_HTTP_ADDRESS);
    if (tlsAddress == null) {
      tlsAddress = MRv2UIConfig.DEFAULT_TLS_HTTP_ADDRESS;
    }
    initPath = tlsAddress + TLPATH;
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
      writer.println(initPath);
      return null;
    }
    return str;
  }
  
}
