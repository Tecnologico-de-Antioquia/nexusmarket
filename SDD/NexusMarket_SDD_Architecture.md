# NexusMarket SDD Extension

## 1. Purpose of this document

This document complements the existing domain model and value object documentation with the application architecture required by Spec-Driven Development (SDD) and Domain-Driven Design (DDD). It intentionally adds the missing design views for:

- application services
- domain ports and interfaces
- inbound and outbound adapters
- controller layer responsibilities
- interaction flows between business rules, persistence, security, and external integrations

This extension is aligned with the project specification described in `especificaciones.txt`, especially the functional objectives OBJ-01 through OBJ-12 and the operational rules defined in the business model.

---

## 2. Architectural context and business boundaries

NexusMarket is a centralized marketplace with a clear separation between business behavior and system infrastructure. The architecture follows a layered DDD model:

1. Domain Layer
   - Entities, value objects, exceptions, and business rules
2. Application Layer
   - Use-case services that orchestrate domain behavior
3. Ports Layer
   - Interfaces that decouple core logic from infrastructure
4. Infrastructure Layer
   - Persistence, authentication, and technical adapters
5. Interface Layer
   - API controllers exposed to external consumers

This structure supports the requirement that the marketplace must manage users, sellers, buyers, warehouses, catalog, inventory, cart, order lifecycle, billing, logistics, returns, and reports without mixing business logic with technology implementation.

---

## 3. Domain decomposition by bounded context

### 3.1 Identity and access context

This context manages the lifecycle of users and their authorization model.

Business responsibilities:
- register and manage users
- assign roles and permissions
- validate access state
- authenticate credentials
- deny access for inactive or invalid accounts

Key domain concepts:
- `User`
- `Person`
- `Role`
- `Permission`
- `AuthenticationResult`
- `PersonState`
- `RolesType`

Rules derived from the specification:
- every operation must be executed by an authenticated user
- each user must have a unique role
- users may only act within their scope
- identity and email must be unique in the platform

### 3.2 Buyer management context

This context covers the commercial profile of the buyer.

Business responsibilities:
- maintain the primary delivery address
- manage alternative delivery addresses
- define buyer commercial status
- allow the buyer to perform purchase flows

Key domain concepts:
- `Buyer`
- `Address`
- `ComercialState`

Rules derived from the specification:
- a buyer cannot manage other buyers' data
- a buyer cannot access warehouse inventory directly
- buyer information is restricted to purchasing and delivery operations

### 3.3 Seller and warehouse context

This context covers vendor registration and warehouse administration.

Business responsibilities:
- seller onboarding performed by the administrator
- warehouse registration and status management
- product storage location assignment
- support for marketplace and seller-owned warehouses

Key domain concepts:
- `Warehouse`
- `WarehouseType`
- `Address`
- seller-related ownership references

Rules derived from the specification:
- sellers cannot self-register
- the administrator is responsible for assigning the initial warehouse
- warehouse inventory is linked to a product and a specific warehouse

### 3.4 Catalog and product context

This context handles product publication, status, and variant definition.

Business responsibilities:
- define product types: physical or digital
- manage price and commercial status
- allow product variants such as size, color, and model
- publish or suspend products

Key domain concepts:
- `Product`
- `Physical`
- `Digital`
- `Variant`
- `ProductState`

Rules derived from the specification:
- physical products require inventory and logistics
- digital products are delivered immediately after successful payment
- catalog items may be published, suspended, or discontinued

### 3.5 Inventory context

This context manages stock and movement control.

Business responsibilities:
- assign product stock to a warehouse
- record all stock movements
- prevent negative inventory
- reserve and release inventory during order lifecycle

Key domain concepts:
- `Inventory`
- `MovementsState`
- `Warehouse`
- `Product`

Rules derived from the specification:
- inventory is linked to a product and warehouse
- movements include receipt, reservation, sale, adjustment, and return
- negative stock is forbidden
- inventory cannot be reserved if it is damaged or unavailable

### 3.6 Cart and order context

This is the central commercial context of the marketplace.

Business responsibilities:
- add, update, and remove products from cart
- validate product availability
- create and manage orders
- progress orders through payment, shipment, and completion

Key domain concepts:
- `Car`
- `Order`
- `OrderState`
- `Transaction`
- `Shipment`
- `TransactionState`
- `ShipmentState`

Order lifecycle derived from the specification:
1. Cart
2. Pending payment
3. Paid
4. Dispatched
5. Delivered / completed

Rules derived from the specification:
- a completed order must never be modified
- payment validation must happen before fulfillment begins
- the order is the formal commercial commitment of the marketplace

