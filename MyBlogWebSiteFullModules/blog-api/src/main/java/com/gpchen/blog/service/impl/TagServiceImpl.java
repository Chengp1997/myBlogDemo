package com.gpchen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.gpchen.blog.dao.TagMapper;
import com.gpchen.blog.model.entity.Tag;
import com.gpchen.blog.service.TagService;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.model.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public Result getHottestTag(int limit) {
        List<Long> tagsId = tagMapper.getHottestTagIds(limit);
        //这里需要判断是否为空，如果为空，查找list的语句里会报错
        if(CollectionUtils.isEmpty(tagsId)){
            return Result.success(Collections.emptyList());
        }
        List<TagVo> tagVoList=copyList(tagMapper.selectBatchIds(tagsId));
        return Result.success(tagVoList);
    }

    @Override
    public Result listAllTags() {
        List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<Tag>());
        List<TagVo> tagVos = copyList(tags);

        return Result.success(tagVos);
    }

    @Override
    public Result getTagsDetail() {
        return Result.success(copyList(tagMapper.selectList(new LambdaQueryWrapper<Tag>())));
    }

    @Override
    public Result getTagDetailsById(String tagId) {
        return Result.success(copy(tagMapper.selectById(tagId)));
    }

    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        List<Tag> tags =tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    private List<TagVo> copyList(List<Tag> tags) {
        List<TagVo> tagVos = new ArrayList<>();
        for(Tag tag:tags){
            tagVos.add(copy(tag));
        }
        return tagVos;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo=new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
}
