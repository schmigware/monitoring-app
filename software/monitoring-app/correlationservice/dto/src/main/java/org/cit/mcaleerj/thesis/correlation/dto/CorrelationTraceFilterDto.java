package org.cit.mcaleerj.thesis.correlation.dto;

import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * The correlation trace filter DTO.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorrelationTraceFilterDto {

  /*
   * Environment UUID.
   */
  @GraphQLNonNull
  @GraphQLQuery(name = "environmentUUID", description = "The environment uuid.")
  private UUID environmentUUID;

  /*
   * Message field name.
   */
  @GraphQLNonNull
  @GraphQLQuery(name = "field", description = "The message field name.")
  private String field;

}
