package net.sinchii.mrv2ui;

public class MRv2TaskInfo {
  
  private String taskId;
  private long startTime;
  private long finishTime;
  
  // CounterInfo
  private MRv2JobCounterInfo counterInfo;
  
  public void setTaskId(String id) {
    taskId = id;
  }
  
  public void setStartTime(long time) {
    startTime = time;
  }
  
  public void setFinishTime(long time) {
    finishTime = time;
  }
  
  public void setCounterInfo(MRv2JobCounterInfo info) {
    counterInfo = info;
  }
  
  public String getTaskId() {
    return taskId;
  }
  
  public long getStartTime() {
    return startTime;
  }
  
  public long getFinishTime() {
    return finishTime;
  }
  
  public MRv2JobCounterInfo getCounterInfo() {
    return counterInfo;
  }
  
  public static MRv2TaskInfo getInstance() {
    return new MRv2TaskInfo();
  }
}
