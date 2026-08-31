# Domian Model

## Description

---

## Domain Class Hierachy

```
Person (Abstract)
 ├── User
 └── Buyer

Role
Permission

Address

Product (Abstract)
   ├── Physical
   └── Digital

Variant

Inventory
Warehouse

Car
Order
Transaction
Shipment
```

---

# Entities

## Person (Abstract)

### Description

Representa cualquier usuario que accede al sistema, agrupando las características del comprador y los demás roles existentes.

La clase no puede ser instanciada.

### Attributes

| Name      | Type   | Description                                              |
|-----------|--------|----------------------------------------------------------|
| id        | int    | Identificador único para cada usuario                    |
| fullName  | string | Nombre completo del usuario                              |
| email     | string | Correo electrónico del usuario                           |
| role      | Role   | Cargo que define los permisos dentro del sistema         |
| state     | PersonState | Estado de acceso para los usuarios                  |

### Relationship

Una persona puede ser definida como comprador. `Buyer`

Una persona puede ser definida como usuario. `Administrator` `Seller` `Logics operator` `Supervisor`

---

## User

### Description

Representa los usuarios que no cuentan con características propias y se diferencian por roles para permitir o denegar las diferentes acciones dentro del sistema.

### Attributes

| Name      | Type      | Description                                              |
|-----------|-----------|----------------------------------------------------------|
| role      | RolesType | Rol dentro del sistema                                   |

--- 

## Buyer

### Description

Representa el comprador que interactua con todo el proceso referente a la optención de un producto o servicio.

### Attributes

| Name      | Type  | Description                                                |
|-------------------|---------|--------------------------------------------------|
| primaryAddress    | Address | Dirección principal para el envío de pedidos     |
| additionalAddress | Address | Direcciones adicionales para el envío de pedidos |
| state             | ComercialState  | Condición para la realización de compras |

### Relationship

Un comprador cuenta con una dirección principal. `Address`

Un comprador cuenta con ninguna o varias direcciones adicionales. `Address`

---

## Role

### Description

Representa los cargos de los usuarios dentro del sistema, para limitar sus interacciones dentro de este agrupando permisos.

### Attributes

| Name        | Type   | Description                     |
|-------------|--------|---------------------------------|
| code        | string | Código unico para cada cargo    |
| name        | string | Nombre representativo del cargo |
| description | string | Descripción del cargo           |

### Relationship

Un rol depende de los permisos asignados al mismo para definir el acceso a los usuarios. `Permission`

---

## Permission

### Description

Representa los permisos de los usuarios dentro del sistema, para limitar sus interacciones dentro de este.

### Attributes

| Name        | Type   | Description                       |
|-------------|--------|-----------------------------------|
| code        | string | Código unico para cada permiso    |
| name        | string | Nombre representativo del permiso |
| description | string | Descripción del permiso           |

---

## Address

### Description

Representa la dirección física de un lugar.

### Attributes

| Name        | Type   | Description                     |
|-------------|--------|---------------------------------|
| address     | string | Dirección                       |

### Relationship

Los compradores poseen una dirección o varias de entrega. `Buyer`
Los envíos cuentan con dirección de origen y destino. `Shipment`
Las bodegas cuentan con direcciones de almacenamiento. `Warehouse`

---

## Product (Abstract)

### Description

Representa un producto tangible o servicio dentro del sistema para su compra/venta.

La clase no puede ser instanciada.

### Attributes

| Name        | Type   | Description                         |
|-------------|--------|-------------------------------------|
| id          | string | Código unico para cada producto     |
| name        | string | Nombre del producto                 |
| state       | ProductState | Estado comercial del producto |

---

## Physical

### Description

Representa un producto tangible dentro del sistema para su compra/venta.

### Attributes

| Name        | Type    | Description            |
|-------------|---------|------------------------|
| stock       | Integer | Cantidad en bodega     |
| variant     | Variant | Variantes del producto |

### Relationship

Los producto tienen variantes como: color, tamaño y modelo. `Variant`

---

## Digital

### Description

Representa un servicio dentro del sistema para su compra/venta.

### Attributes

| Name        | Type   | Description         |
|-------------|--------|---------------------|
| format      | string | Formato del archivo |

---

## Variant

### Description

Representa las variaciones con respecto a los productos físicos.

### Attributes

| Name        | Type   | Description    |
|-------------|--------|----------------|
| color | string  | Color del producto  |
| size  | Integer | Tamaño del producto |
| model | string  | Modelo del producto |

---

## Inventory

### Description

Representa el inventario de los producto dentro del sistema.

### Attributes

| Name        | Type   | Description                                  |
|-------------|--------|----------------------------------------------|
| idProduct    | Integer | Identificador único del producto               |
| idWarehouse  | Integer | Identificador único de la bodega               |
| state        | MovementsState  | Estado del movimiento del producto |

### Relationship

Los productos cuentan con un inventario para gestionar los ingresos, salidas y demás movimientos. `Product`

---

## Warehouse

### Description

Representa las bodegas de almacenamiento del producto.

### Attributes

