package com.expert.service.neo4j.controller;

import com.child.common.result.R;
import com.expert.service.neo4j.node.Allergen;
import com.expert.service.neo4j.repository.AllergenRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expert/allergen")
public class ExpertAllergenController {

    @Resource
    private AllergenRepository allergenRepository;

    // 所有食物列表
    @RequestMapping("/foodList")
    public R<List<String>> foodList(){
        return R.success(allergenRepository.findAllFoods());
    }

    // 新增食物
    @RequestMapping("/addFood")
    public R<String> addFood(String foodName){
        allergenRepository.addFood(foodName);
        return R.success("食物添加成功");
    }

    // 根据食物查过敏原列表
    @RequestMapping("/list")
    public R<List<Allergen>> list(String food){
        return R.success(allergenRepository.findByFood(food));
    }

    // 新增过敏原+自动关联食物
    @RequestMapping("/addAllergen")
    public R<String> addAllergen(String food,String name,String desc){
        allergenRepository.addAllergen(food,name,desc);
        return R.success("过敏原添加成功，关系已自动建立");
    }
}
