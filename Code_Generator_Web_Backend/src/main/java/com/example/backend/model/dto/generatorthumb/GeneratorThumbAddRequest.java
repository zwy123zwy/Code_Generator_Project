package com.example.backend.model.dto.generatorthumb;

import lombok.Data;

import java.io.Serializable;

/**
 * 生成器点赞请求
 *
 * @author codeZhang
 */
@Data
public class GeneratorThumbAddRequest implements Serializable {

    /**
     * 生成器 id
     */
    private Long generatorId;

    private static final long serialVersionUID = 1L;
}