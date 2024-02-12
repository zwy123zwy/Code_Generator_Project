package com.example.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.maker.meta.Meta;
import com.example.maker.meta.enums.FileGenerateTypeEnum;
import com.example.maker.meta.enums.FileTypeEnum;
import com.example.maker.template.enums.FileFilterRangeEnum;
import com.example.maker.template.enums.FileFilterRuleEnum;
import com.example.maker.template.model.*;
import freemarker.template.Template;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模板制作工具
 */

public class TemplateMaker {
    /**
     * 工作空间的目录
     */
    public static final String WORKSPACE_DIRECTORY = ".temp";
    /**
     * 模板文件的后缀
     */
    public static final String TEMPLATE_FILE_SUFFIX = ".ftl";
    /**
     * 元信息名称
     */
    public static final String META_INFORMATION_NAME = "meta.json";

    public static Long makeTemplate(TemplateMakerConfig templateMakerConfig) {
        Long id = templateMakerConfig.getId();
        Meta meta = templateMakerConfig.getMeta();
        String originProjectPath = templateMakerConfig.getOriginProjectPath();
        TemplateMakerFileConfig fileConfig = templateMakerConfig.getFileConfig();
        TemplateMakerModelConfig modelConfig = templateMakerConfig.getModelConfig();
        TemplateMakerOutputConfig outputConfig = templateMakerConfig.getOutputConfig();
        return makeTemplate(meta, originProjectPath, fileConfig, modelConfig,outputConfig ,id);
    }

    /**
     * 生成代码模板的主要方法 ftl文件与meta.json
     *
     * @param newMeta                  元信息
     * @param originProjectPath        原始项目目录
     * @param templateMakerFileConfig  原始文件列表以及过滤配置
     * @param templateMakerModelConfig 原始模型列表以及替换配置
     * @param id                       工作空间代码存放目录
     * @return
     */
    public static long makeTemplate(Meta newMeta,
                                    String originProjectPath,
                                    TemplateMakerFileConfig templateMakerFileConfig,
                                    TemplateMakerModelConfig templateMakerModelConfig,
                                    TemplateMakerOutputConfig templateMakerOutputConfig,
                                    Long id) {
        // 没有 id 则生成,由于生成不重复的目录
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();
        }
        //        业务逻辑
        //       指定原始项目路径 移动到外面

        //当前项目目录
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + WORKSPACE_DIRECTORY;
        //工作空间的地址
        String templatePath = tempDirPath + File.separator + id;
        //        初始的文件是第一次复制，才会进行文件的实际copy操作，覆盖原文件
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originProjectPath, templatePath, true);
        }

        //    一、输入信息
        //    1、项目的基本信息 移动到外面的方法上

        //    2、输入文件信息

        // 输入文件信息，获取到项目根目录，也即工作空间中的项目目录
        String sourceRootPath = FileUtil.loopFiles(new File(templatePath), 1, null)
                .stream()
                .filter(File::isDirectory)
                .findFirst().orElseThrow(RuntimeException::new)
                .getAbsolutePath();

//        String sourceRootPath=templatePath+File.separator+FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");
        // 文件过滤配置
        List<TemplateMakerFileConfig.FileInfoConfig> fileConfigInfoList = templateMakerFileConfig.getFiles();
        // 二、生成文件模板
        //遍历输入文件
        //获取文件配置
        List<Meta.FileConfig.FileInfo> newFileInfoList = makeFileTemplates(templateMakerFileConfig,
                templateMakerModelConfig, sourceRootPath
        );
        // 获取模型配置
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = getModelInfoList(templateMakerModelConfig);
        //        生成配置文件，将meta.json的路径放在工作空间的根目录
        String metaOutputPath = templatePath + File.separator + "meta.json";
        if (FileUtil.exist(metaOutputPath)) {
            newMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath), Meta.class);
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.addAll(newModelInfoList);
            // 配置去重 文件和数据模型
            newMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            newMeta.getModelConfig().setModels(distinctModels(modelInfoList));
