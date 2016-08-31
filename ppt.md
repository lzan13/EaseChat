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


### 项目配置

* 加入SDK配置使用  
    将SDK加入`AndroidStudio`的项目，配置方面主要是`appkey`以及`SDK`的服务（不过呢这一步大家经常会忘） 
    
* 初始化 
项目配置完成我们就可以在我们项目入口的地方进行初始化环信`SDK`(一般在`Application`里初始化，也可以放在`MainActivity`)，
需要注意的是`SDK`的初始化一定要放在调用`SDK`的其他方法调用之前，不然会出现错误；还有就是要注意`SDK`不能进行多次初始化，这点在官方文档也有详细说明；
调用SDK的初始化方法：
`EMClient.getInstance().init(context, options);`
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


### 集成聊天
集成聊天就需要实现消息的收发，消息的发送比较简单，直接创建消息，然后调用发送方法就行了，有时我们还需要设置下消息的回调，来监听消息发送状态，
然后消息的接收稍微麻烦一些，我们需要实现`EMMessageListener`接口，兵实现里边的几个方法，当有新消息的时候就会回调这些方法，我们可以在这里边进行处理
```java
// 新消息的回调
onMessageReceived(List<EMMessage> list);
// 新的透传消息回调
onCmdMessageReceived(List<EMMessage> list);
// 消息已读回调
onMessageReadAckReceived(List<EMMessage> list);
// 消息已发送回调
onMessageDeliveryAckReceived(List<EMMessage> list);
// 消息状态改变回调
onMessageChanged(EMMessage message, Object object);
```


### 结束

OK！
PPT已经看完了，后边创建一个新的项目带领大家过一下，最后会把项目的一些细节也都写上然后放在`github`上，大家可以去`fork`以及`clone`看下
[项目代码地址](https://github.com/lzan13/EaseChat)
有两个分支：`master`分支是这次的代码； `easeui_dev`分支是使用了`EaseUI`方式集成的代码

**一些其他地址**
[SDK的下载](http://www.easemob.com/download/im) 
[开发者后台](http://console.easemob.com)
[开发者文档](http://docs.easemob.com/im/start) 
[API文档](http://www.easemob.com/apidoc/android/chat3.0)