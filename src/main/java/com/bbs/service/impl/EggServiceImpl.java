package com.bbs.service.impl;

import com.bbs.mybatis.inter.AwardsMapper;
import com.bbs.mybatis.inter.AwardsRecordsMapper;
import com.bbs.mybatis.model.Awards;
import com.bbs.mybatis.model.AwardsExample;
import com.bbs.mybatis.model.AwardsRecords;
import com.bbs.mybatis.model.AwardsRecordsExample;
import com.bbs.service.IEggService;
import com.bbs.util.DateUtil;
import com.bbs.util.page.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lihongde on 2016/9/13 11:13
 */
@Service
@Transactional
public class EggServiceImpl implements IEggService {

    @Resource
    private AwardsMapper awardsMapper;

    @Resource
    private AwardsRecordsMapper awardsRecordsMapper;

    @Override
    public List<Awards> findAwards() {
        AwardsExample example = new AwardsExample();
        return awardsMapper.selectByExample(example);
    }

    @Override
    public Awards getAwards(Integer id) {
        return awardsMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<Awards> findPageAwards(int page, int size) {
        AwardsExample example = new AwardsExample();
        RowBounds rowBounds = new RowBounds((page - 1) * size, size);
        List<Awards> list = awardsMapper.selectByExampleWithRowbounds(example, rowBounds);
        return new Page<Awards>(page, size, awardsMapper.countByExample(example), list);

    }

    @Override
    public void addOrUpdate(Awards awards) {
        Integer id = awards.getId();
        if(id == null){
            awardsMapper.insertSelective(awards);
        }else{
            awardsMapper.updateByPrimaryKey(awards);
        }
    }

    @Override
    public void delete(Integer id) {
        awardsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteRecordByAwardId(Integer awardId) {
        AwardsRecordsExample example = new AwardsRecordsExample();
        AwardsRecordsExample.Criteria criteria = example.createCriteria();
        criteria.andAwardIdEqualTo(awardId);
        awardsRecordsMapper.deleteByExample(example);
    }

    @Override
    public Page<AwardsRecords> findPageAwardsRecords(String award, String nickName, String date, int page, int size) {
        AwardsRecordsExample example = new AwardsRecordsExample();
        example.setOrderByClause("create_time desc");
        RowBounds rowBounds = new RowBounds((page -1) * size, size);
        List<AwardsRecords> list = new ArrayList<AwardsRecords>();
        if(StringUtils.isNotEmpty(award) || StringUtils.isNotEmpty(nickName) || StringUtils.isNotEmpty(date)){
            list = awardsRecordsMapper.selectByKeywordsWithRowbounds(award, nickName, date, rowBounds);
            return new Page<AwardsRecords>(page, size, list.size(), list);
        }else{
            list = awardsRecordsMapper.selectByExampleWithRowbounds(example, rowBounds);
            return new Page<AwardsRecords>(page, size, awardsRecordsMapper.countByExample(example), list);
        }
    }

    @Override
    public void addAwardsRecord(AwardsRecords records) {
        awardsRecordsMapper.insert(records);
    }

    @Override
    public List<AwardsRecords> getRecordByUser(Integer userId, Integer postId, Integer times) {
        AwardsRecordsExample example = new AwardsRecordsExample();
        AwardsRecordsExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andPostIdEqualTo(postId);
        if(times> 0){
            criteria.andCreateTimeBetween(DateUtil.getStartTimeOfDate(new Date()), DateUtil.getEndTimeOfDate(new Date()));
        }

        return awardsRecordsMapper.selectByExample(example);

    }

    @Override
    public void deleteByPost(Integer postId) {
        AwardsRecordsExample example = new AwardsRecordsExample();
        AwardsRecordsExample.Criteria criteria = example.createCriteria();
        criteria.andPostIdEqualTo(postId);
        if(awardsRecordsMapper.countByExample(example) > 0){
            awardsRecordsMapper.deleteByExample(example);
        }

    }
}
