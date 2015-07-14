package com.apptanium.api.dstorm.util;

import com.google.appengine.api.datastore.*;
import com.google.gson.*;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author saurabh
 */
public class JsonUtils {
  public static final String CHAR_ENCODING_UTF_8 = "UTF-8";
  public static final Charset CHARSET_UTF_8 = Charset.forName(CHAR_ENCODING_UTF_8);
  public static final Gson GSON = new GsonBuilder().create();

  public static JsonObject objectToJsonObject(Object object) {
    return (JsonObject) GSON.toJsonTree(object);
  }

  public static JsonArray listToJsonArray(Object object) {
    return (JsonArray) GSON.toJsonTree(object);
  }

  public static String objectToJsonString(Object object) {
    return GSON.toJson(object);
  }

  public static JsonObject createObjectNode() {
    return new JsonObject();
  }

  public static JsonArray createArrayNode() {
    return new JsonArray();
  }

  public static JsonElement parseToObject(String jsonString) throws IOException {
    return new JsonParser().parse(jsonString);
  }

  public static String toString(JsonElement jsonElement) {
    return GSON.toJson(jsonElement);
  }

  public static byte[] toStringBytes(JsonElement jsonElement) {
    String jsonString = toString(jsonElement);
    return jsonString.getBytes(CHARSET_UTF_8);
  }

  public static <T> T jsonToObject(String json, Class<T> objectClass) {
    return GSON.fromJson(json, objectClass);
  }

  public static <T> T jsonToObject(JsonElement json, Class<T> objectClass) {
    return GSON.fromJson(json, objectClass);
  }

  public static String optString(JsonObject object, String property, String alt) {
    return object.has(property) ? object.get(property).getAsString() : alt;
  }

  public static Long optLong(JsonObject object, String property, Long alt) {
    return object.has(property) ? object.get(property).getAsLong() : alt;
  }

  public static Double optDouble(final JsonObject object, final String property, final Double alt) {
    if (object.has(property)) {
      try {
        Double val = object.get(property).getAsDouble();
        return val;
      }
      catch (Exception e) {
        //do nothing
      }
    }
    return alt;
  }

  public static String getString(JsonObject node,
                                 String property,
                                 String alt) {
    if(node.has(property)) {
      return node.get(property).getAsString();
    }
    return alt;
  }

  public static Long getLong(JsonObject node,
                             String property,
                             Long alt) {
    if(node.has(property)) {
      return node.get(property).getAsLong();
    }
    return alt;
  }

  public static Boolean getBoolean(JsonObject node,
                                   String property,
                                   Boolean alt) {
    if(node.has(property)) {
      JsonElement valNode = node.get(property);
      if(valNode.isJsonPrimitive()) {
        JsonPrimitive primitive = valNode.getAsJsonPrimitive();
        if(primitive.isBoolean()) {
          return primitive.getAsBoolean();
        }
        else if(primitive.isNumber()) {
          return primitive.getAsInt() > 0;
        }
        else if(primitive.isString()) {
          String val = primitive.getAsString();
          val = val == null ? null : val.toLowerCase();
          return "true".equals(val) || "t".equals(val) || "yes".equals(val) || "y".equals(val);
        }
      }
    }
    return alt;
  }

  public static Double getDecimal(JsonObject node,
                                  String property,
                                  Double alt) {
    if(node.has(property)) {
      return node.get(property).getAsDouble();
    }
    return alt;
  }

  public static Text getText(JsonObject node,
                             String property,
                             Text alt) {
    if(node.has(property)) {
      return new Text(node.get(property).getAsString());
    }
    return alt;
  }

  public static JsonArray toJson(Iterator<? extends PropertyContainer> entities,
                                 Set<String> includes,
                                 String nameField) throws IOException {
    JsonArray array = createArrayNode();
    while (entities.hasNext()) {
      PropertyContainer entity = entities.next();
      array.add(asObjectNode(entity, includes, nameField));
    }
    return array;
  }

  public static JsonArray toJson(List<? extends PropertyContainer> entities,
                                 Set<String> includes,
                                 String nameField) throws IOException {
    JsonArray array = JsonUtils.createArrayNode();
    for (PropertyContainer entity : entities) {
      array.add(asObjectNode(entity, includes, nameField));
    }
    return array;
  }

