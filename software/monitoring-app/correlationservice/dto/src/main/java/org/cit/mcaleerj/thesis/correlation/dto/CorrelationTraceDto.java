package org.cit.mcaleerj.thesis.correlation.dto;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class CorrelationTraceDto {

  /*
   * MessageDto list.
   */
  @GraphQLQuery(name = "messages", description = "The correlated messages.")
  private List<MessageDto> messages = new ArrayList<>();
}
