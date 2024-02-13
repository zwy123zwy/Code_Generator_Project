package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import com.example.backend.model.entity.Generator;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Zhangwenye
* @description 针对表【generator(代码生成器)】的数据库操作Mapper
* @createDate 2024-02-12 16:01:21
* @Entity generator.domain.Generator
*/
public interface GeneratorMapper extends BaseMapper<Generator> {
    @Select("SELECT id, distPath FROM generator WHERE isDelete = 1")
    List<Generator> listDeletedGenerator();
}




