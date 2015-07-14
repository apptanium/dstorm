package com.apptanium.api.dstorm.converter;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;
import com.google.gson.JsonObject;

/**
 * @author saurabh
 */
public class TextConverter extends AbstractConverter<Text> {

  public TextConverter(String property, boolean indexed, boolean required, boolean overwriteWithNull) {
    super(property, indexed, required, overwriteWithNull);
  }

  @Override
  public Text getValue(JsonObject json, Text alternate) {
    if(!json.has(property)) {
      return alternate;
    }
    return new Text(json.get(property).getAsString());
  }

  @Override
  public Text getValue(Entity entity, Text alternate) {
    Text val = (Text) entity.getProperty(property);
    return val == null ? alternate : val;
  }

  @Override
  public void setValue(JsonObject json, Text value) {
    json.addProperty(property, value.getValue());
  }
}
