package com.wxxzin.portfolio.server.domain.project.service.implement;

import org.springframework.stereotype.Service;

import com.wxxzin.portfolio.server.domain.project.entity.TagEntity;
import com.wxxzin.portfolio.server.domain.project.repository.TagRepository;
import com.wxxzin.portfolio.server.domain.project.service.TagService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public TagEntity findTagEntityByTagName(String tagName) {
        return tagRepository.findByTagName(tagName)
                .orElseGet(() -> tagRepository.save(TagEntity.builder().tagName(tagName).build()));
    }
}