  /**
   * todo: write test case for this converter
   * @param entity
   * @param includes
   * @param nameField
   * @return
   * @throws IOException
   */
  public static JsonObject asObjectNode(PropertyContainer entity,
                                        Set<String> includes,
                                        String nameField) throws IOException {
    JsonObject properties = JsonUtils.createObjectNode();
    Map<String,Object> items = entity.getProperties();
    if(nameField != null) {
      Key key = null;
      if(entity instanceof Entity) {
        key = ((Entity) entity).getKey();
      }
      else if(entity instanceof EmbeddedEntity) {
        key = ((EmbeddedEntity)entity).getKey();
      }
      if(key != null && key.getName() != null) {
        properties.addProperty(nameField, key.getName());
      }
    }
    for (Map.Entry<String, Object> entry : items.entrySet()) {
      if(includes != null && !includes.contains(entry.getKey())) {
        continue;
      }
      String key = entry.getKey();
      Object objValue = entry.getValue();
      if(objValue instanceof Text) {
        String textValue = ((Text)objValue).getValue();
        if(textValue.startsWith("[") && textValue.endsWith("]")) {
          properties.add(key, JsonUtils.parseToObject(textValue));
        }
        else if(textValue.startsWith("{") && textValue.endsWith("}")) {
          properties.add(key, JsonUtils.parseToObject(textValue));
        }
        else {
          properties.addProperty(key, textValue);
        }
      }
      else if(objValue instanceof ShortBlob) {
        properties.addProperty(key, Base64.encodeBase64URLSafeString(((ShortBlob) objValue).getBytes()));
      }
      else if(objValue instanceof Date) {
        properties.addProperty(key, ((Date) objValue).getTime());
      }
      else if(objValue instanceof Boolean) {
        properties.addProperty(key, (Boolean) objValue);
      }
      else if(objValue instanceof Long) {
        properties.addProperty(key, (Long) objValue);
      }
      else if(objValue instanceof Integer) {
        properties.addProperty(key, (Integer) objValue);
      }
      else if(objValue instanceof Double) {
        properties.addProperty(key, (Double) objValue);
      }
      else  if(objValue instanceof GeoPt) {
        JsonObject geo = createObjectNode();
        geo.addProperty("latitude", ((GeoPt)objValue).getLatitude());
        geo.addProperty("longitude", ((GeoPt)objValue).getLongitude());
        properties.add(key, geo);
      }
      else if(objValue instanceof List) {
        List objList = (List)objValue;
        if(objList.size() > 0) {
          if(objList.get(0) instanceof String) {
            properties.add(key, toJSONArrayFromJSONCompatibleList(objList));
          }
          else if(objList.get(0) instanceof EmbeddedEntity) {
            properties.add(key, toJSONArray((List<EmbeddedEntity>) objValue));
          }
        }
      }
      else if(objValue instanceof EmbeddedEntity) {
        properties.add(key, asObjectNode((EmbeddedEntity) objValue, null, null));
      }
      else {
        if(objValue == null) {
          properties.add(key, JsonNull.INSTANCE);
        }
        else {
          properties.addProperty(key, objValue.toString());
        }
      }
    }
    return properties;
  }

  public static JsonArray toJSONArrayFromJSONCompatibleList(Collection<?> set) {
    JsonArray array = JsonUtils.createArrayNode();
    for (Object item : set) {
      if(item instanceof String) {
        array.add(new JsonPrimitive((String)item));
      }
      else if(item instanceof Long) {
        array.add(new JsonPrimitive((Long) item));
      }
      else if(item instanceof Integer) {
        array.add(new JsonPrimitive((Integer) item));
      }
      else if(item instanceof Boolean) {
        array.add(new JsonPrimitive((Boolean) item));
      }
    }
    return array;
  }

  public static JsonArray toJSONArray(List<? extends PropertyContainer> entities) throws IOException {
    JsonArray array = JsonUtils.createArrayNode();
    for (PropertyContainer entity : entities) {
      array.add(asObjectNode(entity, null, null));
    }
    return array;
  }

  public static void addIfNotNull(JsonObject result, String key, String value) {
    if(value != null) {
      result.addProperty(key, value);
    }
  }

  public static void addIfNotNull(JsonObject result, String key, Double value) {
    if(value != null) {
      result.addProperty(key, value);
    }
  }

}
