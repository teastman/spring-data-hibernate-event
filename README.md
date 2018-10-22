# Spring Data Hibernate Event

The primary goal of the Spring Data Hibernate Event project is to make it easier to listen for Hibernate spi events that affect given entities via the use of an annotation.

## Dependency ##
Download the jar through Maven:

```xml
<dependency>
    <groupId>com.github.teastman</groupId>
    <artifactId>spring-data-hibernate-event</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Configuration ##
Spring auto configurations are used to listen for Hibernate events on the EntityManagerFactory.  So if you are using spring auto configurations you can skip the next step.

If you are NOT using auto configurations, you can manually add the required configuration by adding an @Import to another configuration. 

```java
@Configuration
@Import(HibernateEventAutoConfiguration.class)
class AppConfig {
...
```

Or you can override the default configuration behaviour by creating your own AnnotatedHibernateEventListenerInvoker bean .
```java
@Configuration
class AppConfig {
 
    @Bean
    public AnnotatedHibernateEventListenerInvoker annotatedHibernateEventHandlerInvoker() {
        AnnotatedHibernateEventListenerInvoker invoker = new AnnotatedHibernateEventListenerInvoker();
        SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.prependListeners(EventType.PRE_UPDATE, invoker);
        registry.prependListeners(EventType.PRE_DELETE, invoker);
        registry.prependListeners(EventType.PRE_INSERT, invoker);
        ...
        return invoker;
    }
}
```

## Utilization ##

You can now use the @HibernateEventListener annotation to bind methods to listen for Hibernate events.

```java
@Service
public class MyListenerService {

    @HibernateEventListener
    public void handlePreInsert(MyEntity entity, PreInsertEvent event){
        // This will be called any time an entity of type MyEntity is first saved.
    }
}
```

To check if a specific field changed in an entity you can use the static utility function getPropertyIndex.

```java
@HibernateEventListener
public void onUpdate(MyEntity entity, PreUpdateEvent event) {
    int index = getPropertyIndex(event, "name");
    if (event.getOldState()[index] != event.getState()[index]) {
        // The name changed.
    }
}
```

## Event Types ##

The valid event types are:

- SaveOrUpdateEvent
- PreInsertEvent
- PreUpdateEvent
- PreDeleteEvent
- PreCollectionRecreateEvent
- PreCollectionUpdateEvent
- PreCollectionRemoveEvent
- PostInsertEvent
- PostUpdateEvent
- PostDeleteEvent
- PostCollectionRecreateEvent
- PostCollectionUpdateEvent
- PostCollectionRemoveEvent
