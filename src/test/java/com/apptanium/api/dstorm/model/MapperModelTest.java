package com.apptanium.api.dstorm.model;

import org.junit.Test;

/**
 * @author saurabh
 */
public class MapperModelTest {

  @Test
  public void testMapperModel() {
    MapperModel model = new MapperModel(TestEntityGood.class);
    System.out.println("model.getName() = " + model.getName());
    assert model.isCached();
    assert model.getCacheNamespace() == null;
  }
}
