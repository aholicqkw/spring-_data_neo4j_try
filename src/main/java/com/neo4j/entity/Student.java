package com.neo4j.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Data
@NodeEntity("Student")
public class Student {
    @Id
    @GeneratedValue
    private Long id;

    @Property("name")
    private String name;

    @Property("age")
    private Integer age;

    @JsonIgnoreProperties({"from", "to"}) // 禁止json序列化无限遍历
    @Relationship(type = "FRIENDSHIP_RELATION", direction = Relationship.OUTGOING)
    private List<FriendshipRelation> friendshipRelationOutList;

    @JsonIgnoreProperties({"from", "to"})
    @Relationship(type = "FRIENDSHIP_RELATION", direction = Relationship.INCOMING)
    private List<FriendshipRelation> friendshipRelationInList;

    public void addOutRelation(FriendshipRelation friendshipRelation) {
        if (this.friendshipRelationOutList == null) {
            this.friendshipRelationOutList = new ArrayList<>();
        }
        this.friendshipRelationOutList.add(friendshipRelation);
    }

    public void addInRelation(FriendshipRelation friendshipRelation) {
        if (this.friendshipRelationInList == null) {
            this.friendshipRelationInList = new ArrayList<>();
        }
        this.friendshipRelationInList.add(friendshipRelation);
    }

}
