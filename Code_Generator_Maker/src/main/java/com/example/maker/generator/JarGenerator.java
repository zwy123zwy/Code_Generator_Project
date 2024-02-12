package com.example.maker.generator;

import java.io.*;
import java.util.Map;
// 生成jar包的方法，必须要有maven 仓库
public class  JarGenerator {

    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        // 清理之前的构建并打包
        // 注意不同操作系统，执行的命令不同
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true";
        String otherMavenCommand = "mvn clean package -DskipTests=true";
        // 必须使用windows下的命令
        // todo 如果上线这边的命令可能需要发生改变，这里需要突出注意
        String mavenCommand = winMavenCommand;

        // 这里一定要拆分！
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        //指定路径
        processBuilder.directory(new File(projectDir));
        Map<String, String> environment = processBuilder.environment();
        System.out.println(environment);
        // 打开这个终端 并执行相关的命令
        Process process = processBuilder.start();

        // 读取命令的输出
        InputStream inputStream = process.getInputStream();
        //缓存读取器
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();
        System.out.println("命令执行结束，退出码：" + exitCode);
    }
//测试代码
    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerate("C:\\Users\\Zhangwenye\\Desktop\\lowCode\\codeGeneratorMaker\\generated");
    }
}