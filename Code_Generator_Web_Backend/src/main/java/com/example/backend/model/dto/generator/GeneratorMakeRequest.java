package com.example.backend.model.dto.generator;

import com.example.maker.meta.Meta;
import lombok.Data;

import java.io.Serializable;

/**
 * 制作代码生成器请求
 *
 * @author ZWY
 */
@Data
public class GeneratorMakeRequest implements Serializable {

    /**
     * 元信息
     */
    private Meta meta;

    /**
     * 模板文件压缩包路径
     */
    private String zipFilePath;

    private static final long serialVersionUID = 1L;
}