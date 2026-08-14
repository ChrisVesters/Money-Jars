package com.cvesters.moneyjars.transaction;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import com.cvesters.moneyjars.transaction.dto.PaymentTransactionDto;

@Configuration
public class TransactionGraphqlConfig {

	@Bean
	RuntimeWiringConfigurer transactionTypeResolver() {
		return wiring -> wiring.type("Transaction",
				type -> type.typeResolver(env -> {
					final Object source = env.getObject();

					return Optional.ofNullable(getTypeName(source))
							.map(name -> env.getSchema().getObjectType(name))
							.orElse(null);
				}));
	}

	private static String getTypeName(final Object source) {
		return switch (source) {
			case PaymentTransactionDto _ -> "PaymentTransaction";
			default -> null;
		};
	}
}
