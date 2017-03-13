package com.bbs.service;

import com.bbs.mybatis.model.VoteConfig;
import com.bbs.util.page.Page;

/**
 * Created by lihongde on 2016/10/1 20:13.
 */
public interface IVoteConfigService {

    Page<VoteConfig> findPageConfigs(String title, int page, int size);

    void addOrUpdateConfig(VoteConfig config);

    VoteConfig getVoteConfig(Integer id);

    void deleteByPost(Integer postId);

    VoteConfig getVoteConfigByPost(Integer postId);
}
