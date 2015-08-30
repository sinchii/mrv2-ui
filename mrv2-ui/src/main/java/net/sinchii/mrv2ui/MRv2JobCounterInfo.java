package net.sinchii.mrv2ui;

import java.util.HashMap;
import java.util.Map;

public class MRv2JobCounterInfo {

  private Map<String, Map<String, Value>> counter;
  
  public MRv2JobCounterInfo() {
    counter = new HashMap<String, Map<String, Value>>();
  }
  
  public Map<String, Map<String, Value>> getCounters() {
    return counter;
  }
  
  public void setMapValue(String groupName, String name, long value) {
    if (!counter.containsKey(groupName)) {
      Map<String, Value> group = new HashMap<String, Value>();
      Value values = new Value();
      group.put(name, values);
      counter.put(groupName, group);
    }
    if (!counter.get(groupName).containsKey(name)) {
      Value values = new Value();
      counter.get(groupName).put(name, values);
    }
    counter.get(groupName).get(name).setMapValue(value);
  }
  
  public void setReduceValue(String groupName, String name, long value) {
    if (!counter.containsKey(groupName)) {
      Map<String, Value> group = new HashMap<String, Value>();
      Value values = new Value();
      group.put(name, values);
      counter.put(groupName, group);
    }
    if (!counter.get(groupName).containsKey(name)) {
      Value values = new Value();
      counter.get(groupName).put(name, values);
    }
    counter.get(groupName).get(name).setReduceValue(value);
  }
  
  public void setTotalValue(String groupName, String name, long value) {
    if (!counter.containsKey(groupName)) {
      Map<String, Value> group = new HashMap<String, Value>();
      Value values = new Value();
      group.put(name, values);
      counter.put(groupName, group);
    }
    if (!counter.get(groupName).containsKey(name)) {
      Value values = new Value();
      counter.get(groupName).put(name, values);
    }
    counter.get(groupName).get(name).setTotalValue(value);
  }
  
  public class Value {
    private long mapValue;
    private long reduceValue;
    private long totalValue;
    
    public Value() {
      mapValue = 0;
      reduceValue = 0;
      totalValue = 0;
    }
    
    public void setMapValue(long value) {
      mapValue = value;
    }
    
    public void setReduceValue(long value) {
      reduceValue = value;
    }
    
    public void setTotalValue(long value) {
      totalValue = value;
    }
    
    public long getMapValue() {
      return mapValue;
    }
    
    public long getReduceValue() {
      return reduceValue;
    }
    
    public long getTotalValue() {
      return totalValue;
    }
  }
  
  public static MRv2JobCounterInfo getInstance() {
    return new MRv2JobCounterInfo();
  }
}
