# Frontend coding guidelines

This file captures the frontend conventions already used in the current React + TypeScript + Vite app and should be followed for all frontend feature development.

## Structure
- Keep feature code together in directories like `src/pages/`, `src/components/`, `src/common/`, `src/jar/`, and `src/transaction/`.
- Keep styles close to the component they belong to using adjacent `*.css` files.
- Use the configured Vite aliases:
  - `@assets` for icons and static assets
  - `@gql` for generated GraphQL documents and types
  - `@src` for imports relative to the src directory
- Keep component modules focused: pages orchestrate routes and child components, features like `jar/` and `transaction/` encapsulate related UI and logic, components are reusable pieces, and common utilities are generic.

## TypeScript and React patterns
- Use `type { ... }` imports for types and `import type` wherever possible.
- Use function components with explicit return type `JSX.Element`.
- Use `React 19` with the latest hooks and component patterns.
- Use hooks with typed state and event handlers; type event handlers properly (e.g., `React.FormEvent`, `React.KeyboardEvent<HTMLDialogElement>`).
- Use `const` for stable functions and component props destructuring.
- Define component props as `Readonly<Props>` types for immutability enforcement.
- Keep components small and move logic to hooks or child components when it grows.
- Prefer semantic HTML and accessible form markup: use `label htmlFor`, `button type`, `aria-label`, visible error messages, and native HTML elements like `<dialog>` for modals.
- Keep boolean state names clear (`isOpen`, `selectedJar`, `selectedCardId`).
- Keep effects clean and re-run only on required dependencies; use `useRef` for DOM element references when needed.
- React Compiler is enabled via `babel-plugin-react-compiler` to optimize component memoization automatically.

Example component pattern:
```tsx
import { useState, useRef, type JSX } from "react";

type Props = {
	isOpen: boolean;
	onToggle: () => void;
	onClose?: () => void;
};

const Sidebar = (props: Readonly<Props>): JSX.Element => {
	const [localState, setLocalState] = useState(false);
	const elementRef = useRef<HTMLDivElement>(null);

	const handleToggle = (): void => {
		props.onToggle();
	};

	const handleKeyDown = (event: React.KeyboardEvent<HTMLDivElement>): void => {
		if (event.key === "Escape" && props.onClose) {
			props.onClose();
		}
	};

	return (
		<aside 
			ref={elementRef}
			className={`sidebar ${props.isOpen ? "open" : "closed"}`}
			onKeyDown={handleKeyDown}>
			{/* ... */}
		</aside>
	);
}

export default Sidebar;
```

## Routing
- Use React Router v7 (`react-router-dom`) for client-side routing.
- Define page-level routes in `src/pages/` using component files (e.g., `DashboardPage.tsx`, `JarsPage.tsx`).
- Use `<BrowserRouter>`, `<Routes>`, and `<Route>` to structure the app.
- Use `Link` and `useLocation` from React Router for navigation and current location awareness.

Example routing pattern:
```tsx
import { BrowserRouter, Routes, Route } from "react-router-dom";
import DashboardPage from "./pages/DashboardPage";
import JarsPage from "./pages/JarsPage";

const App = (): JSX.Element => (
	<BrowserRouter>
		<Routes>
			<Route path="/" element={<DashboardPage />} />
			<Route path="/jars" element={<JarsPage />} />
		</Routes>
	</BrowserRouter>
);
```

## Internationalization
- Use `react-i18next` for multi-language support.
- Import `useTranslation` hook in components that need translations.
- Call `const { t } = useTranslation()` and use `t("key")` to get translated strings.
- Translation keys should be lowercase and use dot notation for nested keys.

Example i18n usage:
```tsx
const { t } = useTranslation();
return <Page title={t("jars")} />;
```

## GraphQL and data fetching
- Use generated GraphQL documents and types from `@gql/graphql`.
- Always import queries/mutations using `GetXDocument`, `CreateXDocument`, `UpdateXDocument`, etc.
- Prefer `useQuery(GetXDocument)` and `useMutation(UpdateXDocument)` from `@apollo/client/react`.
- Pass typed variables explicitly.
- Refetch relevant queries after mutations if the cache is not normalized for the update.
- Keep error handling and loading behavior visible in the UI.

Example GraphQL usage:
```tsx
import { useMutation, useQuery } from "@apollo/client/react";
import {
	CreateJarDocument,
	GetJarsDocument,
	type UpdateJar
} from "@gql/graphql";

const Overview = (): JSX.Element => {
	const { data } = useQuery(GetJarsDocument);
	const [createJar] = useMutation(CreateJarDocument);

	const handleCreate = async (jar: UpdateJar): Promise<void> => {
		await createJar({
			variables: { req: jar },
			refetchQueries: [{ query: GetJarsDocument }]
		});
	};

	return <>{/* ... */}</>;
};
```

