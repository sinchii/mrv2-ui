package net.sinchii.mrv2ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sinchii.mrv2ui.web.MainPage;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@WebServlet(name="MRv2UIMain", urlPatterns={"/mrv2-ui"})
public class MRv2UIMain extends HttpServlet {

  private final static String TLS_HTTP_ADDRESS =
      "http://192.168.73.133:8188/ws/v1/timeline/MAPREDUCE_JOB/";
  
  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    
    res.setContentType("text/html; charset=UTF-8");
    
    PrintWriter writer = res.getWriter();
    mainInfo(writer);
    writer.close();
  }
  
  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doGet(req, res);
  }
  
  private void mainInfo(PrintWriter writer) {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(TLS_HTTP_ADDRESS);

    String str = null;
    try {
      CloseableHttpResponse response = client.execute(httpGet);
      HttpEntity entity = response.getEntity();
      str = EntityUtils.toString(entity);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    new MainPage(writer, str);
  }

}
