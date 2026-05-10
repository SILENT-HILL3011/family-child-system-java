package com.parent.service.neo4j.Repository;

import com.parent.service.neo4j.node.Disease;
import com.parent.service.neo4j.node.Drug;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface DiseaseRepository extends Neo4jRepository<Disease,String> {

    @Query("MATCH (s:Symptom) RETURN s.name ORDER BY s.name")
    List<String> findAllSymptomNames();


    @Query("""
        MATCH (s:Symptom{name:$symptom})-[:INDICATE]->(d:Disease)
        RETURN d
    """)
    List<Disease> findDiseaseBySymptom(String symptom);

    // 2. 根据疾病 → 查推荐药品
    @Query("""
        MATCH (d:Disease{name:$disease})-[:RECOMMEND_DRUG]->(dr:Drug)
        RETURN dr.name AS name, dr.desc AS desc, dr.attention AS attention
    """)
    List<Drug> findDrugByDisease(String disease);

}
