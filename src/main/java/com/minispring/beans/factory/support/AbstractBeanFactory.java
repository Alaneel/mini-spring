package com.minispring.beans.factory.support;

import java.util.concurrent.ConcurrentHashMap;

import com.minispring.beans.factory.BeanDefinition;
import com.minispring.beans.factory.BeanFactory;
import com.minispring.exception.BeansException;
import com.minispring.exception.NoSuchBeanDefinitionException;

/**
 * Abstract base class for {@link BeanFactory} implementations,
 * implementing the ConfigurableBeanFactory interface.
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    private final ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return doGetBean(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        // Attempt to find a bean by type
        String[] beanDefinitionNames = getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition bd = getBeanDefinition(beanName);
            if (requiredType.isAssignableFrom(bd.getBeanClass())) {
                return (T) getBean(beanName);
            }
        }
        throw new NoSuchBeanDefinitionException("No qualifying bean of type " + requiredType.getName() + " found");
    }

    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }

    @SuppressWarnings("unchecked")
    protected <T> T doGetBean(String name, Class<T> requiredType) {
        Object bean = getSingleton(name);
        if (bean != null) {
            return (T) bean;
        }

        BeanDefinition beanDefinition = getBeanDefinition(name);
        bean = createBean(name, beanDefinition);

        if (beanDefinition.isSingleton()) {
            singletonObjects.put(name, bean);
        }

        return (T) bean;
    }

    protected Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

    protected boolean containsSingleton(String beanName) {
        return singletonObjects.containsKey(beanName);
    }

    /**
     * Return whether this factory contains a bean definition with the given name.
     *
     * @param beanName the name of the bean to query
     * @return whether a bean with the given name exists
     */
    protected abstract boolean containsBeanDefinition(String beanName);

    /**
     * Return the bean definition for the given bean name.
     *
     * @param beanName name of the bean to find a definition for
     * @return the BeanDefinition for the given name
     * @throws BeansException if no bean definition is found
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * Create a bean instance for the given bean definition.
     *
     * @param beanName the name of the bean
     * @param beanDefinition the bean definition
     * @return the bean instance
     * @throws BeansException if the instantiation fails
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

    /**
     * Return the names of all beans defined in this factory.
     *
     * @return the names of all beans defined in this factory
     */
    protected abstract String[] getBeanDefinitionNames();
}