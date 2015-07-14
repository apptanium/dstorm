package com.apptanium.api.dstorm.util;

import com.google.appengine.api.datastore.*;
import com.google.gson.*;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author saurabh
 */
public class EntityUtils {
  public static final Random RANDOM = new SecureRandom();



  public static boolean keyExists(Key key, DatastoreService datastoreService) {
    Query query = new Query(key.getKind(), key.getParent());
    query.setFilter(new Query.FilterPredicate("__key__", Query.FilterOperator.EQUAL, key));
    query.setKeysOnly();
    PreparedQuery pq = datastoreService.prepare(query);
    Entity entity = pq.asSingleEntity();
    return entity != null;
  }

  /**
   * Convert a simple JSON object to a PropertyContainer (usually an embedded entity);
   * NOTE: only primitives, or arrays and objects of primitives are converted
   * @param source
   * @param target
   */
  public static void fromJson(JsonObject source, PropertyContainer target) {
    for (Map.Entry<String, JsonElement> entry : source.entrySet()) {
      JsonElement element = entry.getValue();
      if(element.isJsonArray()) {
        JsonArray array = element.getAsJsonArray();
        if(array.size() > 0) {
          JsonElement first = array.get(0);
          if(first.isJsonPrimitive()) {
            JsonPrimitive primitive = first.getAsJsonPrimitive();
            if(primitive.isString()) {
              List<String> list = new ArrayList<>();
              for (JsonElement jsonElement : array) {
                list.add(jsonElement.getAsString());
              }
              target.setProperty(entry.getKey(), list);
            }
            else if(primitive.isNumber()) {
              List<Number> list = new ArrayList<>();
              for (JsonElement jsonElement : array) {
                list.add(jsonElement.getAsNumber());
              }
              target.setProperty(entry.getKey(), list);
            }
            else if(primitive.isBoolean()) {
              List<Boolean> list = new ArrayList<>();
              for (JsonElement jsonElement : array) {
                list.add(jsonElement.getAsBoolean());
              }
              target.setProperty(entry.getKey(), list);
            }
          }
        }
      }
      else if(element.isJsonObject()) {
        EmbeddedEntity embeddedEntity = new EmbeddedEntity();
        fromJson(element.getAsJsonObject(), embeddedEntity);
        target.setProperty(entry.getKey(), embeddedEntity);
      }
      else if(element.isJsonPrimitive()) {
        JsonPrimitive primitive = element.getAsJsonPrimitive();
        if(primitive.isBoolean()) {
          target.setProperty(entry.getKey(), primitive.getAsBoolean());
        }
        else if(primitive.isNumber()) {
          target.setProperty(entry.getKey(), primitive.getAsNumber());
        }
        else if(primitive.isString()) {
          target.setProperty(entry.getKey(), primitive.getAsString());
        }
      }
    }
  }

/*
todo: fix this: handle @Id, @Parent with datastore key interconversions
  public static void toJson(JsonObject result, Entity entity, MapperModel mapperModel, String ... properties) {
    FieldConverter idConverter = mapperModel.getConverter(mapperModel.getIdFieldName());
    result.addProperty(mapperModel.getIdFieldName(), entity.getKey().getName());
    for (String property : properties) {
      FieldConverter converter = mapperModel.getConverter(property);
      converter.fromEntityToJson(entity, result, null);
    }
  }
*/

  public static JsonArray toJson(Iterator<Entity> entities, Set<String> includes, String nameField) throws IOException {
    JsonArray array = JsonUtils.createArrayNode();
    while (entities.hasNext()) {
      PropertyContainer entity = entities.next();
      array.add(asObjectNode(entity, includes, nameField));
    }
    return array;
  }

  public static JsonArray toJson(List<Entity> entities, Set<String> includes, String nameField) throws IOException {
    JsonArray array = JsonUtils.createArrayNode();
    for (Entity entity : entities) {
      array.add(asObjectNode(entity, includes, nameField));
    }
    return array;
  }

  public static JsonObject asObjectNode(PropertyContainer entity, Set<String> includes, String nameField) throws IOException {
    JsonObject properties = JsonUtils.createObjectNode();
    Map<String,Object> items = entity.getProperties();
    if(nameField != null) {
      if(entity instanceof Entity) {
        properties.addProperty(nameField, ((Entity) entity).getKey().getName());
      }
      else if(entity instanceof EmbeddedEntity) {
        Key emKey = ((EmbeddedEntity)entity).getKey();
        properties.addProperty(nameField, emKey != null ? emKey.getName() : "");
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

  public static Set<String> getStringAsSet(String delimitedString) {
    Set<String> set = new HashSet<String>();
    Collections.addAll(set, delimitedString.split("[,]"));
    return set;
  }

  public static List<String> toStringList(Iterator<JsonElement> items) {
    List<String> list = new LinkedList<String>();
    while (items.hasNext()) {
      JsonElement node = items.next();
      list.add(node.toString());
    }
    return list;
  }

  public static Entity setProperty(Entity entity, String propertyName, Object value) {
    entity.setProperty(propertyName, value);
    return entity;
  }

  public static Query.Filter createCompositeQuery(final Query.CompositeFilterOperator operator,
                                                  Query.FilterPredicate ... predicates) {
    if(predicates == null) {
      return null;
    }
    if(predicates.length == 1) {
      return predicates[0];
    }
    return new Query.CompositeFilter(operator,
                                     Arrays.<Query.Filter>asList(predicates));
  }

}
