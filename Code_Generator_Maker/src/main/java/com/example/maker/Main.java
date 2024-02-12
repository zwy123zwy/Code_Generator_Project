package com.example.maker;

import com.example.maker.generator.main.MainGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        MainGenerator generator = new MainGenerator();
        generator.doGenerate();
//        CommandExecutor commandExecutor = new CommandExecutor();
//        args = new String[]{"generate"，"-["};commandExecutor.doExecute(args);
//        CommandExecutor executor = new CommandExecutor()

        //        //这里获取metaJson生成的唯一实例对象
//        Meta meta = MetaManager.getMetaObject();
//        System.out.println(meta);
//        //        输出的根路径 ，在不同项目下打开可能导致位置发生变化
//        String projectPath = System.getProperty("user.dir");
//        String outputPath = projectPath+ File.separator+"generated";
//        // 不存在就创建
//        if(!FileUtil.exist(outputPath)){
//            FileUtil.mkdir(outputPath);
//        }
////        其实很简单，核心思路是“把绝对路径改为相对路径”。只需要把代码生成器依赖的模板文件移动到生成后的代码生成器目录下，
////        比如 .source/项目名 ，然后在生成器中，就可以通过相对路径找到模板文件
//        // 从原始模板文件路径复制到生成的代码包中
//        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
//        String sourceCopyDestPath = outputPath + File.separator +".source";
//        FileUtil.copy(sourceRootPath, sourceCopyDestPath, false);
//        //读取resource目录,这里与源代码存在分歧
//        ClassPathResource resource = new ClassPathResource(" ");
////        System.out.println(resource.toString());
////        System.out.println(resource.getAbsolutePath());
////        System.out.println(resource.getPath().toString());
////        String inputResourcePath =resource.getAbsolutePath();
//        String inputResourcePath = resource.getPath();
//
//        System.out.println(inputResourcePath);
//        //  读取java包的路径
//        String outputBasePackage = meta.getBasePackage();
//        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
//        String outputBaseJavaPackagePath = outputPath+ File.separator+"src/main/java/"+outputBasePackagePath;
//
//
////         dataModel
//        String  inputFilePath =inputResourcePath+File.separator+"templates/java/model/DataModel.java.ftl";
//        String outputFilePath =outputBaseJavaPackagePath+"/model/DataModel.java";
//        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath,meta);
//        // command ListCommand
//        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command" + File.separator + "ListCommand.java.ftl";
//        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/command/ListCommand.java";
//        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
////        command ConfigCommand
//        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command" + File.separator + "ConfigCommand.java.ftl";
//        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/command/ConfigCommand.java";
//        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
//        //command GenerateCommand
////        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command" + File.separator + "GenerateCommand.java.ftl";
////        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/command/GenerateCommand.java";
////        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
//        //command JsonGenerateCommand
////        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command" + File.separator + "JsonGenerateCommand.java.ftl";
////        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/command/JsonGenerateCommand.java";
////        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
//
//
////        // util ConsoleUtil
////        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/util" + File.separator + "ConsoleUtil.java.ftl";
////        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/util/ConsoleUtil.java";
////        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
////
////
////        // util ReflexUtil
////        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/util" + File.separator + "ReflexUtil.java.ftl";
////        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/util/ReflexUtil.java";
////        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
////
////
////        // util ConvertUtil
////        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/util" + File.separator + "ConvertUtil.java.ftl";
////        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/util/ConvertUtil.java";
////        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
//
//
//
//        // cli CommandExecutor
//        inputFilePath = inputResourcePath + File.separator + "templates/java/cli" + File.separator + "CommandExecutor.java.ftl";
//        outputFilePath = outputBaseJavaPackagePath + File.separator + "/cli/CommandExecutor.java";
//        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
//
//
//        // java Main
//        inputFilePath = inputResourcePath + File.separator + "templates/java" + File.separator + "Main.java.ftl";
//        outputFilePath = outputBaseJavaPackagePath + File.separator + "/Main.java";
//        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
//
//        // generator StaticGenerator
//        inputFilePath = inputResourcePath + File.separator + "templates/java/generator" + File.separator + "StaticFileGenerator.java.ftl";
//        outputFilePath = outputBaseJavaPackagePath + File.separator + "/generator/StaticGenerator.java";
//        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
//
//        // generator DynamicGenerator
//        inputFilePath = inputResourcePath + File.separator + "templates/java/generator" + File.separator + "DynamicFileGenerator.java.ftl";
//        outputFilePath = outputBaseJavaPackagePath + File.separator + "/generator/DynamicGenerator.java";
//        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
//
//        // generator MainGenerator
//        inputFilePath = inputResourcePath + File.separator + "templates/java/generator" + File.separator + "FileGenerator.java.ftl";
//        outputFilePath = outputBaseJavaPackagePath + File.separator + "/generator/MainGenerator.java";
//        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
//
//        // pom.xml
//        inputFilePath = inputResourcePath + File.separator + "templates/pom.xml.ftl";
//        outputFilePath = outputPath + File.separator + "pom.xml";
//        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
//        // readme.md
////        inputFilePath = inputResourcePath + File.separator + "templates/README.md.ftl";
////        outputFilePath = outputPath + File.separator + "README.md";
////        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
//
//        System.out.println("文件输出完毕");
//        // 构建jar包
//        JarGenerator.doGenerate(outputPath);
//        String shellOutputFilePath = outputPath + File.separator + "generator";
//        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(),meta.getVersion());
//        String jarPath="target/" + jarName;
//        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);
//
//
//
//        // 生成精简版的程序(产物包)
//        String distOutputpath = outputPath + "-dist";// - 拷贝 jar 包
//        String targetAbsolutePath = distOutputpath + File.separator + "target";
//        FileUtil.mkdir(targetAbsolutePath);
//        String jarAbsolutePath = outputPath + File.separator + jarPath;
//        FileUtil.copy(jarAbsolutePath,targetAbsolutePath,true);
//// - 拷贝脚本文件
//        FileUtil.copy(shellOutputFilePath, distOutputpath, true);
//        FileUtil.copy(shellOutputFilePath + ".bat", distOutputpath, true);//  拷贝源模板文件
//        FileUtil.copy(sourceCopyDestPath, distOutputpath, true);
//        System.out.println("Hello world!");
    }
}
