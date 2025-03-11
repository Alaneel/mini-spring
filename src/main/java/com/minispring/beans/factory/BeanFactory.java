package com.minispring.beans.factory;

import com.minispring.exception.BeansException;

/**
 * Interface defining methods for accessing beans from a container.
 */
public interface BeanFactory {

    /**
     * Return an instance, which may be shared or independent, of the specified bean.
     *
     * @param name the name of the bean to retrieve
     * @return an instance of the bean
     * @throws BeansException if the bean could not be obtained
     */
    Object getBean(String name) throws BeansException;

    /**
     * Return an instance, which may be shared or independent, of the specified bean.
     *
     * @param name the name of the bean to retrieve
     * @param requiredType type the bean must match
     * @return an instance of the bean
     * @throws BeansException if the bean could not be obtained
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    /**
     * Return an instance, which may be shared or independent, of the specified bean.
     *
     * @param requiredType type the bean must match
     * @return an instance of the bean
     * @throws BeansException if the bean could not be obtained
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;

    /**
     * Does this bean factory contain a bean with the given name?
     *
     * @param name the name of the bean to query
     * @return whether a bean with the given name exists
     */
    boolean containsBean(String name);
}