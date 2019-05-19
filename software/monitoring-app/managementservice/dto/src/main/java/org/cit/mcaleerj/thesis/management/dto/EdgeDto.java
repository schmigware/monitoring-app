package org.cit.mcaleerj.thesis.management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdgeDto {

  @GraphQLQuery(name = "id", description = "The edge identifier.")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private long id;

  @GraphQLQuery(name = "label", description = "The edge label.")
  String label;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @GraphQLQuery(name = "source", description = "The source node.")
  String source;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @GraphQLQuery(name = "target", description = "The target node.")
  String target;

}
