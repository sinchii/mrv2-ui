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
import net.sinchii.mrv2ui.dao.TaskInfo;

public class TaskInfoPage {

  private final static String TITLE = "Task Summary";
  private final static String TABLENAME = "taskList";
  private final static String TABLECLASS = "display";
  
  private HTMLPage page;
  private JSON json;
  
  public TaskInfoPage(PrintWriter writer, JSON json) {
    page = new HTMLPage(writer);
    this.json = json;
    render();
  }
  
  private void render() {
    String jobId = json.getSessionJobId();
    String id = jobId.substring(4);
    
    page.output(HTMLPage.DOCTYPE);
    page.html().head().meta_http("X-UA-Compatible", "IE=8")
      .meta_http("Content-type", "text/html; charset=UTF-8")
      .meta_http("Content-Style-Type", "text/css")
      .title(TITLE + " : " + jobId);
    page.script("text/javascript", HTMLPage.JQMINJS)._("script");
    page.script("text/javascript", HTMLPage.JQDTMINJS)._("script");
    page.script("text/javascript", HTMLPage.DTJQMINJS)._("script");
    page.link("stylesheet", HTMLPage.JQUICSS);
    page.link("stylesheet", HTMLPage.DTJQUIMINCSS);
    page.link("stylesheet", HTMLPage.MRV2UICSS);
    page.script().dataTable(TABLENAME)._("script");
    page._("head").body();

    long mapTaskMaximumTime = Long.MIN_VALUE;
    long mapTaskMinimumTime = Long.MAX_VALUE;
    long mapTaskTotalTime = 0;
    long reduceTaskMaximumTime = Long.MIN_VALUE;
    long reduceTaskMimimumTime = Long.MAX_VALUE;
    long reduceTaskTotalTime = 0;
    int mapTasks = 0;
    int reduceTasks = 0;
    
    String mapTaskIdMaximumTime = "";
    String mapTaskIdMinimumTime = "";
    String reduceTaskIdMaximumTime = "";
    String reduceTaskIdMinimumTime = "";
    
    page.h3("Task List : " + jobId);
    page.table(TABLENAME, TABLECLASS).thead().tr()
      .th("taskid", "Task ID")
      .th("tasktype", "Task Type")
      .th("hostname", "Host Name")
      .th("rackname", "Rack Name")
      .th("starttime", "Start Time")
      .th("shufflefinishtime", "Shuffle Finish Time")
      .th("sortfinishtime", "Sort Finish Time")
      .th("finishtime", "Finish Time")
      .th("elapsedtime", "Elapsed Time")._("tr")._("thead");
    page.tbody();
    for (int i = 0; i < json.getNumEvents(); i++) {
      TaskInfo info = json.getMRv2TaskInfo(i, id);
      if (info != null) {
        String t = info.getTaskType();
        long elapsed = info.getFinishTime() - info.getStartTime();
        if (info.getTaskType().equals("MAP")) {
          mapTasks++;
          mapTaskTotalTime += elapsed;
          if (mapTaskMaximumTime < elapsed) {
            mapTaskMaximumTime = elapsed;
            mapTaskIdMaximumTime = info.getTaskId();
          }
          if (mapTaskMinimumTime > elapsed) {
            mapTaskMinimumTime = elapsed;
            mapTaskIdMinimumTime = info.getTaskId();
          }
        } else if (info.getTaskType().equals("REDUCE")) {
          reduceTasks++;
          reduceTaskTotalTime += elapsed;
          if (reduceTaskMaximumTime < elapsed) {
            reduceTaskMaximumTime = elapsed;
            reduceTaskIdMaximumTime = info.getTaskId();
          }
          if (reduceTaskMimimumTime > elapsed) {
            reduceTaskMimimumTime = elapsed;
            reduceTaskIdMinimumTime = info.getTaskId();
          }
        }
        page.tr()
          .td(info.getTaskId())
          .td(t)
          .td(info.getHostName())
          .td(info.getRackName())
          .td(HTMLPage.getDisplayDate(info.getStartTime()))
          .td(t.equals("MAP") ?
              "" : HTMLPage.getDisplayDate(info.getShuffleFinishTime()))
          .td(t.equals("MAP") ?
              "" : HTMLPage.getDisplayDate(info.getSortFinishTime()))
          .td(HTMLPage.getDisplayDate(info.getFinishTime()))
          .td(HTMLPage.getElapsedTime(elapsed))
          ._("tr");
        
      }
    }
    page._("tbody")._("table");
    
    page.h3("Task Information : " + jobId);
    page.table("taskInformation", "zebra").thead().tr()._("tr")._("thead");
    page.tbody()
      .tr().th("Map Tasks").td(Integer.toString(mapTasks))._("tr")
      .tr().th("Reduce Tasks").td(Integer.toString(reduceTasks))._("tr")
      .tr().th("Map task average elapsed time")
        .td(mapTasks > 0 ?
            HTMLPage.getElapsedTime(mapTaskTotalTime/mapTasks) : "")._("tr")
      .tr().th("Map task maximum elapsed time")
        .td(mapTasks > 0 ? HTMLPage.getElapsedTime(mapTaskMaximumTime)
            + " (" + mapTaskIdMaximumTime + ")" : "")._("tr")
      .tr().th("Map task minimum elapsed time")
        .td(mapTasks > 0 ? HTMLPage.getElapsedTime(mapTaskMinimumTime)
            + " (" + mapTaskIdMinimumTime + ")" : "")._("tr")
      .tr().th("Reduce task average elapsed time")
        .td(reduceTasks > 0 ?
            HTMLPage.getElapsedTime(reduceTaskTotalTime/reduceTasks) : "")
            ._("tr")
      .tr().th("Reduce task maximum elapsed time")
        .td(reduceTasks > 0 ? HTMLPage.getElapsedTime(reduceTaskMaximumTime)
            + " (" + reduceTaskIdMaximumTime + ")" : "")._("tr")
      .tr().th("Reduce task minimum elapsed time")
        .td(reduceTasks > 0 ? HTMLPage.getElapsedTime(reduceTaskMimimumTime)
            + " (" + reduceTaskIdMinimumTime + ")" : "")._("tr");
    page._("tbody")._("table");
    page.br().a(HTMLPage.HOME + jobId, "Back");
    page._("body")._("html");
    
  }
}