### 3.7 Billing and payment context

This context records the commercial transaction and financial state of the purchase.

Business responsibilities:
- validate payment or transaction state
- link payment to order and buyer
- support transaction traceability
- prepare financial reporting

Key domain concepts:
- `Transaction`
- `TransactionState`
- `AuthenticationResult` (in a broader flow, for authenticated user session)

### 3.8 Logistics and shipment context

This context covers the physical delivery flow.

Business responsibilities:
- package product for shipment
- dispatch from warehouse
- track shipment status
- confirm delivery and complete order lifecycle

Key domain concepts:
- `Shipment`
- `ShipmentState`
- `Address`
- `Warehouse`

Rules derived from the specification:
- dispatch is only allowed when inventory is available and the order is paid
- the shipment must be linked to destination and origin addresses

### 3.9 Returns and refunds context

This context manages customer support after purchase.

Business responsibilities:
- capture return requests
- validate return eligibility
- issue refunds and inventory adjustments
- update order and financial state

This is a required business capability from the specification, even though the current project skeleton has not yet implemented detailed classes for this process.

### 3.10 Administrative reporting context

This context supports the overview of marketplace operations.

Business responsibilities:
- aggregate data for sales, stock, warehouse activity, returns, and shipment metrics
- support administrators, supervisors, and operational roles
- provide operational dashboards and compliance information

Key classes in the current project:
- `ReportsService`

---

## 4. Application service architecture

The application layer is responsible for orchestrating domain behavior and exposing use cases. The current source structure already reflects this intent through service classes in the `domain.services` package.

### 4.1 Core service inventory

| Service | Responsibility | Primary domain object | Notes |
|---|---|---|---|
| `LoginService` | Authenticates a user against stored identity data | `User` | Validates username, password, and active status |
| `BuyerService` | Encapsulates buyer-oriented use cases | `Buyer` | Candidate for profile creation/update operations |
| `AddressService` | Manages buyer and warehouse addresses | `Address` | Supports shipping and warehouse operations |
| `ProductsService` | Registers and manages catalog items | `Product` | Covers physical and digital products |
| `InventoryService` | Controls stock and movement logic | `Inventory` | Enforces negative stock prevention |
| `AddProductToCarService` | Adds an item to the buyer cart | `Car` | Validates item availability |
| `UpdateProductFromCarService` | Updates quantity or item data in cart | `Car` | Maintains consistent cart state |
| `DeleteProductFromCarService` | Removes an item from cart | `Car` | Supports final purchase list maintenance |
| `ClearCarService` | Clears cart after order or abandonment | `Car` | Business cleanup operation |
| `CreateOrderService` | Creates a commercial order from cart state | `Order` | Starts order lifecycle |
| `GetOrderService` | Retrieves order state or detail | `Order` | Supports status checks and support operations |
| `CancelOrderService` | Cancels an order if allowed by state | `Order` | Enforces immutability rules |
| `ReportsService` | Aggregates reporting data | administrative views | Audit, KPIs, operational follow-up |

### 4.2 Application service responsibilities

#### `LoginService`

Responsibilities:
- load user by username
- compare input password with stored one through a password adapter
- validate role and account state
- return an authenticated result object

Business rules enforced:
- null or missing user is rejected
- unknown username is rejected
- wrong password is rejected
- inactive accounts cannot access the system

#### `BuyerService`

Responsibilities:
- create and update buyer profiles
- maintain commercial status
- tie buyer to user identity and delivery data

Relevant business rules:
- buyer must be linked to an authorized user
- buyer may not operate outside purchase and delivery scope

#### `AddressService`

Responsibilities:
- create and update delivery addresses
- validate address completeness
- support primary and secondary addresses

#### `ProductsService`

Responsibilities:
- create new product records
- classify products as physical/digital
- manage product visibility and lifecycle state
- enable seller product publication flows

#### `InventoryService`

Responsibilities:
- register incoming stock
- reserve stock for orders
- release stock when order is canceled or failed
- register inventory returns and adjustments

Critical invariant:
- stock quantity cannot be negative

#### `AddProductToCarService`

Responsibilities:
- add product to cart
- ensure not exceeding available stock
- maintain independent cart entries per product or variant

#### `UpdateProductFromCarService`

Responsibilities:
- modify item quantity
- reflect availability changes in cart
- reject invalid totals or incompatible inventory

#### `DeleteProductFromCarService`

Responsibilities:
- remove product from active cart
- update cart totals and state

#### `ClearCarService`

Responsibilities:
- empty the buyer cart after successful order or user reset

