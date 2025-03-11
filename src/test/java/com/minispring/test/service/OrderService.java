package com.minispring.test.service;

/**
 * Service interface for order-related operations.
 */
public interface OrderService {

    /**
     * Create a new order.
     *
     * @param productId the product ID
     * @param quantity the quantity
     * @return the order ID
     */
    String createOrder(String productId, int quantity);

    /**
     * Get order details by ID.
     *
     * @param orderId the order ID
     * @return order details as a String
     */
    String getOrderDetails(String orderId);
}