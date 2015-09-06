package net.sinchii.mrv2ui.web;

import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Map;

import net.sinchii.mrv2ui.JSON;
import net.sinchii.mrv2ui.MRv2JobCounterInfo.Value;
import net.sinchii.mrv2ui.MRv2JobInfo;

public class JobInfoPage {

  private final static String TITLE = "Job Summary";

  private HTMLPage page;
  private JSON json;
  
  public JobInfoPage(PrintWriter writer, JSON json) {
    page = new HTMLPage(writer);
    this.json = json;
    render();
  }
  
  public void render() {
    MRv2JobInfo info = json.getMRv2JobInfo();
    page.output(HTMLPage.DOCTYPE);
    page.html().head().meta_http("X-UA-Compatible", "IE=8")
      .meta_http("Content-type", "text/html; charset=UTF-8")
      .meta_http("Content-Style-Type", "text/css")
      .title(TITLE + " : " + info.getJobId());
    page.link("stylesheet", "/mrv2-ui/static/jquery/jquery-ui.css");
    page.link("stylesheet", "/mrv2-ui/static/jquery/jquery.dataTables.css");
    page._("head").body();
    
    // Job Info table
    page.h3(info.getJobId());
    
    long elapsed = info.getFinishTime() - info.getStartTime();
    page.table("JobInfo", "ui-widget-content").thead()
      .tr().th("MapReduce Job Information")._("tr")._("thead");
    
    page.tbody()
      .tr().th("MapReduce Job ID").td(info.getJobId())._("tr")
      .tr().th("MapReduce Job Name").td(info.getJobName())._("tr")
      .tr().th("User").td(info.getUserName())._("tr")
      .tr().th("Status").td(info.getJobStatus())._("tr")
      .tr().th("Queue").td(info.getQueueName())._("tr")
      .tr().th("Started")
        .td(HTMLPage.getDisplayDate(info.getStartTime()))._("tr")
      .tr().th("Finished")
        .td(HTMLPage.getDisplayDate(info.getFinishTime()))._("tr")
      .tr().th("Elapsed").td(HTMLPage.getElapsedTime(elapsed))._("tr")
      .tr().th("Map tasks")
        .td().a("/mrv2-ui/m/task_" + info.getJobId(),
            Integer.toString(info.getMapTasks()))._("td")._("tr")
      .tr().th("Reduce tasks")
        .td().a("/mrv2-ui/m/task_" + info.getJobId(),
            Integer.toString(info.getReduceTasks()))._("td")._("tr")
      ;
    page._("tbody")._("table");
    
    // Counter table
    page.h3("Counter Information");
    page.table("CounterInfo", "ui-widget-content").thead()
      .tr().th("Group Name").th("Name").th("MAP").th("REDUCE").th("TOTAL")
      ._("thead");
    page.tbody();
    NumberFormat nf = NumberFormat.getNumberInstance();
    Map<String, Map<String, Value>> counters = info.getCounterInfo().getCounters();
    for (String gkey : counters.keySet()) {
      Map<String, Value> counter = counters.get(gkey);
      for (String key : counter.keySet()) {
        
        page.tr().th(gkey).th(key)
          .td(nf.format(counter.get(key).getMapValue()))
          .td(nf.format(counter.get(key).getReduceValue()))
          .td(nf.format(counter.get(key).getTotalValue()))
          ._("tr");
      }
    }
    page._("tbody");
    page._("table")._("body")._("html");
  }
}
