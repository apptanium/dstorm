package com.apptanium.api.dstorm.converter;

import com.apptanium.api.dstorm.annotation.DateFormat;
import com.apptanium.api.dstorm.annotation.Property;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.search.GeoPoint;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author saurabh
 */
public class ConverterFactory {
  private static final Set<Class> allowedClasses = new HashSet<>();
  static {
    allowedClasses.add(Boolean.class);
    allowedClasses.add(Date.class);
    allowedClasses.add(Double.class);
    allowedClasses.add(EmbeddedEntity.class);
    allowedClasses.add(Enum.class);
    allowedClasses.add(GeoPt.class);
    allowedClasses.add(Key.class);
    allowedClasses.add(Long.class);
    allowedClasses.add(ShortBlob.class);
    allowedClasses.add(String.class);
    allowedClasses.add(Text.class);
  }

  public static FieldConverter getFieldConverter(Field field) {
    Class type = field.getType();
    Property property = field.getAnnotation(Property.class);
    FieldConverter converter = null;

    if(!allowedClasses.contains(type)) {
      throw new ConverterCreationException("field type '"+ type.getName() +"' is not supported. Please choose a supported type");
    }

    if(type.equals(Boolean.class)) {
      converter = new BooleanConverter(field.getName(), property.indexed(), property.required(), property.removeNull());
    }
    else if(type.equals(Date.class)) {
      String format;
      DateFormat dateFormat = field.getAnnotation(DateFormat.class);
      if(dateFormat == null || dateFormat.value() == null || dateFormat.value().equals("")) {
        format = "yyyyMMddHHmmss.SSS";
      }
      else {
        format = dateFormat.value();
      }
      DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
      converter = new DateConverter(field.getName(), property.indexed(), property.required(), property.removeNull(), formatter);
    }
    else if(type.equals(Double.class)) {
      converter = new DoubleConverter(field.getName(), property.indexed(), property.required(), property.removeNull());
    }
    else if(type.equals(EmbeddedEntity.class)) {
      converter = new EmbeddedEntityConverter(field.getName(), false, property.required(), property.removeNull());
    }
    else if(type.isEnum()) {
      converter = new EnumConverter(field.getName(), property.indexed(), property.required(), property.removeNull(), type);
    }
    else if(type.equals(GeoPoint.class)) {
      converter = new GeoConverter(field.getName(), property.indexed(), property.required(), property.removeNull());
    }
    else if(type.equals(Key.class)) {
      converter = new KeyConverter(field.getName(), property.indexed(), property.required(), property.removeNull());
    }
    else if(type.equals(Long.class)) {
      converter = new LongConverter(field.getName(), property.indexed(), property.required(), property.removeNull());
    }
    else if(type.equals(ShortBlob.class)) {
      converter = new ShortBlobConverter(field.getName(), property.indexed(), property.required(), property.removeNull());
    }
    else if(type.equals(String.class)) {
      converter = new StringConverter(field.getName(), property.indexed(), property.required(), property.removeNull());
    }
    else if(type.equals(Text.class)) {
      converter = new TextConverter(field.getName(), property.indexed(), property.required(), property.removeNull());
    }
    if(converter == null) {
      throw new ConverterCreationException("unknown error creating converter");
    }
    return converter;
  }
}
