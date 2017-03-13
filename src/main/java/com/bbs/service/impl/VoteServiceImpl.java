package com.bbs.service.impl;

import com.bbs.mybatis.inter.UserVoteMapper;
import com.bbs.mybatis.inter.VoteCandidateMapper;
import com.bbs.mybatis.inter.VoteMapper;
import com.bbs.mybatis.model.*;
import com.bbs.service.IVoteService;
import com.bbs.util.DateUtil;
import com.bbs.util.TextUtils;
import com.bbs.util.excel.ExcelImporter;
import com.bbs.util.excel.ImportCallBack;
import com.bbs.util.excel.ImportConfig;
import com.bbs.util.page.Page;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by lihongde on 2016/9/12 15:53
 */
@Service
@Transactional
public class VoteServiceImpl implements IVoteService {

    private Logger logger = LoggerFactory.getLogger(ExcelImporter.class);

    @Resource
    private VoteMapper voteMapper;

    @Resource
    ExcelImporter importer;

    @Resource
    private VoteCandidateMapper voteCandidateMapper;

    @Resource
    private UserVoteMapper userVoteMapper;

    private static final String IMPORT_SQL = "insert into vote_candidate(vote_id, name, avatar, introduction) values(?, ?, ?, ?)";


    @Override
    public Page<Vote> findPageVotes(String section, String title,  int page, int size) {
        RowBounds rowBounds = new RowBounds((page - 1) * size, size);
        if(StringUtils.isNotEmpty(section) || StringUtils.isNotEmpty(title)){
            List<Vote> list = voteMapper.selectByKeywordsWithRowbounds(section, title, rowBounds);
            return new Page<Vote>(page, size, list.size(), list);
        }else{
            VoteExample example = new VoteExample();
            example.setOrderByClause("id desc");
            List<Vote> list = voteMapper.selectByExampleWithRowbounds(example, rowBounds);
            return new Page<Vote>(page, size, voteMapper.countByExample(example), list);
        }
    }


    @Override
    public void addOrUpdateVote(Vote vote) {
        Integer id = vote.getId();
        if(id == null){
            voteMapper.insertSelective(vote);
        }else{
            voteMapper.updateByPrimaryKey(vote);
        }
    }

    @Override
    public Map<String, Object> importExcelFile(MultipartFile candidateFile, Integer voteId) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        result =  importer.setImportConfig(new ImportConfig() {
        @Override
        public String validation(Workbook xwb) {
            return null;
        }

        @Override
        public String getImportSQL() {
            return IMPORT_SQL;
        }

        @Override
        public List<Object[]> getImportData( XSSFSheet sheet, List<Object[]> data) throws Exception{

            Map<String, PictureData> picData = getSheetPictrues07(sheet);
            List<Object[]> dataQueue = new ArrayList<Object[]>();
            for(int i=0; i<data.size(); i++){
                Object[] tempData = new Object[4];
                tempData = data.get(i);
                if(!picData.isEmpty()){
                    tempData[2] =  readExcelImages(picData, i);
                }else{
                    tempData[2] = "";
                }
                dataQueue.add(tempData);
            }
            return dataQueue;
        }

        @Override
        public ImportCallBack getImportCallBack() {
            return new ImportCallBack() {
                @Override
                public void preOperation(Integer voteId, List<Object[]> data) {
                    //导入之前删掉该投票帖的候选人
                    VoteCandidateExample example = new VoteCandidateExample();
                    VoteCandidateExample.Criteria criteria = example.createCriteria();
                    criteria.andVoteIdEqualTo(voteId);
                    voteCandidateMapper.deleteByExample(example);
                }

                @Override
                public void postOperation (SqlSessionTemplate sqlSessionTemplate, List < Object[]>data){

                }
            };

        }
    }).importExcelFile(candidateFile, voteId);

