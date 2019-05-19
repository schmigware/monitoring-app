package org.cit.mcaleerj.thesis.monitorservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Message model.
 *
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "messages")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Message {

  /*
   * ID.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false)
  private long id;

  /*
   * The environment UUID.
   */
  @Column(name = "environment_uuid", nullable = false, updatable = false)
  private UUID environmentUuid;

  /*
   * The edge label.
   */
  @Column(name = "edge_label", nullable = false, updatable = false)
  private String edgeLabel;

  /*
   * The source node name.
   */
  @Column(name = "sourceNode", updatable = false)
  private String sourceNode;

  /**
   * Message timestamp.
   */
  @Column(name = "timestamp", nullable = false, updatable = false)
  private Timestamp timestamp;

  /**
   * Message content.
   */
  @Type(type = "jsonb")
  @Column(name = "content", columnDefinition = "jsonb")
  private String messageContent;

}
