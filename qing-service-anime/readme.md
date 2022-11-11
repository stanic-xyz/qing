发布指南
=========

1. 更新即将发布的 CHANGELOG.md。
2. 更新新版本的 README.md 。
3. 执行 `git commit -am "Update changelog for X.Y.Z."` (此处的 X.Y.Z 表示新版本).
4. 执行 `mvn-release`.
   * `What is the release version for "JavaPoet"? (com.squareup.javapoet) X.Y.Z:` - 点击回车键.
   * `What is SCM release tag or label for "JavaPoet"? (com.squareup.javapoet) javapoet-X.Y.Z:` - 点击回车键.
   * `What is the new development version for "JavaPoet"? (com.squareup.javapoet) X.Y.(Z + 1)-SNAPSHOT:` -
     输入 `X.(Y + 1).0-SNAPSHOT`.
   * 提示是输入你的 GPG 密码 。
5. 访问Sonatype Nexus，并 promote 该版本.

如果第4步或者第5步执行失败:

* Drop the Sonatype repo,
* Fix the problem,
* Manually revert the version change in `pom.xml` made by `mvn-release`,
* Commit代码,
* 从第4步重新开始.

仓库配置
-------------

修改你本地的 `~/.m2/settings.xml`, 具体参见如下:

