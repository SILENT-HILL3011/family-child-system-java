package com.parent.service.neo4j.Repository;

import com.parent.service.neo4j.node.Nutrient;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface NutrientRepository extends Neo4jRepository<Nutrient, String> {

    @Query("""
                MATCH (f:Food{name:$food})-[:HAS_NUTRIENT]->(n:Nutrient)
                RETURN n
            """)
    List<Nutrient> findByFood(String food);

    @Query("MATCH (f:Food) RETURN f.name ORDER BY f.name")
    List<String> findAllFoods();
}
