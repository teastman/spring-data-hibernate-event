package com.github.teastman.hibernate.test;

import com.github.teastman.hibernate.Application;
import com.github.teastman.hibernate.domain.ChildEntityA;
import com.github.teastman.hibernate.domain.ParentEntity;
import com.github.teastman.hibernate.repository.ChildEntityARepository;
import com.github.teastman.hibernate.repository.ParentEntityRepository;
import com.github.teastman.hibernate.service.HibernateEventListenerService;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreUpdateEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AnnotationTests {

    @Autowired
    private ParentEntityRepository parentEntityRepository;

    @Autowired
    private ChildEntityARepository childEntityARepository;

    @SpyBean
    private HibernateEventListenerService service;

    @Test
    public void create_parent_should_call_insert_method() {
        ParentEntity parentEntity = new ParentEntity();
        parentEntity = parentEntityRepository.save(parentEntity);
        verify(service).handlePreInsert(eq(parentEntity), any(PreInsertEvent.class));
    }

    @Test
    public void create_parent_should_not_call_update_method() {
        ParentEntity parentEntity = new ParentEntity();
        parentEntity = parentEntityRepository.save(parentEntity);
        verify(service, never()).handlePreUpdateFirst(eq(parentEntity), any(PreUpdateEvent.class));
    }

    @Test
    public void create_child_a_should_call_insert_method_for_parent() {
        ChildEntityA childEntityA = new ChildEntityA();
        childEntityARepository.save(childEntityA);

        verify(service).handlePreInsert(eq(childEntityA), any(PreInsertEvent.class));
        verify(service).handleChileAPreInsert(eq(childEntityA), any(PreInsertEvent.class));
    }

    @Test
    public void create_child_a_should_not_call_insert_method_for_child_b() {
        ChildEntityA childEntityA = new ChildEntityA();
        childEntityA = childEntityARepository.save(childEntityA);

        verify(service).handlePreInsert(eq(childEntityA), any(PreInsertEvent.class));
        verify(service, never()).handleChileBPreInsert(any(), any(PreInsertEvent.class));
    }

    @Test
    public void should_call_both_pre_update_methods() {
        ParentEntity parentEntity = new ParentEntity();
        parentEntity = parentEntityRepository.save(parentEntity);

        parentEntity.setName("Test Name");
        parentEntity = parentEntityRepository.save(parentEntity);

        verify(service).handlePreUpdateFirst(eq(parentEntity), any(PreUpdateEvent.class));
        verify(service).handlePreUpdateSecond(eq(parentEntity), any(PreUpdateEvent.class));
    }

    @Test
    public void update_parent_should_call_post_update_method() {
        ParentEntity parentEntity = new ParentEntity();
        parentEntity = parentEntityRepository.save(parentEntity);

        parentEntity.setName("Test Name");
        parentEntity = parentEntityRepository.save(parentEntity);

        verify(service).handlePostUpdate(eq(parentEntity), any(PostUpdateEvent.class));
    }

    @Test
    public void should_call_pre_and_post_methods_in_order() {
        ParentEntity parentEntity = new ParentEntity();
        parentEntity = parentEntityRepository.save(parentEntity);

        parentEntity.setName("Test Name");
        parentEntity = parentEntityRepository.save(parentEntity);

        InOrder orderVerifier = Mockito.inOrder(service);
        orderVerifier.verify(service).handlePreUpdateFirst(eq(parentEntity), any(PreUpdateEvent.class));
        orderVerifier.verify(service).handlePostUpdate(eq(parentEntity), any(PostUpdateEvent.class));
    }

    @Test
    public void should_call_ordered_methods_in_order() {
        ParentEntity parentEntity = new ParentEntity();
        parentEntity = parentEntityRepository.save(parentEntity);

        parentEntity.setName("Test Name");
        parentEntity = parentEntityRepository.save(parentEntity);

        InOrder orderVerifier = Mockito.inOrder(service);
        orderVerifier.verify(service).handlePreUpdateFirst(eq(parentEntity), any(PreUpdateEvent.class));
        orderVerifier.verify(service).handlePreUpdateSecond(eq(parentEntity), any(PreUpdateEvent.class));
    }
}
