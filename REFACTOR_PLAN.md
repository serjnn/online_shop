# Application Refactoring Plan: Online Shop Monolith

This document outlines the proposed refactoring plan for the Online Shop Monolith application. The plan addresses package naming inconsistencies, database modeling errors, active bugs (including transactional issues causing potential `LazyInitializationException` in production), security misconfigurations, code smells, MapStruct integration patterns, and documentation mismatches.

---

## 1. Executive Summary & Core Objectives
The primary goal of this refactoring is to transition the current codebase from a prototype-level quality containing severe structural errors, security risks, and package naming inconsistencies, to a production-ready, clean, secure, and easily maintainable Java 17 + Spring Boot 3.x backend service.

### Key Focus Areas:
- **Bug Fixes:** Correct critical transactional boundaries, entity association types, and authentication flow bugs.
- **Naming Conventions & Packaging:** Align the package structures and names with standard Java conventions (e.g., correcting the `sevices` package typo, using lowercase for `JWT` and `DTOs`).
- **REST & RESTful Standards:** Fix HTTP mapping protocols, resource paths, request/response bodies, and state-mutation methods.
- **Security Enhancements:** Lock down endpoints to enforce authentication, and add critical constraints to database schemas.
- **MapStruct Best Practices:** Leverage Spring dependency injection for mapping interfaces rather than manual factory instance lookups.
- **Testing Reliability:** Introduce database virtualization/mocking setups (like an in-memory H2 database) for integration tests so builds are independent of local host databases.

---

## 2. Issues Inventory & Architectural Gaps

Below is a categorized summary of the design issues, bugs, and legacy practices identified in the codebase:

### A. Naming and Structural Typos
1. **Misspelled Service Package:** The package is named `com.serjn.online.sevices` instead of `services`. This typo propagates across almost all classes in the application.
2. **Capitalized Package Names:** Packages `com.serjn.online.JWT` and `com.serjn.online.model.DTOs` violate Java coding standards (which dictate lowercase names: `jwt` and `dto` or `dtos`).

