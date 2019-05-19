package org.cit.mcaleerj.thesis.management.client.model;

import lombok.Data;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetMonitoredEnvironmentsResponse {

  private Data data;
  private List<String> errors;

  public List<EnvironmentDto> getEnvironments() {
    return this.data.monitored;
  }

  @lombok.Data
  private static class Data {
    private List<EnvironmentDto> monitored = new ArrayList<>();
  }

}
