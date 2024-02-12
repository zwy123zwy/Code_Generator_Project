# ${name}

> ${description}
>
> 作者：${author}
>
> 版本: ${version}
>
> 创建时间: ${.now}
>
> 源码地址: [定制化代码生成项目](https://github.com/zwy123zwy/cloudCode)
>
> ${name} 旨在简化模板生成过程,提供快速、高效的代码生成方式，使开发人员能够更专注于业务逻辑和功能开发，用户通过命令行界面交互 通过几个参数就能生成定制化的代码。
>
> 当然，如果你喜欢 ${name} 的话，不妨给我的项目点个 star ⭐️！


## 项目使用

### 项目使用说明

需要执行相关脚本文件，需要安装 java 1.8+ 版本


如在项目目录下执行脚本文件

linux环境
```bash ./generator
```

windows环境
```bash
./generator.bat
```
否则需要拼全脚本路径
```
bash  路径/generator <命令> <选项参数>
```

示例命令：

```
generator generate <#list modelConfig.models as modelInfo>-${modelInfo.abbr} </#list>
```

## 参数说明

<#list modelConfig.models as modelInfo>
${modelInfo?index + 1}）${modelInfo.fieldName}

类型：${modelInfo.type}

描述：${modelInfo.description}

默认值：${modelInfo.defaultValue?c}

缩写： -${modelInfo.abbr}


</#list>