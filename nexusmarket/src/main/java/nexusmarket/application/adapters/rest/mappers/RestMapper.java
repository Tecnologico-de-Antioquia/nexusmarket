package nexusmarket.application.adapters.rest.mappers;

import lombok.experimental.UtilityClass;
import nexusmarket.application.adapters.rest.dtos.AdminDtos;
import nexusmarket.application.adapters.rest.dtos.AuthDtos;
import nexusmarket.application.adapters.rest.dtos.BuyerDtos;
import nexusmarket.application.adapters.rest.dtos.CartDtos;
import nexusmarket.application.adapters.rest.dtos.CatalogDtos;
import nexusmarket.application.adapters.rest.dtos.InventoryDtos;
import nexusmarket.application.adapters.rest.dtos.InvoiceDtos;
import nexusmarket.application.adapters.rest.dtos.OrderDtos;
import nexusmarket.application.adapters.rest.dtos.ReturnDtos;
import nexusmarket.application.adapters.rest.dtos.SellerDtos;
import nexusmarket.application.adapters.rest.dtos.ShipmentDtos;
import nexusmarket.application.adapters.rest.dtos.WarehouseDtos;
import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.models.AuditLog;
import nexusmarket.application.domain.models.AuthenticationResult;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.Cart;
import nexusmarket.application.domain.models.CartItem;
import nexusmarket.application.domain.models.DigitalProduct;
import nexusmarket.application.domain.models.Inventory;
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
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Variant;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.valueobjects.SystemRole;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RestMapper {

    public User toDomain(AuthDtos.LoginRequest dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        return user;
    }

    public AuthDtos.LoginResponse toLoginResponse(AuthenticationResult result) {
        if (result == null) {
            return null;
        }
        User user = result.getAuthenticatedUser();
        AuthDtos.UserInfo info = new AuthDtos.UserInfo(
                user == null ? null : user.getId(),
                user == null ? null : user.getUsername(),
                user == null ? null : user.getFullName(),
                user == null ? null : user.getEmail(),
                user == null || user.getRole() == null ? null : user.getRole().getCode(),
                user == null || user.getState() == null ? null : user.getState().getCode());
        return new AuthDtos.LoginResponse(result.getToken(), "Bearer", result.getExpiresIn(), info);
    }

    public Buyer toDomain(BuyerDtos.RegisterRequest dto) {
        Buyer buyer = new Buyer();
        buyer.setFullName(dto.fullName());
        buyer.setEmail(dto.email());
        buyer.setUsername(dto.username());
        buyer.setPassword(dto.password());
        if (dto.primaryAddress() != null) {
            buyer.setPrimaryAddress(toDomain(dto.primaryAddress()));
        }
        return buyer;
    }

    public Buyer toDomain(BuyerDtos.UpdateRequest dto) {
        Buyer buyer = new Buyer();
        buyer.setFullName(dto.fullName());
        buyer.setEmail(dto.email());
        return buyer;
    }

    public Address toDomain(BuyerDtos.AddressRequest dto) {
        if (dto == null) {
            return null;
        }
        Address address = new Address();
        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setPostalCode(dto.postalCode());
        address.setCountry(dto.country());
        address.setAdditionalInfo(dto.additionalInfo());
        return address;
    }

    public BuyerDtos.AddressResponse toAddressResponse(Address address) {
        if (address == null) {
            return null;
        }
        return new BuyerDtos.AddressResponse(address.getId(), address.getStreet(), address.getCity(),
                address.getState(), address.getPostalCode(), address.getCountry(), address.getAdditionalInfo());
    }

    public BuyerDtos.BuyerResponse toBuyerResponse(Buyer buyer) {
        if (buyer == null) {
            return null;
        }
        List<BuyerDtos.AddressResponse> additional = buyer.getAdditionalAddresses() == null
                ? List.of()
                : buyer.getAdditionalAddresses().stream().map(RestMapper::toAddressResponse).toList();
        return new BuyerDtos.BuyerResponse(buyer.getId(), buyer.getFullName(), buyer.getEmail(),
                buyer.getUsername(),
                buyer.getCommercialState() == null ? null : buyer.getCommercialState().getCode(),
                toAddressResponse(buyer.getPrimaryAddress()), additional);
    }

    public Seller toDomain(SellerDtos.RegisterRequest dto) {
        Seller seller = new Seller();
        seller.setFullName(dto.fullName());
        seller.setEmail(dto.email());
        seller.setUsername(dto.username());
        seller.setPassword(dto.password());
        seller.setCompanyName(dto.companyName());
        seller.setPhoneNumber(dto.phoneNumber());
        return seller;
    }

    public Seller toDomain(SellerDtos.UpdateRequest dto) {
        Seller seller = new Seller();
        seller.setId(dto.id());
        seller.setFullName(dto.fullName());
        seller.setEmail(dto.email());
        seller.setCompanyName(dto.companyName());
        seller.setPhoneNumber(dto.phoneNumber());
        return seller;
    }

    public SellerDtos.SellerResponse toSellerResponse(Seller seller) {
        if (seller == null) {
            return null;
        }
        return new SellerDtos.SellerResponse(seller.getId(), seller.getFullName(), seller.getEmail(),
                seller.getUsername(), seller.getCompanyName(), seller.getPhoneNumber(),
                seller.getState() == null ? null : seller.getState().getCode());
    }

    public Warehouse toDomain(WarehouseDtos.WarehouseRequest dto) {
        if (dto == null) {
            return null;
        }
        Warehouse warehouse = new Warehouse();
        warehouse.setName(dto.name());
        warehouse.setCapacity(dto.capacity());
        warehouse.setAddress(toDomain(dto.address()));
        if (dto.ownerId() != null) {
            Seller owner = new Seller();
            owner.setId(dto.ownerId());
            warehouse.setOwner(owner);
        }
        return warehouse;
    }

    public WarehouseDtos.WarehouseResponse toWarehouseResponse(Warehouse warehouse) {
        if (warehouse == null) {
            return null;
        }
        return new WarehouseDtos.WarehouseResponse(warehouse.getId(), warehouse.getName(),
                warehouse.getType() == null ? null : warehouse.getType().getCode(),
                warehouse.getCapacity(),
                warehouse.getOwner() == null ? null : warehouse.getOwner().getId(),
                toAddressResponse(warehouse.getAddress()));
    }

    public Product toDomain(CatalogDtos.ProductRequest dto) {
        if ("PHYSICAL".equalsIgnoreCase(dto.type())) {
            PhysicalProduct product = new PhysicalProduct();
            product.setName(dto.name());
            product.setDescription(dto.description());
            product.setPrice(dto.price());
            Variant variant = new Variant();
            variant.setColor(dto.color());
            variant.setSize(dto.size());
            variant.setModel(dto.model());
            product.setVariant(variant);
            return product;
        }
        DigitalProduct product = new DigitalProduct();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setFormat(dto.format());
        product.setDownloadUrl(dto.downloadUrl());
        return product;
    }

    public CatalogDtos.ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        String color = null;
        Integer size = null;
        String model = null;
        String format = null;
        String downloadUrl = null;
        if (product instanceof PhysicalProduct physical && physical.getVariant() != null) {
            color = physical.getVariant().getColor();
            size = physical.getVariant().getSize();
            model = physical.getVariant().getModel();
        }
        if (product instanceof DigitalProduct digital) {
            format = digital.getFormat();
            downloadUrl = digital.getDownloadUrl();
        }
        return new CatalogDtos.ProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(),
                product.getProductType() == null ? null : product.getProductType().getCode(),
                product.getState() == null ? null : product.getState().getCode(),
                product.getSeller() == null ? null : product.getSeller().getId(),
                color, size, model, format, downloadUrl);
    }

    public CartDtos.CartResponse toCartResponse(Cart cart) {
        if (cart == null) {
            return null;
        }
        List<CartDtos.CartItemResponse> items = cart.getItems() == null
                ? List.of()
                : cart.getItems().stream().map(RestMapper::toCartItemResponse).toList();
        return new CartDtos.CartResponse(cart.getId(),
                cart.getBuyer() == null ? null : cart.getBuyer().getId(),
                cart.getState() == null ? null : cart.getState().getCode(), items);
    }

    private CartDtos.CartItemResponse toCartItemResponse(CartItem item) {
        return new CartDtos.CartItemResponse(
                item.getProduct() == null ? null : item.getProduct().getId(),
                item.getProduct() == null ? null : item.getProduct().getName(),
                item.getQuantity() == null ? 0 : item.getQuantity(),
                item.getProduct() == null ? null : item.getProduct().getPrice());
    }

    public OrderDtos.OrderResponse toOrderResponse(Order order) {
        if (order == null) {
            return null;
        }
        List<OrderDtos.OrderItemResponse> items = order.getItems() == null
                ? List.of()
                : order.getItems().stream().map(RestMapper::toOrderItemResponse).toList();
        return new OrderDtos.OrderResponse(order.getId(),
                order.getBuyer() == null ? null : order.getBuyer().getId(),
                order.getState() == null ? null : order.getState().getCode(),
                order.getTotalAmount(), order.getCreatedAt(), order.getPaidAt(), order.getDispatchedAt(),
                order.getDeliveredAt(), items,
                order.getInvoice() == null ? null : order.getInvoice().getId(),
                order.getShipment() == null ? null : order.getShipment().getId());
    }

    private OrderDtos.OrderItemResponse toOrderItemResponse(OrderItem item) {
        return new OrderDtos.OrderItemResponse(
                item.getId(),
                item.getProduct() == null ? null : item.getProduct().getId(),
                item.getProduct() == null ? null : item.getProduct().getName(),
                item.getQuantity() == null ? 0 : item.getQuantity(),
                item.getUnitPrice(), item.getSubtotal());
    }

    public InventoryDtos.InventoryResponse toInventoryResponse(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        return new InventoryDtos.InventoryResponse(inventory.getId(),
                inventory.getProduct() == null ? null : inventory.getProduct().getId(),
                inventory.getWarehouse() == null ? null : inventory.getWarehouse().getId(),
                inventory.getAvailableQuantity() == null ? 0 : inventory.getAvailableQuantity(),
                inventory.getReservedQuantity() == null ? 0 : inventory.getReservedQuantity(),
                inventory.getDamagedQuantity() == null ? 0 : inventory.getDamagedQuantity(),
                inventory.getCondition() == null ? null : inventory.getCondition().getCode());
    }

    public InvoiceDtos.InvoiceResponse toInvoiceResponse(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        return new InvoiceDtos.InvoiceResponse(invoice.getId(),
                invoice.getOrder() == null ? null : invoice.getOrder().getId(),
                invoice.getNumber(), invoice.getTotal(),
                invoice.getState() == null ? null : invoice.getState().getCode(),
                invoice.getIssuedAt());
    }

    public ShipmentDtos.ShipmentResponse toShipmentResponse(Shipment shipment) {
        if (shipment == null) {
            return null;
        }
        return new ShipmentDtos.ShipmentResponse(shipment.getId(),
                shipment.getOrder() == null ? null : shipment.getOrder().getId(),
                shipment.getTrackingGuide(),
                shipment.getState() == null ? null : shipment.getState().getCode(),
                toAddressResponse(shipment.getOriginAddress()),
                toAddressResponse(shipment.getDestinationAddress()));
    }

    public ReturnDtos.ReturnResponse toReturnResponse(ReturnRequest returnRequest) {
        if (returnRequest == null) {
            return null;
        }
        List<ReturnDtos.ReturnItemResponse> items = returnRequest.getItems() == null
                ? List.of()
                : returnRequest.getItems().stream().map(RestMapper::toReturnItemResponse).toList();
        return new ReturnDtos.ReturnResponse(returnRequest.getId(),
                returnRequest.getOrder() == null ? null : returnRequest.getOrder().getId(),
                returnRequest.getReason(),
                returnRequest.getState() == null ? null : returnRequest.getState().getCode(),
                returnRequest.getRequestedAt(), items);
    }

    private ReturnDtos.ReturnItemResponse toReturnItemResponse(ReturnItem item) {
        return new ReturnDtos.ReturnItemResponse(
                item.getOrderItem() == null ? null : item.getOrderItem().getId(),
                item.getOrderItem() == null || item.getOrderItem().getProduct() == null
                        ? null : item.getOrderItem().getProduct().getId(),
                item.getQuantity() == null ? 0 : item.getQuantity());
    }

    public ReturnDtos.RefundResponse toRefundResponse(Refund refund) {
        if (refund == null) {
            return null;
        }
        return new ReturnDtos.RefundResponse(refund.getId(),
                refund.getReturnRequest() == null ? null : refund.getReturnRequest().getId(),
                refund.getAmount(),
                refund.getState() == null ? null : refund.getState().getCode(),
                refund.getProcessedAt());
    }

    public AdminDtos.UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return new AdminDtos.UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getUsername(),
                user.getRole() == null ? null : user.getRole().getCode(),
                user.getState() == null ? null : user.getState().getCode());
    }

    public AdminDtos.AuditLogResponse toAuditLogResponse(AuditLog auditLog) {
        if (auditLog == null) {
            return null;
        }
        return new AdminDtos.AuditLogResponse(auditLog.getAuditId(), auditLog.getOperationType(),
                auditLog.getOperationDate(),
                auditLog.getPerformedBy() == null ? null : auditLog.getPerformedBy().getUsername(),
                auditLog.getPerformedByRole() == null ? null : auditLog.getPerformedByRole().getCode(),
                auditLog.getAffectedType(), auditLog.getAffectedId(), auditLog.getDetails());
    }

    public User toDomain(AdminDtos.UserRequest dto) {
        User user = new User();
        user.setFullName(dto.fullName());
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        user.setRole(SystemRole.fromCode(dto.role()));
        return user;
    }

    public User toDomain(AdminDtos.UpdateUserRequest dto) {
        User user = new User();
        user.setId(dto.id());
        user.setFullName(dto.fullName());
        user.setEmail(dto.email());
        if (dto.role() != null) {
            user.setRole(SystemRole.fromCode(dto.role()));
        }
        return user;
    }

    public List<ReturnItem> toReturnItems(List<ReturnDtos.ReturnItemRequest> requests) {
        List<ReturnItem> items = new ArrayList<>();
        if (requests == null) {
            return items;
        }
        for (ReturnDtos.ReturnItemRequest request : requests) {
            ReturnItem item = new ReturnItem();
            OrderItem orderItem = new OrderItem();
            orderItem.setId(request.orderItemId());
            item.setOrderItem(orderItem);
            item.setQuantity(request.quantity());
            items.add(item);
        }
        return items;
    }
}
