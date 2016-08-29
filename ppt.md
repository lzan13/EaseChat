使用环信EaseUI集成聊天
---------------------------

* 作者：      
    * lzan13 
* 开发环境：   
    * Windows 10   
    * AndroidStudio 2.1.2


### 集成准备
* SDK的下载
* 注册成为开发者 
[注册地址](https://console.easemob.com/index_register.html) 
[注册介绍](http://docs.easemob.com/im/000quickstart/10register)
[了解appkey](http://docs.easemob.com/im/000quickstart/10register#创建应用)


### EaseUI介绍

EaseUI 是一个开源的UI库，是基于环信SDK封装了IM功能常用的控件、fragment 等等，旨在帮 助开发者快速集成环信SDK；
使用EaseUI可以只是用几行代码就实现聊天界面，以及消息的收发功能，还是比较方便的；
下载的环信SDK压缩包里面已经包含此库，解压后路径为 `/sdk/examples/easeui`
[详细介绍](http://docs.easemob.com/im/200androidclientintegration/135easeuiuseguide)


### 项目配置

* 导入EaseUI并配置使用  
    将`EaseUI`以`module`导入`AndroidStudio`的项目，配置方面主要是`appkey`以及`SDK`的服务（不过呢这一步大家经常会忘） 
    
* 初始化 
项目配置完成我们就可以在我们项目入口的地方进行初始化环信`SDK`(一般在`Application`里初始化，也可以放在`MainActivity`)，
需要注意的是`SDK`的初始化一定要放在调用`SDK`的其他方法调用之前，不然会出现错误； 这里我们使用的是`EaseUI`，可以直接调用`EaseUI`封装好的初始化方法：
`EaseUI.getInstance().init(context, options);`
其中Options这个参数主要是SDK初始化的一些配置，这里可以配置一些初始化sdk的定义，例如：  
```java
// 设置自动登录
options.setAutoLogin(true);
// 设置（主动或被动）退出群组时，是否删除群聊聊天记录
options.setDeleteMessagesAsExitGroup(false);
```
[详细可查看api文档](http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html)


### 注册与登录
```java
// 注册方法，同步，需要自己异步执行，根据执行情况判断是否注册成功 
EMClient.getInstance().createAccount(psername, password);
// 登录方法，异步，可在回调中监听登录状态 
EMClient.getInstance().login(username, password, callback);
// 退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false 
EMClient.getInstance().logout(false, callback);
```
[一些常见错误码可以参考API文档详细介绍](http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html)


### 集成聊天界面

`EaseUI`已经对聊天界面进行了封装，所以我们在实现聊天界面的时候非常简单，只需要加载`EaseUI`的聊天界面就好，`EaseUI`的聊天界面是在`EaseChatFragment`类里实现的
```java
// 这里直接使用EaseUI封装好的聊天界面 
EaseChatFragment chatFragment = new EaseChatFragment();
// 将参数传递给聊天界面 
chatFragment.setArguments(getIntent().getExtras());
// 加载EaseUI封装的聊天界面Fragment 
getSupportFragmentManager().beginTransaction().add(R.id.ec_layout_container, chatFragment).commit();
```

### 结束

OK！
PPT已经看完了，后边创建一个新的项目带领大家过一下，最后会把项目的一些细节也都写上然后放在`github`上，大家可以去`fork`以及`clone`看下
[项目代码地址](https://github.com/lzan13/EaseChat)
有两个分支：`easeui_dev`分支是这次的代码 `master`分支是没有使用`EaseUI`的代码

**一些其他地址**
[SDK的下载](http://www.easemob.com/download/im) 
[开发者后台](http://console.easemob.com)
[开发者文档](http://docs.easemob.com/im/start) 
[API文档](http://www.easemob.com/apidoc/android/chat3.0)