#### `CreateOrderService`

Responsibilities:
- transform cart into a formal order
- verify buyer, inventory, and payment requirements
- move the order to the pending payment or paid state

#### `CancelOrderService`

Responsibilities:
- cancel orders only before finalization
- release reserved inventory
- prevent changes on completed orders

#### `ReportsService`

Responsibilities:
- collect operational metrics by product, order, warehouse, and shipment
- provide administrative and supervisory views
- support compliance and monitoring

---

## 5. Ports and interfaces

The project already contains the foundational outbound-port pattern. This is the correct DDD strategy for isolating the domain from infrastructure.

### 5.1 Outbound ports

#### `UserRepositoryPort`

Location: `nexusmarket.application.domain.ports.out`

Purpose:
- persist user data
- query user records by username or identity
- check existence
- update data after status changes

Methods:
- `save(User user)`
- `findByUsername(User user)`
- `findById(User user)`
- `existsByUsername(User user)`
- `update(User user)`

This port allows the domain to remain unaware of the concrete storage implementation.

#### `PasswordServicePort`

Location: `nexusmarket.application.domain.ports.out`

Purpose:
- compare a raw password with an encrypted or stored version
- encode plaintext or hashed passwords according to the security strategy

Methods:
- `boolean matches(String rawPassword, String encodedPassword)`
- `String encrypt(String rawPassword)`

This abstraction allows the application to evolve from plain text validation to a cryptographic mechanism without changing the domain service logic.

### 5.2 Conceptual ports for the full marketplace

The current project implements only the first phase of the port pattern, but the complete specification requires several additional ports to support the full domain scope.

#### Product persistence port

`ProductRepositoryPort`

Responsibilities:
- persist and query catalog products
- support variant management
- retrieve products by seller or warehouse

#### Inventory persistence port

`InventoryRepositoryPort`

Responsibilities:
- manage stock records
- query available inventory
- create stock movements
- validate reservation rules

#### Order persistence port

`OrderRepositoryPort`

Responsibilities:
- create and update orders
- track order status transitions
- load order history for admin and support operations

#### Warehouse port

`WarehouseRepositoryPort`

Responsibilities:
- manage warehouse metadata
- map products to warehouses
- support warehouse-type constraints

#### Payment gateway port

`PaymentGatewayPort`

Responsibilities:
- authorize and capture payment
- validate transaction status
- return payment confirmation to the order service

#### Shipment provider port

`ShipmentProviderPort`

Responsibilities:
- dispatch orders to logistics providers
- track packages
- update shipment state

#### Notification port

`NotificationPort`

Responsibilities:
- send confirmation emails
- notify buyers, sellers, or administrators
- support account alerts and order updates

#### Audit and reporting port

`AuditRepositoryPort`

Responsibilities:
- log operational events
- support traceability and compliance
- expose metrics for reporting

These additional ports are the architectural foundation required for a complete, domain-driven marketplace implementation.

---

## 6. Adapters

Adapters implement the interfaces defined by the domain ports and connect the application layer to technology-specific implementations.

### 6.1 Current implemented adapters

#### `InMemoryUserRepository`

Location: `nexusmarket.application.infrastructure.persistence`

Implementation of: `UserRepositoryPort`

Responsibilities:
- maintain a concurrent in-memory user map
- support username-based lookup
- seed initial administrator data
- save or update user state

This adapter is suitable for early development and validation, but it is not production-grade persistence.

#### `PlainPasswordService`

Location: `nexusmarket.application.infrastructure.security`

Implementation of: `PasswordServicePort`

Responsibilities:
- compare raw password strings directly
- encrypt plain text without hashing

This adapter is intentionally simple for educational or prototype scope. In a production-grade implementation, it should be replaced by a secure hashed-password strategy.

### 6.2 Planned adapters for full specification coverage

#### Persistence adapters

- `JpaUserRepositoryAdapter`
- `JpaProductRepositoryAdapter`
- `JpaInventoryRepositoryAdapter`
- `JpaOrderRepositoryAdapter`
- `JpaWarehouseRepositoryAdapter`

Responsibilities:
- persist domain aggregates to relational storage
- map entities and value objects to database schema
- isolate JPA or JDBC technology from domain classes

#### Security adapters

- `BCryptPasswordServiceAdapter`
- `TokenServiceAdapter`
- `SessionManagerAdapter`

Responsibilities:
- secure password handling
- issue and validate authentication tokens
- manage session lifecycle

#### External system adapters

