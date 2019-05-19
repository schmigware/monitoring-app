package org.cit.mcaleerj.thesis.management.service.exception;

/**
 * Management service base exception.
 */
public class ManagementServiceException extends Exception {

  /**
   * ManagementServiceException constructor.
   *
   */
  public ManagementServiceException() {
    super();
  }


  /**
   * ManagementServiceException constructor.
   *
   * @param message Exception message.
   *
   */
  public ManagementServiceException(String message) {
    super(message);
  }


  /**
   * ManagementServiceException constructor.
   *
   * @param message Exception message.
   * @param throwable Exception cause.
   *
   */
  public ManagementServiceException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
