package nexusmarket.application.infrastructure.seed;

import lombok.RequiredArgsConstructor;
import nexusmarket.application.domain.models.Address;
import nexusmarket.application.domain.models.Buyer;
import nexusmarket.application.domain.models.Inventory;
import nexusmarket.application.domain.models.PhysicalProduct;
import nexusmarket.application.domain.models.Product;
import nexusmarket.application.domain.models.Seller;
import nexusmarket.application.domain.models.User;
import nexusmarket.application.domain.models.Warehouse;
import nexusmarket.application.domain.ports.out.BuyerRepositoryPort;
import nexusmarket.application.domain.ports.out.InventoryRepositoryPort;
import nexusmarket.application.domain.ports.out.PasswordServicePort;
import nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import nexusmarket.application.domain.ports.out.SellerRepositoryPort;
import nexusmarket.application.domain.ports.out.UserRepositoryPort;
import nexusmarket.application.domain.ports.out.WarehouseRepositoryPort;
import nexusmarket.application.domain.valueobjects.CommercialState;
import nexusmarket.application.domain.valueobjects.ProductState;
import nexusmarket.application.domain.valueobjects.SystemRole;
import nexusmarket.application.domain.valueobjects.UserStatus;
import nexusmarket.application.domain.valueobjects.WarehouseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final UserRepositoryPort userRepositoryPort;
    private final BuyerRepositoryPort buyerRepositoryPort;
    private final SellerRepositoryPort sellerRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final PasswordServicePort passwordServicePort;

    @Override
    public void run(String... args) {
        if (userRepositoryPort.existsByUsername("admin")) {
            log.info("Datos iniciales ya presentes; se omite la carga.");
            return;
        }

        User admin = createUser("Administrador Nexus", "admin@nexusmarket.com", "admin", "Admin123!",
                SystemRole.ADMINISTRATOR);
        createUser("Supervisor Nexus", "supervisor@nexusmarket.com", "supervisor", "Supervisor123!",
                SystemRole.SUPERVISOR);
        createUser("Operador Logistico", "logistics@nexusmarket.com", "logistics", "Logistics123!",
                SystemRole.LOGISTIC_OPERATOR);
        User sellerUser = createUser("Vendedor Nexus", "seller@nexusmarket.com", "seller", "Seller123!",
                SystemRole.SELLER);
        User buyerUser = createUser("Comprador Nexus", "buyer@nexusmarket.com", "buyer", "Buyer123!",
                SystemRole.BUYER);

        Seller seller = new Seller();
        seller.setId(sellerUser.getId());
        seller.setFullName(sellerUser.getFullName());
        seller.setEmail(sellerUser.getEmail());
        seller.setUsername(sellerUser.getUsername());
        seller.setPassword(sellerUser.getPassword());
        seller.setRole(SystemRole.SELLER);
        seller.setState(UserStatus.ACTIVE);
        seller.setCompanyName("NexusMarket Demo");
        seller.setPhoneNumber("3000000000");
        Seller savedSeller = sellerRepositoryPort.save(seller);

        Buyer buyer = new Buyer();
        buyer.setId(buyerUser.getId());
        buyer.setFullName(buyerUser.getFullName());
        buyer.setEmail(buyerUser.getEmail());
        buyer.setUsername(buyerUser.getUsername());
        buyer.setPassword(buyerUser.getPassword());
        buyer.setRole(SystemRole.BUYER);
        buyer.setState(UserStatus.ACTIVE);
        buyer.setCommercialState(CommercialState.ACTIVE);
        buyer.setPrimaryAddress(createAddress("Calle 100 # 15-20", "Bogota", "Cundinamarca", "110111", "Colombia"));
        buyerRepositoryPort.save(buyer);

        Warehouse warehouse = new Warehouse();
        warehouse.setName("Bodega Principal");
        warehouse.setAddress(createAddress("Carrera 50 # 10-30", "Bogota", "Cundinamarca", "110221", "Colombia"));
        warehouse.setCapacity(1000);
        warehouse.setType(WarehouseType.SELLER);
        warehouse.setOwner(savedSeller);
        Warehouse savedWarehouse = warehouseRepositoryPort.save(warehouse);

        PhysicalProduct product = new PhysicalProduct();
        product.setName("Producto de ejemplo");
        product.setDescription("Producto fisico de demostracion para NexusMarket.");
        product.setPrice(new BigDecimal("99900"));
        product.setSeller(savedSeller);
        product.setState(ProductState.PUBLISHED);
        Product savedProduct = productRepositoryPort.save(product);

        Inventory inventory = new Inventory();
        inventory.setProduct(savedProduct);
        inventory.setWarehouse(savedWarehouse);
        inventory.setAvailableQuantity(50);
        inventoryRepositoryPort.save(inventory);

        log.info("Datos iniciales cargados. Usuario administrador: {} (usuario: admin).", admin.getUsername());
    }

    private User createUser(String fullName, String email, String username, String rawPassword, SystemRole role) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(passwordServicePort.encrypt(rawPassword));
        user.setRole(role);
        user.setState(UserStatus.ACTIVE);
        user.setAuthTokenVersion(1);
        return userRepositoryPort.save(user);
    }

    private Address createAddress(String street, String city, String state, String postalCode, String country) {
        Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(postalCode);
        address.setCountry(country);
        return address;
    }
}