//            //        2输出元信息文件
//            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta),metaOutputPath);
        } else {
            // 1. 构造配置参数对象
//            Meta meta = new Meta() ;
//            meta.setName(name);
//            meta.setDescription(description);
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);

            fileInfoList.addAll(newFileInfoList);
            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.addAll(newModelInfoList);

        }

        // 额外的输出规则
        if (templateMakerOutputConfig != null) {
            if (templateMakerOutputConfig.isRemoveGroupFilesFromRoot()){
                List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
                newMeta.getFileConfig().setFiles(TemplateMakerUtils.removeGroupFilesFromRoot(fileInfoList));
            }
        }
//        2输出元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        return id;
    }

    /**
     * 获取模型配置
     *
     * @param templateMakerModelConfig
     * @return
     */
    private static List<Meta.ModelConfig.ModelInfo> getModelInfoList(TemplateMakerModelConfig templateMakerModelConfig) {
        // 处理模型信息
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>();
        // 非空校验
        if (templateMakerModelConfig == null) {
            return newModelInfoList;
        }
        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();
        if (CollUtil.isEmpty(models)) {
            return newModelInfoList;
        }
        List<Meta.ModelConfig.ModelInfo> inputModelInfoList = models.stream().map(
//                modelInfoConfig ->==>modelInfo
                modelInfoConfig -> {
                    Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
                    BeanUtil.copyProperties(modelInfoConfig, modelInfo);
                    return modelInfo;
                }).collect(Collectors.toList());


//        数据模型分组
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if (modelGroupConfig != null) {
//            String condition = modelGroupConfig.getCondition();
//            String groupKey = modelGroupConfig.getGroupKey();
//            String groupName = modelGroupConfig.getGroupName();
            Meta.ModelConfig.ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelGroupConfig, groupModelInfo);
//            groupModelInfo.setCondition(condition);
//            groupModelInfo.setGroupName(groupName);
//            groupModelInfo.setGroupKey(groupKey);
            //文件全放在一个分组内,先把多个合成一个交给上级// 模型全放到一个分组内
            groupModelInfo.setModels(inputModelInfoList);
//            newModelInfoList = new ArrayList<>();
            newModelInfoList.add(groupModelInfo);
        } else {
            // 不分组九江内容放在列表内
            newModelInfoList.addAll(inputModelInfoList);
        }
        return newModelInfoList;
    }

    /**
     * 用于生成多个文件（包括分组）
     *
     * @param templateMakerFileConfig
     * @param templateMakerModelConfig
     * @param sourceRootPath
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> makeFileTemplates(TemplateMakerFileConfig templateMakerFileConfig,
                                                                    TemplateMakerModelConfig templateMakerModelConfig,
                                                                    String sourceRootPath) {
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        // 非空校验
        if (templateMakerFileConfig == null) {
            return newFileInfoList;
        }
        List<TemplateMakerFileConfig.FileInfoConfig> fileConfigInfoList = templateMakerFileConfig.getFiles();

        if (CollUtil.isEmpty(fileConfigInfoList)) {
            return newFileInfoList;
        }
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileConfigInfoList) {
            String inputFilePath = fileInfoConfig.getPath();
//            String inputFileAbsolutePath = FileUtil.normalize(sourceRootPath + File.separator + inputFilePath);

            if (!inputFilePath.startsWith(sourceRootPath)) {
                inputFilePath = sourceRootPath + File.separator + inputFilePath;
            }
            //传入的是绝对路径，得到最终过滤完成的文件列表
            List<File> fileList = FileFilter.doFilter(inputFilePath, fileInfoConfig.getFilterConfigList());
//            对已经生成的ftl文件不修改
            fileList = fileList.stream()
                    .filter(file -> !file.getAbsolutePath().endsWith(TEMPLATE_FILE_SUFFIX))
                    .collect(Collectors.toList());

            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate( file,fileInfoConfig,templateMakerModelConfig, sourceRootPath);
                newFileInfoList.add(fileInfo);
            }

        }

        // 获取文件组
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if (fileGroupConfig != null) {
            String condition = fileGroupConfig.getCondition();
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();
            // 新增组配置
            Meta.FileConfig.FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
            groupFileInfo.setCondition(condition);
            groupFileInfo.setGroupName(groupName);
            groupFileInfo.setGroupKey(groupKey);
            groupFileInfo.setType(FileTypeEnum.GROUP.getValue());
            //文件全放在一个分组内,先把多个合成一个交给上级
            groupFileInfo.setFiles(newFileInfoList);
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);
        }
        return newFileInfoList;
    }

    /**
     * 生成单个文件
     *
     * @param sourceRootPath
     * @param inputFile
     * @return
     * @author ZWY
     */
    private static Meta.FileConfig.FileInfo makeFileTemplate(
                                                             File inputFile,
                                                             TemplateMakerFileConfig.FileInfoConfig fileInfoConfig,
                                                             TemplateMakerModelConfig templateMakerModelConfig,
                                                             String sourceRootPath) {

        // 注意 win 系统需要对路径进行转义,传入的文件路径是绝对路径，需要改为绝对路径
        String fileInputAbsolutePath = inputFile.getAbsolutePath().replaceAll("\\\\", "/");
        String fileOutputAbsolutePath = fileInputAbsolutePath + TEMPLATE_FILE_SUFFIX;

        // 文件输入输出相对路径（用于生成配置）
        String fileInputPath = fileInputAbsolutePath.replace(sourceRootPath + "/", "");

        String fileOutputPath = fileInputPath + TEMPLATE_FILE_SUFFIX;
        //    二、使用字符串提换、生成模板文件
        // 用于保存之前数据模型的内容
        String fileContent;
        //        输出模板文件
        //        如果已经存在ftl文件，表明不是第一次制作
        boolean hasTemplateFile = FileUtil.exist(fileOutputAbsolutePath);
        if (hasTemplateFile) {
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        } else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }
        //        3、输入模型参数信息

