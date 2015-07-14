package com.apptanium.api.dstorm.converter;

import com.google.appengine.api.datastore.Entity;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

/**
 * @author saurabh
 */
public class DateConverterTest {

  @Test
  public void testConverter() {
    System.out.println(">>> date converter");
    Entity entity = new Entity("test", "test");
    DateConverter converter = new DateConverter("testDate", true, false, true, DateTimeFormat.forPattern("yyyyMMddHHmmss"));

  }
}
