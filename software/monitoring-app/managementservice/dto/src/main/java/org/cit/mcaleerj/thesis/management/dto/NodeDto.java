package org.cit.mcaleerj.thesis.management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeDto {

  @GraphQLQuery(name = "name", description = "The node name.")
  private String name;

}
