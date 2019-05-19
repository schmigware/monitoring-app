package org.cit.mcaleerj.thesis.management.dto;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * The environment profileId DTO.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EnvironmentProfileDto {

  /*
   * ID.
   */
  @GraphQLQuery(name = "id", description = "The environment profileId ID.")
  private String id;

  /*
   * Environment profileId name.
   */
  @GraphQLQuery(name = "name", description = "The environment profileId name.")
  private String name;

  /**
   * Configuration properties.
   */
  @GraphQLQuery(name = "configuration", description = "The environment configuration.")
  private List<ConfigurationPropertyDto> configurationProperties = new ArrayList<>();

}
