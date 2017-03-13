package com.bbs.service;

import com.bbs.mybatis.model.Section;

import java.util.List;

/**
 * Created on 2016/9/4.
 */
public interface ISectionService {

    void addOrUpdate(Section section);

    List<Section> getAllSection();

    Section getSection(Integer id);
}
