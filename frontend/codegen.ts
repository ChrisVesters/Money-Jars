import type { CodegenConfig } from "@graphql-codegen/cli";

const config: CodegenConfig = {
	overwrite: true,
	schema: "http://localhost:7000/graphql",
	documents: "src/**/*.graphql",
	generates: {
		"src/gql/": {
			preset: "client",
			plugins: [],
			config: {
				useTypeImports: true
			}
		}
	}
};

export default config;
