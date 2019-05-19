package org.cit.mcaleerj.thesis.aggregationservice.job;

import org.cit.mcaleerj.thesis.aggregationservice.domain.EnvironmentAggregation;

@FunctionalInterface
public interface AggregationListener {

  void aggregationCreated(EnvironmentAggregation snapshot);

}
