package org.cit.mcaleerj.thesis.discoveryservice.service.exception;

/**
 * Discovery service base exception.
 */
public class DiscoveryServiceException extends Exception {

  /**
   * DiscoveryServiceException constructor.
   *
   */
  public DiscoveryServiceException() {
    super();
  }


  /**
   * DiscoveryServiceException constructor.
   *
   * @param message Exception message.
   *
   */
  public DiscoveryServiceException(String message) {
    super(message);
  }


  /**
   * DiscoveryServiceException constructor.
   *
   * @param message Exception message.
   * @param throwable Exception cause.
   *
   */
  public DiscoveryServiceException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
