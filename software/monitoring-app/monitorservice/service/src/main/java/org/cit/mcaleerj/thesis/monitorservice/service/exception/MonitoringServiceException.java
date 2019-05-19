package org.cit.mcaleerj.thesis.monitorservice.service.exception;

/**
 * Monitoring service base exception.
 */
public class MonitoringServiceException extends Exception {

  /**
   * MonitoringServiceException constructor.
   *
   */
  public MonitoringServiceException() {
    super();
  }


  /**
   * MonitoringServiceException constructor.
   *
   * @param message Exception message.
   *
   */
  public MonitoringServiceException(String message) {
    super(message);
  }


  /**
   * MonitoringServiceException constructor.
   *
   * @param message Exception message.
   * @param throwable Exception cause.
   *
   */
  public MonitoringServiceException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
