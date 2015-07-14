package com.apptanium.api.dstorm.converter;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.ShortBlob;
import com.google.gson.JsonObject;
import org.apache.commons.codec.binary.Base64;

/**
 * @author saurabh
 */
public class ShortBlobConverter extends AbstractConverter<ShortBlob> {

  public ShortBlobConverter(String property, boolean indexed, boolean required, boolean overwriteWithNull) {
    super(property, indexed, required, overwriteWithNull);
  }

  /**
   * assuming the short blob is represented by a base64 encoded string for the key of 'property', this
   * will return a short blob based on that base64 string
   * @param json the object with the key-value pair
   * @param alternate the alternate value to use if property is not present in json
   * @return short blob made from encoding the base64 value of property in json
   */
  @Override
  public ShortBlob getValue(JsonObject json, ShortBlob alternate) {
    if(!json.has(property)) {
      return alternate;
    }
    return new ShortBlob(Base64.decodeBase64(json.get(property).getAsString()));
  }

  @Override
  public ShortBlob getValue(Entity entity, ShortBlob alternate) {
    ShortBlob val = (ShortBlob) entity.getProperty(property);
    return val == null ? alternate : val;
  }

  @Override
  public void setValue(JsonObject json, ShortBlob value) {
    json.addProperty(property, Base64.encodeBase64URLSafeString(value.getBytes()));
  }

}
