package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    private Product testProduct1;
    private Product testProduct2;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();

        testProduct1 = new Product("Laptop", 1200.0);
        testProduct2 = new Product("Mouse", 25.0);
    }

    @Test
    void fullCRUDFlow_ShouldWorkCorrectly() throws Exception {
        // Create order
        Order newOrder = new Order();
        newOrder.setProducts(Arrays.asList(testProduct1, testProduct2));

        String createResponse = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.totalCost", is(1225.0)))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Order createdOrder = objectMapper.readValue(createResponse, Order.class);
        Long orderId = createdOrder.getId();

        // Get order by ID
        mockMvc.perform(get("/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderId.intValue())))
                .andExpect(jsonPath("$.totalCost", is(1225.0)));

        // Get all orders
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // Update order
        Order updateOrder = new Order();
        updateOrder.setTotalCost(1500.0);
        updateOrder.setProducts(Arrays.asList(testProduct1));

        mockMvc.perform(put("/orders/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCost", is(1500.0)));

        // Delete order
        mockMvc.perform(delete("/orders/" + orderId))
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/orders/" + orderId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createMultipleOrders_ShouldReturnAllInGetRequest() throws Exception {
        Order order1 = new Order();
        order1.setProducts(Arrays.asList(testProduct1));

        Order order2 = new Order();
        order2.setProducts(Arrays.asList(testProduct2));

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}

