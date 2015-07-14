package com.apptanium.api.dstorm.converter;

/**
 * @author saurabh
 */
public class ConverterCreationException extends RuntimeException {

  public ConverterCreationException() {
    super();
  }

  public ConverterCreationException(String message) {
    super(message);
  }

  public ConverterCreationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConverterCreationException(Throwable cause) {
    super(cause);
  }

  protected ConverterCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
