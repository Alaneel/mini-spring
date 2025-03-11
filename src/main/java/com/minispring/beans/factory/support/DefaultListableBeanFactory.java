package com.minispring.beans.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.minispring.beans.factory.support.AbstractAutowireCapableBeanFactory;
import com.minispring.exception.BeansException;
import com.minispring.exception.NoSuchBeanDefinitionException;

/**
 * Default implementation of the ConfigurableListableBeanFactory interface.
 * This is a full-fledged bean factory that supports both singleton and prototype beans.
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private final List<String> beanDefinitionNames = new ArrayList<>();

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition bd = beanDefinitionMap.get(beanName);
        if (bd == null) {
            throw new NoSuchBeanDefinitionException(beanName);
        }
        return bd;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionNames.toArray(new String[0]);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> result = new HashMap<>();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition bd = beanDefinitionMap.get(beanName);
            if (type.isAssignableFrom(bd.getBeanClass())) {
                result.put(beanName, getBean(beanName, type));
            }
        }
        return result;
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        for (String beanName : beanDefinitionNames) {
            BeanDefinition bd = beanDefinitionMap.get(beanName);
            if (bd.isSingleton() && !bd.isLazyInit()) {
                getBean(beanName);
            }
        }
    }

    @Override
    protected boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    /**
     * Register a new bean definition with this registry.
     *
     * @param beanName the name of the bean
     * @param beanDefinition definition of the bean
     * @throws BeansException if registration fails
     */
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeansException {
        if (beanDefinitionMap.containsKey(beanName)) {
            // For this simple implementation, we're just replacing the existing definition
            beanDefinitionMap.put(beanName, beanDefinition);
        } else {
            beanDefinitionMap.put(beanName, beanDefinition);
            beanDefinitionNames.add(beanName);
        }
    }
}