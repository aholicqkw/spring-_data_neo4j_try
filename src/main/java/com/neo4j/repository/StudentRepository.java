package com.neo4j.repository;

import com.neo4j.entity.FriendshipRelation;
import com.neo4j.entity.Student;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface StudentRepository extends Neo4jRepository<Student, Long> {

    Student findByName(String name);

    @Query("match p=(a:Student)-[r:FRIENDSHIP_RELATION]->(b:Student) where a.name = {0} return p")
    List<FriendshipRelation> outFriendship(String name);

    @Query("match p=(a:Student)<-[r:FRIENDSHIP_RELATION]-(b:Student) where a.name={0} return p")
    List<FriendshipRelation> inFriendship(String name);

    @Query("match p=(a:Student) <- [r:FRIENDSHIP_RELATION] ->(b:Student) <- [rr:FRIENDSHIP_RELATION] -> (c:Student) where b.name = {0} return p")
    List<FriendshipRelation> bothFriendship(String name);

}
