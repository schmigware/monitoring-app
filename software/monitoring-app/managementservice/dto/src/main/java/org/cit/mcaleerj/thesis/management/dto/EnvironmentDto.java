package org.cit.mcaleerj.thesis.management.dto;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The environment DTO.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EnvironmentDto {

  /*
   * UUID.
   */
  @GraphQLQuery(name = "uuid", description = "The environment identifier.")
  private UUID uuid;

  /*
   * Environment name.
   */
  @GraphQLQuery(name = "name", description = "The environment name.")
  private String name;

  /**
   * Environment profileId id.
   */
  @GraphQLQuery(name = "profileId", description = "The environment profileId id.")
  private String profileId;

  /**
   * Configuration properties.
   */
  @GraphQLQuery(name = "configuration", description = "The environment configuration.")
  private List<ConfigurationPropertyDto> configurationProperties = new ArrayList<>();

}
