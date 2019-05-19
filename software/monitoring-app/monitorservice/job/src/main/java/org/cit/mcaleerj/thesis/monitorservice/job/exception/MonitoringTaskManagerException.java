package org.cit.mcaleerj.thesis.monitorservice.job.exception;

/**
 * Monitoring task manager base exception.
 */
public class MonitoringTaskManagerException extends Exception {

  /**
   * MonitoringTaskManagerException constructor.
   *
   */
  public MonitoringTaskManagerException() {
    super();
  }


  /**
   * MonitoringTaskManagerException constructor.
   *
   * @param message Exception message.
   *
   */
  public MonitoringTaskManagerException(String message) {
    super(message);
  }


  /**
   * MonitoringTaskManagerException constructor.
   *
   * @param message Exception message.
   * @param throwable Exception cause.
   *
   */
  public MonitoringTaskManagerException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
