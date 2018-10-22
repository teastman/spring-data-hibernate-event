package io.github.teastman.hibernate.service;

import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreUpdateEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import io.github.teastman.hibernate.annotation.HibernateEventListener;
import io.github.teastman.hibernate.domain.ChildEntityA;
import io.github.teastman.hibernate.domain.ChildEntityB;
import io.github.teastman.hibernate.domain.ParentEntity;

@Service
public class HibernateEventListenerService {

    @HibernateEventListener
    public void handlePreInsert(ParentEntity entity, PreInsertEvent event){

    }

    @HibernateEventListener
    public void handleChileAPreInsert(ChildEntityA entity, PreInsertEvent event){

    }

    @HibernateEventListener
    public void handleChileBPreInsert(ChildEntityB entity, PreInsertEvent event){

    }

    @Order(2)
    @HibernateEventListener
    public void handlePreUpdateSecond(ParentEntity entity, PreUpdateEvent event){

    }

    @Order(1)
    @HibernateEventListener
    public void handlePreUpdateFirst(ParentEntity entity, PreUpdateEvent event){

    }

    @HibernateEventListener
    public void handlePostUpdate(ParentEntity entity, PostUpdateEvent event){

    }
}
