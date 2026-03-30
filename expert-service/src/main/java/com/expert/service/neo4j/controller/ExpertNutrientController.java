package com.expert.service.neo4j.controller;

import com.child.common.result.R;
import com.expert.service.neo4j.node.Nutrient;
import com.expert.service.neo4j.repository.NutrientRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expert/nutrient")
public class ExpertNutrientController {

    @Resource
    private NutrientRepository nutrientRepository;

    // 食物列表
    @RequestMapping("/foodList")
    public R<List<String>> foodList(){
        return R.success(nutrientRepository.findAllFoods());
    }

    // 新增食物（和过敏共用Food，重复添加不会重复炸库，MERGE/CREATE不影响）
    @RequestMapping("/addFood")
    public R<String> addFood(String foodName){
        nutrientRepository.addFood(foodName);
        return R.success("食物添加成功");
    }

    // 查询某食物营养
    @RequestMapping("/list")
    public R<List<Nutrient>> list(String food){
        return R.success(nutrientRepository.findByFood(food));
    }

    // 新增营养+自动关联
    @RequestMapping("/addNutrient")
    public R<String> addNutrient(String food,String name,String desc){
        nutrientRepository.addNutrient(food,name,desc);
        return R.success("营养添加成功，关系自动建立");
    }
}