### B. Database Design & Entity Modeling
1. **Incorrect BucketItem-to-Product Relationship:** In [BucketItem.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/model/entities/BucketItem.java#L19), the `product` association is declared as `@OneToOne`. This prevents multiple clients from having the same product in their shopping buckets simultaneously. It must be refactored to `@ManyToOne`.
2. **No Email Unique Constraint:** In [Client.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/model/entities/Client.java#L22), the email field (`mail`) lacks a `unique = true` database constraint. Multiple accounts can register with the exact same email address.
3. **Normalized Order Details:** In [OrderDetails.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/model/entities/OrderDetails.java#L22), the `products_ids` are saved as a comma-separated String (`String products_ids`). This breaks normalization and database querying capabilities. It should use an association or `@ElementCollection`.
4. **Naming Inconsistencies (Java fields vs. DB columns):** [OrderDetails.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/model/entities/OrderDetails.java#L22-L25) uses snake_case for field names (`products_ids`, `created_at`). Java code should use camelCase (`productIds`, `createdAt`), leaving Hibernate/Spring physical naming strategies to handle database snake_case columns.

### C. Service Layer & Transactional Issues
1. **Lack of Transaction Boundaries:** `BucketService`, `ClientService`, `ProductService`, and `OrderDetailsService` lack `@Transactional` annotations on class or method levels.
2. **LazyInitializationException Risk:** Because [BucketService.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/sevices/BucketService.java#L43) lacks `@Transactional`, calling `removeProductFromBucket` which invokes `bucket.getBucketItems()` will throw a `LazyInitializationException` in production since there is no active Hibernate session.
3. **Overuse of Extractor Hack:** [BucketItemsExtractor.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/sevices/utils/BucketItemsExtractor.java#L12) was introduced as a read-only transactional workaround to fetch lazy-loaded collection items. With proper transaction boundaries at the service layer, this class is entirely redundant and should be deleted.
4. **Service Layering Violation:** In [AuthService.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/sevices/AuthService.java#L48), `@RequestBody` is used on a method parameter. This is a Spring Web annotation that belongs strictly in the controller layer, not the service layer.

### D. Security & Controller Failures
1. **Disabled Authentication Enforcements:** In [SecurityConfiguration.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/config/SecurityConfiguration.java#L42), security has been disabled for all authenticated endpoints using `registry.anyRequest().permitAll()`. Anyone can access private client endpoints without valid tokens.
2. **Wrong Exception Class in Authentication:** In [ClientDetailService.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/sevices/ClientDetailService.java#L24), looking up a non-existent email throws a `NoSuchElementException`. It should throw Spring Security's native `UsernameNotFoundException` so the filter chain maps it to a standard Bad Credentials / Unauthorized HTTP response.
3. **Incorrect HTTP GET for State Modification:** In [ShoppingController.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/controllers/ShoppingController.java#L27), the purchase endpoint is mapped as `@GetMapping("/purchase")` which mutates balances and inventory. It must be `@PostMapping("/purchase")`.
4. **Brittle Primitive Payload Mapping:** In [ShoppingController.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/controllers/ShoppingController.java#L34) and [ClientController.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/controllers/ClientController.java#L42), endpoints receive a naked `Long` or `String` in the request body. This is fragile and error-prone for JSON parsers. They should use a wrapped request DTO or query/path parameters.
5. **No 201 Created Status:** Creating resources (e.g., adding bucket items or registering users) returns a generic `200 OK` or `void`. They should return `201 Created` or appropriate DTO resources.

### E. Mappings & Code Duplication
1. **Unmapped MapStruct Properties Warning:** Compilation warnings occur inside [OrderDetailsMapper.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/mappers/OrderDetailsMapper.java#L15) due to unmapped properties (`orderDate`, `status`, `totalAmount`). This is because the fields mismatch between `OrderDetails` entity and `OrderDetailsDto`.
2. **Legacy MapStruct Retrieval Pattern:** All MapStruct mappers use static `INSTANCE = Mappers.getMapper(...)` methods. Modern Spring Boot applications should configure `componentModel = "spring"` on `@Mapper` for dependency injection.
3. **Duplicate or Copied Handler Names:** Inside [ShoppingExceptions.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/exceptions/handlers/ShoppingExceptions.java#L19) and [AuthExceptionsHandler.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/exceptions/handlers/AuthExceptionsHandler.java#L13), exception handler methods are copy-pasted and both named `handleNoSuchProductException` regardless of the actual exception type handled.

### F. Broken Documentation & Testing
1. **Invalid HTTP Request Samples:** The [requests_example.http](file:///E:/intell_projects/online_shop_monolyth/requests_example.http) matches neither the controller routes (e.g. paths use `/api/v1/clients/me` instead of `/api/v1/me`) nor the JSON field names (e.g. payload uses `"email"` instead of `"mail"`).
2. **Environment-Dependent Tests:** Running `mvn test` fails unless an external PostgreSQL database is running on localhost. The integration tests ([UserControllerITest.java](file:///E:/intell_projects/online_shop_monolyth/src/test/java/com/serjn/online/UserControllerITest.java) and [UserBucketITest.java](file:///E:/intell_projects/online_shop_monolyth/src/test/java/com/serjn/online/ITests/Controller/UserBucketItest.java)) lack a test profile configuration that uses H2 or Testcontainers.

---

## 3. Refactoring Roadmap

We suggest a phased refactoring process to minimize risks and ensure that tests remain green.

### Phase 1: Package Structure and Name Corrections
* **Target Package Names:**
  - Rename package `com.serjn.online.sevices` to `com.serjn.online.services`.
  - Rename package `com.serjn.online.JWT` to `com.serjn.online.jwt`.
  - Rename package `com.serjn.online.model.DTOs` to `com.serjn.online.model.dto`.
* **Imports Cleanup:** Automatically adjust package imports across all controller, service, mapper, security, and entity classes to reflect package names.

### Phase 2: Database and Entity Corrections
* **Refactor [BucketItem.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/model/entities/BucketItem.java#L19):**
  - Replace `@OneToOne` with `@ManyToOne` for the `product` reference.
* **Refactor [Client.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/model/entities/Client.java):**
  - Add `@Column(unique = true)` to the `mail` field to ensure email uniqueness.
  - Rename field `mail` to `email` for semantic consistency (optional, but highly recommended; requires matching changes in login logic).
* **Refactor [OrderDetails.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/model/entities/OrderDetails.java):**
  - Rename `products_ids` -> `productIds` and `created_at` -> `createdAt`.
  - Replace comma-separated string column with a `@ElementCollection` storing product IDs, or establish a many-to-many relationship with `Product`.

### Phase 3: Service Layer Cleanups & Transaction Boundaries
* **Enforce Transaction Boundaries:**
  - Add `@Transactional` to `services` classes. Read-only service methods should be annotated with `@Transactional(readOnly = true)`.
  - Write operations in `BucketService`, `PurchaseService`, `ClientService` should be marked `@Transactional`.
* **Deprecate Hack classes:**
  - Remove [BucketItemsExtractor.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/sevices/utils/BucketItemsExtractor.java) completely.
  - Inside [BucketService.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/sevices/BucketService.java) and [PurchaseService.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/sevices/PurchaseService.java), query `bucket.getBucketItems()` directly since Hibernate sessions will now stay open within transactional boundaries.
* **Clean Service Layer Parameter Annotations:**
  - Remove `@RequestBody` from [AuthService.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/sevices/AuthService.java#L48).

### Phase 4: API Endpoint and Controller Corrections
* **Correct REST Methods:**
  - Update `purchase()` endpoint in [ShoppingController.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/controllers/ShoppingController.java#L27) to `@PostMapping("/purchase")`.
* **Fix Primitive Payload Bindings:**
  - Create proper request payloads or use `@RequestParam` / `@PathVariable` instead of naked primitive body payloads for:
    - Adding product to bucket: [ShoppingController.java#L34](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/controllers/ShoppingController.java#L34).
    - Patching client address: [ClientController.java#L42](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/controllers/ClientController.java#L42).
* **Fix HTTP Status Codes:**
  - Annotate creation endpoints with `@ResponseStatus(HttpStatus.CREATED)` or return `ResponseEntity<Void>` with status `201`.

### Phase 5: Security Configuration Corrections
* **Restore Authorization Enforcements:**
  - In [SecurityConfiguration.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/config/SecurityConfiguration.java#L42), replace `registry.anyRequest().permitAll()` with `registry.anyRequest().hasRole("client")` or standard role check to protect non-public APIs.
* **Fix UserDetailsService Exceptions:**
  - Update [ClientDetailService.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/sevices/ClientDetailService.java#L24) to throw `UsernameNotFoundException` instead of `NoSuchElementException`.

### Phase 6: MapStruct Mapping Enhancements
* **Fix Compile Warnings:**
  - Map target properties in [OrderDetailsMapper.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/mappers/OrderDetailsMapper.java):
    - Map `createdAt` to `orderDate` using `@Mapping(source = "createdAt", target = "orderDate")`.
    - Map `sum` to `totalAmount` using `@Mapping(source = "sum", target = "totalAmount")`.
    - Handle `status` field mapping (e.g. mapping to a constant or adding `status` to entity).
* **Upgrade to Spring Component Model:**
  - Change `@Mapper` to `@Mapper(componentModel = "spring")` on all interfaces inside the `mappers` package.
  - Delete `INSTANCE` constants from all mapper interfaces and inject them as standard Spring Beans using `@Autowired` or constructor injection.

### Phase 7: Testing Environment virtualization (In-Memory database)
* **Add H2 Database Dependency:**
  - Add `h2` test dependency to [pom.xml](file:///E:/intell_projects/online_shop_monolyth/pom.xml) under `<dependencies>` with test scope.
* **Configure Test Profile:**
  - Create `application-test.yaml` (or configure inline properties inside the test class) to point datasource to `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1`.
  - Annotate integration tests with `@ActiveProfiles("test")` so they run reliably in CI/CD and locally without requiring an active PostgreSQL container.

### Phase 8: Sync Documentation & Exception handlers
* **Sync [requests_example.http](file:///E:/intell_projects/online_shop_monolyth/requests_example.http):**
  - Standardize endpoints URLs (`/api/v1/clients/me` -> `/api/v1/me`).
  - Fix payload keys (`"email"` -> `"mail"`).
  - Correct HTTP methods (e.g., `POST` for `/api/v1/purchase`).
* **Rename Exception Handler Methods:**
  - Clean up duplicated method names in [ShoppingExceptions.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/exceptions/handlers/ShoppingExceptions.java#L19) and [AuthExceptionsHandler.java](file:///E:/intell_projects/online_shop_monolyth/src/main/java/com/serjn/online/exceptions/handlers/AuthExceptionsHandler.java#L13) to prevent code ambiguity.
