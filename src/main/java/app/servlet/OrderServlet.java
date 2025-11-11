package app.servlet;

import app.model.Order;
import app.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {
    private final ConcurrentHashMap<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Order order = objectMapper.readValue(request.getReader(), Order.class);
            order.setId(idGenerator.getAndIncrement());
            if (order.getDate() == null) {
                order.setDate(LocalDate.now());
            }
            if (order.getCost() == null && order.getProducts() != null) {
                order.setCost(calculateTotalCost(order.getProducts()));
            }
            orders.put(order.getId(), order);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), order);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Order ID is required\"}");
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            Order order = orders.get(id);
            if (order == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Order not found\"}");
                return;
            }

            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), order);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid order ID\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Order ID is required\"}");
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            Order existingOrder = orders.get(id);
            if (existingOrder == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Order not found\"}");
                return;
            }

            Order updatedOrder = objectMapper.readValue(request.getReader(), Order.class);
            updatedOrder.setId(id);
            if (updatedOrder.getDate() == null) {
                updatedOrder.setDate(existingOrder.getDate());
            }
            if (updatedOrder.getCost() == null && updatedOrder.getProducts() != null) {
                updatedOrder.setCost(calculateTotalCost(updatedOrder.getProducts()));
            } else if (updatedOrder.getCost() == null) {
                updatedOrder.setCost(existingOrder.getCost());
            }
            if (updatedOrder.getProducts() == null) {
                updatedOrder.setProducts(existingOrder.getProducts());
            }

            orders.put(id, updatedOrder);

            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(), updatedOrder);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid order ID\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Order ID is required\"}");
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            Order removedOrder = orders.remove(id);
            if (removedOrder == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Order not found\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid order ID\"}");
        }
    }

    private Double calculateTotalCost(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return 0.0;
        }
        return products.stream()
                .mapToDouble(p -> p.getCost() != null ? p.getCost() : 0.0)
                .sum();
    }
}

