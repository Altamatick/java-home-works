package app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderRepository orderRepository;

    private Order testOrder;
    private Product testProduct1;
    private Product testProduct2;

    @BeforeEach
    void setUp() {
        testProduct1 = new Product(1L, "Laptop", 1200.0);
        testProduct2 = new Product(2L, "Mouse", 25.0);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setTotalCost(1225.0);
        testOrder.setProducts(Arrays.asList(testProduct1, testProduct2));
        testOrder.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() throws Exception {
        List<Order> orders = Arrays.asList(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].totalCost", is(1225.0)));

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getAllOrders_ShouldReturnEmptyList_WhenNoOrders() throws Exception {
        when(orderRepository.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrderById_ShouldReturnOrder_WhenOrderExists() throws Exception {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.totalCost", is(1225.0)))
                .andExpect(jsonPath("$.products", hasSize(2)));

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void getOrderById_ShouldReturnNotFound_WhenOrderDoesNotExist() throws Exception {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/999"))
                .andExpect(status().isNotFound());

        verify(orderRepository, times(1)).findById(999L);
    }

    @Test
    void createOrder_ShouldCreateAndReturnOrder() throws Exception {
        Order newOrder = new Order();
        newOrder.setProducts(Arrays.asList(testProduct1));

        Order savedOrder = new Order();
        savedOrder.setId(2L);
        savedOrder.setProducts(Arrays.asList(testProduct1));
        savedOrder.setTotalCost(1200.0);
        savedOrder.setCreatedAt(LocalDateTime.now());

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.totalCost", is(1200.0)));

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_ShouldCalculateTotalCost_WhenNotProvided() throws Exception {
        Order newOrder = new Order();
        newOrder.setProducts(Arrays.asList(testProduct1, testProduct2));
        // totalCost не вказано

        Order savedOrder = new Order();
        savedOrder.setId(2L);
        savedOrder.setProducts(Arrays.asList(testProduct1, testProduct2));
        savedOrder.setTotalCost(1225.0);
        savedOrder.setCreatedAt(LocalDateTime.now());

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalCost", is(1225.0)));

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void updateOrder_ShouldUpdateAndReturnOrder_WhenOrderExists() throws Exception {
        Order updatedDetails = new Order();
        updatedDetails.setTotalCost(1500.0);
        updatedDetails.setProducts(Arrays.asList(testProduct1, testProduct2));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        mockMvc.perform(put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void updateOrder_ShouldReturnNotFound_WhenOrderDoesNotExist() throws Exception {
        Order updatedDetails = new Order();
        updatedDetails.setTotalCost(1500.0);

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/orders/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isNotFound());

        verify(orderRepository, times(1)).findById(999L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void deleteOrder_ShouldDeleteOrder_WhenOrderExists() throws Exception {
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteOrder_ShouldReturnNotFound_WhenOrderDoesNotExist() throws Exception {
        when(orderRepository.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/orders/999"))
                .andExpect(status().isNotFound());

        verify(orderRepository, times(1)).existsById(999L);
        verify(orderRepository, never()).deleteById(any());
    }
}

