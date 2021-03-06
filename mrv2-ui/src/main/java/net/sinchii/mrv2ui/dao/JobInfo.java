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

public class JobInfo {

  private String containerId;
  private String jobId;
  private String jobName;
  private String userName;
  private long submitTime;
  private long startTime;
  private long finishTime;
  
  private int mapTasks;
  private int reduceTasks;
  
  // JobInfoPage
  private String jobStatus;
  private String queueName;
  
  // CounterInfo
  private JobCounterInfo counterInfo;
  
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
  
  public void setJobStatus(String status) {
    jobStatus = status;
  }
  
  public void setQueueName(String queue) {
    queueName = queue;
  }
  
  public void setCounterInfo(JobCounterInfo info) {
    counterInfo = info;
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
  
  public String getJobStatus() {
    return jobStatus;
  }
  
  public String getQueueName() {
    return queueName;
  }
  
  public JobCounterInfo getCounterInfo() {
    return counterInfo;
  }
  
  public static JobInfo getInstance() {
    return new JobInfo();
  }
}
