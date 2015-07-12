package com.apptanium.api.dstorm.annotation;

import java.lang.annotation.*;

/**
 * @author saurabh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface Property {
  boolean indexed();
  boolean required();
  boolean removeNull();
}
