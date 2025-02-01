package com.wxxzin.portfolio.server.domain.project.service;

import com.wxxzin.portfolio.server.domain.project.entity.TagEntity;

public interface TagService {
    TagEntity findTagEntityByTagName(String tagName);
}
