package com.example.model;

import lombok.Data;


/**
 * 数据模型
 */
@Data
public class DataModel {

    /**
     * 是否生成 .gitignore文件
     */
    public boolean needGit = true;

    /**
     * 是否生成循环
     */
    public boolean loop = false;

    /**
     * 作者注释
     */
    public String author = "example";

    /**
     * 输出信息
     */
    public String outputText = "sum = ";
}