对jimmer的建议：

[](https://github.com/h2non/filetype)
不太喜欢动态的东西，包括apt生成的，每次启动项目的时候都需要编译生成，对于刚打开项目的人优点不太友好，尤其是项目比较大了以后，负担可能会更重，对于已经测试号，固化版本的代码，也没有必要每次都重新生成了
可不可以这样，类似与package-lock.json，对与已经测试好没有问题的部分进行版本固化（例如直接生成到classpath）

在上面一点的基础上，关于生成的代码的格式问题：生成的代码，可能会参与到代码审查里面，一些格式问题，变量命名问题，都有可能过不了ci，所以，生成的代码格式，期望可以和手写的代码格式保持一致，
比如代码风格，变量明明风格，让使用的人可选
这样，生成的代码的风格可以和手写的代码，在格式上保持统一
目前我是直接忽略自动生成的代码的审查，避免产生影响，可以考虑通过引入一些代码风格插件如checkstyle，.editorconfig等，来保证生成的代码格式和手写的代码格式一致
我觉得这个地方可以做成类似与插件的东西，如果没有精力实现，可以留一个口子，由第三方的开源项目来完成