        return result;
    }

    @Override
    public Vote getVote(Integer voteId) {
        return voteMapper.selectByPrimaryKey(voteId);
    }

    @Override
    public Page<VoteCandidate> findCandidateByVoteId(Integer voteId, int page, int size) {
        VoteCandidateExample example = new VoteCandidateExample();
        VoteCandidateExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("votes desc");
        criteria.andVoteIdEqualTo(voteId);
        RowBounds rowBounds = new RowBounds((page-1) * size, size);
        List<VoteCandidate> list = voteCandidateMapper.selectByExampleWithRowbounds(example, rowBounds);
        return new Page<VoteCandidate>(page, size, voteCandidateMapper.countByExample(example), list);
    }

    @Override
    public List<VoteCandidate> getCandidateByVoteId(Integer voteId) {
        VoteCandidateExample example = new VoteCandidateExample();
        VoteCandidateExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("votes desc");
        criteria.andVoteIdEqualTo(voteId);
        return voteCandidateMapper.selectByExample(example);
    }

    @Override
    public VoteCandidate getCandidate(Integer voteId, String name) {
        VoteCandidateExample example = new VoteCandidateExample();
        VoteCandidateExample.Criteria criteria = example.createCriteria();
        criteria.andVoteIdEqualTo(voteId);
        criteria.andNameEqualTo(name);
        return voteCandidateMapper.selectByExample(example).get(0);
    }

    @Override
    public void updateCandidate(VoteCandidate candidate) {
        voteCandidateMapper.updateByPrimaryKey(candidate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUserVote(UserVote userVote) {
        userVoteMapper.insert(userVote);
    }

    @Override
    public List<UserVote> findUserVote(Integer userId, Integer voteId, Integer times) {
        UserVoteExample example = new UserVoteExample();
        UserVoteExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andVoteIdEqualTo(voteId);
        if(times > 0){
            criteria.andVoteTimeBetween(DateUtil.getStartTimeOfDate(new Date()), DateUtil.getEndTimeOfDate(new Date()));
        }

        return userVoteMapper.selectByExample(example);
    }

    @Override
    public Vote findVoteByPostId(Integer postId) {
        VoteExample example = new VoteExample();
        VoteExample.Criteria criteria = example.createCriteria();
        criteria.andPostIdEqualTo(postId);
        if(voteMapper.countByExample(example) > 0){
            return voteMapper.selectByExample(example).get(0);
        }
        return null;
    }

    @Override
    public void delete(Integer voteId) {
        voteMapper.deleteByPrimaryKey(voteId);
    }

    @Override
    public void deleteByPost(Integer postId) {
        VoteExample example = new VoteExample();
        VoteExample.Criteria criteria = example.createCriteria();
        criteria.andPostIdEqualTo(postId);
        if(voteMapper.countByExample(example) > 0){
            voteMapper.deleteByExample(example);
        }

    }

    @Override
    public void deleteVoteCandidate(Integer voteId) {
        VoteCandidateExample example = new VoteCandidateExample();
        VoteCandidateExample.Criteria criteria = example.createCriteria();
        criteria.andVoteIdEqualTo(voteId);
        if(voteCandidateMapper.countByExample(example) > 0){
            voteCandidateMapper.deleteByExample(example);
        }

    }

    @Override
    public void deleteUserVote(Integer voteId) {
        UserVoteExample example = new UserVoteExample();
        UserVoteExample.Criteria criteria = example.createCriteria();
        criteria.andVoteIdEqualTo(voteId);
        if(userVoteMapper.countByExample(example) > 0){
            userVoteMapper.deleteByExample(example);
        }

    }

    @Override
    public Integer countCandidateVote(Integer voteId, String name) {
        UserVoteExample example = new UserVoteExample();
        UserVoteExample.Criteria criteria = example.createCriteria();
        criteria.andVoteIdEqualTo(voteId);
        criteria.andNameEqualTo(name);
        return userVoteMapper.countByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int countUserVote(Integer userId, Integer voteId, String date) {
        return userVoteMapper.countUserVote(userId, voteId, date);
    }

    @Override
    public List<UserVote> find(Integer userId, Integer voteId, String name, Date voteTime) {
        UserVoteExample example = new UserVoteExample();
        UserVoteExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andVoteIdEqualTo(voteId);
        criteria.andNameEqualTo(name);
        criteria.andVoteTimeEqualTo(voteTime);
        return userVoteMapper.selectByExample(example);
    }

    @Override
    public int voteNum(Integer voteId, boolean distinct) {

        return userVoteMapper.countVotes(voteId, distinct);
    }

    private String readExcelImages(Map<String, PictureData> picData, int index) throws IOException {
        String coverImageName = "";
        String realPath = TextUtils.getConfig("post_image", this);

            Object key[] = picData.keySet().toArray();
            for (int i = 0; i < picData.size(); i++) {
                if(Integer.parseInt(key[i].toString())-1 == index){
                    PictureData pic = picData.get(key[i]);
                    coverImageName = UUID.randomUUID().toString() +  ".png" ;
                    FileUtils.writeByteArrayToFile(new File(realPath, coverImageName), pic.getData());
                    logger.info("writing image of:{} to realPath:{}", coverImageName, realPath);
                }

            }

        return TextUtils.getConfig("user_avatar_url", this) + coverImageName;
    }


    public Map<String, PictureData> getSheetPictrues07(XSSFSheet sheet) {
        Map<String, PictureData> sheetIndexPicMap = new HashMap<String, PictureData>();
        for (POIXMLDocumentPart dr : sheet.getRelations()) {
            if (dr instanceof XSSFDrawing) {
                XSSFDrawing drawing = (XSSFDrawing) dr;
                List<XSSFShape> shapes = drawing.getShapes();
                for (XSSFShape shape : shapes) {
                    XSSFPicture pic = (XSSFPicture) shape;
                    XSSFClientAnchor anchor = pic.getPreferredSize();
                    CTMarker ctMarker = anchor.getFrom();
                    String picIndex = String.valueOf(ctMarker.getRow());
                    sheetIndexPicMap.put(picIndex, pic.getPictureData());
                }
            }
        }

        return sheetIndexPicMap;
    }

}
