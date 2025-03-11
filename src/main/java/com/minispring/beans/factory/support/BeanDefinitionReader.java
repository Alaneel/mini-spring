package com.minispring.beans.factory.support;

import com.minispring.exception.BeansException;

/**
 * Interface for bean definition readers.
 */
public interface BeanDefinitionReader {

    /**
     * Register bean definitions from a class.
     *
     * @param beanClass the class to register
     * @throws BeansException if bean definition registration fails
     */
    void registerBeanDefinition(Class<?> beanClass) throws BeansException;

    /**
     * Register bean definitions from package by component scanning.
     *
     * @param basePackage the package to scan
     * @throws BeansException if bean definition registration fails
     */
    void registerBeanDefinitions(String basePackage) throws BeansException;
}