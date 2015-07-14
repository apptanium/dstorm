package com.apptanium.api.dstorm;

import com.apptanium.api.dstorm.converter.BooleanConverterTest;
import com.apptanium.api.dstorm.converter.DateConverterTest;
import com.apptanium.api.dstorm.model.MapperModelTest;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author saurabh
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BooleanConverterTest.class,
        DateConverterTest.class,
        MapperModelTest.class,
})
public class TestSuite {

  private static final LocalServiceTestHelper dataStoreHelper =
          new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy());

  private static final LocalServiceTestHelper memCacheHelper =
          new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());


  @BeforeClass
  public static void setup() {
    System.out.println(">>> setup");
    dataStoreHelper.setUp();
    memCacheHelper.setUp();
  }

  @AfterClass
  public static void teardown() {
    System.out.println(">>> teardown");
    dataStoreHelper.tearDown();
    memCacheHelper.tearDown();
  }
}
