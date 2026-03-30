package com.parent.service.neo4j.controller;

import com.child.common.result.R;
import com.parent.service.neo4j.Repository.ContentRepository;
import com.parent.service.neo4j.node.Content;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/child/knowledge")
public class KnowledgeController {

    @Resource
    private ContentRepository contentRepository;

    @RequestMapping("/query")
    public R<List<Content>> query(@NotEmpty String age, @NotEmpty String subject){
        return R.success(contentRepository.findByAgeAndSubject(age,subject));
    }

    @RequestMapping("/ages")
    public R<List<String>> getAges() {
        return R.success(contentRepository.findAllAges());
    }

    @RequestMapping("/subjects")
    public R<List<String>> getSubjects() {
        return R.success(contentRepository.findAllSubjects());
    }


}
