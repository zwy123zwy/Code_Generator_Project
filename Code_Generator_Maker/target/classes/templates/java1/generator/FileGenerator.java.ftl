package ${basePackage}.generator;
import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

<#--/**-->
<#--* 核心生成器(未分组)-->
<#--*/-->
<#--public class FileGenerator {-->

<#--/**-->
<#--* 生成-->
<#--*-->
<#--* @param model 数据模型-->
<#--* @throws TemplateException-->
<#--* @throws IOException-->
<#--*/-->
<#--public static void doGenerate(Object model) throws TemplateException, IOException {-->
<#--    String inputRootPath = "${fileConfig.inputRootPath}";-->
<#--    String outputRootPath = "${fileConfig.outputRootPath}";-->

<#--    String inputPath;-->
<#--    String outputPath;-->
<#--    <#list fileConfig.files as fileInfo>-->

<#--        inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();-->
<#--        outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();-->
<#--        <#if fileInfo.generateType == "static">-->
<#--            StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);-->
<#--        <#else >-->
<#--            DynamicFileGenerator.doGenerate(inputPath, outputPath, model);-->
<#--        </#if>-->
<#--    </#list>-->
<#--    }-->
<#--}-->



<#--宏定义 <#macro 宏定义方法名 参数1 参数2>-->
<#--indent为代码的空格 fileInfo为传入的参数-->
<#macro generateFile indent fileInfo>
    ${indent}inputPath = new File(inputRootPath, "${fileInfo.inputPath}").getAbsolutePath();
    ${indent}outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
    <#if fileInfo.generateType == "static">
        ${indent}StaticGenerator.copyFilesByHutool(inputPath, outputPath);
    <#else>
        ${indent}DynamicGenerator.doGenerate(inputPath, outputPath, model);
    </#if>
</#macro>

/**
* @author ${author}
* 核心生成器
*/
public class FileGenerator {
<#--重复的代码使用宏定义 -->
/**
* 核心生成器
* @param model model数据模型
* @throws IOException IOException
* @throws TemplateException TemplateException
*/
public static void doGenerate(Object model) throws IOException, TemplateException {

        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";


        String inputPath;
        String outputPath;
        <#-- 第六期 获取模型变量 -->
        <#list modelConfig.models as modelInfo>
        <#-- 有分组 -->
        <#if modelInfo.groupKey??>
                <#list modelInfo.models as subModelInfo>
                ${subModelInfo.type} ${subModelInfo.fieldName} = model.${modelInfo.groupKey}.${subModelInfo.fieldName};
                </#list>
            <#else>
                ${modelInfo.type} ${modelInfo.fieldName} = model.${modelInfo.fieldName};
        </#if>
        </#list>

        <#list fileConfig.files as fileInfo>
            <#--如果有组的条件   -->
            <#if fileInfo.groupKey??>
                // groupKey = ${fileInfo.groupKey}
            <#if fileInfo.condition??>
            if (${fileInfo.condition}) {
                <#list fileInfo.files as fileInfo>
                         <#--重复的代码逻辑 使用宏定义-->
                         <#--indent指定代码的缩进长度，传入fileInfo参数-->
                    <@generateFile fileInfo=fileInfo indent="            " />
                </#list>
            }
            <#else>
                <#list fileInfo.files as fileInfo>
                    <@generateFile fileInfo=fileInfo indent="        " />
                </#list>
            </#if>

            <#else>
            <#if fileInfo.condition??>
                if(${fileInfo.condition}) {
                    <@generateFile fileInfo=fileInfo indent="            " />
                }
            <#else>
                <@generateFile fileInfo=fileInfo indent="        " />
            </#if>
            </#if>
        </#list>
    }
}
