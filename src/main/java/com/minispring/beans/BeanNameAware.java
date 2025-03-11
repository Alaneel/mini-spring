package com.minispring.beans;

/**
 * Interface to be implemented by beans that want to be aware of their bean name in a container.
 */
public interface BeanNameAware {

    /**
     * Set the name of the bean in the container.
     * This method is invoked after population of normal bean properties but before an
     * initialization callback such as InitializingBean's afterPropertiesSet.
     *
     * @param name the name of the bean in the container
     */
    void setBeanName(String name);
}