- `StripePaymentAdapter`
- `MercadoPagoAdapter`
- `DHLShippingAdapter`
- `SendGridNotificationAdapter`
- `S3DocumentStorageAdapter`

Responsibilities:
- integrate the marketplace with payment, logistics, and communication systems

#### Event-driven adapters

- `DomainEventPublisherAdapter`
- `KafkaProducerAdapter`
- `OutboxPublisherAdapter`

Responsibilities:
- propagate domain events after order placement, shipment, return, and payment events

---

## 7. Controller layer design

The controller layer is the external interface between clients and the application service layer. Although the current project does not yet include concrete controller classes, the SDD defines the appropriate API surface derived from the business specification.

### 7.1 Authentication and user controllers

#### `AuthController`

Responsibilities:
- expose login endpoint
- authenticate users with username and password
- return `AuthenticationResult`

Suggested endpoints:
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/logout`
- `GET /api/v1/auth/me`

#### `UserController`

Responsibilities:
- create, list, and update users
- validate role assignment
- expose administrative user management operations

Suggested endpoints:
- `POST /api/v1/users`
- `GET /api/v1/users/{id}`
- `PUT /api/v1/users/{id}`
- `GET /api/v1/users`

### 7.2 Buyer and seller controllers

#### `BuyerController`

Responsibilities:
- register and maintain buyer profile data
- add addresses
- expose buyer purchase history and active cart reference

Suggested endpoints:
- `POST /api/v1/buyers`
- `GET /api/v1/buyers/{id}`
- `PUT /api/v1/buyers/{id}`
- `POST /api/v1/buyers/{id}/addresses`

#### `SellerController`

Responsibilities:
- manage seller registration and lifecycle
- allow seller-owned product publication
- support warehouse association and operational control

Suggested endpoints:
- `POST /api/v1/sellers`
- `GET /api/v1/sellers/{id}`
- `PUT /api/v1/sellers/{id}`
- `GET /api/v1/sellers/{id}/products`

### 7.3 Catalog and inventory controllers

#### `CatalogController`

Responsibilities:
- expose catalog information for public or role-based listing
- list products by status and category
- support search and filtering by variant and product type

Suggested endpoints:
- `GET /api/v1/catalog/products`
- `GET /api/v1/catalog/products/{id}`
- `POST /api/v1/catalog/products`
- `PUT /api/v1/catalog/products/{id}`

#### `InventoryController`

Responsibilities:
- receive warehouse stock entries
- manage movements and reservations
- expose current inventory and stock health metrics

Suggested endpoints:
- `POST /api/v1/inventory/entries`
- `POST /api/v1/inventory/reservations`
- `GET /api/v1/inventory/warehouses/{warehouseId}`
- `GET /api/v1/inventory/products/{productId}`

### 7.4 Cart and order controllers

#### `CartController`

Responsibilities:
- add items to cart
- update quantities
- remove items or clear the cart
- calculate current cart totals

Suggested endpoints:
- `POST /api/v1/cart/items`
- `PUT /api/v1/cart/items/{itemId}`
- `DELETE /api/v1/cart/items/{itemId}`
- `DELETE /api/v1/cart`

#### `OrderController`

Responsibilities:
- create orders from cart state
- retrieve order details and status
- cancel or process approved order operations

Suggested endpoints:
- `POST /api/v1/orders`
- `GET /api/v1/orders/{id}`
- `GET /api/v1/orders`
- `PATCH /api/v1/orders/{id}/cancel`
- `PATCH /api/v1/orders/{id}/confirm`

### 7.5 Billing and shipment controllers

#### `BillingController`

Responsibilities:
- create payment requests
- confirm payment status
- provide transaction history for a buyer or order

Suggested endpoints:
- `POST /api/v1/billing/orders/{orderId}/payments`
- `GET /api/v1/billing/orders/{orderId}/transactions`

#### `ShipmentController`

Responsibilities:
- dispatch shipments
- update delivery status
- support finalization after delivery

Suggested endpoints:
- `POST /api/v1/shipments`
- `GET /api/v1/shipments/{id}`
- `PATCH /api/v1/shipments/{id}/status`

### 7.6 Returns and reports controllers

#### `ReturnController`

Responsibilities:
- manage refund and return workflows
- validate return eligibility
- update inventory and payment state after approval

Suggested endpoints:
- `POST /api/v1/returns`
- `GET /api/v1/returns/{id}`
- `PUT /api/v1/returns/{id}/approve`

#### `ReportController`

Responsibilities:
- provide administrative queries by warehouse, seller, shipment, and returns
- expose operational KPIs

Suggested endpoints:
- `GET /api/v1/reports/orders`
- `GET /api/v1/reports/inventory`
- `GET /api/v1/reports/shipments`
- `GET /api/v1/reports/returns`

---

## 8. Core interaction flows

### 8.1 Login flow

1. Client sends credentials to `AuthController`
2. `AuthController` calls `LoginService`
3. `LoginService` invokes `UserRepositoryPort.findByUsername`
4. The repository returns the stored user
5. `LoginService` validates the password via `PasswordServicePort.matches`
6. The account state is validated against `PersonState`
7. `AuthenticationResult` is returned to the client

This flow confirms the domain service is isolated from technical security details.

### 8.2 Create order flow

1. Buyer selects products in cart
2. `CartController` coordinates with `AddProductToCarService` and `UpdateProductFromCarService`
3. `CreateOrderService` checks buyer and inventory data
4. `OrderRepositoryPort` stores the order state
5. `InventoryRepositoryPort` reserves available stock
6. Payment is validated using `PaymentGatewayPort`
7. `ShipmentProviderPort` begins dispatch when the order is paid
8. `OrderState` progresses through the payment and shipment workflow

### 8.3 Return and refund flow

1. Buyer submits a return request through `ReturnController`
2. `ReturnController` delegates to a return domain service
3. The system verifies whether the order is eligible for return
4. `InventoryRepositoryPort` processes stock re-entry
5. `PaymentGatewayPort` or refund logic issues the reimbursement
6. The order and shipment histories are updated
7. A reporting event is emitted for operational follow-up

---

## 9. Invariants and constraints derived from the specification

These are the critical business rules that the application services and ports must preserve:

1. Authentication requirement
   - every operation must be executed by an authenticated user
2. Role exclusivity
   - each user has exactly one active role
3. Role-based access control
   - no participant can modify data outside their role scope
4. User uniqueness
   - email and identity data must be unique
5. Inventory integrity
   - no negative inventory is allowed
6. Product validity
   - physical products require inventory and shipping; digital products do not
7. Order immutability
   - a completed order cannot be modified
8. Seller restriction
   - sellers cannot self-register; only the administrator can create them
9. Warehouse linkage
   - inventory is always linked to a product and a warehouse
10. Catalog lifecycle
   - a product can be published, suspended, or discontinued

---

## 10. Traceability to specification objectives

| Specification objective | Architectural element |
|---|---|
| OBJ-01: manage all users | `UserController`, `UserRepositoryPort`, `LoginService` |
| OBJ-02: register and administer sellers | `SellerController`, seller-related services |
| OBJ-03: manage registered buyers | `BuyerController`, `BuyerService` |
| OBJ-04: manage warehouses | `WarehouseRepositoryPort`, `InventoryController`, `AddressService` |
| OBJ-05: catalog management | `CatalogController`, `ProductsService` |
| OBJ-06: distributed inventory management | `InventoryService`, `InventoryRepositoryPort` |
| OBJ-07: cart management | `CartController`, cart services |
| OBJ-08: complete order lifecycle | `OrderController`, `CreateOrderService`, `CancelOrderService` |
| OBJ-09: billing | `BillingController`, `PaymentGatewayPort` |
| OBJ-10: logistics | `ShipmentController`, `ShipmentProviderPort` |
| OBJ-11: returns and refunds | `ReturnController`, refund logic |
| OBJ-12: administrative reporting | `ReportController`, `ReportsService` |

---

## 11. DDD design decisions

### Aggregate orientation

The most relevant aggregates are:
- `User` aggregate
- `Buyer` aggregate
- `Product` aggregate
- `Inventory` aggregate
- `Car` aggregate
- `Order` aggregate
- `Shipment` aggregate

### Why this model fits the specification

The business specification describes a marketplace with multiple operational domains but a single, central commercial process. The aggregate and service structure above preserves domain integrity while keeping the codebase adaptable to future infrastructure changes.

### Separation of concerns

- Domain rules stay inside entities and use-case services
- persistence logic is isolated in repository implementations
- password and security logic is isolated through an outbound port
- HTTP concerns are kept outside the domain in controller classes

This is the essential design principle behind the current structure and the complete SDD extension.

---

## 12. Final architectural recommendation

The project should continue the clean architecture pattern already started by the codebase:

1. keep domain rules in `domain.models` and `domain.services`
2. keep outbound contracts in `domain.ports.out`
3. continue implementing infrastructure adapters in `infrastructure`
4. add REST controllers as the public entry point for each bounded context
5. ensure all flows are validated against the business rules in the specification

This implementation strategy will keep the system aligned with both the business specification and a sound DDD architecture.
