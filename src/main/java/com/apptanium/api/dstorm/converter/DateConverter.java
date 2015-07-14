package com.apptanium.api.dstorm.converter;

import com.google.appengine.api.datastore.Entity;
import com.google.gson.JsonObject;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @author saurabh
 */
public class DateConverter extends AbstractConverter<Date> {
  /**
   * the formatter for the JSON interconversion; this does not impact the resolution
   * at which the time is stored in the database.
   */
  private final DateTimeFormatter formatter;

  public DateConverter(String property, boolean indexed, boolean required, boolean removeNull, DateTimeFormatter formatter) {
    super(property, indexed, required, removeNull);
    this.formatter = formatter;
  }

  @Override
  public Date getValue(JsonObject json, Date alternate) {
    if(!json.has(property)) {
      return alternate;
    }
    String value = json.get(property).getAsString();
    Date date = formatter.parseDateTime(value).toDate();
    return date;
  }

  @Override
  public Date getValue(Entity entity, Date alternate) {
    Date val = (Date) entity.getProperty(property);
    return val == null ? alternate : val;
  }

  @Override
  public void setValue(JsonObject json, Date value) {
    json.addProperty(property, formatter.print(value.getTime()));
  }
}
