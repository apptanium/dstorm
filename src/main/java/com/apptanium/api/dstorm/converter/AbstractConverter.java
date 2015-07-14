package com.apptanium.api.dstorm.converter;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.JsonObject;

/**
 * @author saurabh
 */
public abstract class AbstractConverter<T> implements FieldConverter<T> {
  protected final String property;
  protected final boolean indexed;
  protected final boolean required;
  protected final boolean removeNull;

  protected AbstractConverter(String property, boolean indexed, boolean required, boolean removeNull) {
    this.property = property;
    this.indexed = indexed;
    this.required = required;
    this.removeNull = removeNull;
  }

  @Override
  public String property() {
    return property;
  }

  public boolean isIndexed() {
    return indexed;
  }

  public boolean isRequired() {
    return required;
  }

  public boolean isRemoveNull() {
    return removeNull;
  }

  @Override
  public void fromJsonToEntity(JsonObject json, Entity entity, T alternate) {
    setValue(entity, getValue(json, alternate));
  }

  @Override
  public void fromEntityToJson(Entity entity, JsonObject json, T alternate) {
    setValue(json, getValue(entity, alternate));
  }

  @Override
  public void setValue(Entity entity,  T value) {
    if(value == null && removeNull) {
      entity.removeProperty(property);
      return;
    }
    if(indexed) {
      entity.setProperty(property, value);
    }
    else {
      entity.setUnindexedProperty(property, value);
    }
  }
}
