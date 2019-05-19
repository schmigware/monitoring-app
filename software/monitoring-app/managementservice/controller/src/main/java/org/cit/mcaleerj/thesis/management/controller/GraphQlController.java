package org.cit.mcaleerj.thesis.management.controller;

import graphql.ErrorType;
import graphql.ExceptionWhileDataFetching;
import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import io.leangen.graphql.metadata.strategy.query.PublicResolverBuilder;
import io.leangen.graphql.metadata.strategy.value.jackson.JacksonValueMapperFactory;
import org.cit.mcaleerj.thesis.management.resolver.CommandResolver;
import org.cit.mcaleerj.thesis.management.service.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Management Service GraphQL endpoint controller.
 *
 */
@RestController
public class GraphQlController {

  private static final Logger log = LoggerFactory.getLogger(GraphQlController.class);

  /*
   * Request query, operation and variables.
   */
  private static final String QUERY = "query";
  private static final String OPERATION_NAME = "operationName";
  private static final String VARS = "variables";

  /*
   * Graphql executor.
   */
  private GraphQL graphQL;

  /**
   * GraphQlController constructor.
   *
   * @param managementService  The management service.
   *
   */
  public GraphQlController(final ManagementService managementService) {

    GraphQLSchema schema = new GraphQLSchemaGenerator()
        .withResolverBuilders(
            new AnnotatedResolverBuilder(),
            new PublicResolverBuilder("org.cit.mcaleerj.thesis.management"))
        .withOperationsFromSingleton(new CommandResolver(managementService))
        .withValueMapperFactory(new JacksonValueMapperFactory("org.cit.mcaleerj.thesis.management.dto"))
        .generate();

    this.graphQL = GraphQL.newGraphQL(schema).build();
  }


  /**
   * Graphql endpoint handler.
   */
  @PostMapping(
      value = "/graphql",
      consumes = APPLICATION_JSON_UTF8_VALUE,
      produces = APPLICATION_JSON_UTF8_VALUE
  )
  @ResponseBody
  @CrossOrigin
  public Object indexGraphQl(@RequestBody Map<String, Object> request) {

    final String query = (String) request.get(QUERY);
    final String operationName = (String) request.get(OPERATION_NAME);
    @SuppressWarnings("unchecked")
    Map<String, Object> variables = (Map<String, Object>) request.get(VARS);

    if (variables == null) {
      variables = new HashMap<>();
    }

    log.debug("GraphQL query started");

    final ExecutionResult executionResult = this.graphQL.execute(query, operationName,  null, variables);

    log.debug("GraphQL query end");

    if (!executionResult.getErrors().isEmpty()) {
      return sanitize(executionResult);
    }

    return executionResult;
  }

  /**
   * This extracts and logs the errors from a failed execution.
   *
   * @param executionResult The execution result.
   *
   * @return  The execution result with the list of errors.
   *
   * @since 1.0.0M5
   */
  private ExecutionResult sanitize(ExecutionResult executionResult) {
    return new ExecutionResultImpl(
            executionResult
                    .getErrors()
                    .stream()
                    .peek(err -> log.error(err.getMessage()))
                    .map(this::sanitize)
                    .collect(Collectors.toList()));
  }

  /**
   * Failure cause extraction.
   *
   */
  private GraphQLError sanitize(final GraphQLError error) {
    if (error instanceof ExceptionWhileDataFetching) {
      return new GraphQLError() {
        @Override
        public String getMessage() {
          Throwable cause = ((ExceptionWhileDataFetching) error).getException().getCause();
          return cause instanceof InvocationTargetException
              ? ((InvocationTargetException) cause).getTargetException().getMessage()
              : cause.getMessage();
        }

        @Override
        public List<SourceLocation> getLocations() {
          return error.getLocations();
        }

        @Override
        public ErrorType getErrorType() {
          return error.getErrorType();
        }
      };
    }

    return error;
  }
}
