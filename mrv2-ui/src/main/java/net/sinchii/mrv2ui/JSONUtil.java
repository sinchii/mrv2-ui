package net.sinchii.mrv2ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JSONUtil {

  Gson gson;
  JsonElement element;
  JsonObject object;
  JsonArray array;
  
  public final static String ENTITIES = "entities";
  public final static String EVENTS = "events";
  private final static String EVENTTYPE = "eventtype";
  private final static String EVENTINFO = "eventinfo";
  
  public JSONUtil() {
    gson = new Gson();
  }
  
  public void setEntities(String str) {
    array = gson.fromJson(str, JsonObject.class).
        get(ENTITIES).getAsJsonArray();
  }
  
  public void setEvent(String str) {
    object = gson.fromJson(str, JsonObject.class);
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
  
  public MRv2JobInfo getMRv2JobInfo(int index) {
    MRv2JobInfo info = MRv2JobInfo.getInstance();
    info.setJobId(array.get(index).getAsJsonObject().
        get("entity").getAsString());
    
    JsonArray e = array.get(index).getAsJsonObject().
        get(EVENTS).getAsJsonArray();
    setMRv2JobResult(info, e);
    return info;
  }
  
  public MRv2JobInfo getMRv2JobInfo() {
    MRv2JobInfo info = MRv2JobInfo.getInstance();
    info.setJobId(object.get("entity").getAsString());
    setMRv2JobResult(info, object.get(EVENTS).getAsJsonArray());
    setMRv2JobResultCounter(info, object.get(EVENTS).getAsJsonArray());
    return info;
  }
  
  private void setMRv2JobResult(MRv2JobInfo info, JsonArray array) {
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
  
  private void setMRv2JobResultCounter(MRv2JobInfo info, JsonArray array) {
    MRv2JobCounterInfo counterInfo = MRv2JobCounterInfo.getInstance();
    for (int i = 0; i < array.size(); i++) {
      JsonObject o = array.get(i).getAsJsonObject();
      if (o.get(EVENTTYPE).getAsString().equals("JOB_FINISHED")) {
        JsonObject finish = o.get(EVENTINFO).getAsJsonObject();
        // Map
        //finish.get("MAP_COUNTERS_GROUPS").getAsJsonArray();
        // Reduce
        //finish.get("REDUCE_COUNTERS_GROUPS").getAsJsonArray();
        // Total
        JsonArray total = finish.get("TOTAL_COUNTERS_GROUPS").getAsJsonArray();
        for (int a = 0; a < total.size(); a++) {
          JsonObject obj = total.get(a).getAsJsonObject();
          String groupName = obj.get("DISPLAY_NAME").getAsString();
          JsonArray counters = obj.get("COUNTERS").getAsJsonArray();
          for (int b = 0; b < counters.size(); b++) {
            JsonObject counter = counters.get(b).getAsJsonObject();
            String counterName = counter.get("DISPLAY_NAME").getAsString();
            long value = counter.get("VALUE").getAsLong();
            counterInfo.setTotalValue(groupName, counterName, value);
          }
        }
      }
    }
    info.setCounterInfo(counterInfo);
  }
}
