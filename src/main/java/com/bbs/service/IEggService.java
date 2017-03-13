package com.bbs.service;

import com.bbs.mybatis.model.Awards;
import com.bbs.mybatis.model.AwardsRecords;
import com.bbs.util.page.Page;

import java.util.List;

/**
 * Created by lihongde on 2016/9/13 10:53
 */
public interface IEggService {

    /**
     * 获得所有奖项记录
     * @return
     */
    List<Awards> findAwards();

    Awards getAwards(Integer id);

    Page<Awards> findPageAwards(int page, int size);

    void addOrUpdate(Awards awards);

    void delete(Integer id);

    void deleteRecordByAwardId(Integer awardId);

    Page<AwardsRecords> findPageAwardsRecords(String adard, String nickName, String date, int page, int size);

    void addAwardsRecord(AwardsRecords records);

    /**
     * 获得当天的投票记录
     * @param userId
     * @param postId
     * @param times
     * @return
     */
    List<AwardsRecords> getRecordByUser(Integer userId, Integer postId, Integer times);

    void deleteByPost(Integer postId);
}
