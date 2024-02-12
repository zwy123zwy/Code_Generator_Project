package com.example.generator;
import com.example.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
* 核心生成器(未分组)
*/
public class FileGenerator {

/**
* 生成
*
* @param model 数据模型
* @throws TemplateException
* @throws IOException
*/
public static void doGenerate(Object model) throws TemplateException, IOException {
    String inputRootPath = ".source/acm-template-pro";
    String outputRootPath = "generated";

    String inputPath;
    String outputPath;

        inputPath = new File(inputRootPath, "src/com/example/acm/MainTemplate.java.ftl").getAbsolutePath();
        outputPath = new File(outputRootPath, "src/com/example/acm/MainTemplate.java").getAbsolutePath();
            DynamicFileGenerator.doGenerate(inputPath, outputPath, model);

        inputPath = new File(inputRootPath, ".gitignore").getAbsolutePath();
        outputPath = new File(outputRootPath, ".gitignore").getAbsolutePath();
            StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);

        inputPath = new File(inputRootPath, "README.md").getAbsolutePath();
        outputPath = new File(outputRootPath, "README.md").getAbsolutePath();
            StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
    }
}



