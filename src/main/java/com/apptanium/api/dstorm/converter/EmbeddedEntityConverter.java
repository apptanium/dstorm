package com.apptanium.api.dstorm.converter;

import com.apptanium.api.dstorm.util.EntityUtils;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 * @author saurabh
 */
public class EmbeddedEntityConverter extends AbstractConverter<EmbeddedEntity> {

  public EmbeddedEntityConverter(String property, boolean indexed, boolean required, boolean overwriteWithNull) {
    super(property, indexed, required, overwriteWithNull);
  }

  @Override
  public EmbeddedEntity getValue(JsonObject json, EmbeddedEntity alternate) {
    EmbeddedEntity value = alternate;
    if(json.has(property)) {
      value = new EmbeddedEntity();
      EntityUtils.fromJson(json.getAsJsonObject(property), value);
    }
    return value;
  }

  @Override
  public EmbeddedEntity getValue(Entity entity, EmbeddedEntity alternate) {
    EmbeddedEntity value = (EmbeddedEntity) entity.getProperty(property);
    return value == null ? alternate : value;
  }

  @Override
  public void setValue(JsonObject json, EmbeddedEntity value) {
    try {
      JsonObject result = EntityUtils.asObjectNode(value, null, null);
      json.add(property, result);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
