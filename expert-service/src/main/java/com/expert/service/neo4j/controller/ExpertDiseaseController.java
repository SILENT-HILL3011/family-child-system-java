package com.expert.service.neo4j.controller;


import com.child.common.result.R;
import com.expert.service.neo4j.node.Disease;
import com.expert.service.neo4j.node.Drug;
import com.expert.service.neo4j.node.Symptom;
import com.expert.service.neo4j.repository.ExpertDiseaseRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expert/disease")
public class ExpertDiseaseController {

    @Resource
    private ExpertDiseaseRepository repository;

    // ====================== 疾病 ======================
    @RequestMapping("/diseaseList")
    public R<List<String>> diseaseList() {
        return R.success(repository.findAllDiseaseNames());
    }

    @RequestMapping("/addDisease")
    public R<String> addDisease(String name, String desc, String suitAge) {
        repository.addDisease(name, desc, suitAge);
        return R.success("疾病添加成功");
    }

    @RequestMapping("/getDisease")
    public R<List<Disease>> getDisease(String name) {
        return R.success(repository.findDiseaseByName(name));
    }

    // ====================== 症状 ======================
    @RequestMapping("/symptomList")
    public R<List<String>> symptomList() {
        return R.success(repository.findAllSymptomNames());
    }

    @RequestMapping("/addSymptom")
    public R<String> addSymptom(String name, String desc) {
        repository.addSymptom(name, desc);
        return R.success("症状添加成功");
    }

    @RequestMapping("/getSymptom")
    public R<List<Symptom>> getSymptom(String name) {
        return R.success(repository.findSymptomByName(name));
    }

    // ====================== 药品 ======================
    @RequestMapping("/drugList")
    public R<List<String>> drugList() {
        return R.success(repository.findAllDrugNames());
    }

    @RequestMapping("/addDrug")
    public R<String> addDrug(String name, String desc, String attention) {
        repository.addDrug(name, desc, attention);
        return R.success("药品添加成功");
    }

    @RequestMapping("/getDrug")
    public R<List<Drug>> getDrug(String name) {
        return R.success(repository.findDrugByName(name));
    }

    // ====================== 关系维护 ======================
    @RequestMapping("/relateSymptom")
    public R<String> relateSymptom(String diseaseName, String symptomName) {
        repository.relateDiseaseToSymptom(diseaseName, symptomName);
        return R.success("疾病-症状关系已建立");
    }

    @RequestMapping("/relateDrug")
    public R<String> relateDrug(String diseaseName, String drugName) {
        repository.relateDiseaseToDrug(diseaseName, drugName);
        return R.success("疾病-药品关系已建立");
    }
}
