package org.cit.mcaleerj.thesis.correlation.client.exception;

/**
 * Correlation service base exception.
 */
public class CorrelationServiceClientException extends Exception {

  /**
   * CorrelationServiceClientException constructor.
   *
   */
  public CorrelationServiceClientException() {
    super();
  }


  /**
   * CorrelationServiceClientException constructor.
   *
   * @param message Exception message.
   *
   */
  public CorrelationServiceClientException(String message) {
    super(message);
  }


  /**
   * CorrelationServiceClientException constructor.
   *
   * @param message Exception message.
   * @param throwable Exception cause.
   *
   */
  public CorrelationServiceClientException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
