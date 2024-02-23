# 数据库初始化


-- 创建库
create database if not exists my_code;

-- 切换库
use my_code;

create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (userAccount)
) comment '用户' collate = utf8mb4_unicode_ci;

INSERT INTO my_code.user (id, userAccount, userPassword, userName, userAvatar, userProfile, userRole)
VALUES (1, 'admin', 'b0dd3697a192885d7c055db46155b26a', '超级管理员',
        'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png', '我有一头小毛驴我从来也不骑', 'admin');
INSERT INTO my_code.user (id, userAccount, userPassword, userName, userAvatar, userProfile, userRole)
VALUES (2, 'user', 'b0dd3697a192885d7c055db46155b26a', 'knight张',
        'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png', '我有一头小毛驴我从来也不骑', 'user');

-- 代码生成器表
create table if not exists generator
(
    id                      bigint auto_increment comment 'id' primary key,
    name                    varchar(128)                       null comment '名称',
    description             text                               null comment '描述',
    basePackage             varchar(128)                       null comment '基础包',
    version                 varchar(128)                       null comment '版本',
    author                  varchar(128)                       null comment '作者',
    tags                    varchar(1024)                              null comment '标签列表（json 数组）',
    picture                 varchar(256)                       null comment '图片',
    fileConfig              text                               null comment '文件配置（json字符串）',
    modelConfig             text                               null comment '模型配置（json字符串）',
    distPath                text                               null comment '代码生成器产物路径',
    status                  int      default 0                 not null comment '状态',
    userId                  bigint                             not null comment '创建用户 id',
    createTime              datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime              datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete                tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '代码生成器' collate = utf8mb4_unicode_ci;

INSERT INTO my_code.generator (id, name, description, basePackage, version, author, tags, picture, fileConfig,
                                     modelConfig, distPath, status, userId)
VALUES (1, 'ACM 模板项目', 'ACM 模板项目生成器', 'com.example', '1.0', 'knight张', '["Java"]',
        'https://pic.yupi.icu/1/_r0_c1851-bf115939332e.jpg', '{}', '{}', null, 0, 1);
INSERT INTO my_code.generator (id, name, description, basePackage, version, author, tags, picture, fileConfig,
                                     modelConfig, distPath, status, userId)
VALUES (2, 'Spring Boot 初始化模板', 'Spring Boot 初始化模板项目生成器', 'com.example', '1.0', 'knight张', '["Java"]',
        'https://pic.yupi.icu/1/_r0_c0726-7e30f8db802a.jpg', '{}', '{}', null, 0, 1);
INSERT INTO my_code.generator (id, name, description, basePackage, version, author, tags, picture, fileConfig,
                                     modelConfig, distPath, status, userId)
VALUES (3, '外卖', '外卖项目生成器', 'com.example', '1.0', 'knight张', '["Java", "前端"]',
        'https://pic.yupi.icu/1/_r1_c0cf7-f8e4bd865b4b.jpg', '{}', '{}', null, 0, 1);
INSERT INTO my_code.generator (id, name, description, basePackage, version, author, tags, picture, fileConfig,
                                     modelConfig, distPath, status, userId)
VALUES (4, '用户中心', '用户中心项目生成器', 'com.example', '1.0', 'knight张', '["Java", "前端"]',
        'https://pic.yupi.icu/1/_r1_c1c15-79cdecf24aed.jpg', '{}', '{}', null, 0, 1);
INSERT INTO my_code.generator (id, name, description, basePackage, version, author, tags, picture, fileConfig,
                                     modelConfig, distPath, status, userId)
VALUES (5, '商城', '商城项目生成器', 'com.example', '1.0', 'knight张', '["Java", "前端"]',
        'https://pic.yupi.icu/1/_r1_c0709-8e80689ac1da.jpg', '{}', '{}', null, 0, 1);

-- 代码生成器点赞表（硬删除）
create table if not exists generator_thumb
(
    id          bigint auto_increment comment 'id' primary key,
    generatorId bigint                             not null comment '生成器 id',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (generatorId),
    index idx_userId (userId)
) comment '代码生成器点赞表';

-- 代码生成器收藏表（硬删除）
create table if not exists generator_favour
(
    id          bigint auto_increment comment 'id' primary key,
    generatorId bigint                             not null comment '生成器 id',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (generatorId),
    index idx_userId (userId)
) comment '代码生成器收藏表';

# 修改生成器表
ALTER TABLE `generator`
ADD COLUMN `favourNum` int  COLLATE utf8_general_ci DEFAULT NULL COMMENT '收藏数' AFTER `status`,
ADD COLUMN `thumbNum` int  COLLATE utf8_general_ci DEFAULT NULL COMMENT '点赞数' AFTER `favourNum` ;
ALTER TABLE `generator`
    ALTER COLUMN `favourNum` SET DEFAULT  0,
    ALTER COLUMN `thumbNum` SET DEFAULT  0;

INSERT INTO my_code.generator (
    `name`,
    `description`,
    `basePackage`,
    `version`,
    `author`,
    `tags`,
    `picture`,
    `fileConfig`,
    `modelConfig`,
    `distPath`,
    `status`,
    `thumbNum`,
    `favourNum`,
    `userId`,
    `createTime`,
    `updateTime`,
    `isDelete`
)
VALUES
    (
        'ACM 模板代码',
        'ACM 模板项目生成器',
        'com.azhang',
        '1.0',
        'knight张',
        '["Java", "前端"]',
        'https://pic.yupi.icu/1/_r0_c1851-bf115939332e.jpg',
        '{\"inputRootPath\": \".source/acm-template-pro\",\"outputRootPath\": \"generated\",\"sourceRootPath\": \"C:/Users/Zhangwenye/Desktop/lowCode/demoProjects/acm-template-pro\",\"type\": \"dir\",\"files\": [{\"groupKey\": \"git\",\"groupName\": \"开源\",\"type\": \"group\",\"condition\": \"needGit\",\"files\": [{\"inputPath\": \".gitignore\",\"outputPath\": \".gitignore\",\"type\": \"file\",\"generateType\": \"static\"},{\"inputPath\": \"README.md\",\"outputPath\": \"README.md\",\"type\": \"file\",\"generateType\": \"static\"}]},{\"inputPath\": \"src/com/example/acm/MainTemplate.java.ftl\",\"outputPath\": \"src/com/example/acm/MainTemplate.java\",\"type\": \"file\",\"generateType\": \"dynamic\"}]}',
        '{\"models\": [{\"fieldName\": \"needGit\",\"type\": \"boolean\",\"description\": \"是否生成 .gitignore 文件\",\"defaultValue\": true},{\"fieldName\": \"loop\",\"type\": \"boolean\",\"description\": \"是否生成循环\",\"defaultValue\": false,\"abbr\": \"l\"},{\"groupKey\": \"mainTemplate\",\"groupName\": \"核心模板\",\"type\": \"MainTemplate\",\"description\": \"用于生成核心模板文件\",\"condition\": \"loop\",\"models\": [{\"fieldName\": \"author\",\"type\": \"String\",\"description\": \"作者注释\",\"defaultValue\": \"Zwy\",\"abbr\": \"a\"},{\"fieldName\": \"outputText\",\"type\": \"String\",\"description\": \"输出信息\",\"defaultValue\": \"outputText = \",\"abbr\": \"o\"}]}]}',
        NULL,
        0,
        12,
        4,
        1,
        '2024-01-04 21:57:59',
        '2024-01-11 18:53:05',
        0
    );