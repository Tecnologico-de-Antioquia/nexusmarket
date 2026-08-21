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

Product
   ├── Physical
   └── Digital

Inventory
Warehouse

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
| full_name | string | Nombre completo del usuario                              |
| email     | string | Correo electrónico del usuario                           |
| role      | Role   | Cargo que define los permisos dentro del sistema         |
| state     | StateUser | Estado de acceso para los usuarios                    |

### Relationship

Una persona puede ser definida como comprador. `Buyer`

Una persona puede ser definida como usuario. `Administrator` `Seller` `Logics operator` `Supervisor`

---

## User

### Description

Representa los usuarios que no cuentan con características propias y se diferencian por roles para permitir o denegar las diferentes acciones dentro del sistema.

--- 

## Buyer

### Description

Representa el comprador que interactua con todo el proceso referente a la optención de un producto o servicio.

### Attributes

| Name      | Type   | Description                                                |
|--------------------|---------|--------------------------------------------------|
| primary_address    | Address | Dirección principal para el envío de pedidos     |
| additional_address | Address | Direcciones adicionales para el envío de pedidos |
| comercial_state    | string  | Condición para la realización de compras         |

### Relationship

Un comprador cuenta con una dirección principal. `Address`

Un comprador cuenta con ninguna o varias direcciones adicionales. `Address`

---

## Roles

### Description

Representa los cargos de los usuarios dentro del sistema, para limitar sus interacciones dentro de este.

### Attributes

| Name        | Type   | Description                     |
|-------------|--------|---------------------------------|
| id          | int    | Identificador único del cargo   |
| code        | string | Código unico para cada cargo    |
| name        | string | Nombre representativo del cargo |
| description | string | Descripción del cargo           |

### Relationship

Un rol depende de los permisos asignados al mismo para definir el acceso a los usuarios. `Permission`

---