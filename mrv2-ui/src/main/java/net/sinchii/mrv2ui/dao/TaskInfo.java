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
