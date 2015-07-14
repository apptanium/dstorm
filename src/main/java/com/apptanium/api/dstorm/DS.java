package com.apptanium.api.dstorm;

import com.apptanium.api.dstorm.model.MapperModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author saurabh
 */
public class DS {

  private static final DS ds = new DS();

  private static final Map<Class,MapperModel> registry = new HashMap<>();


  public static DS ds() {
    return ds;
  }

  public void register(Class modelClass) {
    MapperModel model = new MapperModel(modelClass);
    registry.put(modelClass, model);
  }

}
