package com.minispring.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import com.minispring.annotation.Autowired;
import com.minispring.annotation.Value;
import com.minispring.beans.BeanNameAware;
import com.minispring.beans.factory.BeanDefinition;
import com.minispring.core.ClassUtils;
import com.minispring.exception.BeansException;

/**
 * Abstract bean factory superclass that implements default bean creation,
 * with the full capabilities specified by the BeanDefinition class.
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        // Create the bean instance
        Object bean = doCreateBean(beanName, beanDefinition);

        // Initialize the bean
        try {
            populateBean(beanName, bean, beanDefinition);
            initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Error initializing bean: " + beanName, e);
        }

        return bean;
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Object bean = null;

        try {
            // Try to use a constructor with @Autowired annotation first
            Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
            Constructor<?> autowiredConstructor = null;

            for (Constructor<?> constructor : constructors) {
                if (constructor.isAnnotationPresent(Autowired.class)) {
                    autowiredConstructor = constructor;
                    break;
                }
            }

            if (autowiredConstructor != null) {
                // Handle autowired constructor
                Class<?>[] paramTypes = autowiredConstructor.getParameterTypes();
                Object[] args = new Object[paramTypes.length];

                for (int i = 0; i < paramTypes.length; i++) {
                    args[i] = getBean(paramTypes[i]);
                }

                autowiredConstructor.setAccessible(true);
                bean = autowiredConstructor.newInstance(args);
            } else {
                // Use default constructor
                bean = beanClass.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new BeansException("Error creating bean with name '" + beanName + "': " + e.getMessage(), e);
        }

        return bean;
    }

    protected void populateBean(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException {
        Class<?> beanClass = beanDefinition.getBeanClass();

        // Handle field injection
        Set<Field> fields = ClassUtils.getAllFields(beanClass);

        for (Field field : fields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            Value value = field.getAnnotation(Value.class);

            if (autowired != null) {
                field.setAccessible(true);
                Object dependencyBean = null;

                try {
                    // Get the dependency bean by type
                    dependencyBean = getBean(field.getType());
                    field.set(bean, dependencyBean);
                } catch (Exception e) {
                    if (autowired.required()) {
                        throw new BeansException("Failed to inject field: " + field.getName() + " in bean: " + beanName, e);
                    }
                }
            } else if (value != null) {
                field.setAccessible(true);

                try {
                    // Set the value directly (simple implementation)
                    String propertyValue = value.value();

                    // Remove the "${" and "}" if present
                    if (propertyValue.startsWith("${") && propertyValue.endsWith("}")) {
                        propertyValue = propertyValue.substring(2, propertyValue.length() - 1);
                        // Ideally, you would lookup this property in a properties file
                        // For this simple implementation, we'll just use the value as is
                    }

                    setFieldValue(field, bean, propertyValue);
                } catch (Exception e) {
                    throw new BeansException("Failed to set value for field: " + field.getName() + " in bean: " + beanName, e);
                }
            }
        }

        // Handle method injection (setter injection)
        Set<Method> methods = ClassUtils.getAllMethods(beanClass);

        for (Method method : methods) {
            Autowired autowired = method.getAnnotation(Autowired.class);

            if (autowired != null) {
                method.setAccessible(true);
                Class<?>[] paramTypes = method.getParameterTypes();
                Object[] args = new Object[paramTypes.length];

                try {
                    // Get all dependencies by type
                    for (int i = 0; i < paramTypes.length; i++) {
                        args[i] = getBean(paramTypes[i]);
                    }

                    method.invoke(bean, args);
                } catch (Exception e) {
                    if (autowired.required()) {
                        throw new BeansException("Failed to inject method: " + method.getName() + " in bean: " + beanName, e);
                    }
                }
            }
        }
    }

    protected void initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
        // Call BeanNameAware
        if (bean instanceof BeanNameAware) {
            ((BeanNameAware) bean).setBeanName(beanName);
        }

        // You could implement other initialization methods here
        // such as InitializingBean.afterPropertiesSet() or custom @PostConstruct methods
    }

    private void setFieldValue(Field field, Object bean, String value) throws IllegalAccessException {
        Class<?> fieldType = field.getType();

        if (fieldType == String.class) {
            field.set(bean, value);
        } else if (fieldType == int.class || fieldType == Integer.class) {
            field.set(bean, Integer.parseInt(value));
        } else if (fieldType == long.class || fieldType == Long.class) {
            field.set(bean, Long.parseLong(value));
        } else if (fieldType == double.class || fieldType == Double.class) {
            field.set(bean, Double.parseDouble(value));
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            field.set(bean, Boolean.parseBoolean(value));
        } else {
            field.set(bean, value);
        }
    }
}