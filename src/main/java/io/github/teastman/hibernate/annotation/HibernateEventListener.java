package io.github.teastman.hibernate.annotation;

import java.lang.annotation.*;

/**
 * Annotation that marks a method as a listener for hibernate spi events.
 *
 * <p>The first parameter of the annotated method must be the entity that
 * hibernate affected. The second parameter must be of type org.hibernate.event.spi.AbstractEvent
 *
 * <p>It is also possible to use Spring's
 * {@link org.springframework.core.annotation.Order @Order} annotation to define the order
 * in which listeners for a given event type are invoked.
 *
* @author Tyler Eastman
*/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface HibernateEventListener {
}
