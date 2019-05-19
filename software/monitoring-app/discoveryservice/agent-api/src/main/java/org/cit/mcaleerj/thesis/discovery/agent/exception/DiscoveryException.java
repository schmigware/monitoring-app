package org.cit.mcaleerj.thesis.discovery.agent.exception;

/**
 * Discovery exception.
 */
public class DiscoveryException extends Exception {

  /**
   * DiscoveryException constructor.
   *
   */
  public DiscoveryException() {
    super();
  }


  /**
   * DiscoveryException constructor.
   *
   * @param message Exception message.
   *
   */
  public DiscoveryException(String message) {
    super(message);
  }


  /**
   * DiscoveryException constructor.
   *
   * @param message Exception message.
   * @param throwable Exception cause.
   *
   */
  public DiscoveryException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
