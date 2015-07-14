package com.apptanium.api.dstorm.converter;

import com.apptanium.api.dstorm.util.JsonUtils;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.JsonObject;

/**
 * @author saurabh
 */
public class BooleanConverter extends AbstractConverter<Boolean> {


  public BooleanConverter(String property, boolean indexed, boolean required, boolean removeNull) {
    super(property, indexed, required, removeNull);
  }

  @Override
  public Boolean getValue(JsonObject json, Boolean alternate) {
    return json.has(property) ? JsonUtils.getBoolean(json, property, alternate) : alternate;
  }

  @Override
  public Boolean getValue(Entity entity, Boolean alternate) {
    Boolean val = (Boolean) entity.getProperty(property);
    return val == null ? alternate : val;
  }

  @Override
  public void setValue(JsonObject json, Boolean value) {
    json.addProperty(property, value);
  }
}
