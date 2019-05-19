package org.cit.mcaleerj.thesis.management.dto;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * The environment filter DTO.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvironmentFilterDto {

  /*
   * Environment UUID.
   */
  @GraphQLQuery(name = "uuid", description = "The environment uuid.")
  private UUID uuid;

}
