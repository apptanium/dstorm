package com.apptanium.api.dstorm.annotation;

import java.lang.annotation.*;

/**
 * @author saurabh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface DateFormat {
  String value();
}
