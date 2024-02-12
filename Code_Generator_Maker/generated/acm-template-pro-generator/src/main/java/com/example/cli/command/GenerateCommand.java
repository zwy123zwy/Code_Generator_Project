package com.example.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.example.generator.FileGenerator;
import com.example.model.DataModel;
import lombok.Data;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "generate", description = "生成代码", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {

    @Option(names = {"-ng", "--needGit"}, arity = "0..1", description = "是否生成 .gitignore文件", interactive = true, echo = true)
    private boolean needGit = true;

    @Option(names = {"-l", "--loop"}, arity = "0..1", description = "是否生成循环", interactive = true, echo = true)
    private boolean loop = false;

    @Option(names = {"-a", "--author"}, arity = "0..1", description = "作者注释", interactive = true, echo = true)
    private String author = "example";

    @Option(names = {"-o", "--outputText"}, arity = "0..1", description = "输出信息", interactive = true, echo = true)
    private String outputText = "sum = ";

    public Integer call() throws Exception {
        DataModel dataModel = new DataModel();
        BeanUtil.copyProperties(this, dataModel);
        FileGenerator.doGenerate(dataModel);
        return 0;
    }
}
