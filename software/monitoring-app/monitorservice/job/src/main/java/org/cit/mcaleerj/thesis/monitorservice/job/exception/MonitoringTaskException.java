package org.cit.mcaleerj.thesis.monitorservice.job.exception;

/**
 * Monitoring task exception.
 */
public class MonitoringTaskException extends Exception {

  /**
   * MonitoringTaskException constructor.
   *
   */
  public MonitoringTaskException() {
    super();
  }


  /**
   * MonitoringTaskException constructor.
   *
   * @param message Exception message.
   *
   */
  public MonitoringTaskException(String message) {
    super(message);
  }


  /**
   * MonitoringTaskException constructor.
   *
   * @param message Exception message.
   * @param throwable Exception cause.
   *
   */
  public MonitoringTaskException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
