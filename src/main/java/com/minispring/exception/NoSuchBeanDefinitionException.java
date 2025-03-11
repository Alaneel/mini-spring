package com.minispring.exception;

/**
 * Exception thrown when a BeanFactory is asked for a bean instance name for which it
 * cannot find a definition.
 */
public class NoSuchBeanDefinitionException extends BeansException {

    private final String beanName;

    public NoSuchBeanDefinitionException(String beanName) {
        super("No bean named '" + beanName + "' available");
        this.beanName = beanName;
    }

    public NoSuchBeanDefinitionException(String beanName, String message) {
        super("No bean named '" + beanName + "' available: " + message);
        this.beanName = beanName;
    }

    public String getBeanName() {
        return this.beanName;
    }
}