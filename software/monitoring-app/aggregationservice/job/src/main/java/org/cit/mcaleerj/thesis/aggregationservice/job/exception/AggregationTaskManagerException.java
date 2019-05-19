package org.cit.mcaleerj.thesis.aggregationservice.job.exception;

/**
 * Aggregation task manager base exception.
 */
public class AggregationTaskManagerException extends Exception {

  /**
   * AggregationTaskManagerException constructor.
   *
   */
  public AggregationTaskManagerException() {
    super();
  }


  /**
   * AggregationTaskManagerException constructor.
   *
   * @param message Exception message.
   *
   */
  public AggregationTaskManagerException(String message) {
    super(message);
  }


  /**
   * AggregationTaskManagerException constructor.
   *
   * @param message Exception message.
   * @param throwable Exception cause.
   *
   */
  public AggregationTaskManagerException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
