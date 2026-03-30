package com.parent.service.neo4j.controller;

import com.child.common.result.R;
import com.parent.service.neo4j.Repository.NutrientRepository;
import com.parent.service.neo4j.node.Nutrient;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/child/nutrient")
public class NutrientController {

    @Resource
    private NutrientRepository nutrientRepository;


    @RequestMapping("/query")
    public R<List<Nutrient>> query(@NotEmpty String food) {
        return R.success(nutrientRepository.findByFood(food));
    }

    @RequestMapping("/foods")
    public R<List<String>> getNutrientFoods() {
        return R.success(nutrientRepository.findAllFoods());
    }
}
