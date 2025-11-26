package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").exists());
    }

    @Test
    void testGetOrderById() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.products").isArray());
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateOrder() throws Exception {
        Order newOrder = new Order();
        newOrder.setProducts(Arrays.asList(
            new Product(10L, "Клавіатура", 2000.0),
            new Product(11L, "Миша", 800.0)
        ));

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalCost").value(2800.0))
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products.length()").value(2));
    }

    @Test
    void testCreateOrderWithEmptyProducts() throws Exception {
        Order newOrder = new Order();
        newOrder.setProducts(Arrays.asList());

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalCost").value(0.0))
                .andExpect(jsonPath("$.products").isEmpty());
    }
}
