package net.sinchii.mrv2ui.web;

import java.io.PrintWriter;
import java.util.Map;

import net.sinchii.mrv2ui.JSON;
import net.sinchii.mrv2ui.dao.JobCounterInfo.Value;
import net.sinchii.mrv2ui.dao.JobInfo;

public class JobInfoCSV {

  private final static String TITLE = "Job Summary CSV";
  
  private final static String CGMR = "Map-Reduce Framework";
  private final static String CGFS = "File System Counters";
  private final static String CGJOB = "Job Counters ";
  
  private HTMLPage page;
  private JSON json;
  
  public JobInfoCSV(PrintWriter writer, JSON json) {
    page = new HTMLPage(writer);
    this.json = json;
    json.setEntities();
    render();
  }
  
  public void render() {
    page.html().head().title(TITLE)._("head");
    for (Header header : Header.values()) {
      page.output(header.name() + ",");
    }
    page.br().output("\n");
    
    for (int i = 0; i < json.getNumEvents(); i++) {
      JobInfo info = json.getMRv2JobInfo(i);
      long elapsed = info.getFinishTime() - info.getStartTime();
      
      Map<String, Map<String, Value>> c = info.getCounterInfo().getCounters();
      page.output(info.getJobId() + ","
          + info.getJobName() + "," + info.getUserName() + ","
          + HTMLPage.getDisplayDate(info.getSubmitTime()) + ","
          + HTMLPage.getDisplayDate(info.getStartTime()) + ","
          + HTMLPage.getDisplayDate(info.getFinishTime()) + ","
          + HTMLPage.getElapsedTime(elapsed) + ","
          + info.getMapTasks() + "," + info.getReduceTasks() + ","
          + c.get(CGMR).get(COUNTERNAME[0]).getMapValue() + ","
          + c.get(CGMR).get(COUNTERNAME[1]).getMapValue() + ","
          + c.get(CGMR).get(COUNTERNAME[2]).getMapValue() + ","
          + c.get(CGMR).get(COUNTERNAME[3]).getReduceValue() + ","
          + c.get(CGMR).get(COUNTERNAME[4]).getReduceValue() + ","
          + c.get(CGMR).get(COUNTERNAME[2]).getReduceValue() + ","
          + c.get(CGFS).get(COUNTERNAME[5]).getMapValue() + ","
          + c.get(CGFS).get(COUNTERNAME[6]).getReduceValue() + ","
          + c.get(CGJOB).get(COUNTERNAME[7]).getMapValue() + ","
          + c.get(CGJOB).get(COUNTERNAME[8]).getReduceValue() + ","
          + c.get(CGMR).get(COUNTERNAME[9]).getMapValue() + ","
          + c.get(CGMR).get(COUNTERNAME[9]).getReduceValue() + ","
          + c.get(CGMR).get(COUNTERNAME[10]).getMapValue() + ","
          + c.get(CGMR).get(COUNTERNAME[10]).getReduceValue() + ","
          );
    }
  }
  
  private enum Header {
    JOBID,
    JOBNAME,
    USER,
    SUBMITTIME,
    STARTTIME,
    FINISHTIME,
    ELAPSEDTIME,
    MAP_TASKS,
    REDUCE_TASKS,
    MAP_INPUT_RECORDS,
    MAP_OUTPUT_RECORDS,
    MAP_SPILLED_RECORDS,
    REDUCE_INPUT_RECORDS,
    REDUCE_OUTPUT_RECORDS,
    REDUCE_SPILLED_RECORDS,
    MAP_HDFS_READ_BYTES,
    REDUCE_HDFS_WRITE_BYTES,
    MAP_TASK_TIME_MS,
    REDUCE_TASK_TIME_MS,
    MAP_TOTAL_COMMITED_HEAP_BYTES,
    REDUCE_TOTAL_COMMITED_HEAP_BYTES,
    MAP_GCTIMES,
    REDUCE_GCTIMES
  }
  
  private final static String[] COUNTERNAME = {
    "Map input records",
    "Map output records",
    "Spilled Records",
    "Reduce input records",
    "Reduce output records",
    "HDFS: Number of bytes read",
    "HDFS: Number of bytes written",
    "Total time spent by all map tasks (ms)",
    "Total time spent by all reduce tasks (ms)",
    "Total committed heap usage (bytes)",
    "GC time elapsed (ms)"
  };
}
