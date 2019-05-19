package org.cit.mcaleerj.thesis.management.client.exception;

/**
 * Management service base exception.
 */
public class ManagementServiceClientException extends Exception {

  /**
   * ManagementServiceException constructor.
   *
   */
  public ManagementServiceClientException() {
    super();
  }


  /**
   * ManagementServiceException constructor.
   *
   * @param message Exception message.
   *
   */
  public ManagementServiceClientException(String message) {
    super(message);
  }


  /**
   * ManagementServiceException constructor.
   *
   * @param message Exception message.
   * @param throwable Exception cause.
   *
   */
  public ManagementServiceClientException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
