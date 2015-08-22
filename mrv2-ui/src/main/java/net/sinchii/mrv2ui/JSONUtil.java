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
  JsonArray array;
  
  private final static String ENTITIES = "entities";
  private final static String EVENTS = "events";
  private final static String EVENTTYPE = "eventtype";
  private final static String EVENTINFO = "eventinfo";
  
  public JSONUtil() {
    gson = new Gson();
  }
  
  public void set(String str) {
    array = gson.fromJson(str, JsonObject.class).
        get(ENTITIES).getAsJsonArray();
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
    for (int i = 0; i < e.size(); i++) {
      JsonObject o = e.get(i).getAsJsonObject();
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
        info.setFinishTime(o.get(EVENTINFO).getAsJsonObject().
            get("FINISH_TIME").getAsLong());
      }
    }
    return info;
  }
}
