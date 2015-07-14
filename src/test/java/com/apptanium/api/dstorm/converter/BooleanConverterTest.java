package com.apptanium.api.dstorm.converter;

import com.google.appengine.api.datastore.Entity;
import org.junit.Test;

/**
 * @author saurabh
 */
public class BooleanConverterTest {

  @Test
  public void testConverter() {
    System.out.println(">>> boolean converter");
    Entity entity = new Entity("test", "test");
    BooleanConverter converter = new BooleanConverter("testBoolean", true, true, true);
    converter.setValue(entity, Boolean.FALSE);

    assert entity.hasProperty(converter.property());
    assert entity.getProperty(converter.property()).equals(Boolean.FALSE);
    assert !entity.isUnindexedProperty(converter.property());
    assert converter.getValue(entity, null) != null;
    assert converter.getValue(entity, Boolean.TRUE).equals(Boolean.FALSE);

    converter.setValue(entity, null);
    assert !entity.hasProperty(converter.property());
    assert converter.getValue(entity, Boolean.TRUE).equals(Boolean.TRUE);
  }
}
