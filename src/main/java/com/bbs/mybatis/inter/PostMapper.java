package com.bbs.mybatis.inter;

import com.bbs.mybatis.model.Post;
import com.bbs.mybatis.model.PostExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface PostMapper {
    int countByExample(PostExample example);

    int deleteByExample(PostExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Post record);

    int insertSelective(Post record);

    List<Post> selectByExampleWithBLOBsWithRowbounds(PostExample example, RowBounds rowBounds);

    List<Post> selectByExampleWithBLOBs(PostExample example);

    List<Post> selectByExampleWithRowbounds(PostExample example, RowBounds rowBounds);

    List<Post> selectByExample(PostExample example);

    Post selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Post record, @Param("example") PostExample example);

    int updateByExampleWithBLOBs(@Param("record") Post record, @Param("example") PostExample example);

    int updateByExample(@Param("record") Post record, @Param("example") PostExample example);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKeyWithBLOBs(Post record);

    int updateByPrimaryKey(Post record);

    List<Post> selectByKeywordsWithBLOBsWithRowbounds(@Param("section_id") String sectionId, @Param("title") String title, RowBounds rowBounds);
}