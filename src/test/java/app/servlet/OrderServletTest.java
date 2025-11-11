package app.servlet;

import app.model.Order;
import app.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, OrderServletTest.TestLoggerExtension.class})
class OrderServletTest {

    static class TestLoggerExtension implements TestWatcher {
        @Override
        public void testSuccessful(ExtensionContext context) {
            System.out.println("✓ PASSED: " + context.getDisplayName());
        }

        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            System.out.println("✗ FAILED: " + context.getDisplayName() + " - " + cause.getMessage());
        }

        @Override
        public void testAborted(ExtensionContext context, Throwable cause) {
            System.out.println("⊘ ABORTED: " + context.getDisplayName());
        }
    }

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private OrderServlet servlet;
    private StringWriter stringWriter;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws ServletException {
        servlet = new OrderServlet();
        servlet.init();
        stringWriter = new StringWriter();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    private void resetStringWriter() {
        stringWriter = new StringWriter();
    }

    @Test
    void testDoPost_CreateOrder() throws Exception {
        System.out.println("\n[TEST] testDoPost_CreateOrder - Створення замовлення з усіма полями");
        // Arrange
        resetStringWriter();
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", 50.25));
        products.add(new Product(2L, "Product 2", 100.25));

        Order order = new Order();
        order.setDate(LocalDate.of(2025, 11, 11));
        order.setCost(150.50);
        order.setProducts(products);

        String json = objectMapper.writeValueAsString(order);
        BufferedReader reader = new BufferedReader(new StringReader(json));

        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(response).setContentType("application/json");
        String result = stringWriter.toString();
        assertFalse(result.isEmpty());
        assertTrue(result.contains("\"id\""));
    }

    @Test
    void testDoPost_CreateOrderWithAutoDateAndCost() throws Exception {
        System.out.println("\n[TEST] testDoPost_CreateOrderWithAutoDateAndCost - Створення замовлення з автоматичною датою та вартістю");
        // Arrange
        resetStringWriter();
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", 50.0));
        products.add(new Product(2L, "Product 2", 100.0));

        Order order = new Order();
        order.setProducts(products);

        String json = objectMapper.writeValueAsString(order);
        BufferedReader reader = new BufferedReader(new StringReader(json));

        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(response).setContentType("application/json");
        String result = stringWriter.toString();
        assertFalse(result.isEmpty());
        assertTrue(result.contains("\"id\""));
    }

    @Test
    void testDoGet_GetOrderById() throws Exception {
        System.out.println("\n[TEST] testDoGet_GetOrderById - Отримання замовлення по ID");
        // Arrange
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", 50.25));

        Order order = new Order(1L, LocalDate.of(2025, 11, 11), 50.25, products);
        String json = objectMapper.writeValueAsString(order);
        BufferedReader reader = new BufferedReader(new StringReader(json));

        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.doPost(request, response);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response, atLeastOnce()).setContentType("application/json");
        String result = stringWriter.toString();
        assertTrue(result.contains("\"id\":1"));
        assertTrue(result.contains("Product 1"));
    }

    @Test
    void testDoGet_OrderNotFound() throws Exception {
        System.out.println("\n[TEST] testDoGet_OrderNotFound - Отримання неіснуючого замовлення");
        // Arrange
        when(request.getPathInfo()).thenReturn("/999");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String result = stringWriter.toString();
        assertTrue(result.contains("Order not found"));
    }

    @Test
    void testDoGet_InvalidPathInfo() throws Exception {
        System.out.println("\n[TEST] testDoGet_InvalidPathInfo - Отримання замовлення з невалідним шляхом");
        // Arrange
        when(request.getPathInfo()).thenReturn("/");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String result = stringWriter.toString();
        assertTrue(result.contains("Order ID is required"));
    }

    @Test
    void testDoGet_InvalidOrderId() throws Exception {
        System.out.println("\n[TEST] testDoGet_InvalidOrderId - Отримання замовлення з невалідним ID");
        // Arrange
        when(request.getPathInfo()).thenReturn("/invalid");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String result = stringWriter.toString();
        assertTrue(result.contains("Invalid order ID"));
    }

    @Test
    void testDoPut_UpdateOrder() throws Exception {
        System.out.println("\n[TEST] testDoPut_UpdateOrder - Оновлення замовлення");
        // Arrange - Create order first
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", 50.25));

        Order order = new Order();
        order.setDate(LocalDate.of(2025, 11, 11));
        order.setCost(50.25);
        order.setProducts(products);

        String createJson = objectMapper.writeValueAsString(order);
        BufferedReader createReader = new BufferedReader(new StringReader(createJson));

        when(request.getReader()).thenReturn(createReader);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        servlet.doPost(request, response);

        // Update order
        List<Product> updatedProducts = new ArrayList<>();
        updatedProducts.add(new Product(1L, "Updated Product", 75.0));

        Order updatedOrder = new Order();
        updatedOrder.setDate(LocalDate.of(2025, 11, 12));
        updatedOrder.setCost(75.0);
        updatedOrder.setProducts(updatedProducts);

        String updateJson = objectMapper.writeValueAsString(updatedOrder);
        BufferedReader updateReader = new BufferedReader(new StringReader(updateJson));

        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(updateReader);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // Act
        servlet.doPut(request, response);

        // Assert
        verify(response, atLeastOnce()).setContentType("application/json");
        String result = stringWriter.toString();
        assertTrue(result.contains("\"id\":1"));
        assertTrue(result.contains("Updated Product"));
    }

    @Test
    void testDoPut_OrderNotFound() throws Exception {
        System.out.println("\n[TEST] testDoPut_OrderNotFound - Оновлення неіснуючого замовлення");
        // Arrange
        resetStringWriter();
        when(request.getPathInfo()).thenReturn("/999");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // Act
        servlet.doPut(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String result = stringWriter.toString();
        assertTrue(result.contains("Order not found"));
    }

    @Test
    void testDoDelete_DeleteOrder() throws Exception {
        System.out.println("\n[TEST] testDoDelete_DeleteOrder - Видалення замовлення");
        // Arrange - Create order first
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", 50.25));

        Order order = new Order();
        order.setDate(LocalDate.of(2025, 11, 11));
        order.setCost(50.25);
        order.setProducts(products);

        String json = objectMapper.writeValueAsString(order);
        BufferedReader reader = new BufferedReader(new StringReader(json));

        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        servlet.doPost(request, response);

        when(request.getPathInfo()).thenReturn("/1");

        // Act
        servlet.doDelete(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);

        // Verify order is deleted
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        servlet.doGet(request, response);
        verify(response, atLeastOnce()).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void testDoDelete_OrderNotFound() throws Exception {
        System.out.println("\n[TEST] testDoDelete_OrderNotFound - Видалення неіснуючого замовлення");
        // Arrange
        when(request.getPathInfo()).thenReturn("/999");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // Act
        servlet.doDelete(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String result = stringWriter.toString();
        assertTrue(result.contains("Order not found"));
    }

    @Test
    void testDoDelete_InvalidPathInfo() throws Exception {
        System.out.println("\n[TEST] testDoDelete_InvalidPathInfo - Видалення замовлення з невалідним шляхом");
        // Arrange
        when(request.getPathInfo()).thenReturn("/");
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // Act
        servlet.doDelete(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String result = stringWriter.toString();
        assertTrue(result.contains("Order ID is required"));
    }

    @Test
    void testDoPost_InvalidJson() throws Exception {
        System.out.println("\n[TEST] testDoPost_InvalidJson - Обробка невалідного JSON");
        // Arrange
        String invalidJson = "{ invalid json }";
        BufferedReader reader = new BufferedReader(new StringReader(invalidJson));

        when(request.getReader()).thenReturn(reader);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // Act
        servlet.doPost(request, response);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String result = stringWriter.toString();
        assertTrue(result.contains("error"));
    }
}

