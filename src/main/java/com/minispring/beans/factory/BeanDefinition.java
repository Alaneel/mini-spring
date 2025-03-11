package com.minispring.beans.factory;

/**
 * A BeanDefinition describes a bean instance, which has property values and
 * constructor argument values.
 */
public class BeanDefinition {

    private Class<?> beanClass;
    private String scope = "singleton";
    private boolean lazyInit = false;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isSingleton() {
        return "singleton".equals(scope);
    }

    public boolean isPrototype() {
        return "prototype".equals(scope);
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }
}