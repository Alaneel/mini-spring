package com.minispring.context;

import com.minispring.beans.factory.BeanFactory;
import com.minispring.exception.BeansException;

/**
 * Central interface to provide configuration for an application.
 * This is read-only while the application is running, but may be
 * reloaded if the implementation supports this.
 */
public interface ApplicationContext extends BeanFactory {

    /**
     * Return the name of this context.
     *
     * @return the name of this context
     */
    String getApplicationName();

    /**
     * Return the timestamp when this context was first loaded.
     *
     * @return the timestamp (ms) when this context was first loaded
     */
    long getStartupDate();

    /**
     * Notify all listeners registered with this application of an application event.
     * Events may be framework events or application-specific events.
     *
     * @param event the event to publish
     */
    void publishEvent(Object event);

    /**
     * Close this application context, releasing all resources and locks that the
     * implementation might hold.
     *
     * @throws BeansException if closing is unsuccessful
     */
    void close() throws BeansException;

    /**
     * Determine whether this application context is active.
     *
     * @return whether this context is active
     */
    boolean isActive();
}