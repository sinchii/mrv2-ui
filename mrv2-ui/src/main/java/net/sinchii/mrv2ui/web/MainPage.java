package net.sinchii.mrv2ui.web;

import java.io.PrintWriter;

import net.sinchii.mrv2ui.JSON;
import net.sinchii.mrv2ui.MRv2JobInfo;

public class MainPage {

  private final static String TITLE = "Main Page";
  private final static String TABLENAME = "jobList";
  private final static String TABLECLASS = "display";
  
  private HTMLPage page;
  private JSON json;
  
  public MainPage(PrintWriter writer, JSON json) {
    page = new HTMLPage(writer);
    this.json = json;
    json.setEntities();
    render();
  }
  
  public void render() {
    page.output(HTMLPage.DOCTYPE);
    page.html().head().meta_http("X-UA-Compatible", "IE=8")
      .meta_http("Content-type", "text/html; charset=UTF-8")
      .title(TITLE);
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
    
    page.h3("MapReduce Job List");
    
    page.table(TABLENAME, TABLECLASS).thead().tr()
      .th("jobid", "Job ID").th("jobname", "Job Name").th("user", "User")
      .th("submittime", "Submit Time").th("startime", "Start Time")
      .th("finsihtime", "Finish Time").th("maptasks", "Map Tasks")
      .th("reducetasks", "Reduce Tasks")._("tr")._("thead");
    page.tbody();
    for (int i = 0; i < json.getNumEvents(); i++) {
      MRv2JobInfo info = json.getMRv2JobInfo(i);
      page.tr()
        .td().a("/mrv2-ui/m/" + info.getJobId(), info.getJobId())._("td")
        .td(info.getJobName()).td(info.getUserName())
        .td(HTMLPage.getDisplayDate(info.getSubmitTime()))
        .td(HTMLPage.getDisplayDate(info.getStartTime()))
        .td(HTMLPage.getDisplayDate(info.getFinishTime()))
        .td(Integer.toString(info.getMapTasks()))
        .td(Integer.toString(info.getReduceTasks()))._("tr");
    }
    page._("tbody")._("table")._("body")._("html");
  }
}
