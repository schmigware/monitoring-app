package org.cit.mcaleerj.thesis.management.client.model;

import lombok.Data;
import org.cit.mcaleerj.thesis.management.dto.EnvironmentProfileDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetEnvironmentProfilesResponse {

  private Data data;
  private List<String> errors;

  public List<EnvironmentProfileDto> getEnvironmentProfiles() {
    return this.data.environmentProfiles;
  }

  @lombok.Data
  private static class Data {
    private List<EnvironmentProfileDto> environmentProfiles = new ArrayList<>();
  }

}
