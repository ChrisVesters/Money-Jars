package com.cvesters.moneyjars.config;

import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

import com.cvesters.moneyjars.common.exceptions.DuplicateEntityException;
import com.cvesters.moneyjars.common.exceptions.MissingEntityException;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

@Component
public class GraphqlExceptionResolver
		extends DataFetcherExceptionResolverAdapter {

	@Override
	protected GraphQLError resolveToSingleError(Throwable exception,
			DataFetchingEnvironment environment) {

		if (exception instanceof MissingEntityException _) {
			return GraphqlErrorBuilder.newError(environment)
					.message("Entity not found")
					.errorType(ErrorType.NOT_FOUND)
					.build();
		}

		if (exception instanceof DuplicateEntityException _) {
			return GraphqlErrorBuilder.newError(environment)
					.message("Entity already exists")
					.errorType(ErrorType.BAD_REQUEST)
					.build();
		}

		return null;
	}
}