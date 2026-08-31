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