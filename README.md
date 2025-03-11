# Mini-Spring Framework

## Introduction

Mini-Spring is a lightweight implementation of the core Spring Framework's Inversion of Control (IoC) container. This project provides a simplified but functional version of Spring's dependency injection system, allowing developers to understand the internal workings of Spring while having a minimalist framework that follows similar design patterns and principles.

## Project Overview

The Mini-Spring framework implements the following key features:

- **Dependency Injection**: Automatic wiring of dependencies through annotations
- **Bean Lifecycle Management**: Creation and initialization of beans
- **Component Scanning**: Automatic detection of annotated classes
- **Annotation-based Configuration**: `@Component`, `@Autowired`, `@Value`, etc.
- **Multiple Bean Scopes**: Support for both singleton and prototype scopes

## Core Concepts and Architecture

### Inversion of Control (IoC)

IoC is a design principle where the control flow of a program is inverted: instead of the programmer controlling the flow, the framework takes control, calling into the code when appropriate. In Mini-Spring, this is implemented through the IoC container which:

1. Creates and manages objects (beans)
2. Injects dependencies between objects
3. Manages object lifecycle

### Dependency Injection (DI)

DI is a specific form of IoC where dependencies between objects are injected by an external entity (the container) rather than being created by the object itself. Mini-Spring supports three types of dependency injection:

1. **Constructor Injection**: Dependencies provided through constructors
2. **Field Injection**: Direct injection into class fields
3. **Setter/Method Injection**: Injection through setter methods

### BeanFactory and ApplicationContext

Mini-Spring follows Spring's hierarchical design:

- **BeanFactory**: The core interface providing basic bean management capabilities
- **ApplicationContext**: A more feature-rich container building on BeanFactory with additional functionality

## Key Components

### Annotations

- **`@Component`**: Marks a class as a Spring-managed component
- **`@Autowired`**: Marks a dependency for auto-wiring
- **`@Value`**: Provides values for fields or parameters
- **`@ComponentScan`**: Configures component scanning directives
- **`@Scope`**: Defines the scope of a bean (singleton or prototype)

### Bean Definition and Registration

- **`BeanDefinition`**: Contains metadata about a bean, including class, scope, and initialization parameters
- **`DefaultListableBeanFactory`**: The primary registry for bean definitions
- **`ClassPathBeanDefinitionScanner`**: Scans the classpath for component classes

### Bean Creation and Initialization

- **`AbstractBeanFactory`**: Abstract base class for bean factories
- **`AbstractAutowireCapableBeanFactory`**: Provides autowiring capabilities
- **`BeanNameAware`**: Interface for beans that need to know their bean name

### Context

- **`ApplicationContext`**: Primary interface for accessing beans
- **`AnnotationConfigApplicationContext`**: Context implementation that accepts annotated classes as input

## Working Mechanism

### Bootstrap Process

1. **Context Initialization**: The `AnnotationConfigApplicationContext` is created with configuration classes
2. **Bean Definition Registration**: Configuration classes are processed, and `@ComponentScan` is used to find components
3. **Bean Definition Scanning**: The framework scans packages to identify classes with `@Component` annotations
4. **Bean Definition Registration**: For each component, bean definitions are created and registered
5. **Context Refresh**: All non-lazy singleton beans are instantiated

### Bean Creation Process

1. **Bean Definition Retrieval**: The container looks up the bean definition
2. **Bean Instantiation**: The container creates an instance of the bean
    - For autowired constructors, dependencies are resolved first
    - For default constructors, a simple instantiation is performed
3. **Property Population**: Fields and methods marked with `@Autowired` or `@Value` are processed
4. **Aware Interface Callbacks**: Interfaces like `BeanNameAware` are handled
5. **Bean Caching**: For singletons, the bean is cached for future use

### Dependency Resolution

1. **Dependency Identification**: Identify dependencies through `@Autowired` annotations
2. **Dependency Lookup**: Look up the dependency in the container by type
3. **Dependency Injection**: Set the dependency on the target bean

## Class Hierarchy and Responsibilities

### Bean Factory Layer

- **`BeanFactory`**: Core interface defining methods for accessing beans
- **`AbstractBeanFactory`**: Base implementation providing template methods
- **`AbstractAutowireCapableBeanFactory`**: Adds autowiring support
- **`DefaultListableBeanFactory`**: Concrete implementation with list capabilities

### Context Layer

- **`ApplicationContext`**: High-level container interface extending `BeanFactory`
- **`AnnotationConfigApplicationContext`**: Implementation supporting annotation-based configuration

### Support Classes

- **`ClassUtils`**: Utilities for class operations
- **`BeanDefinitionReader`**: Interface for reading bean definitions
- **`ClassPathBeanDefinitionScanner`**: Scanner for component classes

## How to Use

### Maven Configuration

```xml
<dependencies>
    <dependency>
        <groupId>com.minispring</groupId>
        <artifactId>mini-spring</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

### Creating Components

```java
// Define a service interface
public interface UserService {
    String getCurrentUser();
    void setCurrentUser(String username);
}

// Implement the service with components
@Component
public class UserServiceImpl implements UserService {
    @Value("${default.user:Admin}")
    private String defaultUser;
    
