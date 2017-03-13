package com.bbs.service.impl;

import com.bbs.mybatis.inter.SectionMapper;
import com.bbs.mybatis.model.Section;
import com.bbs.mybatis.model.SectionExample;
import com.bbs.service.ISectionService;
import com.bbs.util.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/9/4.
 */
@Service
@Transactional
public class SectionServiceImpl implements ISectionService {

    @Resource
    SectionMapper sectionMapper;

    @Override
    public void addOrUpdate(Section section) {
        Integer id = section.getId();
        if(id == null){
            sectionMapper.insert(section);
        }else{
            sectionMapper.updateByPrimaryKey(section);
        }
    }

    @Override
    public List<Section> getAllSection() {
        SectionExample example = new SectionExample();
        SectionExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(Constants.YES_OR_NO.NO);
        return sectionMapper.selectByExample(example);
    }

    @Override
    public Section getSection(Integer id) {
        return sectionMapper.selectByPrimaryKey(id);
    }
}
