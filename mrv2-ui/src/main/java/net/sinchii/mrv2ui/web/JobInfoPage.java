package net.sinchii.mrv2ui.web;

import java.io.PrintWriter;

import net.sinchii.mrv2ui.JSONUtil;
import net.sinchii.mrv2ui.MRv2JobInfo;

public class JobInfoPage {

  private final static String TITLE = "Job Summary";

  private HTMLPage page;
  private JSONUtil json;
  private String str;
  
  public JobInfoPage(PrintWriter writer, String str) {
    page = new HTMLPage(writer);
    json = new JSONUtil();
    this.str = str;
    json.setEvent(str);
    render();
  }
  
  public void render() {
    MRv2JobInfo info = json.getMRv2JobInfo();
    page.output(HTMLPage.DOCTYPE);
    page.html().head().meta_http("X-UA-Compatible", "IE=8")
      .meta_http("Content-type", "text/html; charset=UTF-8")
      .meta_http("Content-Style-Type", "text/css")
      .title(TITLE + " : " + info.getJobId());
    page.link("stylesheet", "text/css",
        "static/jquery/jquery-ui.css");
    page.link("stylesheet", "text/css",
        "static/jquery/jquery.dataTables.css");
    page._("head").body();
    
    // Job Info table
    page.h3(info.getJobId());
    
    long elapsed = info.getFinishTime() - info.getStartTime();
    page.table("JobInfo", "display").thead()
      .tr().th("MapReduce Job Information")._("tr")._("thead");
    page.tbody()
      .tr().th("MapReduce Job ID").td(info.getJobId())._("tr")
      .tr().th("MapReduce Job Name").td(info.getJobName())._("tr")
      .tr().th("User").td(info.getUserName())._("tr")
      .tr().th("Status").td(info.getJobStatus())._("tr")
      .tr().th("Queue").td(info.getQueueName())._("tr")
      .tr().th("Started")
        .td(HTMLPage.getDispayDate(info.getStartTime()))._("tr")
      .tr().th("Finished")
        .td(HTMLPage.getDispayDate(info.getFinishTime()))._("tr")
      .tr().th("Elapsed").td(HTMLPage.getElapsedTime(elapsed))._("tr")
      .tr().th("Map tasks")
        .td(Integer.toString(info.getMapTasks()))._("tr")
      .tr().th("Reduce tasks")
        .td(Integer.toString(info.getReduceTasks()))._("tr")
      ;
    page._("tbody")._("table");
    
    // Counter table
    page.h3("Counter Information");
    page.table();
    page._("table")._("body")._("html");
  }
}