    private String currentUser;

    @Override
    public String getCurrentUser() {
        return currentUser != null ? currentUser : defaultUser;
    }

    @Override
    public void setCurrentUser(String username) {
        this.currentUser = username;
    }
}
```

### Setting Up Configuration

```java
@ComponentScan(basePackages = "com.example.application")
public class AppConfig {
    // Configuration class can be empty if using component scanning
}
```

### Starting the Application

```java
public class Application {
    public static void main(String[] args) {
        // Initialize the application context
        ApplicationContext context = MiniSpringApplication.run(AppConfig.class, args);
        
        // Retrieve and use beans
        UserService userService = context.getBean(UserService.class);
        System.out.println("Current user: " + userService.getCurrentUser());
    }
}
```

## Example Application

The Mini-Spring framework includes a sample application demonstrating its capabilities:

```java
public class TestApplication {
    public static void main(String[] args) {
        // Start the application
        ApplicationContext context = MiniSpringApplication.run(AppConfig.class, args);
        
        // Get the services
        UserService userService = context.getBean(UserService.class);
        OrderService orderService = context.getBean(OrderService.class);
        
        // Test the services
        System.out.println("Current user: " + userService.getCurrentUser());
        
        userService.setCurrentUser("John Doe");
        System.out.println("Current user after update: " + userService.getCurrentUser());
        
        String orderId = orderService.createOrder("PROD-1234", 5);
        System.out.println("Created order: " + orderId);
        
        String orderDetails = orderService.getOrderDetails(orderId);
        System.out.println("Order details: " + orderDetails);
    }
}
```

## Detailed Implementation Analysis

### Bean Definition Registration

Bean definitions are registered in two ways:

1. **Direct Registration**: When configuration classes are provided to the application context
2. **Component Scanning**: Automatic detection of `@Component` annotated classes

Component scanning uses the Reflections library to find annotated classes:

```java
protected int doScan(String basePackage) {
    Reflections reflections = new Reflections(basePackage);
    Set<Class<?>> componentClasses = reflections.getTypesAnnotatedWith(Component.class);
    
    for (Class<?> componentClass : componentClasses) {
        String beanName = determineBeanName(componentClass);
        BeanDefinition beanDefinition = createBeanDefinition(componentClass);
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }
}
```

### Dependency Injection Process

Field injection is implemented by examining fields annotated with `@Autowired`:

```java
protected void populateBean(String beanName, Object bean, BeanDefinition beanDefinition) {
    Class<?> beanClass = beanDefinition.getBeanClass();
    
    // Handle field injection
    Set<Field> fields = ClassUtils.getAllFields(beanClass);
    
    for (Field field : fields) {
        Autowired autowired = field.getAnnotation(Autowired.class);
        
        if (autowired != null) {
            field.setAccessible(true);
            Object dependencyBean = getBean(field.getType());
            field.set(bean, dependencyBean);
        }
    }
    
    // Method injection would be handled similarly
}
```

### Singleton vs Prototype Scopes

Bean scopes determine how instances are created and cached:

- **Singleton**: One instance per container
- **Prototype**: New instance each time requested

The `AbstractBeanFactory` handles this distinction:

```java
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
```

## Design Patterns Used

### Factory Pattern

The `BeanFactory` hierarchy implements the factory pattern, creating beans based on bean definitions.

### Template Method Pattern

`AbstractBeanFactory` uses the template method pattern, defining the skeleton of operations while allowing subclasses to override specific steps.

### Singleton Pattern

Singleton-scoped beans implement a variant of the singleton pattern, where the container ensures only one instance exists.

### Observer Pattern

The event publishing system (though minimally implemented) follows the observer pattern.

## Limitations and Differences from Spring

While Mini-Spring implements core IoC features, it has several limitations compared to the full Spring Framework:

1. **Limited Annotation Support**: Only implements a subset of Spring's annotations
2. **Simplified Bean Lifecycle**: Lacks the full lifecycle callbacks of Spring
3. **No AOP Support**: Does not implement Aspect-Oriented Programming
4. **Limited Configuration Options**: No XML or Java-based configuration beyond annotations
5. **No Property Resolution**: Simplistic implementation of `@Value` without full property resolution
6. **No Profiles or Environments**: Lacks support for different deployment environments

## Extending the Framework

Mini-Spring can be extended in several ways:

1. **Add Support for More Annotations**: Implement additional Spring annotations
2. **Enhance Bean Lifecycle**: Add `@PostConstruct`/`@PreDestroy` support
3. **Implement Property Resolution**: Add proper property source support
4. **Add AOP Capabilities**: Implement basic aspect-oriented programming
5. **Add Event Support**: Enhance the event publishing system

## Conclusion

Mini-Spring provides a simplified yet functional implementation of Spring's IoC container. By studying its code, developers can gain a better understanding of how Spring works internally while having a lightweight framework for simple applications.

The framework demonstrates core principles of IoC and DI using clean, modular design and standard design patterns. While not a replacement for the full Spring Framework, it serves as both a learning tool and a foundation for understanding more complex frameworks.