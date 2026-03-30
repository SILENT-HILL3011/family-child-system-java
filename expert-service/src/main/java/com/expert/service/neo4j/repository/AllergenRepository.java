package com.expert.service.neo4j.repository;

import com.expert.service.neo4j.node.Allergen;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface AllergenRepository extends Neo4jRepository<Allergen, String> {

    // 查所有食物（下拉）
    @Query("MATCH (f:Food) RETURN f.name ORDER BY f.name")
    List<String> findAllFoods();

    // 根据食物查过敏原
    @Query("MATCH (f:Food{name:$food})-[:HAS_ALLERGEN]->(a:Allergen) RETURN a")
    List<Allergen> findByFood(String food);

    // 新增食物节点
    @Query("CREATE (f:Food{name:$name})")
    void addFood(String name);

    // 新增过敏原 + 自动建关系
    @Query("""
            MATCH (f:Food{name:$food})
            MERGE (a:Allergen{name:$name, desc:$desc})
            MERGE (f)-[:HAS_ALLERGEN]->(a)
            """)
    void addAllergen(String food, String name, String desc);
}
