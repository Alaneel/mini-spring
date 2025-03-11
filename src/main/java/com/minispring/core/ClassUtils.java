package com.minispring.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for handling class-related operations.
 */
public class ClassUtils {

    /**
     * Gets all declared fields including those from superclasses.
     *
     * @param clazz the class to analyze
     * @return set of all fields
     */
    public static Set<Field> getAllFields(Class<?> clazz) {
        Set<Field> fields = new HashSet<>();
        Class<?> currentClass = clazz;

        while (currentClass != null && currentClass != Object.class) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }

    /**
     * Gets all declared methods including those from superclasses.
     *
     * @param clazz the class to analyze
     * @return set of all methods
     */
    public static Set<Method> getAllMethods(Class<?> clazz) {
        Set<Method> methods = new HashSet<>();
        Class<?> currentClass = clazz;

        while (currentClass != null && currentClass != Object.class) {
            methods.addAll(Arrays.asList(currentClass.getDeclaredMethods()));
            currentClass = currentClass.getSuperclass();
        }

        return methods;
    }

    /**
     * Determines the simple class name for the supplied class.
     *
     * @param clazz the class
     * @return the simple class name
     */
    public static String getSimpleName(Class<?> clazz) {
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf('.');
        return (lastDotIndex != -1 ? className.substring(lastDotIndex + 1) : className);
    }

    /**
     * Converts the first letter of the given String to lowercase.
     *
     * @param name the string to convert
     * @return the converted string
     */
    public static String lowerFirstLetter(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}