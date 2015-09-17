package net.sinchii.mrv2ui.dao;


public class TaskInfo {
  
  private String taskId;
  private String taskType;
  private String hostName;
  private String rackName;
  private long startTime;
  private long shuffileFinishTime;
  private long sortFinishTime;
  private long finishTime;
  
  // CounterInfo
  private JobCounterInfo counterInfo;
  
  public void setTaskId(String id) {
    taskId = id;
  }
  
  public void setTaskType(String type) {
    taskType = type;
  }
  
  public void setHostName(String name) {
    hostName = name;
  }
  
  public void setRackName(String rack) {
    rackName = rack;
  }
  
  public void setStartTime(long time) {
    startTime = time;
  }
  
  public void setShuffleFinishTime(long time) {
    shuffileFinishTime = time;
  }
  
  public void setSortFinishTime(long time) {
    sortFinishTime = time;
  }
  
  public void setFinishTime(long time) {
    finishTime = time;
  }
  
  public void setCounterInfo(JobCounterInfo info) {
    counterInfo = info;
  }
  
  public String getTaskId() {
    return taskId;
  }
  
  public String getTaskType() {
    return taskType;
  }
  
  public String getHostName() {
    return hostName;
  }
  
  public String getRackName() {
    return rackName;
  }
  
  public long getStartTime() {
    return startTime;
  }
  
  public long getShuffleFinishTime() {
    return shuffileFinishTime;
  }
  
  public long getSortFinishTime() {
    return sortFinishTime;
  }
  
  public long getFinishTime() {
    return finishTime;
  }
  
  public JobCounterInfo getCounterInfo() {
    return counterInfo;
  }
  
  public static TaskInfo getInstance() {
    return new TaskInfo();
  }
}
