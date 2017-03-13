package com.bbs.service.impl;

import com.bbs.mybatis.inter.VoteConfigMapper;
import com.bbs.mybatis.model.VoteConfig;
import com.bbs.mybatis.model.VoteConfigExample;
import com.bbs.service.IVoteConfigService;
import com.bbs.util.page.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihongde on 2016/10/1 20:18.
 */
@Service
public class VoteConfigServiceImpl implements IVoteConfigService {

    @Resource
    private VoteConfigMapper voteConfigMapper;

    @Override
    public Page<VoteConfig> findPageConfigs(String title, int page, int size) {
        VoteConfigExample example = new VoteConfigExample();
        example.setOrderByClause("id desc");
        RowBounds rowBounds = new RowBounds((page- 1) * size, size);
        List<VoteConfig> list = new ArrayList<VoteConfig>();
        if(StringUtils.isNotEmpty(title)){
            list = voteConfigMapper.selectByKeywordsWithRowbounds(title.trim(), rowBounds);
            return new Page<VoteConfig>(page, size, list.size(), list);
        }else{
            list = voteConfigMapper.selectByExampleWithRowbounds(example, rowBounds);
            return new Page<VoteConfig>(page, size, voteConfigMapper.countByExample(example), list);
        }
    }

    @Override
    public void addOrUpdateConfig(VoteConfig config) {
        Integer id = config.getId();
        if(id == null){
            voteConfigMapper.insert(config);
        }else{
            voteConfigMapper.updateByPrimaryKey(config);
        }
    }

    @Override
    public VoteConfig getVoteConfig(Integer id) {
        return voteConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteByPost(Integer postId) {
        VoteConfigExample example = new VoteConfigExample();
        VoteConfigExample.Criteria criteria = example.createCriteria();
        criteria.andPostIdEqualTo(postId);
        voteConfigMapper.deleteByExample(example);
    }

    @Override
    public VoteConfig getVoteConfigByPost(Integer postId) {
        VoteConfigExample example = new VoteConfigExample();
        VoteConfigExample.Criteria criteria = example.createCriteria();
        criteria.andPostIdEqualTo(postId);
        if(voteConfigMapper.countByExample(example) > 0){
            return voteConfigMapper.selectByExample(example).get(0);
        }
        return null;
    }
}
