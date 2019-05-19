package org.cit.mcaleerj.thesis.management.dto;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * The environment profileId filter DTO.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvironmentProfileFilterDto {

  /*
   * Profile id.
   */
  @GraphQLQuery(name = "id", description = "The environment profileId id.")
  private String id;

}
