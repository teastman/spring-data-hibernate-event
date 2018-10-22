package com.github.teastman.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.teastman.hibernate.domain.ParentEntity;

public interface ParentEntityRepository extends JpaRepository<ParentEntity, Long> {
}
