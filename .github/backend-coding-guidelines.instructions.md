# Backend coding guidelines

This file captures the backend conventions already used in the current codebase and should be followed for all backend feature development.
The `jar` and `transaction` packages are the canonical templates for architecture, and new backend code should follow the same structure whether it is handwritten or generated.

## Structure
- Keep each feature in its own package under `backend/src/main/java/com/cvesters/moneyjars/<feature>/`.
- Mirror the existing split for new features: `controller`, `service`, `storage`, `bdo`, `dao`, and `dto`.
- Use package naming consistent with existing code, e.g. `com.cvesters.moneyjars.jar`, `com.cvesters.moneyjars.transaction`.
- Name repository interfaces `*Repository.java`, storage gateway classes `*StorageGateway.java`, and business services `*Service.java`.

## Layering and responsibilities
- Put GraphQL entry points in `*Controller.java` classes and annotate them with `@Controller`, `@QueryMapping`, and `@MutationMapping`.
- For nested GraphQL field resolution, use separate resolver classes with `@Controller` and `@SchemaMapping`.
- Keep controllers thin: convert request DTOs into action payloads, call service methods, and return response DTOs.
- Put business logic in `*Service.java` classes annotated with `@Service`.
- Put persistence access behind `*StorageGateway.java` classes that translate between JPA entities and business domain objects.
- Keep repository interfaces in `*Repository.java` and persistence entities in `*Dao.java`.
- Services should use domain-specific runtime exceptions such as `MissingEntityException` for not-found cases, while gateways should preserve persistence semantics.

Example controller + resolver pattern:

```java
@Controller
public class TransactionController {
    @MutationMapping
    public TransactionDto createTransaction(@Argument TransactionActionDto.Create req) {
        var created = transactionService.create(req.toBdo());
        return new TransactionDto(created);
    }
}

@Controller
public class TransactionResolver {
    @SchemaMapping(typeName = "Transaction", field = "jar")
    public JarDto jar(TransactionDto transaction) {
        return jarService.find(transaction.getJarId())
                .map(JarDto::new)
                .orElse(null);
    }
}
```

## Service / gateway pattern
- `*Service` methods should validate non-null inputs with `Objects.requireNonNull(...)`.
- `create(...)` methods should convert action payloads to BDOs and delegate persistence to the storage gateway.
- `update(...)` methods should first load the existing BDO, throw `MissingEntityException` if absent, apply the update action, and then save via the gateway.
- `delete(...)` methods should delegate deletion to the storage gateway without exposing persistence details.

Example service behavior:

```java
public Jar update(final long id, final JarAction.Update action) {
    Objects.requireNonNull(action);

    final Jar jar = storage.find(id)
            .orElseThrow(MissingEntityException::new);
    action.applyOn(jar);

    return storage.update(jar);
}
```

## BDO and action conventions
- Place business domain objects in `...bdo` packages.
- Use explicit constructors with validation rather than open, unvalidated data containers.
- Use records for action payloads and validate them in compact constructors.
- Group create/update action records inside an action container class such as `JarAction` or `TransactionAction`.
- Use `toBdo()` to convert create actions into new domain objects.
- Use `applyOn(...)` on update actions to mutate domain objects in a controlled, validated way.
- Domain constructors and setters should enforce invariants and reject invalid input early.

Example action record:

```java
public static record Update(String name, String description) {
    public Update {
        Validate.notBlank(name);
        Objects.requireNonNull(description);
    }

    public void applyOn(final Jar jar) {
        Objects.requireNonNull(jar);
        jar.setName(name);
        jar.setDescription(description);
    }
}
```

## DAO conventions
- Keep persistence entities in `...dao` packages.
- Annotate JPA entities with `@Entity` and `@Table`.
- Provide a JPA no-args constructor using `@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)`.
- Implement conversion helpers such as `toBdo()` and `updateWith(...)`.
- Keep DAO constructors null-safe and avoid exposing internal persistence state beyond getters.
- Prefer `Objects.requireNonNull(...)` in DAO constructors and updates.

Example DAO update helper:

```java
public void updateWith(final Jar bdo) {
    Objects.requireNonNull(bdo);
    this.name = bdo.getName();
    this.description = bdo.getDescription();
}
```

## DTO conventions
- Use records for request payloads and conversion helpers like `toBdo()`.
- Group request records in final DTO container classes such as `JarActionDto` or `TransactionActionDto`.
- Use simple response DTO classes with getters for responses.
- Use Lombok annotations such as `@Getter` and `@NoArgsConstructor` when needed for framework binding.
- Keep DTOs focused on data transfer only; do not put business logic in them.

Example request DTO:

```java
public final class JarActionDto {
    public static record Create(String name, String description) {
        public JarAction.Create toBdo() {
            return new JarAction.Create(name, description);
        }
    }
}
```

Example response DTO:

```java
@Getter
public class JarDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal balance;

    public JarDto(final Jar bdo) {
        Objects.requireNonNull(bdo);
        this.id = bdo.getId();
        this.name = bdo.getName();
        this.description = bdo.getDescription();
        this.balance = bdo.getBalance();
    }
}
```

## Testing conventions
- Use JUnit 5 with `@Nested` tests to group related scenarios.
- Use AssertJ for assertions and `assertThatThrownBy(...)` for exceptions.
- Use dedicated fixtures such as `TestJar` and `TestTransaction` for reusable sample data.
- Use Mockito to mock dependencies and verify interactions.
- Prefer `GraphQlTester` for controller/resolver slice tests.

### Controller / resolver tests
- Use `@GraphQlTest(...)` for GraphQL controller and resolver slice tests.
- Mock dependencies with `@MockitoBean`.
- Test GraphQL documents using multi-line text blocks.
- Include success, failure, and validation edge cases.

Example GraphQL test snippet:

```java
String document = """
    mutation {
        createJar(req: { name: \"Household\", description: \"Monthly expenses\" }) {
            id
            name
            description
        }
    }
    """;

graphQlTester.document(document).execute();
```

### Service tests
- Mock gateway dependencies instead of testing the repository layer.
- Verify correct domain conversions and exception paths.
- Use `inOrder(...)` only when interaction sequence matters.

### DAO and BDO tests
- Verify entity conversion, `toBdo()`, `updateWith(...)`, and action application.
- Confirm that invalid action payloads and null inputs fail fast.
- Validate that update helpers only mutate intended fields.

## Generated code
- Generated code should follow the same conventions as handwritten code.
- Use `jar` and `transaction` packages as templates for package structure, naming, and conversion patterns.
- If generated output differs, adjust the generator or add a wrapper layer to align with project conventions.

## Practical rule of thumb
When adding a new backend feature, start by creating the same layers and naming style already used by `jar/` and `transaction/`, then wire the feature through the existing Spring Boot + GraphQL pattern.
