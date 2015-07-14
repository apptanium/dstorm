package com.apptanium.api.dstorm.converter;

import com.apptanium.api.dstorm.util.JsonUtils;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.GeoPt;
import com.google.gson.JsonObject;

/**
 * @author saurabh
 */
public class GeoConverter extends AbstractConverter<GeoPt> {
  public static final String KEY_LAT = "lat", KEY_LON = "lon";

  public GeoConverter(String property, boolean indexed, boolean required, boolean overwriteWithNull) {
    super(property, indexed, required, overwriteWithNull);
  }

  @Override
  public GeoPt getValue(JsonObject json, GeoPt alternate) {
    if(!json.has(property)) {
      return alternate;
    }
    JsonObject val = json.getAsJsonObject(property);
    return new GeoPt(val.get(KEY_LAT).getAsFloat(), val.get(KEY_LON).getAsFloat());
  }

  @Override
  public GeoPt getValue(Entity entity, GeoPt alternate) {
    GeoPt val = (GeoPt) entity.getProperty(property);
    return val == null ? alternate : val;
  }

  @Override
  public void setValue(JsonObject json, GeoPt value) {
    JsonObject geoPtJson = JsonUtils.createObjectNode();
    geoPtJson.addProperty(KEY_LAT, value.getLatitude());
    geoPtJson.addProperty(KEY_LON, value.getLongitude());
    json.add(property, geoPtJson);
  }
}
