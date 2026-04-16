import { StrictMode } from "react";
import * as ReactDOM from "react-dom/client";

import { ApolloClient, HttpLink, InMemoryCache } from "@apollo/client";
import { ApolloProvider } from "@apollo/client/react";

import "./index.css";
import App from "./App.tsx";

const client = new ApolloClient({
	link: new HttpLink({
		uri: "http://localhost:7000/graphql"
	}),
	cache: new InMemoryCache()
});

const container = document.getElementById("root");
if (!container) {
	throw new Error("Root container missing in index.html");
}

const root = ReactDOM.createRoot(container);
root.render(
	<StrictMode>
		<ApolloProvider client={client}>
			<App />
		</ApolloProvider>
	</StrictMode>
);
