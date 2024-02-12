package com.example.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootinit.model.dto.post.PostQueryRequest;
import com.example.springbootinit.model.entity.Post;

/**
 * ���ӷ���
 */
public interface PostService extends IService<Post> {

    /**
     * ��ȡ��ѯ����
     *
     * @param postQueryRequest
     * @return
     */
    QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest);

    /**
     * �� ES ��ѯ
     *
     * @param postQueryRequest
     * @return
     */
    Page<Post> searchFromEs(PostQueryRequest postQueryRequest);
}
