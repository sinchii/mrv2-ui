package net.sinchii.mrv2ui.web;

import java.io.PrintWriter;

import net.sinchii.mrv2ui.JSON;
import net.sinchii.mrv2ui.MRv2TaskInfo;

public class TaskInfoPage {

  private final static String TITLE = "Task Summary";
  private final static String TABLENAME = "taskList";
  private final static String TABLECLASS = "display";
  
  private HTMLPage page;
  private JSON json;
  
  public TaskInfoPage(PrintWriter writer, JSON json) {
    page = new HTMLPage(writer);
    this.json = json;
    this.json.setEntities();
    render();
  }
  
  private void render() {
    String jobId = json.getSessionJobId();
    page.output(HTMLPage.DOCTYPE);
    page.html().head().meta_http("X-UA-Compatible", "IE=8")
      .meta_http("Content-type", "text/html; charset=UTF-8")
      .meta_http("Content-Style-Type", "text/css")
      .title(TITLE + " : " + jobId);
    page.script("text/javascript",
        "/mrv2-ui/static/jquery/jquery-1.11.3.min.js")._("script");
    page.script("text/javascript",
        "/mrv2-ui/static/jquery/jquery-ui.min.js")._("script");
    page.script("text/javascript",
        "/mrv2-ui/static/jquery/jquery.dataTables.min.js")._("script");
    page.link("stylesheet", "/mrv2-ui/static/jquery/jquery-ui.css");
    page.link("stylesheet", "/mrv2-ui/static/jquery/jquery.dataTables.css");
    page.script().dataTable(TABLENAME)._("script");
    page._("head").body();
    
    page.h3(jobId + " Task List");
    
    page.table(TABLENAME, TABLECLASS).thead().tr()
      .th("taskid", "Task ID").th("starttime", "Start Time")
      .th("finishtime", "Finish Time")._("tr")._("thead");
    page.tbody();
    String id = jobId.substring(4);
    for (int i = 0; i < json.getNumEvents(); i++) {
      MRv2TaskInfo info = json.getMRv2TaskInfo(i, id);
      if (info != null) {
        page.tr()
          .td(info.getTaskId())
          .td(HTMLPage.getDisplayDate(info.getStartTime()))
          .td(HTMLPage.getDisplayDate(info.getFinishTime()))._("tr");
      }
    }
    page._("tbody")._("table")._("body")._("html");
  }
}
