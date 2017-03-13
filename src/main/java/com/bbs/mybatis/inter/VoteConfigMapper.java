package com.bbs.mybatis.inter;

import com.bbs.mybatis.model.VoteConfig;
import com.bbs.mybatis.model.VoteConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface VoteConfigMapper {
    int countByExample(VoteConfigExample example);

    int deleteByExample(VoteConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(VoteConfig record);

    int insertSelective(VoteConfig record);

    List<VoteConfig> selectByExampleWithRowbounds(VoteConfigExample example, RowBounds rowBounds);

    List<VoteConfig> selectByExample(VoteConfigExample example);

    VoteConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") VoteConfig record, @Param("example") VoteConfigExample example);

    int updateByExample(@Param("record") VoteConfig record, @Param("example") VoteConfigExample example);

    int updateByPrimaryKeySelective(VoteConfig record);

    int updateByPrimaryKey(VoteConfig record);

    List<VoteConfig> selectByKeywordsWithRowbounds(@Param("title") String title, RowBounds rowBounds);
}