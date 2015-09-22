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

package net.sinchii.mrv2ui;

import java.util.ArrayList;
import java.util.List;

import net.sinchii.mrv2ui.dao.JobCounterInfo;
import net.sinchii.mrv2ui.dao.JobInfo;
import net.sinchii.mrv2ui.dao.TaskInfo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JSON {

  private String strJson;
  private String sessionJobId;
  JsonElement element;
  JsonObject object;
  JsonArray array;
  private long minimumTime;
  
  private final static String ENTITIES = "entities";
  private final static String EVENTS = "events";
  private final static String EVENTTYPE = "eventtype";
  private final static String EVENTINFO = "eventinfo";
  private final static String ENTITY = "entity";
  private final static String STARTTIME = "starttime";
  
  public JSON() { }
  
  public JSON(String str) {
    Gson gson = new Gson();
    strJson = str;
    object = gson.fromJson(str, JsonObject.class);
    minimumTime = Long.MAX_VALUE;
  }
  
  public JSON(String str, String jobId) {
    this(str);
    sessionJobId = jobId;
  }
  
  public void setEntities() {
    array =  object.get(ENTITIES).getAsJsonArray();
  }
  
  public void setTaskEntities(String entityFilter) {
    array = object.get(ENTITIES).getAsJsonArray();
    for (int i = 0; i < array.size(); i++) {
      String s = array.get(i).getAsJsonObject().get(ENTITY).getAsString();
      if (!s.contains(entityFilter)) {
        array.remove(i);
      } else {
        long time = array.get(i).getAsJsonObject().get(STARTTIME).getAsLong();
        if (minimumTime > time) {
          minimumTime = time;
        }
      }
    }
  }
  
  public void addTaskEntities(String str, String entityFilter) {
    Gson gson = new Gson();
    JsonArray a =
        gson.fromJson(str, JsonObject.class).get(ENTITIES).getAsJsonArray();
    for (int i = 0; i < a.size(); i++) {
      String s = a.get(i).getAsJsonObject().get(ENTITY).getAsString();
      if (s.contains(entityFilter) &&
          !array.contains(a.get(i).getAsJsonObject())) {
        array.add(a.get(i).getAsJsonObject());
        long time = a.get(i).getAsJsonObject().get(STARTTIME).getAsLong();
        if (minimumTime > time) {
          minimumTime = time;
        }
      }
    }
  }
  
  public long getTaskMinimumTime() {
    return minimumTime;
  }
  
  public List<String> getEvents() {
    List<String> ret = new ArrayList<String>();
    for (int i = 0; i < array.size(); i++) {
      ret.add(array.get(i).getAsJsonObject().get(EVENTS).toString());
    }
    return ret;
  }
  
  public int getNumEvents() {
    return array.size();
  }
  
  public long getJobStartTime(int index) {
    JsonArray a = array.get(index).getAsJsonObject().
        get(EVENTS).getAsJsonArray();
    
    for (int i = 0; i < a.size(); i++) {
      JsonObject o = a.get(i).getAsJsonObject();
      if (o.get(EVENTTYPE).getAsString().equals("JOB_INITED")) {
        return o.get(EVENTINFO).getAsJsonObject().
            get("START_TIME").getAsLong();
      }
    }
    return -1;
  }
  
  public long getJobFinishTime(int index) {
    JsonArray a = array.get(index).getAsJsonObject().
        get(EVENTS).getAsJsonArray();
    
    for (int i = 0; i < a.size(); i++) {
      JsonObject o = a.get(i).getAsJsonObject();
      if (o.get(EVENTTYPE).getAsString().equals("JOB_FINISHED")) {
        return o.get(EVENTINFO).getAsJsonObject().
            get("FINISH_TIME").getAsLong();
      }
    }
    return -1;
  }
  
  public JobInfo getMRv2JobInfo(int index) {
    JobInfo info = JobInfo.getInstance();
    info.setJobId(array.get(index).getAsJsonObject().
        get(ENTITY).getAsString());
    
    JsonArray e = array.get(index).getAsJsonObject().
        get(EVENTS).getAsJsonArray();
    setMRv2JobResult(info, e);
    setMRv2JobResultCounter(info, e);
    return info;
  }
  
  public JobInfo getMRv2JobInfo() {
    JobInfo info = JobInfo.getInstance();
    info.setJobId(object.get(ENTITY).getAsString());
    setMRv2JobResult(info, object.get(EVENTS).getAsJsonArray());
    setMRv2JobResultCounter(info, object.get(EVENTS).getAsJsonArray());
    return info;
  }
  
  private void setMRv2JobResult(JobInfo info, JsonArray array) {
    for (int i = 0; i < array.size(); i++) {
      JsonObject o = array.get(i).getAsJsonObject();
      if (o.get(EVENTTYPE).getAsString().equals("AM_STARTED")) {
        info.setContainerId(o.get(EVENTINFO).getAsJsonObject().
            get("CONTAINER_ID").getAsString());
      }
      if (o.get(EVENTTYPE).getAsString().equals("JOB_SUBMITTED")) {
        info.setJobName(o.get(EVENTINFO).getAsJsonObject().
            get("JOB_NAME").getAsString());
        info.setUserName(o.get(EVENTINFO).getAsJsonObject().
            get("USER_NAME").getAsString());
        info.setSubmitTime(o.get(EVENTINFO).getAsJsonObject().
            get("SUBMIT_TIME").getAsLong());
        info.setQueueName(o.get(EVENTINFO).getAsJsonObject().
            get("QUEUE_NAME").getAsString());
      }
      if (o.get(EVENTTYPE).getAsString().equals("JOB_INITED")) {
        info.setStartTime(o.get(EVENTINFO).getAsJsonObject().
            get("START_TIME").getAsLong());
        info.setMapTasks(o.get(EVENTINFO).getAsJsonObject().
            get("TOTAL_MAPS").getAsInt());
        info.setReduceTasks(o.get(EVENTINFO).getAsJsonObject().
            get("TOTAL_REDUCES").getAsInt());
      }
      if (o.get(EVENTTYPE).getAsString().equals("JOB_FINISHED")) {
        info.setJobStatus(o.get(EVENTINFO).getAsJsonObject().
            get("JOB_STATUS").getAsString());
        info.setFinishTime(o.get(EVENTINFO).getAsJsonObject().
            get("FINISH_TIME").getAsLong());
      }
    }
  }
  
  private void setMRv2JobResultCounter(JobInfo info, JsonArray array) {
    JobCounterInfo counterInfo = JobCounterInfo.getInstance();
    for (int i = 0; i < array.size(); i++) {
      JsonObject o = array.get(i).getAsJsonObject();
      if (o.get(EVENTTYPE).getAsString().equals("JOB_FINISHED")) {
        JsonObject finish = o.get(EVENTINFO).getAsJsonObject();
        // Map
        JsonArray m = finish.get("MAP_COUNTERS_GROUPS").getAsJsonArray();
        parseCounterValue(counterInfo, m, "MAP");
        // Reduce
        JsonArray r = finish.get("REDUCE_COUNTERS_GROUPS").getAsJsonArray();
        parseCounterValue(counterInfo, r, "REDUCE");
        // Total
        JsonArray t =  finish.get("TOTAL_COUNTERS_GROUPS").getAsJsonArray();
        parseCounterValue(counterInfo, t, "TOTAL");
      }
    }
    info.setCounterInfo(counterInfo);
  }
  
  private void parseCounterValue(
      JobCounterInfo info, JsonArray array, String type) {
    for (int i = 0; i < array.size(); i++) {
      JsonObject o = array.get(i).getAsJsonObject();
      String groupName = o.get("DISPLAY_NAME").getAsString();
      JsonArray counters = o.get("COUNTERS").getAsJsonArray();
      for (int j = 0; j <counters.size(); j++) {
        JsonObject counter = counters.get(j).getAsJsonObject();
        String counterName = counter.get("DISPLAY_NAME").getAsString();
        long value = counter.get("VALUE").getAsLong();
        if (type.equals("MAP")) {
          info.setMapValue(groupName, counterName, value);
        } else if (type.equals("REDUCE")) {
          info.setReduceValue(groupName, counterName, value);
        } else if (type.equals("TOTAL")) {
          info.setTotalValue(groupName, counterName, value);
        }
      }
    }
  }
  
  public String getString() {
    return strJson;
  }
  
  public String getSessionJobId() {
    return sessionJobId;
  }
  
  public TaskInfo getMRv2TaskInfo(int index, String jobId) {
    String taskId =
        array.get(index).getAsJsonObject().get(ENTITY).getAsString();
    if (!taskId.contains(jobId)) {
      return null;
    }
    TaskInfo info = TaskInfo.getInstance();
    info.setTaskId(taskId);
    info.setStartTime(
        array.get(index).getAsJsonObject().get("starttime").getAsLong());
    JsonArray a = array.get(index).getAsJsonObject()
        .get(EVENTS).getAsJsonArray();
    for (int i = 0; i < a.size(); i++) {
      JsonObject o = a.get(i).getAsJsonObject();
      // Succeeded Map Attempt 
      if (o.get(EVENTTYPE).getAsString().equals("MAP_ATTEMPT_FINISHED")) {
        if (o.get(EVENTINFO).getAsJsonObject()
            .get("STATUS").getAsString().equals("SUCCEEDED")) {
          setTaskAttemptInfo(info, o.get(EVENTINFO).getAsJsonObject());
        }
      }
      // Succeeded Reduce Attempt
      if (o.get(EVENTTYPE).getAsString().equals("REDUCE_ATTEMPT_FINISHED")) {
        if (o.get(EVENTINFO).getAsJsonObject()
            .get("STATUS").getAsString().equals("SUCCEEDED")) {
          setTaskAttemptInfo(info, o.get(EVENTINFO).getAsJsonObject());
          setReduceTaskAttemptInfo(info, o.get(EVENTINFO).getAsJsonObject());
        }
      }
      if (o.get(EVENTTYPE).getAsString().equals("TASK_FINISHED")) {
        info.setTaskType(
            o.get(EVENTINFO).getAsJsonObject().get("TASK_TYPE").getAsString());
        info.setFinishTime(
            o.get(EVENTINFO).getAsJsonObject().get("FINISH_TIME").getAsLong());
      }
    }
    return info;
  }
  
  private void setTaskAttemptInfo(TaskInfo info, JsonObject o) {
    info.setHostName(o.get("HOSTNAME").getAsString());
    info.setRackName(o.get("RACK_NAME").getAsString());
  }
  
  private void setReduceTaskAttemptInfo(TaskInfo info, JsonObject o) {
    info.setShuffleFinishTime(o.get("SHUFFLE_FINISH_TIME").getAsLong());
    info.setSortFinishTime(o.get("SORT_FINISH_TIME").getAsLong());
  }
}
