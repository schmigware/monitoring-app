package org.cit.mcaleerj.thesis.testbed.validationservice.resolver;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import lombok.NonNull;
import org.cit.mcaleerj.thesis.testbed.validationservice.service.impl.DummyService;
import org.springframework.stereotype.Component;

/**
 * GraphQL query/mutation resolver.
 */
@Component
public class CommandResolver {

  /*
   * The service interface.
   */
  private final DummyService dummyService;

  /**
   * Constructor.
   *
   * @param dummyService {@link DummyService} instance.
   */
  public CommandResolver(@NonNull final DummyService dummyService) {
    this.dummyService = dummyService;
  }

  /**
   * Set processing delay.
   *
   * @param time Processing time in miliseconds
   * @return The effective processing delay
   */
  @GraphQLMutation(name = "setProcessingTime")
  public long setProcessingTime(
          @GraphQLArgument(name = "time") @GraphQLNonNull final long time) {
    return this.dummyService.setProcessingTime(time);
  }


}
