package com.apptanium.api.dstorm.converter;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.JsonObject;

/**
 * @author saurabh
 */
public class EnumConverter<T extends Enum<T>> extends AbstractConverter<T> {
  private final Class<T> enumClass;

  public EnumConverter(String property, boolean indexed, boolean required, boolean overwriteWithNull, Class<T> enumClass) {
    super(property, indexed, required, overwriteWithNull);
    this.enumClass = enumClass;
  }

  @Override
  public T getValue(JsonObject json, T alternate) {
    if(json.has(property)) {
      try {
        return Enum.valueOf(enumClass, json.getAsJsonObject().get(property).getAsString());
      }
      catch (IllegalArgumentException e) {
        return alternate;
      }
    }
    return alternate;
  }

  @Override
  public T getValue(Entity entity, T alternate) {
    String val = (String) entity.getProperty(property);
    return val == null ? alternate : Enum.valueOf(enumClass, val);
  }

  @Override
  public void setValue(Entity entity, T value) {
    if(indexed) {
      entity.setProperty(property, value.name());
    }
    else {
      entity.setUnindexedProperty(property, value.name());
    }
  }

  @Override
  public void setValue(JsonObject json, T value) {
    json.addProperty(property, value.name());
  }
}
