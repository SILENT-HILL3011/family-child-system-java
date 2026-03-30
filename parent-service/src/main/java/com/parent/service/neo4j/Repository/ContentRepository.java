package com.parent.service.neo4j.Repository;

import com.parent.service.neo4j.node.Content;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface ContentRepository extends Neo4jRepository<Content,String> {

    @Query("""
    MATCH (a:Age{name:$age})-[:HAS_SUBJECT]->(s:Subject{name:$subject})-[:HAS_CONTENT]->(c:Content)
    RETURN c
    """)
    List<Content> findByAgeAndSubject(String age, String subject);

    @Query("MATCH (a:Age) RETURN a.name")
    List<String> findAllAges();

    @Query("MATCH (s:Subject) RETURN s.name")
    List<String> findAllSubjects();
}
