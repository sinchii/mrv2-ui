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

package net.sinchii.mrv2ui.web;

import java.io.PrintWriter;

import net.sinchii.mrv2ui.JSON;
import net.sinchii.mrv2ui.dao.JobInfo;

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
    page.script("text/javascript", HTMLPage.JQMINJS)._("script");
    page.script("text/javascript", HTMLPage.JQDTMINJS)._("script");
    page.script("text/javascript", HTMLPage.DTJQMINJS)._("script");
    page.link("stylesheet", HTMLPage.JQUICSS);
    page.link("stylesheet", HTMLPage.DTJQUIMINCSS);
    page.script().dataTable(TABLENAME)._("script");
    page._("head").body();
    
    page.h3("MapReduce Job List");
    
    page.table(TABLENAME, TABLECLASS).thead().tr()
      .th("jobid", "Job ID").th("jobname", "Job Name").th("user", "User")
      .th("submittime", "Submit Time").th("startime", "Start Time")
      .th("finsihtime", "Finish Time").th("elapsedtime", "Elapsed Time")
      .th("maptasks", "Map Tasks")
      .th("reducetasks", "Reduce Tasks")._("tr")._("thead");
    page.tbody();
    for (int i = 0; i < json.getNumEvents(); i++) {
      JobInfo info = json.getMRv2JobInfo(i);
      long elapsed = info.getFinishTime() - info.getStartTime();
      page.tr()
        .td().a("/mrv2-ui/m/" + info.getJobId(), info.getJobId())._("td")
        .td(info.getJobName()).td(info.getUserName())
        .td(HTMLPage.getDisplayDate(info.getSubmitTime()))
        .td(HTMLPage.getDisplayDate(info.getStartTime()))
        .td(HTMLPage.getDisplayDate(info.getFinishTime()))
        .td(HTMLPage.getElapsedTime(elapsed))
        .td(Integer.toString(info.getMapTasks()))
        .td(Integer.toString(info.getReduceTasks()))._("tr");
    }
    page._("tbody")._("table");
    page._("body")._("html");
  }
}
