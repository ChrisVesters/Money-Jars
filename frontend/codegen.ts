import type { CodegenConfig } from "@graphql-codegen/cli";

const config: CodegenConfig = {
	overwrite: true,
	schema: "http://localhost:10020/graphql",
	documents: "src/**/*.graphql",
	generates: {
		"src/gql/": {
			preset: "client",
			plugins: [],
			config: {
				useTypeImports: true,
				enumType: "const"
			}
		}
	}
};

export default config;
