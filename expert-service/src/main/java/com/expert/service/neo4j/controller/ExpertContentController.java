package com.expert.service.neo4j.controller;

import com.child.common.result.R;
import com.expert.service.neo4j.node.Content;
import com.expert.service.neo4j.repository.ContentRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expert/content")
public class ExpertContentController {

    @Resource
    private ContentRepository contentRepository;

    @RequestMapping("/ageList")
    public R<List<String>> ageList() {
        return R.success(contentRepository.findAllAges());
    }

    // 新增年龄
    @RequestMapping("/addAge")
    public R<String> addAge(String ageName) {
        contentRepository.addAge(ageName);
        return R.success("添加成功");
    }

    // 学科列表
    @RequestMapping("/subjectList")
    public R<List<String>> subjectList() {
        return R.success(contentRepository.findAllSubjects());
    }

    // 新增学科
    @RequestMapping("/addSubject")
    public R<String> addSubject(String subName) {
        contentRepository.addSubject(subName);
        return R.success("添加成功");
    }

    // 内容列表
    @RequestMapping("/list")
    public R<List<Content>> list(String age, String subject) {
        return R.success(contentRepository.findByAgeAndSubject(age, subject));
    }

    // 新增内容（自动建关系）
    @RequestMapping("/addContent")
    public R<String> addContent(String age, String subject, String name, String desc) {
        contentRepository.addContent(age, subject, name, desc);
        return R.success("添加成功");
    }
}
