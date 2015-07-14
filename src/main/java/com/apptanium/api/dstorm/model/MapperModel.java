package com.apptanium.api.dstorm.model;

import com.apptanium.api.dstorm.annotation.Cached;
import com.apptanium.api.dstorm.annotation.Id;
import com.apptanium.api.dstorm.annotation.Kind;
import com.apptanium.api.dstorm.annotation.Property;
import com.apptanium.api.dstorm.converter.ConverterFactory;
import com.apptanium.api.dstorm.converter.FieldConverter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author saurabh
 */
public class MapperModel {
  private final Class modelClass;
  private final String name;
  private final boolean isCached;
  private final String cacheNamespace;
  private final Map<String,FieldConverter> properties = new HashMap<>();
  private final Field idField;
  private final Field parentField;

  public MapperModel(Class modelClass) {
    System.out.println("modelClass = " + modelClass.getSimpleName());
    this.modelClass = modelClass;
    this.name = modelClass.getSimpleName();
    Kind kindAnnotation = (Kind)modelClass.getAnnotation(Kind.class);
    if(kindAnnotation == null) {
      throw new ModelException("@Kind annotation missing on supplied class");
    }
    Cached cachedAnnotation = (Cached)modelClass.getAnnotation(Cached.class);
    isCached = cachedAnnotation != null;
    cacheNamespace = isCached ? (cachedAnnotation.namespace().equals("") ? null : cachedAnnotation.namespace()) : null ;

    Field[] fields = modelClass.getDeclaredFields();
    Field idFieldTemp = null;
    for (Field field : fields) {
      Property property = field.getAnnotation(Property.class);
      if(property == null) {
        Id id = field.getAnnotation(Id.class);
        if(id != null) {
          idFieldTemp = field;
          if(!String.class.equals(idFieldTemp.getType()) && !Long.class.equals(idFieldTemp.getType())) {
            throw new ModelException("invalid @Id type: must be String or Long");
          }
        }
      }
      //todo: put in parent processing logic
      FieldConverter converter = ConverterFactory.getFieldConverter(field);
      properties.put(field.getName(), converter);
    }
    if(idFieldTemp == null) {
      throw new ModelException("class doesn't have an @Id field");
    }

    idField = idFieldTemp;
  }

  public String getName() {
    return name;
  }

  public Class getModelClass() {
    return modelClass;
  }

  public boolean isCached() {
    return isCached;
  }

  public String getCacheNamespace() {
    return cacheNamespace;
  }

  public FieldConverter getConverter(String property) {
    return properties.get(property);
  }

  public String getIdFieldName() {
    return idField.getName();
  }
}
