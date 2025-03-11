package com.minispring.test;

import com.minispring.MiniSpringApplication;
import com.minispring.context.ApplicationContext;
import com.minispring.test.config.AppConfig;
import com.minispring.test.service.OrderService;
import com.minispring.test.service.UserService;

/**
 * Test application to demonstrate the Mini Spring framework.
 */
public class TestApplication {

    public static void main(String[] args) {
        // Start the application
        ApplicationContext context = MiniSpringApplication.run(AppConfig.class, args);

        // Get the services
        UserService userService = context.getBean(UserService.class);
        OrderService orderService = context.getBean(OrderService.class);

        // Test the services
        System.out.println("Current user: " + userService.getCurrentUser());

        userService.setCurrentUser("John Doe");
        System.out.println("Current user after update: " + userService.getCurrentUser());

        String orderId = orderService.createOrder("PROD-1234", 5);
        System.out.println("Created order: " + orderId);

        String orderDetails = orderService.getOrderDetails(orderId);
        System.out.println("Order details: " + orderDetails);
    }
}