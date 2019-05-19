package org.cit.mcaleerj.thesis.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Monitoring Task DTO.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitoringTaskDto {

  /*
   * UUID.
   */
  private UUID uuid;

  /*
   * Monitored environment.
   */
  private EnvironmentDto environment;

}
