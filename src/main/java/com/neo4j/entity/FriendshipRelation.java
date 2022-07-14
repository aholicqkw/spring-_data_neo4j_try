package com.neo4j.entity;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;


@Getter
@Setter
@RelationshipEntity(type = "FRIENDSHIP_RELATION")
public class FriendshipRelation {
    @Id
    @GeneratedValue
    private Long id;

    @Property("name")
    private String name;

    @StartNode
    private Student from;

    @EndNode
    private Student to;

}
