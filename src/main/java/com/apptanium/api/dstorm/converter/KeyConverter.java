package com.apptanium.api.dstorm.converter;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.JsonObject;

/**
 * @author saurabh
 */
public class KeyConverter extends AbstractConverter<Key> {

  public KeyConverter(String property, boolean indexed, boolean required, boolean overwriteWithNull) {
    super(property, indexed, required, overwriteWithNull);
  }

  @Override
  public Key getValue(JsonObject json, Key alternate) {
    if(!json.has(property)) {
      return alternate;
    }
    return KeyFactory.stringToKey(json.get(property).getAsString());
  }

  @Override
  public Key getValue(Entity entity, Key alternate) {
    Key val = (Key) entity.getProperty(property);
    return val == null ? alternate : val;
  }

  @Override
  public void setValue(JsonObject json, Key value) {
    json.addProperty(property, KeyFactory.keyToString(value));
  }
}
