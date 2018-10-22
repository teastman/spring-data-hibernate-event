package io.github.teastman.hibernate.domain;

import javax.persistence.Entity;

@Entity
public class ChildEntityA extends ParentEntity{

    private String childFieldA;

    public String getChildFieldA() {
        return childFieldA;
    }

    public void setChildFieldA(String childFieldA) {
        this.childFieldA = childFieldA;
    }
}
