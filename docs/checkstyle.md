### 代码风格

本项目 添加了 checkstyle 插件，来保证每位提交者代码的风格保持一致，减少无效代码的修改。本篇文章主要讲解如何在 IDEA 中添加
CheckStyle 插件，并引入项目所提供的 checkstyle.xml 配置。

### 安装 CheckStyle-IDEA

- 进入 IDEA 插件市场。
- 搜索 CheckStyle-IDEA，点击安装即可。

### 配置 CheckStyle

- 进入 CheckStyle 配置（File | Settings | Tools | Checkstyle）。
- 选择 Checkstyle 版本：8.39。
- 在配置文件中点击添加按钮，配置描述可随便填写（推荐 Halo Checks），选择 ./config/checkstyle/checkstyle.xml，点击下一步和完成。
- 勾选刚刚创建的配置文件。

### 配置 Editor

- 进入编辑器配置（File | Settings | Editor | Code Style）
- 导入 checkstyle.xm 配置：

![](resources\checkstyle.webp)

- 选择 ./config/checkstyle/checkstyle.xml 配置文件，点击确定即可。

至此，有关代码风格检查工具和格式化配置已经完成。
