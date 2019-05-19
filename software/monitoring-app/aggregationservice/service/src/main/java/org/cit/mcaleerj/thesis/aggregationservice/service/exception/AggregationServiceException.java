package org.cit.mcaleerj.thesis.aggregationservice.service.exception;

/**
 * Aggregation service base exception.
 */
public class AggregationServiceException extends Exception {

  /**
   * AggregationServiceException constructor.
   *
   */
  public AggregationServiceException() {
    super();
  }


  /**
   * AggregationServiceException constructor.
   *
   * @param message Exception message.
   *
   */
  public AggregationServiceException(String message) {
    super(message);
  }


  /**
   * AggregationServiceException constructor.
   *
   * @param message Exception message.
   * @param throwable Exception cause.
   *
   */
  public AggregationServiceException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
