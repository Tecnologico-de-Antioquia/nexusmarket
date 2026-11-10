package nexusmarket.application.domain.models;

import nexusmarket.application.domain.exceptions.InsufficientStockException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryTest {

    @Test
    void reserveAndReleaseKeepQuantitiesConsistent() {
        Inventory inventory = new Inventory();
        inventory.setAvailableQuantity(10);

        inventory.reserve(4);
        assertEquals(6, inventory.getAvailableQuantity());
        assertEquals(4, inventory.getReservedQuantity());

        inventory.releaseReservation(4);
        assertEquals(10, inventory.getAvailableQuantity());
        assertEquals(0, inventory.getReservedQuantity());
    }

    @Test
    void cannotReserveMoreThanAvailable() {
        Inventory inventory = new Inventory();
        inventory.setAvailableQuantity(3);
        assertThrows(InsufficientStockException.class, () -> inventory.reserve(5));
    }

    @Test
    void cannotAdjustBelowZero() {
        Inventory inventory = new Inventory();
        inventory.setAvailableQuantity(2);
        assertThrows(InsufficientStockException.class, () -> inventory.adjust(-5));
    }
}
