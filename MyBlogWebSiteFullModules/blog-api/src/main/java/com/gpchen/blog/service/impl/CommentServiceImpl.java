package com.gpchen.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gpchen.blog.dao.CommentMapper;
import com.gpchen.blog.model.entity.Comment;
import com.gpchen.blog.model.vo.CommentVo;
import com.gpchen.blog.model.vo.UserVo;
import com.gpchen.blog.model.vo.Result;
import com.gpchen.blog.model.vo.params.CommentParams;
import com.gpchen.blog.service.CommentService;
import com.gpchen.blog.service.SysUserService;
import com.gpchen.blog.util.UserThreadLocal;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result listArticleComments(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Comment::getArticleId,articleId);
        //注意，主评论 level=1;
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments=commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVos = copyList(comments);
        return Result.success(commentVos);
    }

    @Override
    public Result giveComments(CommentParams commentParams) {
        Long articleId = commentParams.getArticleId();
        String content = commentParams.getContent();
        Long parent = commentParams.getParent();
        Long toUserId = commentParams.getToUserId();

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCreateDate(System.currentTimeMillis());
        comment.setArticleId(articleId);
        comment.setAuthorId(UserThreadLocal.get().getId());
        //注意这两个必须得判断是否为空，因为如果单独新的评论的话，值为null
        comment.setParentId(parent==null? 0 : parent);
        comment.setToUid(toUserId==null ? 0:toUserId);
        //这里注意处理！！要考虑是否为parent,从而来设置level
        if(parent==null||parent==0)comment.setLevel(1);
        else comment.setLevel(2);
        this.commentMapper.insert(comment);

        CommentVo commentVo = copy(comment);


        System.out.println(commentVo.getAuthor()+"123455");
        return Result.success(commentVo);
    }

    private List<CommentVo> copyList(List<Comment> comments){
        List<CommentVo> commentVos = new ArrayList<>();
        for(Comment comment:comments){
            commentVos.add(copy(comment));
        }
        return commentVos;
    }

    private CommentVo copy(Comment comment){
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //time
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //author
        UserVo userVo = sysUserService.findUserById(comment.getAuthorId());
        commentVo.setAuthor(userVo);
        //children comments
        List<CommentVo> commentVos = getChildrenVos(comment.getId());
        commentVo.setChildrens(commentVos);
        //toUser
        if(comment.getLevel()>1){//isChildren
            commentVo.setToUser(sysUserService.findUserById(comment.getToUid()));
        }
        return commentVo;
    }

    private List<CommentVo> getChildrenVos(Long commentId){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Comment::getParentId,commentId);
        //注意，主评论 level=1;
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> comments=commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVos = copyList(comments);
        return commentVos;
    }
}
