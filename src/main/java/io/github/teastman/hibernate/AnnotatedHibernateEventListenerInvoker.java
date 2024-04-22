package io.github.teastman.hibernate;

import io.github.teastman.hibernate.annotation.HibernateEventListener;
import io.github.teastman.hibernate.exception.AssignableParameterException;
import io.github.teastman.hibernate.exception.InvalidParameterCount;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * Service which registers and calls methods that are annotated with @HibernateEventListener
 *
 * @author Tyler Eastman
 */
public class AnnotatedHibernateEventListenerInvoker implements
        PostCollectionRecreateEventListener,
        PostCollectionRemoveEventListener,
        PostCollectionUpdateEventListener,
        PreCollectionRecreateEventListener,
        PreCollectionRemoveEventListener,
        PreCollectionUpdateEventListener,
        SaveOrUpdateEventListener,
        PreInsertEventListener,
        PreDeleteEventListener,
        PreUpdateEventListener,
        PostCommitInsertEventListener,
        PostDeleteEventListener,
        PostUpdateEventListener,
        BeanPostProcessor {

    private final MultiValueMap<Class<?>, EventHandlerMethod> handlerMethods = new LinkedMultiValueMap<>();

    @Override
    public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
        onEvent(event, event.getAffectedOwnerOrNull());
    }

    @Override
    public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
        onEvent(event, event.getAffectedOwnerOrNull());
    }

    @Override
    public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
        onEvent(event, event.getAffectedOwnerOrNull());
    }

    @Override
    public void onPreRecreateCollection(PreCollectionRecreateEvent event) {
        onEvent(event, event.getAffectedOwnerOrNull());
    }

    @Override
    public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
        onEvent(event, event.getAffectedOwnerOrNull());
    }

    @Override
    public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
        onEvent(event, event.getAffectedOwnerOrNull());
    }

    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
        onEvent(event, event.getEntity());
    }

    @Override
    public boolean requiresPostCommitHanding(EntityPersister entityPersister) {
        return false;
    }

    @Override
    public void onPostInsert(PostInsertEvent event) {
        onEvent(event, event.getEntity());
    }

    @Override
    public void onPostInsertCommitFailed(PostInsertEvent postInsertEvent) {
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        onEvent(event, event.getEntity());
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        onEvent(event, event.getEntity());
    }

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        return onEvent(event, event.getEntity());
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        return onEvent(event, event.getEntity());
    }

    @Override
    public boolean onPreUpdate(PreUpdateEvent event) {
        return onEvent(event, event.getEntity());
    }

    private boolean onEvent(AbstractEvent event, Object entity) {
        if (handlerMethods.get(event.getClass()) == null)
            return false;

        for (EventHandlerMethod handlerMethod : handlerMethods.get(event.getClass())) {
            if (ClassUtils.isAssignable(handlerMethod.targetType, entity.getClass()))
                ReflectionUtils.invokeMethod(handlerMethod.method, handlerMethod.bean, entity, event);
        }

        return false;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        for (Method method : ReflectionUtils.getUniqueDeclaredMethods(ClassUtils.getUserClass(bean))) {
            try {
                register(bean, method, HibernateEventListener.class);
            } catch (InvalidParameterCount | AssignableParameterException e) {
                throw new BeanInitializationException("@HibernateEventListener method could not be registered.", e);
            }
        }

        return bean;
    }

    private <T extends Annotation> void register(Object bean, Method method, Class<T> annotationType) throws InvalidParameterCount, AssignableParameterException {
        if (AnnotationUtils.findAnnotation(method, annotationType) == null)
            return;

        if (method.getParameterCount() != 2)
            throw new InvalidParameterCount("The method must have exactly 2 parameters.");

        Class<?> entityClass = ResolvableType.forMethodParameter(method, 0, bean.getClass()).resolve();
        Class<?> eventClass = ResolvableType.forMethodParameter(method, 1, bean.getClass()).resolve();

        if (!ClassUtils.isAssignable(AbstractEvent.class, eventClass))
            throw new AssignableParameterException("The second parameter must be of type org.hibernate.event.spi.AbstractEvent.");

        ReflectionUtils.makeAccessible(method);
        EventHandlerMethod handlerMethod = new EventHandlerMethod(entityClass, bean, method);

        handlerMethods.add(eventClass, handlerMethod);
        List<EventHandlerMethod> events = handlerMethods.get(eventClass);
        if (events.size() > 1) {
            Collections.sort(events);
            handlerMethods.put(eventClass, events);
        }
    }

    static class EventHandlerMethod implements Comparable<EventHandlerMethod> {
        final Class<?> targetType;
        final Method method;
        final Object bean;

        public EventHandlerMethod(Class<?> targetType, Object bean, Method method) {
            this.targetType = targetType;
            this.method = method;
            this.bean = bean;
        }

        @Override
        public int compareTo(EventHandlerMethod o) {
            return AnnotationAwareOrderComparator.INSTANCE.compare(this.method, o.method);
        }
    }
}