## Forms and validation
- Keep form state local to the form component using `useState`.
- Validate required fields before submitting.
- Display inline error messages next to invalid inputs.
- Use controlled inputs for form field values (e.g., `value={name}` with `onChange={e => setName(e.target.value)}`).
- Keep submit handlers async and handle API errors gracefully.
- Type form event handlers properly: `const handleSubmit = async (e: React.FormEvent): Promise<void> => { /* ... */ }`
- Reset form state when opening a form for creation vs. editing (use `useEffect` with the input data as dependency).

Example form pattern:
```tsx
import { useEffect, useState, type JSX } from "react";
import type { Jar, UpdateJar } from "@gql/graphql";

type JarFormProps = {
	jar?: Jar;
	onClose: () => void;
	onConfirm: (jar: UpdateJar) => void;
};

const JarForm = (props: Readonly<JarFormProps>): JSX.Element => {
	const [name, setName] = useState(props.jar?.name ?? "");
	const [description, setDescription] = useState(props.jar?.description ?? "");

	useEffect(() => {
		setName(props.jar?.name ?? "");
		setDescription(props.jar?.description ?? "");
	}, [props.jar]);

	const handleSubmit = async (e: React.FormEvent): Promise<void> => {
		e.preventDefault();
		
		if (!name.trim()) return; // TODO: Show error

		props.onConfirm({ name, description });
	};

	return (
		<form onSubmit={handleSubmit}>
			<div className="form-group">
				<label htmlFor="name">Name</label>
				<input
					id="name"
					type="text"
					value={name}
					onChange={e => setName(e.target.value)}
				/>
			</div>
			<button type="submit">Save</button>
			<button type="button" onClick={props.onClose}>Cancel</button>
		</form>
	);
};

export default JarForm;
```

## Styling and Modals
- Use module-level CSS files imported from the component or page (e.g., `Sidebar.css` imported in `Sidebar.tsx`).
- Keep class names descriptive and scoped to the feature.
- Use utility CSS only when it is shared across multiple features.
- Prefer layout classes in the component markup and turn repeated visual patterns into reusable CSS rules.
- Use native HTML `<dialog>` elements for modals with `showModal()` and `close()` methods; wrap in a `Modal` utility component for consistent behavior.
- Handle dialog lifecycle in `useEffect` based on an `isOpen` prop and call `onClose` callback on Escape key or backdrop clicks.

Example modal pattern:
```tsx
import { useEffect, useRef, type JSX } from "react";

type ModalProps = {
	isOpen: boolean;
	onClose?: () => void;
	children: React.ReactNode;
};

const Modal = (props: Readonly<ModalProps>): JSX.Element => {
	const modalRef = useRef<HTMLDialogElement>(null);

	useEffect(() => {
		const modalElement = modalRef.current;
		if (!modalElement) return;

		if (props.isOpen) {
			modalElement.showModal();
		} else {
			modalElement.close();
		}
	}, [props.isOpen]);

	const handleKeyDown = (event: React.KeyboardEvent<HTMLDialogElement>): void => {
		if (event.key === "Escape" && props.onClose) {
			props.onClose();
		}
	};

	return (
		<dialog ref={modalRef} onKeyDown={handleKeyDown}>
			{props.children}
		</dialog>
	);
};

export default Modal;
```

## Component and code hygiene
- Avoid large inline JSX blocks by splitting UI into smaller components when needed.
- Keep TODO comments limited and meaningful; remove them once addressed.
- Avoid deeply nested ternaries and prefer early returns in event handlers.
- Use `useEffect` only for side effects; do not derive UI state imperatively unless necessary.

## Test guidance
- Add frontend tests for new components, pages, and GraphQL hooks when the project test setup is available.
- Prefer component-level tests with a React test runner and DOM assertions.
- Test user interactions, form validation, and API integration through mocked GraphQL documents.

## Tooling and linting
- Run `npm run lint` to ensure ESLint passes (uses `@eslint/js`, TypeScript ESLint, `eslint-plugin-react-hooks`, and `eslint-plugin-react-refresh`).
- Run `npm run codegen` after GraphQL schema or document changes.
- Run `npm run build` to compile TypeScript and build with Vite.
- Use the existing Vite setup and path aliases from `vite.config.ts`: `@assets`, `@gql`, and `@src`.
- ESLint config extends recommended configs for best practices and React 19 compatibility.

## Practical rule of thumb
When adding frontend work, follow the current app structure:
- Keep React UI in `src/` organized by feature (`pages/`, `jar/`, `transaction/`, etc.)
- Use Vite aliases (`@assets`, `@gql`, `@src`) for clean imports
- Keep types explicit with `Readonly<Props>` for component props and proper event handler typing
- Prefer generated GraphQL types from `@gql/graphql` over manual shapes
- Use React Router for page navigation and i18n for translations
- Leverage the React Compiler for automatic performance optimization
- Style components with adjacent CSS files and native HTML elements like `<dialog>` for accessibility
