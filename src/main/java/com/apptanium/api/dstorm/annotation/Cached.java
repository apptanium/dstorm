package com.apptanium.api.dstorm.annotation;

import java.lang.annotation.*;

/**
 * @author saurabh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Cached {
  String namespace();
}
