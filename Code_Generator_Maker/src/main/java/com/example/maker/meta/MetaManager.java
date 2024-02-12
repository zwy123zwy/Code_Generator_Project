package com.example.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

public class MetaManager {
//保证多线程的可见性，并防止指令重排
    private static volatile Meta meta;

    private MetaManager() {
        // 私有构造函数，防止外部实例化
    }

//    双检索单例模式 懒汉式
    public static Meta getMetaObject() {
        if (meta == null) {
            synchronized (MetaManager.class) {
                if (meta == null) {
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    private static Meta initMeta() {
//        String metaJson = ResourceUtil.readUtf8Str("meta1.json");
        String metaJson = ResourceUtil.readUtf8Str("springboot-meta.json");
//        String metaJson = ResourceUtil.readUtf8Str("springboot-init-meta1.json");
        Meta newMeta = JSONUtil.toBean(metaJson, Meta.class);
        //todo 校验和处理默认值
        MetaValidator.doValidAndFill(newMeta);
        return newMeta;
    }
}