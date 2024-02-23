package com.example;

import com.example.cli.CommandExecutor;

public class Main {

    public static void main(String[] args) {
        CommandExecutor commandExecutor = new CommandExecutor();
        args = new String[]{"json-generate", "--file=C:/Users/Zhangwenye/Desktop/Code_Generator/Code_Generator_Maker/generated/acm-template-pro-generator/test.json"};
        commandExecutor.doExecute(args);
    }
}