//        支持多个模型，对同一个文件的内容，遍历模型进行多轮替换
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        // 最新替换数据模型内容的容器
        String newFileContent = fileContent;
        String replacement;

        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();
        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : models) {
            //不是分组

            if (modelGroupConfig == null) {
                replacement = String.format("${%s}", modelInfoConfig.getFieldName());
            } else {
                // 存在分组
                String groupKey = modelGroupConfig.getGroupKey();
                replacement = String.format("${%s.%s}", groupKey, modelInfoConfig.getFieldName());
            }
            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), replacement);
        }
// 路径替换
        TemplateMakerModelConfig.ModelInfoConfig fileDirPathConfig = templateMakerModelConfig.getFileDirPathConfig();
        if(fileDirPathConfig!=null){
            String[] inputPathAndFileSuffix = fileInputPath.split("\\.");
            if(inputPathAndFileSuffix.length > 1){
                fileInputPath =
                        inputPathAndFileSuffix[0].replace("/", ".")
                                .replace(fileDirPathConfig.getReplaceText(), "{" + fileDirPathConfig.getFieldName() + "}")
                                .replace(".", "/");
                for (int i = 1; i < inputPathAndFileSuffix.length; i++) {
                    fileInputPath += "." + inputPathAndFileSuffix[i];
                }
            }

        }

        //文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileOutputPath);
        fileInfo.setOutputPath(fileInputPath);
        fileInfo.setCondition(fileInfoConfig.getCondition());
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());

        boolean contentEquals = newFileContent.equals(fileContent);
        //之前不存在模板文件，并且没有更改内容，静态生成
        if (!hasTemplateFile) {
            if (contentEquals) {
                //输入路径没有ftl后缀
                fileInfo.setInputPath(fileInputPath);
                fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
            } else {
                FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
            }
        }else if (!contentEquals) {
            // 有模板文件，且增加了新坑，生成模板文件
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }

//        判断是否存在ftl文件使用何种方法
//         以下代码存在bug，会将二次动态生成文件类型改变静态的
//        if(newFileContent.equals(fileContent)){
//            fileInfo.setOutputPath(fileInputPath);
//            fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
//        }else{
//            fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
//            FileUtil.writeUtf8String(newFileContent,fileOutputAbsolutePath);
//        }
//        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());

        return fileInfo;
    }
