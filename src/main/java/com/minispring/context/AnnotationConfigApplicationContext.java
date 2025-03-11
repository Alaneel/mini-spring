package com.minispring.context;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minispring.annotation.ComponentScan;
import com.minispring.beans.factory.BeanDefinition;
import com.minispring.beans.factory.ConfigurableListableBeanFactory;
import com.minispring.beans.factory.DefaultListableBeanFactory;
import com.minispring.context.support.ClassPathBeanDefinitionScanner;
import com.minispring.core.ClassUtils;
import com.minispring.exception.BeansException;

/**
 * Standalone application context, accepting component classes as input.
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationConfigApplicationContext.class);

    private final DefaultListableBeanFactory beanFactory;
    private final ClassPathBeanDefinitionScanner scanner;
    private final String applicationName;
    private final long startupDate;
    private boolean active = true;

    /**
     * Create a new AnnotationConfigApplicationContext with the given configuration classes.
     *
     * @param componentClasses one or more component or configuration classes
     */
    public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
        this.applicationName = "MiniSpringContext-" + System.currentTimeMillis();
        this.startupDate = System.currentTimeMillis();

        this.beanFactory = new DefaultListableBeanFactory();
        this.scanner = new ClassPathBeanDefinitionScanner(beanFactory);

        // Register the component classes
        if (componentClasses != null && componentClasses.length > 0) {
            register(componentClasses);
        }

        // Refresh the context
        refresh();
    }

    /**
     * Register one or more component classes to be processed.
     *
     * @param componentClasses the component classes to register
     */
    public void register(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            registerBean(componentClass);

            // Process @ComponentScan if present
            ComponentScan componentScan = componentClass.getAnnotation(ComponentScan.class);
            if (componentScan != null) {
                String[] basePackages = componentScan.basePackages();

                if (basePackages.length == 0) {
                    // If no basePackages specified, use the package of the configuration class
                    basePackages = new String[] { componentClass.getPackage().getName() };
                }

                scan(basePackages);
            }
        }
    }

    /**
     * Register a bean from the given component class.
     *
     * @param componentClass the component class
     */
    private void registerBean(Class<?> componentClass) {
        String beanName = ClassUtils.lowerFirstLetter(componentClass.getSimpleName());

        BeanDefinition beanDefinition = new BeanDefinition(componentClass);
        beanFactory.registerBeanDefinition(beanName, beanDefinition);

        logger.info("Registered bean definition for class: {}", componentClass.getName());
    }

    /**
     * Scan the specified packages for components.
     *
     * @param basePackages the packages to scan
     */
    public void scan(String... basePackages) {
        int beanCount = scanner.scan(basePackages);
        logger.info("Found {} components in packages: {}", beanCount, Arrays.toString(basePackages));
    }

    /**
     * Refresh the context, creating all non-lazy singleton beans.
     */
    public void refresh() {
        try {
            // Pre-instantiate all singleton beans
            beanFactory.preInstantiateSingletons();
            logger.info("Context refreshed: {}", applicationName);
        } catch (BeansException e) {
            logger.error("Error refreshing context", e);
            throw e;
        }
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return beanFactory.getBean(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return beanFactory.getBean(requiredType);
    }

    @Override
    public boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public long getStartupDate() {
        return startupDate;
    }

    @Override
    public void publishEvent(Object event) {
        // Simple implementation - just log the event
        logger.info("Event published: {}", event);
    }

    @Override
    public void close() throws BeansException {
        active = false;
        logger.info("Closing application context: {}", applicationName);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * Get the underlying bean factory.
     *
     * @return the bean factory
     */
    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}