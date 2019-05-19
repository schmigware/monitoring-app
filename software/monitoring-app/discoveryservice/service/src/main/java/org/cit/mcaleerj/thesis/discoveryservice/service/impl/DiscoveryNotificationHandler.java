package org.cit.mcaleerj.thesis.discoveryservice.service.impl;

import lombok.NonNull;
import org.cit.mcaleerj.thesis.discoveryservice.service.DiscoveryListener;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles dispatch of discovery notifications.
 */
class DiscoveryNotificationHandler {

  /*
   * Logging.
   */
  private static final Logger log = LoggerFactory.getLogger(DiscoveryNotificationHandler.class);

  private Map<EnvironmentDto, List<DiscoveryListener>> listenerMap = Collections.synchronizedMap(new HashMap<>());

  void addDiscoveryListener(@NonNull final DiscoveryListener listener, @NonNull final EnvironmentDto environment) {

    this.listenerMap.computeIfAbsent(environment, k -> new ArrayList<>());
    final List<DiscoveryListener> listeners = this.listenerMap.get(environment);
    listeners.add(listener);
  }

  void removeDiscoveryListener(@NonNull final DiscoveryListener listener, @NonNull final EnvironmentDto environment) {
    List<DiscoveryListener> listeners = this.listenerMap.get(environment);
    if (listeners != null) {
      listeners.remove(listener);
    }
  }

  void topologyDiscovered(@NonNull final EnvironmentDto environment, @NonNull final TopologyDto topology) {
    final List<DiscoveryListener> listeners = this.listenerMap.get(environment);
    if (!CollectionUtils.isEmpty(listeners)) {
      log.info(MessageFormat.format("Notifying {0} discovery listener(s).", listeners.size()));
      listeners.forEach(listener -> listener.topologyDiscovered(topology));
    }
  }
}
