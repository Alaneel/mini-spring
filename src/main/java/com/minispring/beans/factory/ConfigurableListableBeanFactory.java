package com.minispring.beans.factory;

import java.util.Map;

import com.minispring.exception.BeansException;

/**
 * Extension of the {@link BeanFactory} interface to be implemented by bean factories
 * that can enumerate all their bean instances, rather than attempting bean lookup
 * by name one by one.
 */
public interface ConfigurableListableBeanFactory extends BeanFactory {

    /**
     * Return the bean definition for the given bean name.
     *
     * @param beanName name of the bean to find a definition for
     * @return the BeanDefinition for the given name
     * @throws BeansException if no bean definition is found
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * Return the names of all beans defined in this container.
     *
     * @return the names of all beans defined in this container
     */
    String[] getBeanDefinitionNames();

    /**
     * Return a map of all beans of the given type.
     *
     * @param type the class or interface to match
     * @return a Map with the matching beans, containing the bean names as
     * keys and the corresponding bean instances as values
     * @throws BeansException if a bean could not be created
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    /**
     * Pre-instantiate all singleton beans.
     *
     * @throws BeansException if a bean could not be instantiated
     */
    void preInstantiateSingletons() throws BeansException;
}