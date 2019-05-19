package org.cit.mcaleerj.thesis.management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class TopologyDto {

  /*
   * Topology UUID.
   */
  @GraphQLQuery(name = "uuid", description = "The topology identifier.")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private UUID uuid;

  /*
   * Environment UUID.
   */
  @NonNull
  @GraphQLQuery(name = "environmentUuid", description = "The environment identifier.")
  private UUID environmentUuid;

  /*
   * Environment name.
   */
  @NonNull
  @GraphQLQuery(name = "environmentName", description = "The environment name.")
  private String environmentName;

  /*
   * Node list.
   */
  @NonNull
  @GraphQLQuery(name = "nodes", description = "The topology nodes.")
  private List<NodeDto> nodes = new ArrayList<>();

  /*
   * Edge list.
   */
  @NonNull
  @GraphQLQuery(name = "edges", description = "The topology edges.")
  private List<EdgeDto> edges = new ArrayList<>();
}
