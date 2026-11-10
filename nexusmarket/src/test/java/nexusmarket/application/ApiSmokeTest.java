package nexusmarket.application;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void buyerCompletesCheckoutPaymentDeliveryAndRefund() throws Exception {
        String buyerToken = login("buyer", "Buyer123!", "BUYER");

        MvcResult catalog = mockMvc.perform(get("/api/v1/buyer/catalog")
                        .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andReturn();
        String productId = JsonPath.read(body(catalog), "$[0].id");

        mockMvc.perform(post("/api/v1/buyer/cart/items")
                        .header("Authorization", "Bearer " + buyerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":\"" + productId + "\",\"quantity\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].quantity").value(2));

        String checkoutBody = """
                {"destinationAddress":{"street":"Calle 1 # 2-3","city":"Bogota",
                "state":"Cundinamarca","postalCode":"110111","country":"Colombia"}}
                """;
        MvcResult checkout = mockMvc.perform(post("/api/v1/buyer/orders")
                        .header("Authorization", "Bearer " + buyerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(checkoutBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("PENDING_PAYMENT"))
                .andExpect(jsonPath("$.totalAmount").value(199800.0))
                .andReturn();
        String orderId = JsonPath.read(body(checkout), "$.id");

        mockMvc.perform(post("/api/v1/buyer/orders/" + orderId + "/pay")
                        .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("PAID"))
                .andExpect(jsonPath("$.invoiceId").isNotEmpty())
                .andExpect(jsonPath("$.shipmentId").isNotEmpty());

        MvcResult orderDetail = mockMvc.perform(get("/api/v1/buyer/orders/" + orderId)
                        .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").isNotEmpty())
                .andReturn();
        String orderItemId = JsonPath.read(body(orderDetail), "$.items[0].id");

        String logisticsToken = login("logistics", "Logistics123!", "LOGISTIC_OPERATOR");
        mockMvc.perform(post("/api/v1/logistic-operator/orders/" + orderId + "/dispatch")
                        .header("Authorization", "Bearer " + logisticsToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("DISPATCHED"));
        mockMvc.perform(post("/api/v1/logistic-operator/orders/" + orderId + "/deliver")
                        .header("Authorization", "Bearer " + logisticsToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("DELIVERED"));

        String returnBody = "{\"orderId\":\"" + orderId + "\",\"reason\":\"Producto defectuoso\","
                + "\"items\":[{\"orderItemId\":\"" + orderItemId + "\",\"quantity\":1}]}";
        MvcResult returnResult = mockMvc.perform(post("/api/v1/buyer/returns")
                        .header("Authorization", "Bearer " + buyerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(returnBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("REQUESTED"))
                .andReturn();
        String returnId = JsonPath.read(body(returnResult), "$.id");

        String adminToken = login("admin", "Admin123!", "ADMINISTRATOR");
        mockMvc.perform(post("/api/v1/admin/returns/" + returnId + "/approve")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("APPROVED"));
        mockMvc.perform(post("/api/v1/admin/returns/" + returnId + "/receive")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("RECEIVED"));

        MvcResult refunds = mockMvc.perform(get("/api/v1/admin/refunds")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(99900.0))
                .andReturn();
        String refundId = JsonPath.read(body(refunds), "$[0].id");
        mockMvc.perform(post("/api/v1/admin/refunds/" + refundId + "/process")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("PROCESSED"));

        mockMvc.perform(get("/api/v1/buyer/orders")
                        .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(orderId))
                .andExpect(jsonPath("$[0].state").value("DELIVERED"));

        mockMvc.perform(get("/api/v1/admin/reports/orders")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/admin/reports/sales")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void administratorCanListUsers() throws Exception {
        String adminToken = login("admin", "Admin123!", "ADMINISTRATOR");

        mockMvc.perform(get("/api/v1/admin/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").isNotEmpty());
    }

    @Test
    void anonymousRequestIsRejected() throws Exception {
        mockMvc.perform(get("/api/v1/buyer/profile"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED"));
    }

    @Test
    void buyerCannotAccessAdministratorEndpoints() throws Exception {
        String buyerToken = login("buyer", "Buyer123!", "BUYER");

        mockMvc.perform(get("/api/v1/admin/users")
                        .header("Authorization", "Bearer " + buyerToken))
                .andExpect(status().isForbidden());
    }

    private String login(String username, String password, String role) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.role").value(role))
                .andReturn();
        return JsonPath.read(body(result), "$.token");
    }

    private String body(MvcResult result) throws Exception {
        return result.getResponse().getContentAsString(StandardCharsets.UTF_8);
    }
}
