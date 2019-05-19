package org.cit.mcaleerj.thesis.management.client.model;

import lombok.Data;
import org.cit.mcaleerj.thesis.management.dto.TopologyDto;

import java.util.List;

@Data
public class MergeTopologyResponse {

  private Data data;
  private List<String> errors;

  public TopologyDto getTopologyDto() {
    return this.data.updateTopology;
  }

  @lombok.Data
  private static class Data {
    private TopologyDto updateTopology;
  }

}
