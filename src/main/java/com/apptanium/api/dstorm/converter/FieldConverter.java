package com.apptanium.api.dstorm.converter;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.JsonObject;

/**
 * @author saurabh
 */
public interface FieldConverter<T> {
  public String property();
  public boolean isIndexed();
  public boolean isRequired();
  public boolean isRemoveNull();
  public void fromJsonToEntity(JsonObject json, Entity entity, T alternate);
  public void fromEntityToJson(Entity entity, JsonObject json, T alternate);
  public T getValue(JsonObject json, T alternate);
  public T getValue(Entity entity, T alternate);

  public void setValue(Entity entity, T value);

  /**
   * Set value from the given object T to the json object supplied
   * @param json the target json to populate
   * @param value the value to set in the json
   */
  public void setValue(JsonObject json, T value);
}