| Name        | Type   | Description                                  |
|-------------|--------|----------------------------------------------|
| id       | Integer | Identificador único de la bodega               |
| address  | Address | Dirección física de la bodega               |
| capacity | Integer  | Capacidad de almacenamiento |
| type     | WarehouseType  | Tipo de producto |
| supplier | Integer  | Proveedor |

### Relationship

Las bodegas cuentan con una dirección física. `Address`
Los proveedores cuentan con bodegas. `User`

---

## Car

### Description

Representa los elementos dentro del carrito de compras que aún no han sido efectuados como un pedido.

### Attributes

| Name        | Type   | Description                                      |
|-------------|--------|--------------------------------------------------|
| products       | List<Products> | Lista de productos                    |
| idTransaction  | Integer        | Identificador único de la transacción |
| state          | string  | Estado del carrito de compras |

### Relationship

Los carritos de compra se relacionan con las transacciones. `Transactions`

---

## Order

### Description

Representa los elementos dentro del carrito de compras una vez efectuado el pago.

### Attributes

| Name        | Type   | Description                                      |
|-------------|--------|--------------------------------------------------|
| products       | List<Products> | Lista de productos                    |
| idTransaction  | Integer        | Identificador único de la transacción |
| shipment       | Shipment       | Envio del producto |
| state          | OrderState     | Estado del pedido |

### Relationship

Los pedidos se relacionan con las transacciones una vez se efectua el pago. `Transactions`
Los pedidos cuentan con la trazabilidad del envío. `Shipment`

---

## Transaction

### Description

Representa las transacciones monetarias dentro del sistema.

### Attributes

| Name        | Type   | Description                         |
|-------------|--------|-------------------------------------|
| id       | Integer | Identificador único de la transacción |
| idBuyer  | Integer | Identificador único del comprador     |
| state    | TransactionState     | Estado de la transacción |

---

## Shipment

### Description

Representa las transacciones monetarias dentro del sistema.

### Attributes

| Name        | Type   | Description                         |
|-------------|--------|-------------------------------------|
| destinationAddress | Address | Dirección de destino        |
| originAddress      | Address | Dirección de origen         |
| trackingGuide      | TransactionState     | Número de guía |
| state              | ShipmentState     | Estado del envío  |

---

# Domain Value Objects

## Description

---

## ComercialState

### Description

Representa los estados comerciales del producto.

### Allowed Values

| Code        | Name   | Description        |
|-------------|--------|--------------------|
| ACTIVE   | Activo | Dirección de destino  |
| INACTIVE | Inactivo | Dirección de origen |

---

## MovementsState

### Description

Representa los estados de los movimientos de inventario.

### Allowed Values

| Code        | Name   | Description        |
|-------------|--------|--------------------|
| RECEIPT   | Recibido | Recibido  |
| RESERVATION | Reservado | Reservado |
| SALE_OUT | Salida | Salida |
| ADJUSMENT | Ajuste | Ajuste |
| RETURN | Devuelto | Devuelto |

---

## OrderState

### Description

Representa los estados de los pedidos.

### Allowed Values

| Code        | Name   | Description        |
|-------------|--------|--------------------|
| CANCELLED | Cancelado | Cancelado  |
| COMPLETED | Completado | Completado |

---

## PersonState

### Description

Representa los estados las personas dentro del sistema.

### Allowed Values

| Code        | Name   | Description        |
|-------------|--------|--------------------|
| ACTIVE | Activo | Activo  |
| INACTIVE | Inactivo | Inactivo |

---

## ProductState

### Description

Representa los estados lso productos.

### Allowed Values

| Code        | Name   | Description                    |
|-------------|--------|--------------------------------|
| PUBLISHED | Publicado | Producto publicado            |
| SUSPENDED | Suspendido | Producto suspendido          |
| DISCONTINUED | Descontinuado | Producto descontinuado |

---

## RolesType

### Description

Representa los tipos de roles dentro del sistema.

### Allowed Values

| Code        | Name   | Description                    |
|-------------|--------|--------------------------------|
| SELLER | Vendedor | Vendedor            |
| ADMINISTRATOR | Administrador | Administrador          |
| LOGIC_OPERATOR | Operador logístico | Operador logístico |
| SUPERVISOR | Supervisor | Supervisor |

---

## ShipmentState

### Description

Representa los estado del envío.

### Allowed Values

| Code        | Name   | Description                    |
|-------------|--------|--------------------------------|
| DISPATCHED | Despachado | Despachado            |
| DELIVERED | Entregado | Entregado          |

---

## TransactionState

### Description

Representa los estado de las transacciones.

### Allowed Values

| Code        | Name   | Description                    |
|-------------|--------|--------------------------------|
| PENDING | Pendiente | Pendiente            |
| PAID | Pagado | Pagado          |

---

## WarehouseType

### Description

Representa los tipos de bodegas.

### Allowed Values

| Code        | Name   | Description                    |
|-------------|--------|--------------------------------|
| MARKETPLACE | Marketplace | Bodega propia del marketplace            |
| SELLER | Vendedor | Bodega propia del proveedor          |

---