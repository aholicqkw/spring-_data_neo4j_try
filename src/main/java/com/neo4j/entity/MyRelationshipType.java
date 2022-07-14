package com.neo4j.entity;

import org.neo4j.graphdb.RelationshipType;

public enum MyRelationshipType implements RelationshipType {
    JVM_LANGIAGES, NON_JVM_LANGIAGES;
}
