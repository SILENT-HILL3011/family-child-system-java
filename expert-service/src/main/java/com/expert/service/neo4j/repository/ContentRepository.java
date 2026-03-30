package com.expert.service.neo4j.repository;

import com.expert.service.neo4j.node.Content;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface ContentRepository extends Neo4jRepository<Content, String> {
    @Query("MATCH (a:Age) RETURN a.name ORDER BY a.name")
    List<String> findAllAges();

    // 查询学科列表
    @Query("MATCH (s:Subject) RETURN s.name ORDER BY s.name")
    List<String> findAllSubjects();

    // 根据年龄+学科查询内容节点
    @Query("MATCH (a:Age)-[:HAS_SUBJECT]->(s:Subject)-[:HAS_CONTENT]->(c:Content) WHERE a.name=$age AND s.name=$subject RETURN c")
    List<Content> findByAgeAndSubject(String age, String subject);

    // 新增年龄
    @Query("CREATE (a:Age{name:$name})")
    void addAge(String name);

    // 新增学科
    @Query("CREATE (s:Subject{name:$name})")
    void addSubject(String name);

    // 新增Content节点 + 自动建立关系
    @Query("""
                MATCH (a:Age{name:$age})
                MATCH (s:Subject{name:$subject})
                MERGE (a)-[:HAS_SUBJECT]->(s)
                MERGE (c:Content{name:$name, desc:$desc})
                MERGE (s)-[:HAS_CONTENT]->(c)
            """)
    void addContent(String age, String subject, String name, String desc);
}
