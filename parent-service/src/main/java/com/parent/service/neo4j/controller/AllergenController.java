package com.parent.service.neo4j.controller;

import com.child.common.result.R;
import com.parent.service.neo4j.Repository.AllergenRepository;
import com.parent.service.neo4j.node.Allergen;
import com.parent.service.neo4j.node.Content;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/child/allergen")
public class AllergenController {

    @Resource
    private AllergenRepository allergenRepository;

    @RequestMapping("/query")
    public R<List<Allergen>> query(@NotEmpty String food){
        return R.success(allergenRepository.findByFood(food));
    }
    @RequestMapping("/category")
    public R<List<Map<String, Object>>> category(@NotEmpty String category) {
        return R.success(allergenRepository.findByCategory(category));
    }

    @RequestMapping("/foods")
    public R<List<String>> getAllergenFoods() {
        return R.success(allergenRepository.findAllFoods());
    }
}
