package com.example.maker.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.example.maker.meta.Meta;
import com.example.maker.template.enums.FileFilterRangeEnum;
import com.example.maker.template.enums.FileFilterRuleEnum;
import com.example.maker.template.model.*;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TemplateMakerTest {

    @Test
    public void makeTemplate1() {
        String name="acm-template-generator";
        String description="ACM 示例模板生成器";
        Meta meta = new Meta() ;
        meta.setName(name);
        meta.setDescription(description);
        String projectPath=System.getProperty("user.dir");
        String originProjectPath= FileUtil.getAbsolutePath(new File(projectPath).getParent()+ File.separator+"demoProjects/springboot-init");
//        String fileInputPath1="src/main/resources/application.yml";
        String fileInputPath1="src/main/java/com/example/springbootinit/common";
        String fileInputPath2="src/main/java/com/example/springbootinit/constant";
        String fileInputPath3="src/main/java/com/example/springbootinit/controller";
        String fileInputPath4="src/main/resources/application.yml";



//        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
//        modelInfo.setFieldName("className");
//        modelInfo.setType("String");
//        String searchStr="BaseResponse";
        // 文件分组
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig1.setPath(fileInputPath1);
        //文件过滤配置
//        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
//        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
//                .range(FileFilterRangeEnum.FILE_NAME.getValue())
//                .rule(FileFilterRuleEnum.CONTAINS.getValue())
//                .value("Base")
//                .build();
//        fileFilterConfigList.add(fileFilterConfig);
//        fileInfoConfig1.setFilterConfigList(fileFilterConfigList);

        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(fileInputPath2);
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig3 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig3.setPath(fileInputPath3);

        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        // 在分组是变成了下级的file
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1));


//        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
//        fileGroupConfig.setCondition("output===");
//        fileGroupConfig.setGroupName("测试分组2");
//        fileGroupConfig.setGroupKey("dssd顶顶顶");
//        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);

        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        // 模型分组配置
//        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
//        modelGroupConfig.setGroupKey("mysql");
//        modelGroupConfig.setGroupName("数据库配置");
//        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);
        // 具体字段的配置信息
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();

        modelInfoConfig1.setFieldName("className");
        modelInfoConfig1.setType("String");
//        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("BaseResponse");

//        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
//        modelInfoConfig2.setFieldName( "username");
//        modelInfoConfig2.setType("string");
//        modelInfoConfig2.setDefaultValue("root");
//        modelInfoConfig2.setReplaceText("root");
        templateMakerModelConfig.setModels(Arrays.asList(modelInfoConfig1));
        TemplateMaker.makeTemplate(meta, originProjectPath,templateMakerFileConfig,templateMakerModelConfig,new TemplateMakerOutputConfig(),2L);
    }
    @Test
    public void test(){
        String rootPath="examples/springboot-init/";
        String configStr= ResourceUtil.readUtf8Str(rootPath+"templateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        Long id = TemplateMaker.makeTemplate(templateMakerConfig);
        //用户不断追加信息编辑
        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker1.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker2.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker3.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker4.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker5.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker6.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker7.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);
        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker8.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        System.out.println(id);


    }
    @Test
    public void makeSpringBootTemplateTest() {
        String rootPath = "examples/springboot-init-plus/";
        String configStr = ResourceUtil.readUtf8Str(rootPath + "TemplateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        templateMakerConfig.setId(3L);
        long id = TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "TemplateMaker1.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        templateMakerConfig.setId(3L);
        TemplateMaker.makeTemplate(templateMakerConfig);
//
//        configStr = ResourceUtil.readUtf8Str(rootPath + "TemplateMaker2.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        templateMakerConfig.setId(2L);
//        TemplateMaker.makeTemplate(templateMakerConfig);
//
//        configStr = ResourceUtil.readUtf8Str(rootPath + "TemplateMaker3.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        templateMakerConfig.setId(2L);
//        TemplateMaker.makeTemplate(templateMakerConfig);
//
//        configStr = ResourceUtil.readUtf8Str(rootPath + "TemplateMaker4.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        templateMakerConfig.setId(2L);
//        TemplateMaker.makeTemplate(templateMakerConfig);
//
//
//        configStr = ResourceUtil.readUtf8Str(rootPath + "TemplateMaker5.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        templateMakerConfig.setId(2L);
//        TemplateMaker.makeTemplate(templateMakerConfig);
//
//
//        configStr = ResourceUtil.readUtf8Str(rootPath + "TemplateMaker6.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        templateMakerConfig.setId(2L);
//        TemplateMaker.makeTemplate(templateMakerConfig);
//
//        configStr = ResourceUtil.readUtf8Str(rootPath + "TemplateMaker7.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        templateMakerConfig.setId(2L);
//        TemplateMaker.makeTemplate(templateMakerConfig);
//
//        configStr = ResourceUtil.readUtf8Str(rootPath + "TemplateMaker8.json");
//        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
//        templateMakerConfig.setId(2L);
//        TemplateMaker.makeTemplate(templateMakerConfig);



        System.out.println(id);
    }
}