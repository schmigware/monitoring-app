package org.cit.mcaleerj.thesis.correlation.dto;

import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * MessageDto model.
 *
 */
@NoArgsConstructor
@Data
public class MessageDto {

  /*
   * The edge label.
   */
  @GraphQLQuery(name = "edgeLabel", description = "The edge label.")
  private String edgeLabel;

  /*
   * The source node name.
   */
  @GraphQLQuery(name = "sourceNode", description = "The source node name.")
  private String sourceNode;

  /**
   * The timestamp.
   */
  @GraphQLQuery(name = "timestamp", description = "The timestamp.")
  private Timestamp timestamp;

  /**
   * The content.
   */
  @GraphQLQuery(name = "messageContent", description = "The message content.")
  private String messageContent;

}
