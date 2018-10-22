package com.github.teastman.hibernate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.teastman.hibernate.domain.ChildEntityA;

public interface ChildEntityARepository extends JpaRepository<ChildEntityA, Long> {
}
