package org.cit.mcaleerj.thesis.testbed.validationservice.service.impl;

public interface DummyService {

  /**
   * Process an incoming message.
   *
   * @param message message to process
   */
  void handleMessage(Object message);

  /**
   * Sets the message processing time.
   *
   * @param time processing time in miliseconds
   *
   * @return the effective processing time
   */
  long setProcessingTime(long time);
}
