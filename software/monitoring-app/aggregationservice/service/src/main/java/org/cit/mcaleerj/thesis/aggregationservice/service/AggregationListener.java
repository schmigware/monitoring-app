package org.cit.mcaleerj.thesis.aggregationservice.service;

import org.cit.mcaleerj.thesis.aggregationservice.dto.EnvironmentSnapshotDto;

public interface AggregationListener {

  void aggregationCreated(EnvironmentSnapshotDto snapshot);

}
