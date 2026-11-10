package nexusmarket.application.domain.models;

import nexusmarket.application.domain.exceptions.InvalidStateTransitionException;
import nexusmarket.application.domain.valueobjects.OrderState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderLifecycleTest {

    @Test
    void orderFollowsHappyPath() {
        Order order = new Order();
        order.submit();
        assertEquals(OrderState.PENDING_PAYMENT, order.getState());

        order.pay();
        assertEquals(OrderState.PAID, order.getState());
        assertNotNull(order.getPaidAt());

        order.dispatch();
        assertEquals(OrderState.DISPATCHED, order.getState());
        assertNotNull(order.getDispatchedAt());

        order.deliver();
        assertEquals(OrderState.DELIVERED, order.getState());
        assertNotNull(order.getDeliveredAt());
    }

    @Test
    void orderCannotBePaidTwice() {
        Order order = new Order();
        order.submit();
        order.pay();
        assertThrows(InvalidStateTransitionException.class, order::pay);
    }

    @Test
    void deliveredOrderCannotBeCancelled() {
        Order order = new Order();
        order.submit();
        order.pay();
        order.dispatch();
        order.deliver();
        assertThrows(InvalidStateTransitionException.class, order::cancel);
    }
}
