package com.expert.service.neo4j.repository;

import com.expert.service.neo4j.node.Nutrient;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface NutrientRepository extends Neo4jRepository<Nutrient, String> {
    // 所有食物下拉
    @Query("MATCH (f:Food) RETURN f.name ORDER BY f.name")
    List<String> findAllFoods();

    // 根据食物查营养
    @Query("MATCH (f:Food{name:$food})-[:HAS_NUTRIENT]->(n:Nutrient) RETURN n")
    List<Nutrient> findByFood(String food);

    // 新增食物（复用，和过敏模块共用Food节点）
    @Query("CREATE (f:Food{name:$name})")
    void addFood(String name);

    // 新增营养 + 自动绑食物关系
    @Query("""
            MATCH (f:Food{name:$food})
            MERGE (n:Nutrient{name:$name, desc:$desc})
            MERGE (f)-[:HAS_NUTRIENT]->(n)
            """)
    void addNutrient(String food, String name, String desc);
}
