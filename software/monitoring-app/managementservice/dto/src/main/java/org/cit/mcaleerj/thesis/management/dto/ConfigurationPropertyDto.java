package org.cit.mcaleerj.thesis.management.dto;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Environment configuration property DTO.
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ConfigurationPropertyDto {

  /*
   * Property name.
   */
  @GraphQLQuery(name = "name", description = "The configuration property name.")
  private String name;

  /*
   * Property value.
   */
  @GraphQLQuery(name = "value", description = "The configuration property value.")
  private String value;

}
