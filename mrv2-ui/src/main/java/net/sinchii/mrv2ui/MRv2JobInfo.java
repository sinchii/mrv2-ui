package net.sinchii.mrv2ui;

public class MRv2JobInfo {

  private String containerId;
  private String jobId;
  private String jobName;
  private String userName;
  private long submitTime;
  private long startTime;
  private long finishTime;
  
  private int mapTasks;
  private int reduceTasks;
  
  public void setContainerId(String id) {
    containerId = id;
  }
  
  public void setJobId(String id) {
    jobId = id;
  }
  
  public void setJobName(String name) {
    jobName = name;
  }
  
  public void setUserName(String user) {
    userName = user;
  }
  
  public void setSubmitTime(long time) {
    submitTime = time;
  }
  
  public void setStartTime(long time) {
    startTime = time;
  }
  
  public void setFinishTime(long time) {
    finishTime = time;
  }
  
  public void setMapTasks(int tasks) {
    mapTasks = tasks;
  }
  
  public void setReduceTasks(int tasks) {
    reduceTasks = tasks;
  }
  
  public String getContainerId() {
    return containerId;
  }
  
  public String getJobId() {
    return jobId;
  }
  
  public String getJobName() {
    return jobName;
  }
  
  public String getUserName() {
    return userName;
  }
  
  public long getSubmitTime() {
    return submitTime;
  }
  
  public long getStartTime() {
    return startTime;
  }
  
  public long getFinishTime() {
    return finishTime;
  }
  
  public int getMapTasks() {
    return mapTasks;
  }
  
  public int getReduceTasks() {
    return reduceTasks;
  }
  
  public static MRv2JobInfo getInstance() {
    return new MRv2JobInfo();
  }
}