//

    /**
     * 模型去重
     *
     * @param modelInfoList
     * @return
     */
    private static List<Meta.ModelConfig.ModelInfo> distinctModels(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
//        ArrayList<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(modelInfoList.stream().collect(
//                //把列表转换为map
//                Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (exist, replaceMent) -> replaceMent)
//        ).values());
//        return newModelInfoList;
        // 策略：同分组内模型 merge，不同分组保留

        // 1. 有分组的，以组为单位划分
        Map<String, List<Meta.ModelConfig.ModelInfo>> groupKeyModelInfoListMap = modelInfoList
                .stream()
                .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey)
                );


        // 2. 同组内的模型配置合并
        // 保存每个组对应的合并后的对象 map
        Map<String, Meta.ModelConfig.ModelInfo> groupKeyMergedModelInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.ModelConfig.ModelInfo>> entry : groupKeyModelInfoListMap.entrySet()) {
            List<Meta.ModelConfig.ModelInfo> tempModelInfoList = entry.getValue();
            List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(tempModelInfoList.stream()
                    .flatMap(modelInfo -> modelInfo.getModels().stream())
                    .collect(
                            Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (exist, replaceMent) -> replaceMent)
                    ).values());

            // 使用新的 group 配置
            Meta.ModelConfig.ModelInfo newModelInfo = CollUtil.getLast(tempModelInfoList);
            newModelInfo.setModels(newModelInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedModelInfoMap.put(groupKey, newModelInfo);
        }

        // 3. 将模型分组添加到结果列表
        List<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(groupKeyMergedModelInfoMap.values());

        // 4. 将未分组的模型添加到结果列表
        List<Meta.ModelConfig.ModelInfo> noGroupModelInfoList = modelInfoList.stream().filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupModelInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (exist, replaceMent) -> replaceMent)
                ).values()));
        return resultList;
    }

    /**
     * 文件去重
     *
     * @param fileInfoList
     * @return
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
//        ArrayList<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(fileInfoList.stream().collect(
//                //把列表转换为map
//                Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (exist, replaceMent) -> replaceMent)
//        ).values());
//        return newFileInfoList;
        // 策略：同分组内文件 merge，不同分组保留   想把所有groupKey相同的合并在一起

        // 1. 有分组的，以组为单位划分
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoListMap = fileInfoList
                .stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey)
                );


        // 2. 同组内的文件配置合并
        // 保存每个组对应的合并后的对象 map
        //合并后的信息容器
        Map<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()) {
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            // [1,2,2,3]
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    //一个对象转化为多个对象
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
                    .collect(
                            Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath, o -> o, (exist, replaceMent) -> replaceMent)
                    ).values());
            //[1,2,3]
            // 使用新的 group 配置,最终得到的合并具体的配置信息
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            // 合并的文件列表信息
            newFileInfo.setFiles(newFileInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey, newFileInfo);
        }
//
        // 3. 将文件分组添加到结果列表
        List<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());

        // 4. 将未分组的文件添加到结果列表
        List<Meta.FileConfig.FileInfo> noGroupFileInfoList = fileInfoList.stream().filter(
                        // groupkey为空
                        fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());

        resultList.addAll(new ArrayList<>(noGroupFileInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath, o -> o, (exist, replaceMent) -> replaceMent)
                ).values()));
        return resultList;
    }

    public static void main(String[] args) {
        // 工作空间隔离：1.指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "demoProjects/acm-template";

        // 工作空间隔离：2.复制目录
        long id = IdUtil.getSnowflakeNextId();
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(templatePath);
        }
        FileUtil.copy(originProjectPath,templatePath,true);
        // 一、输入信息
        // 1. 输入项目基本信息
        String name = "acm-template-pro-generator";
        String description = "ACM 示例模板生成器";

        // 2. 输入文件信息
//        String projectPath = System.getProperty("user.dir");
        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();

        // 注意 win 系统需要对路径进行转义
        sourceRootPath = sourceRootPath.replaceAll("\\\\", "/");

        String fileInputPath = "src/com/example/acm/MainTemplate.java";
        String fileOutputPath = fileInputPath + ".ftl";

        // 3. 输入模型参数信息
        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("outputText");
        modelInfo.setType("String");
        modelInfo.setDescription("mySum = ");

        // 二、使用字符串替换，生成模板文件
        String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        // 读取原文件
        String fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        String replacement = String.format("${%s}", modelInfo.getFieldName());
        // 替换新内容
        String newFileContent = StrUtil.replace(fileContent,"Sum: ",replacement);

        // 输出模板文件
        String fileOutputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;
        FileUtil.writeUtf8String(newFileContent,fileOutputAbsolutePath);

        // 三、生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";

        // 1. 构造配置参数
        Meta meta = new Meta();
        meta.setName(name);
        meta.setDescription(description);

        Meta.FileConfig fileConfig = new Meta.FileConfig();
        meta.setFileConfig(fileConfig);
        fileConfig.setSourceRootPath(sourceRootPath);
        List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
        fileConfig.setFiles(fileInfoList);
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        fileInfoList.add(fileInfo);

        Meta.ModelConfig modelConfig = new Meta.ModelConfig();
        meta.setModelConfig(modelConfig);
        List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
        modelConfig.setModels(modelInfoList);
        modelInfoList.add(modelInfo);

        // 2. 输出元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(meta),metaOutputPath);
//        String name = "acm-template-generator";
//        String description = "ACM 示例模板生成器";
//        Meta meta = new Meta();
//        meta.setName(name);
//        meta.setDescription(description);
//        String projectPath = System.getProperty("user.dir");
//        String originProjectPath = FileUtil.getAbsolutePath(new File(projectPath).getParent() + File.separator + "demoProjects/springboot-init");
//        String fileInputPath1 = "src/main/java/com/example/springbootinit/common";
//        String fileInputPath2 = "src/main/java/com/example/springbootinit/constant";
//        String fileInputPath3 = "src/main/java/com/example/springbootinit/controller";
//        String fileInputPath4 = "src/main/resources/application.yml";
//        List<String> inputFileList = Arrays.asList(fileInputPath1, fileInputPath2);
////        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
////        modelInfo.setFieldName("outputText");
////        modelInfo.setType("String");
////        modelInfo.setDefaultValue("sum = ");
//
//        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
//        modelInfo.setFieldName("className");
//        modelInfo.setType("String");
//
////        String searchStr="Sum: ";B
//        String searchStr = "BaseResponse";
//        // 文件分组
//        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
//        fileInfoConfig1.setPath(fileInputPath1);
//        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
//        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
//                .range(FileFilterRangeEnum.FILE_NAME.getValue())
//                .rule(FileFilterRuleEnum.CONTAINS.getValue())
//                .value("Base")
//                .build();
//        fileFilterConfigList.add(fileFilterConfig);
//        fileInfoConfig1.setFilterConfigList(fileFilterConfigList);
//
//        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
//        fileInfoConfig2.setPath(fileInputPath2);
//        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig3 = new TemplateMakerFileConfig.FileInfoConfig();
//        fileInfoConfig3.setPath(fileInputPath3);
//        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = Arrays.asList(fileInfoConfig1, fileInfoConfig3);
//        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
//        // 在分组是变成了下级的file
//        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1, fileInfoConfig2, fileInfoConfig3));
//
//
//        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
//        fileGroupConfig.setCondition("output===");
//        fileGroupConfig.setGroupName("测试分组2");
//        fileGroupConfig.setGroupKey("dssd顶顶顶");
//        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);
//        //模型分组
//        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
//        // 模型分组配置
//        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
//        modelGroupConfig.setGroupKey("mysql");
//        modelGroupConfig.setGroupName("数据库配置");
//        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);
//        // 具体字段的配置信息
//        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
//
//        modelInfoConfig1.setFieldName("className");
//        modelInfoConfig1.setType("String");
//        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my db");
//        modelInfoConfig1.setReplaceText("BaseResponse");
//
//        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
//        modelInfoConfig2.setFieldName("username");
//        modelInfoConfig2.setType("string");
//        modelInfoConfig2.setDefaultValue("root");
//        modelInfoConfig2.setReplaceText("root");
//        templateMakerModelConfig.setModels(Arrays.asList(modelInfoConfig1, modelInfoConfig2));
//        TemplateMaker.makeTemplate(meta, originProjectPath, templateMakerFileConfig, templateMakerModelConfig, 2L);
    }


}
