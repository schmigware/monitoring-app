
package org.cit.mcaleerj.thesis.correlation.resolver;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import lombok.NonNull;
import org.cit.mcaleerj.thesis.correlation.dto.CorrelationTraceDto;
import org.cit.mcaleerj.thesis.correlation.dto.CorrelationTraceFilterDto;
import org.cit.mcaleerj.thesis.correlation.service.CorrelationService;
import org.cit.mcaleerj.thesis.correlation.service.exception.CorrelationServiceException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * GraphQL query/mutation resolver.
 */
@Component
public class CommandResolver {

  /*
   * The correlation service.
   */
  private final CorrelationService correlationService;

  /**
   * Constructor.
   *
   * @param correlationService {@link CorrelationService} instance.
   */
  public CommandResolver(@NonNull final CorrelationService correlationService) {
    this.correlationService = correlationService;
  }

  /**
   * Get correlation traces.
   *
   * @param filter {@link CorrelationTraceFilterDto} instance
   *
   * @return {@link CorrelationTraceDto} instance
   *
   * @throws CorrelationServiceException
   */
  @GraphQLMutation(name = "getTraces")
  public List<CorrelationTraceDto> getCorrelationTraces(@GraphQLArgument(name = "filter")
                                                    final CorrelationTraceFilterDto filter) throws CorrelationServiceException {
    return this.correlationService.getTraces(filter.getEnvironmentUUID(), filter.getField());
  }

}
