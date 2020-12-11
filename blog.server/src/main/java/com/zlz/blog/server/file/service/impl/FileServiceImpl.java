package com.zlz.blog.server.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlz.blog.common.entity.article.BlogArticle;
import com.zlz.blog.common.entity.article.BlogContent;
import com.zlz.blog.common.entity.file.BlogFile;
import com.zlz.blog.common.exception.BlogException;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.common.util.WebUtil;
import com.zlz.blog.server.article.mapper.ArticleMapper;
import com.zlz.blog.server.article.service.ArticleContentService;
import com.zlz.blog.server.file.mapper.FileMapper;
import com.zlz.blog.server.file.service.FileService;
import com.zlz.fastdfs.util.FastdfsUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020/1/13 13:59
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FastdfsUtil fastdfsUtil;
    @Resource
    private ArticleContentService articleContentService;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private FileMapper fileMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultSet uploadImage(MultipartFile file) throws IOException, NoSuchAlgorithmException {

        MessageDigest md5I = MessageDigest.getInstance("MD5");
        md5I.update(file.getBytes());
        BigInteger bi = new BigInteger(1, md5I.digest());
        String md5 = bi.toString(16);
        String fileName = file.getOriginalFilename();

        QueryWrapper<BlogFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        BlogFile blogFile1 = fileMapper.selectOne(queryWrapper);

        if (null != blogFile1) {
            return ResultSet.success("上传成功", blogFile1.getFileUrl());
        }

        int base = 200 * 1024;
        float quality = base / file.getSize();
        if (quality > 1) {
            quality = 1;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream()).size(800, 600).outputQuality(quality).toOutputStream(out);
        byte[] bytes = out.toByteArray();

        String path = WebUtil.uploadFile(bytes, fileName, fastdfsUtil);
        if (StringUtils.isEmpty(path)) {
            throw new BlogException("文件上传失败");
        }
        BlogFile blogFile = new BlogFile();
        blogFile.setFileName(fileName);
        assert fileName != null;
        String[] split = fileName.split("\\.");
        if (split.length > 1) {
            blogFile.setFileType(split[split.length - 1]);
        }
        blogFile.setFileUrl(path);
        blogFile.setMd5(md5);
        blogFile.setFileSize(String.valueOf(bytes.length));
        fileMapper.insert(blogFile);
        return StringUtils.isEmpty(path) ? ResultSet.error("上传失败") : ResultSet.success("上传成功", path);
    }

    @Override
    public void downloadFile(String path, HttpServletResponse response) {
        WebUtil.downloadFile(path, response, fastdfsUtil);
    }

    @Override
    public ResultSet getMdString(HttpServletResponse response, HttpServletRequest request) {
        return null;
    }

    @Override
    public void downloadMd(Long id, HttpServletResponse response, HttpServletRequest request) {

        //查询文章正文信息
        BlogArticle blogArticle = getBlogArticle(id, request);
        BlogContent blogContent = blogArticle.getBlogContent();

        if (StringUtils.isEmpty(blogContent.getContentMd())) {
            throw new BlogException("未获取到文章信息,请先编辑此文章");
        }
        byte[] bytes = blogContent.getContentMd().getBytes();

        //设置文件名并写入到response输出流
        String title = blogArticle.getTitle();
        WebUtil.downloadFile(title + ".md", bytes, response);
    }

    @Override
    public void downloadHtml5(Long id, HttpServletResponse response, HttpServletRequest request) {

        //查询文章正文信息
        BlogArticle blogArticle = getBlogArticle(id, request);
        BlogContent blogContent = blogArticle.getBlogContent();

        if (StringUtils.isEmpty(blogContent.getContentMd())) {
            throw new BlogException("未获取到文章信息,请先编辑此文章");
        }
        byte[] bytes = blogContent.getContentHtml().getBytes();

        //设置文件名并写入到response输出流
        String title = blogArticle.getTitle();
        WebUtil.downloadFile(title + ".html", bytes, response);
    }

    /**
     * 查询文章title，和正文信息
     *
     * @param id      id
     * @param request request
     * @return BlogArticle
     */
    private BlogArticle getBlogArticle(Long id, HttpServletRequest request) {

        //检查数据
        if (null == id) {
            throw new BlogException("获取文章信息失败");
        }

        //设置查询条件
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).select("id", "title").last("limit 1");
        BlogArticle blogArticle = articleMapper.selectOne(queryWrapper);

        if (null == blogArticle) {
            throw new BlogException("获取文章信息失败");
        }

        //查询文章正文信息
        ResultSet resultSet = articleContentService.queryBody(id, request);
        if (!ResultSet.isSuccess(resultSet)) {
            throw new BlogException("未获取到文章信息,请先编辑此文章");
        }
        BlogContent blogContent = (BlogContent) resultSet.getEntity();
        blogArticle.setBlogContent(blogContent);

        return blogArticle;
    }


    public static void main(String[] args) {
        //本身
        Data data1 = new Data("1", "0","0");
        //子集
        Data data2 = new Data("2", "1","1");
        Data data3 = new Data("3", "1","1");
        Data data4 = new Data("4", "2","2,1");
        Data data5 = new Data("5", "3","3,1");
        Data data6 = new Data("6", "3","3,1");
        Data data7 = new Data("7", "6","6,3,1");
        Data data8 = new Data("8", "6","6,3,1");
        Data data9 = new Data("9", "8","8,6,3,1");
        Data data10 = new Data("10", "9","9,8,6,3,1");
        Data data11 = new Data("11", "10","10,9,8,6,3,1");
        List<Data> list = new ArrayList<>();
        list.add(data2);list.add(data3);list.add(data4);list.add(data5);list.add(data6);list.add(data7);
        list.add(data8);list.add(data9);list.add(data10);list.add(data11);

        //树形结构储存
        List<Tree<Data>> currentTrees = new ArrayList<>();

        Map<String, List<Tree<Data>>> childrenTree = new HashMap<>();

        //创建树形结构数据
        for (Data data : list) {
            //数据转换
            setParentIds(data);

            //创建树形结构
            Tree<Data> dataTree = new Tree<>(data, data.getId());
            //使用自己的id作为key，方便等下处理上下级关系
            currentTrees.add(dataTree);

            String parentId = data.getParentId();
            List<Tree<Data>> children = childrenTree.computeIfAbsent(parentId, k -> new ArrayList<>());
            children.add(dataTree);
        };

        Tree<Data> top = new Tree<>(data1, data1.getId());
        currentTrees.add(top);
        //遍历所有的数据，获取它的子数据
        for (Tree<Data> tree : currentTrees) {
            tree.setChildren(childrenTree.get(tree.getData().getId()));
        }
        System.out.println(top);
    }

    private static void setParent(){
        
    }

    private static void setParentIds(Data data){
        String ids = data.getParentIds();
        if(StringUtils.isEmpty(ids)){
            data.setParentIdsList(new String[]{});
            return;
        }
        data.setParentIdsList(ids.split(","));
    }
}

class Tree<T>{

    private String id;

    private T data;

    private List<Tree<Data>> children = new ArrayList<>();

    public Tree(T data, String id) {
        this.data = data;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<Tree<Data>> getChildren() {
        return children;
    }

    public void setChildren(List<Tree<Data>> children) {
        this.children = children;
    }
}

class Data{
    private String id;

    private String parentId;

    private String parentIds;

    private String[] parentIdsList;

    public Data(String id, String parentId, String parentIds) {
        this.id = id;
        this.parentId = parentId;
        this.parentIds = parentIds;
    }

    public String[] getParentIdsList() {
        return parentIdsList;
    }

    public void setParentIdsList(String[] parentIdsList) {
        this.parentIdsList = parentIdsList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", parentIds=" + parentIds +
                '}';
    }
}
