package com.apptanium.api.dstorm.converter;

import com.apptanium.api.dstorm.util.JsonUtils;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.JsonObject;

/**
 * @author saurabh
 */
public class DoubleConverter extends AbstractConverter<Double> {


  public DoubleConverter(String property, boolean indexed, boolean required, boolean removeNull) {
    super(property, indexed, required, removeNull);
  }

  @Override
  public Double getValue(JsonObject json, Double alternate) {
    return json.has(property) ? JsonUtils.getDecimal(json.getAsJsonObject(), property, alternate) : alternate;
  }

  @Override
  public Double getValue(Entity entity, Double alternate) {
    Double val = (Double) entity.getProperty(property);
    return val == null ? alternate : val;
  }

  @Override
  public void setValue(JsonObject json, Double value) {
    json.addProperty(property, value);
  }
}
