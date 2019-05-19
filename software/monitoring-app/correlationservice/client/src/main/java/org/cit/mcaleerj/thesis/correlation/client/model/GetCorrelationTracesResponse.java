package org.cit.mcaleerj.thesis.correlation.client.model;

import lombok.Data;
import org.cit.mcaleerj.thesis.correlation.dto.CorrelationTraceDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetCorrelationTracesResponse {

  private Data data;

  private List<String> errors;

  public List<CorrelationTraceDto> getCorrelationTraces() {
    return this.data.getCorrelationTraces;
  }

  @lombok.Data
  private static class Data {
    private List<CorrelationTraceDto> getCorrelationTraces = new ArrayList<>();
  }

}
