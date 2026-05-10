package com.expert.service.neo4j.repository;

import com.expert.service.neo4j.node.Disease;
import com.expert.service.neo4j.node.Drug;
import com.expert.service.neo4j.node.Symptom;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface ExpertDiseaseRepository extends Neo4jRepository<Disease,String> {

    // ====================== 疾病 ======================
    @Query("MATCH (d:Disease) RETURN d.name ORDER BY d.name")
    List<String> findAllDiseaseNames();

    @Query("MATCH (d:Disease{name:$name}) RETURN d")
    List<Disease> findDiseaseByName(String name);

    @Query("CREATE (d:Disease{name:$name, desc:$desc, suitAge:$suitAge})")
    void addDisease(String name, String desc, String suitAge);

    // ====================== 症状 ======================
    @Query("MATCH (s:Symptom) RETURN s.name ORDER BY s.name")
    List<String> findAllSymptomNames();

    @Query("MATCH (s:Symptom{name:$name}) RETURN s")
    List<Symptom> findSymptomByName(String name);

    @Query("CREATE (s:Symptom{name:$name, desc:$desc})")
    void addSymptom(String name, String desc);

    // ====================== 药品 ======================
    @Query("MATCH (dr:Drug) RETURN dr.name ORDER BY dr.name")
    List<String> findAllDrugNames();

    @Query("MATCH (dr:Drug{name:$name}) RETURN dr")
    List<Drug> findDrugByName(String name);

    @Query("CREATE (dr:Drug{name:$name, desc:$desc, attention:$attention})")
    void addDrug(String name, String desc, String attention);

    // ====================== 关系维护 ======================
    // 疾病 -> 症状 (HAS_SYMPTOM)
    @Query("""
            MATCH (d:Disease{name:$diseaseName})
            MATCH (s:Symptom{name:$symptomName})
            MERGE (d)-[:HAS_SYMPTOM]->(s)
            """)
    void relateDiseaseToSymptom(String diseaseName, String symptomName);

    // 疾病 -> 药品 (HAS_DRUG)
    @Query("""
            MATCH (d:Disease{name:$diseaseName})
            MATCH (dr:Drug{name:$drugName})
            MERGE (d)-[:HAS_DRUG]->(dr)
            """)
    void relateDiseaseToDrug(String diseaseName, String drugName);
}
