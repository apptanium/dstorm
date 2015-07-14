package com.apptanium.api.dstorm.converter;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.JsonObject;

/**
 * @author saurabh
 */
public class LongConverter extends AbstractConverter<Long> {

  public LongConverter(String property, boolean indexed, boolean required, boolean overwriteWithNull) {
    super(property, indexed, required, overwriteWithNull);
  }

  @Override
  public Long getValue(JsonObject json, Long alternate) {
    if(!json.has(property)) {
      return alternate;
    }
    return json.get(property).getAsLong();
  }

  @Override
  public Long getValue(Entity entity, Long alternate) {
    Long val = (Long) entity.getProperty(property);
    return val == null ? alternate : val;
  }

  @Override
  public void setValue(JsonObject json, Long value) {
    json.addProperty(property, value);
  }
}
