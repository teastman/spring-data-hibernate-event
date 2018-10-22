package io.github.teastman.hibernate.domain;

import javax.persistence.Entity;

@Entity
public class ChildEntityB extends ParentEntity{

    private String childFieldB;

    public String getChildFieldB() {
        return childFieldB;
    }

    public void setChildFieldB(String childFieldB) {
        this.childFieldB = childFieldB;
    }
}
