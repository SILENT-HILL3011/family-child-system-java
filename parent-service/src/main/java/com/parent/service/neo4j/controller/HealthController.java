package com.parent.service.neo4j.controller;

import com.child.common.result.R;
import com.parent.service.neo4j.Repository.DiseaseRepository;
import com.parent.service.neo4j.node.Disease;
import com.parent.service.neo4j.node.Drug;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/child/health")
public class HealthController {

    @Resource
    private DiseaseRepository diseaseRepository;

    @RequestMapping("/symptom/list")
    public R<List<String>> getSymptomList() {
        return R.success(diseaseRepository.findAllSymptomNames());
    }

    // 根据症状查疾病
    @RequestMapping("/disease")
    public R<List<Disease>> getDisease(@NotEmpty String symptom) {
        return R.success(diseaseRepository.findDiseaseBySymptom(symptom));
    }

    // 根据疾病查药品
    @RequestMapping("/drug")
    public R<List<Drug>> getDrug(@NotEmpty String disease) {
        return R.success(diseaseRepository.findDrugByDisease(disease));
    }
}
