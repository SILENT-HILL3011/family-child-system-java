package com.parent.service.neo4j.service;

import com.parent.service.neo4j.Repository.ContentRepository;
import com.parent.service.neo4j.node.Content;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeService {

    @Resource
    private ContentRepository contentRepository;

    public List<Content> getByList(String age,String subject){
        return contentRepository.findByAgeAndSubject(age,subject);
    }
}
