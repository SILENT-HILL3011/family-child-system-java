package com.parent.service.neo4j.Repository;

import com.parent.service.neo4j.node.Allergen;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Map;

public interface AllergenRepository extends Neo4jRepository<Allergen, String> {

    @Query("""
                MATCH (f:Food{name:$foodName})-[:HAS_ALLERGEN]->(a:Allergen)
                RETURN a
            """)
    List<Allergen> findByFood(String foodName);

    // 根据【分类】查所有食物及过敏
    @Query("""
        MATCH (c:FoodCategory{name:$category})-[:INCLUDE]->(f:Food)-[:HAS_ALLERGEN]->(a:Allergen)
        RETURN f.name as foodName, a.name as allergenName, a.desc as allergenDesc
    """)
    List<Map<String, Object>> findByCategory(String category);

    @Query("MATCH (f:Food) RETURN f.name ORDER BY f.name")
    List<String> findAllFoods();
}
