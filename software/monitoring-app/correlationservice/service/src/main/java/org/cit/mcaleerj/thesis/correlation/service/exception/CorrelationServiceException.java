package org.cit.mcaleerj.thesis.correlation.service.exception;

/**
 * Correlation service base exception.
 */
public class CorrelationServiceException extends Exception {

  /**
   * CorrelationServiceException constructor.
   *
   */
  public CorrelationServiceException() {
    super();
  }


  /**
   * CorrelationServiceException constructor.
   *
   * @param message Exception message.
   *
   */
  public CorrelationServiceException(String message) {
    super(message);
  }


  /**
   * CorrelationServiceException constructor.
   *
   * @param message Exception message.
   * @param throwable Exception cause.
   *
   */
  public CorrelationServiceException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
