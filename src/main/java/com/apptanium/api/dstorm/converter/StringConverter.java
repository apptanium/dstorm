package com.apptanium.api.dstorm.converter;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.JsonObject;

/**
 * @author saurabh
 */
public class StringConverter extends AbstractConverter<String> {

  public StringConverter(String property, boolean indexed, boolean required, boolean overwriteWithNull) {
    super(property, indexed, required, overwriteWithNull);
  }

  @Override
  public String getValue(JsonObject json, String alternate) {
    if(!json.has(property)) {
      return alternate;
    }
    return json.get(property).getAsString();
  }

  @Override
  public String getValue(Entity entity, String alternate) {
    String val = (String) entity.getProperty(property);
    return val == null ? alternate : val;
  }

  @Override
  public void setValue(JsonObject json, String value) {
    json.addProperty(property, value);
  }
}
