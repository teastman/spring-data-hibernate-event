package io.github.teastman.hibernate.tool;

import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

import java.util.Arrays;

/**
 * Utility class for working with Hibernate spi events.
 *
 * @author Tyler Eastman
 */
public class HibernateEventUtils {

    public static int getPropertyIndex(AbstractPreDatabaseOperationEvent event, String property){
        return getPropertyIndex(event.getPersister(), property);
    }

    public static int getPropertyIndex(PreUpdateEvent event, String property){
        return getPropertyIndex(event.getPersister(), property);
    }

    public static int getPropertyIndex(PreInsertEvent event, String property){
        return getPropertyIndex(event.getPersister(), property);
    }

    public static int getPropertyIndex(PreDeleteEvent event, String property){
        return getPropertyIndex(event.getPersister(), property);
    }

    public static int getPropertyIndex(PostUpdateEvent event, String property){
        return getPropertyIndex(event.getPersister(), property);
    }

    public static int getPropertyIndex(PostInsertEvent event, String property){
        return getPropertyIndex(event.getPersister(), property);
    }

    public static int getPropertyIndex(PostDeleteEvent event, String property){
        return getPropertyIndex(event.getPersister(), property);
    }

    public static int getPropertyIndex(SaveOrUpdateEvent event, String property){
        return getPropertyIndex(event.getEntry().getPersister(), property);
    }

    public static int getPropertyIndex(EntityPersister persister, String property){
        return Arrays.asList(persister.getPropertyNames()).indexOf(property);
    }
}
