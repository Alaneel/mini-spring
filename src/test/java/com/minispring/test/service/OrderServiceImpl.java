package com.minispring.test.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.minispring.annotation.Autowired;
import com.minispring.annotation.Component;
import com.minispring.annotation.Value;
import com.minispring.beans.BeanNameAware;

/**
 * Implementation of the OrderService interface.
 */
@Component("orderService")
public class OrderServiceImpl implements OrderService, BeanNameAware {

    private final Map<String, String> orderDetails = new HashMap<>();

    @Autowired
    private UserService userService;

    @Value("${order.prefix:ORD}")
    private String orderPrefix;

    private String beanName;

    @Override
    public String createOrder(String productId, int quantity) {
        // Get the current user
        String user = userService.getCurrentUser();

        // Generate order ID
        String orderId = orderPrefix + "-" + UUID.randomUUID().toString().substring(0, 8);

        // Create order details
        String details = String.format("Order for product %s, quantity %d by user %s",
                productId, quantity, user);

        orderDetails.put(orderId, details);

        return orderId;
    }

    @Override
    public String getOrderDetails(String orderId) {
        return orderDetails.getOrDefault(orderId, "Order not found: " + orderId);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
        System.out.println("Bean name set to: " + name);
    }

    public String getBeanName() {
        return beanName;
    }
}