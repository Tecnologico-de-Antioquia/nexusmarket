package nexusmarket.application.adapters.persistence.jpa.mappers;

import lombok.experimental.UtilityClass;
import nexusmarket.application.adapters.persistence.jpa.entities.AddressEmbeddable;
import nexusmarket.application.adapters.persistence.jpa.entities.AddressEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.BuyerEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.CartEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.CartItemEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.InventoryEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.InventoryMovementEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.InvoiceEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.OrderEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.OrderItemEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.ProductEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.RefundEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.ReturnItemEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.ReturnRequestEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.SellerEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.ShipmentEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.TransactionEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.UserEntity;
import nexusmarket.application.adapters.persistence.jpa.entities.WarehouseEntity;
import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.Cart;
import nexusmarket.application.domain.models.CartItem;
import nexusmarket.application.domain.models.DigitalProduct;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.InventoryMovement;
import nexusmarket.application.domain.models.Invoice;
import nexusmarket.application.domain.models.Order;
import nexusmarket.application.domain.models.OrderItem;
import nexusmarket.application.domain.models.PhysicalProduct;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.Refund;
import nexusmarket.application.domain.models.ReturnItem;
import nexusmarket.application.domain.models.ReturnRequest;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.models.Shipment;
import nexusmarket.application.domain.models.Transaction;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Variant;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.valueobjects.CartState;
import nexusmarket.application.domain.valueobjects.CommercialState;
import nexusmarket.application.domain.valueobjects.InventoryCondition;
import nexusmarket.application.domain.valueobjects.InvoiceState;
import nexusmarket.application.domain.valueobjects.MovementType;
import nexusmarket.application.domain.valueobjects.OrderState;
import nexusmarket.application.domain.valueobjects.PaymentState;
import nexusmarket.application.domain.valueobjects.ProductState;
import nexusmarket.application.domain.valueobjects.ProductType;
import nexusmarket.application.domain.valueobjects.RefundState;
import nexusmarket.application.domain.valueobjects.ReturnState;
import nexusmarket.application.domain.valueobjects.ShipmentState;
import nexusmarket.application.domain.valueobjects.SystemRole;
import nexusmarket.application.domain.valueobjects.UserStatus;
import nexusmarket.application.domain.valueobjects.WarehouseType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class JpaMapper {

    public static String ensureId(String id) {
        return id == null || id.isBlank() ? UUID.randomUUID().toString() : id;
    }

    public static AddressEmbeddable toEmbeddable(Address address) {
        if (address == null) {
            return null;
        }
        AddressEmbeddable embeddable = new AddressEmbeddable();
        embeddable.setStreet(address.getStreet());
        embeddable.setCity(address.getCity());
        embeddable.setState(address.getState());
        embeddable.setPostalCode(address.getPostalCode());
        embeddable.setCountry(address.getCountry());
        embeddable.setAdditionalInfo(address.getAdditionalInfo());
        return embeddable;
    }

    public static Address toDomain(AddressEmbeddable embeddable) {
        if (embeddable == null) {
            return null;
        }
        Address address = new Address();
        address.setStreet(embeddable.getStreet());
        address.setCity(embeddable.getCity());
        address.setState(embeddable.getState());
        address.setPostalCode(embeddable.getPostalCode());
        address.setCountry(embeddable.getCountry());
        address.setAdditionalInfo(embeddable.getAdditionalInfo());
        return address;
    }

    public static AddressEntity toEntity(Address address) {
        if (address == null) {
            return null;
        }
        AddressEntity entity = new AddressEntity();
        entity.setId(ensureId(address.getId()));
        entity.setStreet(address.getStreet());
        entity.setCity(address.getCity());
        entity.setState(address.getState());
        entity.setPostalCode(address.getPostalCode());
        entity.setCountry(address.getCountry());
        entity.setAdditionalInfo(address.getAdditionalInfo());
        return entity;
    }

    public static Address toDomain(AddressEntity entity) {
        if (entity == null) {
            return null;
        }
        Address address = new Address();
        address.setId(entity.getId());
        address.setStreet(entity.getStreet());
        address.setCity(entity.getCity());
        address.setState(entity.getState());
        address.setPostalCode(entity.getPostalCode());
        address.setCountry(entity.getCountry());
        address.setAdditionalInfo(entity.getAdditionalInfo());
        return address;
    }

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(ensureId(user.getId()));
        entity.setFullName(user.getFullName());
        entity.setEmail(user.getEmail());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole() == null ? null : user.getRole().getCode());
        entity.setState(user.getState() == null ? null : user.getState().getCode());
        entity.setAuthTokenVersion(user.getAuthTokenVersion() == null ? 1 : user.getAuthTokenVersion());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        User user = new User();
        user.setId(entity.getId());
        user.setFullName(entity.getFullName());
        user.setEmail(entity.getEmail());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setRole(entity.getRole() == null ? null : SystemRole.fromCode(entity.getRole()));
        user.setState(entity.getState() == null ? null : UserStatus.fromCode(entity.getState()));
        user.setAuthTokenVersion(entity.getAuthTokenVersion() == null ? 1 : entity.getAuthTokenVersion());
        return user;
    }

    public static BuyerEntity toEntity(Buyer buyer) {
        if (buyer == null) {
            return null;
        }
        BuyerEntity entity = new BuyerEntity();
        entity.setId(ensureId(buyer.getId()));
        entity.setUserId(entity.getId());
        entity.setFullName(buyer.getFullName());
        entity.setEmail(buyer.getEmail());
        entity.setUsername(buyer.getUsername());
        entity.setPassword(buyer.getPassword());
        entity.setRole(buyer.getRole() == null ? null : buyer.getRole().getCode());
        entity.setState(buyer.getState() == null ? null : buyer.getState().getCode());
        entity.setCommercialState(buyer.getCommercialState() == null ? null : buyer.getCommercialState().getCode());
        entity.setPrimaryAddress(toEntity(buyer.getPrimaryAddress()));
        List<AddressEntity> additional = new ArrayList<>();
        if (buyer.getAdditionalAddresses() != null) {
            for (Address address : buyer.getAdditionalAddresses()) {
                additional.add(toEntity(address));
            }
        }
        entity.setAdditionalAddresses(additional);
        return entity;
    }

    public static Buyer toDomain(BuyerEntity entity) {
        if (entity == null) {
            return null;
        }
        Buyer buyer = new Buyer();
        buyer.setId(entity.getId());
        buyer.setFullName(entity.getFullName());
        buyer.setEmail(entity.getEmail());
        buyer.setUsername(entity.getUsername());
        buyer.setPassword(entity.getPassword());
        buyer.setRole(entity.getRole() == null ? null : SystemRole.fromCode(entity.getRole()));
        buyer.setState(entity.getState() == null ? null : UserStatus.fromCode(entity.getState()));
        buyer.setCommercialState(entity.getCommercialState() == null
                ? null : CommercialState.fromCode(entity.getCommercialState()));
        buyer.setPrimaryAddress(toDomain(entity.getPrimaryAddress()));
        List<Address> additional = new ArrayList<>();
        if (entity.getAdditionalAddresses() != null) {
            for (AddressEntity addressEntity : entity.getAdditionalAddresses()) {
                additional.add(toDomain(addressEntity));
            }
        }
        buyer.setAdditionalAddresses(additional);
        return buyer;
    }

    public static SellerEntity toEntity(Seller seller) {
        if (seller == null) {
            return null;
        }
        SellerEntity entity = new SellerEntity();
        entity.setId(ensureId(seller.getId()));
        entity.setUserId(entity.getId());
        entity.setFullName(seller.getFullName());
        entity.setEmail(seller.getEmail());
        entity.setUsername(seller.getUsername());
        entity.setPassword(seller.getPassword());
        entity.setRole(seller.getRole() == null ? null : seller.getRole().getCode());
        entity.setState(seller.getState() == null ? null : seller.getState().getCode());
        entity.setCompanyName(seller.getCompanyName());
        entity.setPhoneNumber(seller.getPhoneNumber());
        return entity;
    }

    public static Seller toDomain(SellerEntity entity) {
        if (entity == null) {
            return null;
        }
        Seller seller = new Seller();
        seller.setId(entity.getId());
        seller.setFullName(entity.getFullName());
        seller.setEmail(entity.getEmail());
        seller.setUsername(entity.getUsername());
        seller.setPassword(entity.getPassword());
        seller.setRole(entity.getRole() == null ? null : SystemRole.fromCode(entity.getRole()));
        seller.setState(entity.getState() == null ? null : UserStatus.fromCode(entity.getState()));
        seller.setCompanyName(entity.getCompanyName());
        seller.setPhoneNumber(entity.getPhoneNumber());
        return seller;
    }

    public static WarehouseEntity toEntity(Warehouse warehouse) {
        if (warehouse == null) {
            return null;
        }
        WarehouseEntity entity = new WarehouseEntity();
        entity.setId(ensureId(warehouse.getId()));
        entity.setName(warehouse.getName());
        entity.setAddress(toEmbeddable(warehouse.getAddress()));
        entity.setCapacity(warehouse.getCapacity());
        entity.setType(warehouse.getType() == null ? null : warehouse.getType().getCode());
        entity.setOwnerId(warehouse.getOwner() == null ? null : warehouse.getOwner().getId());
        return entity;
    }

    public static Warehouse toDomain(WarehouseEntity entity) {
        if (entity == null) {
            return null;
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setId(entity.getId());
        warehouse.setName(entity.getName());
        warehouse.setAddress(toDomain(entity.getAddress()));
        warehouse.setCapacity(entity.getCapacity());
        warehouse.setType(entity.getType() == null ? null : WarehouseType.fromCode(entity.getType()));
        if (entity.getOwnerId() != null) {
            Seller owner = new Seller();
            owner.setId(entity.getOwnerId());
            warehouse.setOwner(owner);
        }
        return warehouse;
    }

    public static ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        ProductEntity entity = new ProductEntity();
        entity.setId(ensureId(product.getId()));
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setSellerId(product.getSeller() == null ? null : product.getSeller().getId());
        entity.setState(product.getState() == null ? null : product.getState().getCode());
        entity.setProductType(product.getProductType() == null ? null : product.getProductType().getCode());
        if (product instanceof PhysicalProduct physical && physical.getVariant() != null) {
            entity.setColor(physical.getVariant().getColor());
            entity.setSize(physical.getVariant().getSize());
            entity.setModel(physical.getVariant().getModel());
        }
        if (product instanceof DigitalProduct digital) {
            entity.setFormat(digital.getFormat());
            entity.setDownloadUrl(digital.getDownloadUrl());
        }
        return entity;
    }

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        Product product;
        if (ProductType.DIGITAL.getCode().equalsIgnoreCase(entity.getProductType())) {
            DigitalProduct digital = new DigitalProduct();
            digital.setFormat(entity.getFormat());
            digital.setDownloadUrl(entity.getDownloadUrl());
            product = digital;
        } else {
            PhysicalProduct physical = new PhysicalProduct();
            Variant variant = new Variant();
            variant.setColor(entity.getColor());
            variant.setSize(entity.getSize());
            variant.setModel(entity.getModel());
            if (variant.getColor() != null || variant.getSize() != null || variant.getModel() != null) {
                physical.setVariant(variant);
            }
            product = physical;
        }
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        product.setPrice(entity.getPrice());
        product.setState(entity.getState() == null ? null : ProductState.fromCode(entity.getState()));
        if (entity.getSellerId() != null) {
            Seller seller = new Seller();
            seller.setId(entity.getSellerId());
            product.setSeller(seller);
        }
        return product;
    }

    public static InventoryEntity toEntity(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        InventoryEntity entity = new InventoryEntity();
        entity.setId(ensureId(inventory.getId()));
        entity.setProductId(inventory.getProduct() == null ? null : inventory.getProduct().getId());
        entity.setWarehouseId(inventory.getWarehouse() == null ? null : inventory.getWarehouse().getId());
        entity.setAvailableQuantity(inventory.getAvailableQuantity());
        entity.setReservedQuantity(inventory.getReservedQuantity());
        entity.setDamagedQuantity(inventory.getDamagedQuantity());
        entity.setCondition(inventory.getCondition() == null ? null : inventory.getCondition().getCode());
        return entity;
    }

    public static Inventory toDomain(InventoryEntity entity) {
        if (entity == null) {
            return null;
        }
        Inventory inventory = new Inventory();
        inventory.setId(entity.getId());
        if (entity.getProductId() != null) {
            PhysicalProduct product = new PhysicalProduct();
            product.setId(entity.getProductId());
            inventory.setProduct(product);
        }
        if (entity.getWarehouseId() != null) {
            Warehouse warehouse = new Warehouse();
            warehouse.setId(entity.getWarehouseId());
            inventory.setWarehouse(warehouse);
        }
        inventory.setAvailableQuantity(entity.getAvailableQuantity());
        inventory.setReservedQuantity(entity.getReservedQuantity());
        inventory.setDamagedQuantity(entity.getDamagedQuantity());
        inventory.setCondition(entity.getCondition() == null
                ? null : InventoryCondition.fromCode(entity.getCondition()));
        return inventory;
    }

    public static InventoryMovementEntity toEntity(InventoryMovement movement) {
        if (movement == null) {
            return null;
        }
        InventoryMovementEntity entity = new InventoryMovementEntity();
        entity.setId(ensureId(movement.getId()));
        entity.setInventoryId(movement.getInventory() == null ? null : movement.getInventory().getId());
        entity.setProductId(movement.getInventory() == null || movement.getInventory().getProduct() == null
                ? null : movement.getInventory().getProduct().getId());
        entity.setWarehouseId(movement.getInventory() == null || movement.getInventory().getWarehouse() == null
                ? null : movement.getInventory().getWarehouse().getId());
        entity.setType(movement.getType() == null ? null : movement.getType().getCode());
        entity.setQuantity(movement.getQuantity());
        entity.setOccurredAt(movement.getOccurredAt());
        entity.setPerformedById(movement.getPerformedBy() == null ? null : movement.getPerformedBy().getId());
        entity.setReference(movement.getReference());
        return entity;
    }

    public static InventoryMovement toDomain(InventoryMovementEntity entity) {
        if (entity == null) {
            return null;
        }
        InventoryMovement movement = new InventoryMovement();
        movement.setId(entity.getId());
        if (entity.getInventoryId() != null) {
            Inventory inventory = new Inventory();
            inventory.setId(entity.getInventoryId());
            movement.setInventory(inventory);
        }
        movement.setType(entity.getType() == null ? null : MovementType.fromCode(entity.getType()));
        movement.setQuantity(entity.getQuantity());
        movement.setOccurredAt(entity.getOccurredAt());
        if (entity.getPerformedById() != null) {
            User user = new User();
            user.setId(entity.getPerformedById());
            movement.setPerformedBy(user);
        }
        movement.setReference(entity.getReference());
        return movement;
    }

    public static CartEntity toEntity(Cart cart) {
        if (cart == null) {
            return null;
        }
        CartEntity entity = new CartEntity();
        entity.setId(ensureId(cart.getId()));
        entity.setBuyerId(cart.getBuyer() == null ? null : cart.getBuyer().getId());
        entity.setState(cart.getState() == null ? null : cart.getState().getCode());
        List<CartItemEntity> items = new ArrayList<>();
        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                CartItemEntity itemEntity = new CartItemEntity();
                itemEntity.setId(ensureId(item.getId()));
                itemEntity.setProductId(item.getProduct() == null ? null : item.getProduct().getId());
                itemEntity.setQuantity(item.getQuantity());
                items.add(itemEntity);
            }
        }
        entity.setItems(items);
        return entity;
    }

    public static Cart toDomain(CartEntity entity) {
        if (entity == null) {
            return null;
        }
        Cart cart = new Cart();
        cart.setId(entity.getId());
        if (entity.getBuyerId() != null) {
            Buyer buyer = new Buyer();
            buyer.setId(entity.getBuyerId());
            cart.setBuyer(buyer);
        }
        cart.setState(entity.getState() == null ? null : CartState.fromCode(entity.getState()));
        List<CartItem> items = new ArrayList<>();
        if (entity.getItems() != null) {
            for (CartItemEntity itemEntity : entity.getItems()) {
                CartItem item = new CartItem();
                item.setId(itemEntity.getId());
                if (itemEntity.getProductId() != null) {
                    PhysicalProduct product = new PhysicalProduct();
                    product.setId(itemEntity.getProductId());
                    item.setProduct(product);
                }
                item.setQuantity(itemEntity.getQuantity());
                items.add(item);
            }
        }
        cart.setItems(items);
        return cart;
    }

    public static OrderEntity toEntity(Order order) {
        if (order == null) {
            return null;
        }
        OrderEntity entity = new OrderEntity();
        entity.setId(ensureId(order.getId()));
        entity.setBuyerId(order.getBuyer() == null ? null : order.getBuyer().getId());
        entity.setBuyerEmail(order.getBuyer() == null ? null : order.getBuyer().getEmail());
        entity.setBuyerFullName(order.getBuyer() == null ? null : order.getBuyer().getFullName());
        entity.setState(order.getState() == null ? null : order.getState().getCode());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setPaidAt(order.getPaidAt());
        entity.setDispatchedAt(order.getDispatchedAt());
        entity.setDeliveredAt(order.getDeliveredAt());
        entity.setCancelledAt(order.getCancelledAt());
        entity.setInvoiceId(order.getInvoice() == null ? null : order.getInvoice().getId());
        entity.setTransactionId(order.getTransaction() == null ? null : order.getTransaction().getId());
        entity.setShipmentId(order.getShipment() == null ? null : order.getShipment().getId());
        entity.setReturnRequestId(order.getReturnRequest() == null ? null : order.getReturnRequest().getId());
        List<OrderItemEntity> items = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                OrderItemEntity itemEntity = new OrderItemEntity();
                itemEntity.setId(ensureId(item.getId()));
                itemEntity.setProductId(item.getProduct() == null ? null : item.getProduct().getId());
                itemEntity.setProductName(item.getProduct() == null ? null : item.getProduct().getName());
                itemEntity.setSellerId(item.getProduct() == null || item.getProduct().getSeller() == null
                        ? null : item.getProduct().getSeller().getId());
                itemEntity.setProductType(item.getProduct() == null || item.getProduct().getProductType() == null
                        ? null : item.getProduct().getProductType().getCode());
                itemEntity.setUnitPrice(item.getUnitPrice());
                itemEntity.setQuantity(item.getQuantity());
                itemEntity.setSubtotal(item.getSubtotal());
                items.add(itemEntity);
            }
        }
        entity.setItems(items);
        return entity;
    }

    public static Order toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }
        Order order = new Order();
        order.setId(entity.getId());
        if (entity.getBuyerId() != null) {
            Buyer buyer = new Buyer();
            buyer.setId(entity.getBuyerId());
            buyer.setEmail(entity.getBuyerEmail());
            buyer.setFullName(entity.getBuyerFullName());
            order.setBuyer(buyer);
        }
        order.setState(entity.getState() == null ? null : OrderState.fromCode(entity.getState()));
        order.setTotalAmount(entity.getTotalAmount());
        order.setCreatedAt(entity.getCreatedAt());
        order.setPaidAt(entity.getPaidAt());
        order.setDispatchedAt(entity.getDispatchedAt());
        order.setDeliveredAt(entity.getDeliveredAt());
        order.setCancelledAt(entity.getCancelledAt());
        List<OrderItem> items = new ArrayList<>();
        if (entity.getItems() != null) {
            for (OrderItemEntity itemEntity : entity.getItems()) {
                OrderItem item = new OrderItem();
                item.setId(itemEntity.getId());
                item.setQuantity(itemEntity.getQuantity());
                item.setUnitPrice(itemEntity.getUnitPrice());
                item.setSubtotal(itemEntity.getSubtotal());
                Product product;
                if (ProductType.DIGITAL.getCode().equalsIgnoreCase(itemEntity.getProductType())) {
                    product = new DigitalProduct();
                } else {
                    product = new PhysicalProduct();
                }
                product.setId(itemEntity.getProductId());
                product.setName(itemEntity.getProductName());
                product.setPrice(itemEntity.getUnitPrice());
                if (itemEntity.getSellerId() != null) {
                    Seller seller = new Seller();
                    seller.setId(itemEntity.getSellerId());
                    product.setSeller(seller);
                }
                item.setProduct(product);
                items.add(item);
            }
        }
        order.setItems(items);
        return order;
    }

    public static InvoiceEntity toEntity(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(ensureId(invoice.getId()));
        entity.setOrderId(invoice.getOrder() == null ? null : invoice.getOrder().getId());
        entity.setBuyerId(invoice.getOrder() == null || invoice.getOrder().getBuyer() == null
                ? null : invoice.getOrder().getBuyer().getId());
        entity.setNumber(invoice.getNumber());
        entity.setIssuedAt(invoice.getIssuedAt());
        entity.setTotal(invoice.getTotal());
        entity.setState(invoice.getState() == null ? null : invoice.getState().getCode());
        return entity;
    }

    public static Invoice toDomain(InvoiceEntity entity) {
        if (entity == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setId(entity.getId());
        if (entity.getOrderId() != null) {
            Order order = new Order();
            order.setId(entity.getOrderId());
            if (entity.getBuyerId() != null) {
                Buyer buyer = new Buyer();
                buyer.setId(entity.getBuyerId());
                order.setBuyer(buyer);
            }
            invoice.setOrder(order);
        }
        invoice.setNumber(entity.getNumber());
        invoice.setIssuedAt(entity.getIssuedAt());
        invoice.setTotal(entity.getTotal());
        invoice.setState(entity.getState() == null ? null : InvoiceState.fromCode(entity.getState()));
        return invoice;
    }

    public static TransactionEntity toEntity(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        TransactionEntity entity = new TransactionEntity();
        entity.setId(ensureId(transaction.getId()));
        entity.setOrderId(transaction.getOrder() == null ? null : transaction.getOrder().getId());
        entity.setBuyerId(transaction.getOrder() == null || transaction.getOrder().getBuyer() == null
                ? null : transaction.getOrder().getBuyer().getId());
        entity.setAmount(transaction.getAmount());
        entity.setState(transaction.getState() == null ? null : transaction.getState().getCode());
        entity.setProviderReference(transaction.getProviderReference());
        return entity;
    }

    public static Transaction toDomain(TransactionEntity entity) {
        if (entity == null) {
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.setId(entity.getId());
        if (entity.getOrderId() != null) {
            Order order = new Order();
            order.setId(entity.getOrderId());
            if (entity.getBuyerId() != null) {
                Buyer buyer = new Buyer();
                buyer.setId(entity.getBuyerId());
                order.setBuyer(buyer);
            }
            transaction.setOrder(order);
        }
        transaction.setAmount(entity.getAmount());
        transaction.setState(entity.getState() == null ? null : PaymentState.fromCode(entity.getState()));
        transaction.setProviderReference(entity.getProviderReference());
        return transaction;
    }

    public static ShipmentEntity toEntity(Shipment shipment) {
        if (shipment == null) {
            return null;
        }
        ShipmentEntity entity = new ShipmentEntity();
        entity.setId(ensureId(shipment.getId()));
        entity.setOrderId(shipment.getOrder() == null ? null : shipment.getOrder().getId());
        entity.setTrackingGuide(shipment.getTrackingGuide());
        entity.setState(shipment.getState() == null ? null : shipment.getState().getCode());
        entity.setOriginAddress(toEmbeddable(shipment.getOriginAddress()));
        entity.setDestinationAddress(toEmbeddable(shipment.getDestinationAddress()));
        return entity;
    }

    public static Shipment toDomain(ShipmentEntity entity) {
        if (entity == null) {
            return null;
        }
        Shipment shipment = new Shipment();
        shipment.setId(entity.getId());
        if (entity.getOrderId() != null) {
            Order order = new Order();
            order.setId(entity.getOrderId());
            shipment.setOrder(order);
        }
        shipment.setTrackingGuide(entity.getTrackingGuide());
        shipment.setState(entity.getState() == null ? null : ShipmentState.fromCode(entity.getState()));
        shipment.setOriginAddress(toDomain(entity.getOriginAddress()));
        shipment.setDestinationAddress(toDomain(entity.getDestinationAddress()));
        return shipment;
    }

    public static ReturnRequestEntity toEntity(ReturnRequest returnRequest) {
        if (returnRequest == null) {
            return null;
        }
        ReturnRequestEntity entity = new ReturnRequestEntity();
        entity.setId(ensureId(returnRequest.getId()));
        entity.setOrderId(returnRequest.getOrder() == null ? null : returnRequest.getOrder().getId());
        entity.setBuyerId(returnRequest.getOrder() == null || returnRequest.getOrder().getBuyer() == null
                ? null : returnRequest.getOrder().getBuyer().getId());
        entity.setReason(returnRequest.getReason());
        entity.setState(returnRequest.getState() == null ? null : returnRequest.getState().getCode());
        entity.setRequestedAt(returnRequest.getRequestedAt());
        List<ReturnItemEntity> items = new ArrayList<>();
        if (returnRequest.getItems() != null) {
            for (ReturnItem item : returnRequest.getItems()) {
                ReturnItemEntity itemEntity = new ReturnItemEntity();
                itemEntity.setId(ensureId(item.getId()));
                itemEntity.setOrderItemId(item.getOrderItem() == null ? null : item.getOrderItem().getId());
                itemEntity.setProductId(item.getOrderItem() == null || item.getOrderItem().getProduct() == null
                        ? null : item.getOrderItem().getProduct().getId());
                itemEntity.setQuantity(item.getQuantity());
                items.add(itemEntity);
            }
        }
        entity.setItems(items);
        return entity;
    }

    public static ReturnRequest toDomain(ReturnRequestEntity entity) {
        if (entity == null) {
            return null;
        }
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setId(entity.getId());
        if (entity.getOrderId() != null) {
            Order order = new Order();
            order.setId(entity.getOrderId());
            returnRequest.setOrder(order);
        }
        returnRequest.setReason(entity.getReason());
        returnRequest.setState(entity.getState() == null ? null : ReturnState.fromCode(entity.getState()));
        returnRequest.setRequestedAt(entity.getRequestedAt());
        List<ReturnItem> items = new ArrayList<>();
        if (entity.getItems() != null) {
            for (ReturnItemEntity itemEntity : entity.getItems()) {
                ReturnItem item = new ReturnItem();
                item.setId(itemEntity.getId());
                if (itemEntity.getOrderItemId() != null) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setId(itemEntity.getOrderItemId());
                    item.setOrderItem(orderItem);
                }
                item.setQuantity(itemEntity.getQuantity());
                items.add(item);
            }
        }
        returnRequest.setItems(items);
        return returnRequest;
    }

    public static RefundEntity toEntity(Refund refund) {
        if (refund == null) {
            return null;
        }
        RefundEntity entity = new RefundEntity();
        entity.setId(ensureId(refund.getId()));
        entity.setReturnId(refund.getReturnRequest() == null ? null : refund.getReturnRequest().getId());
        entity.setAmount(refund.getAmount());
        entity.setState(refund.getState() == null ? null : refund.getState().getCode());
        entity.setProcessedAt(refund.getProcessedAt());
        return entity;
    }

    public static Refund toDomain(RefundEntity entity) {
        if (entity == null) {
            return null;
        }
        Refund refund = new Refund();
        refund.setId(entity.getId());
        if (entity.getReturnId() != null) {
            ReturnRequest returnRequest = new ReturnRequest();
            returnRequest.setId(entity.getReturnId());
            refund.setReturnRequest(returnRequest);
        }
        refund.setAmount(entity.getAmount());
        refund.setState(entity.getState() == null ? null : RefundState.fromCode(entity.getState()));
        refund.setProcessedAt(entity.getProcessedAt());
        return refund;
    }
}
