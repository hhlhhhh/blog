### 项目总览

由于是应付学校工程实践，项目编码，本项目花费4天1晚完成

**声明**：项目**根据github：https://github.com/zhaoguoshun  博主仿写（非常感谢，为我节约了大部分布局思考时间）**，其中采用了**一部分的css**（标签云、文章列表以及文章详情的文章部分），**数据库**根据原作者优化剪裁补充。除此都由本人亲自完成，项目结构、项目编码与参考博主大相径庭。



本项目工作量并不小（可以在本项目基础上增加一些功能，比如博主之间的消息发送），**可以用于工程实践、毕设以及个人学习**

项目肯定还存在一定小问题，自行更正就好。

个人qq： 3032229620



项目分为7个模块：公告、评论、文章标签、专栏、友情链接、标签、用户

![image-20240719151134523](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/image-20240719151134523.png)



**危**：数据库表之间我没有添加约束，请自行阅读好项目，避免因为数据库问题影响项目运行。

### 项目运行

前端我使用的是前台node19.9.0，后台14.21.3，后端使用的是java17

与本项目尽量保持一致，前端可以用nvm管理自己的node版本

前端直接npm i加载依赖就好，然后npm run dev。

后端：

![Snipaste_2024-07-19_16-07-02](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/Snipaste_2024-07-19_16-22-22.png)

这个jks文件是使用keytool生成的，你也可以下载后用keytool生成替换就好。

```
//私钥生成指令
//keytool -genkey -alias private -keyalg RSA -storepass pass123 -keysize 1024 -keystore private.jks -validity 3655
```



### **项目功能**

只展示前台主要功能，更多细节自行发现，后台也是运行查看	=《》=

#### **登录**

![](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/Snipaste_2024-07-19_16-05-25.png)





![](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/Snipaste_2024-07-19_16-05-11.png)





#### 注册

![Snipaste_2024-07-19_16-07-02](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/Snipaste_2024-07-19_16-07-02.png)



#### 首页

![Snipaste_2024-07-19_16-07-02](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/Snipaste_2024-07-19_16-08-18.png)



#### 公告

![Snipaste_2024-07-19_16-07-02](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/Snipaste_2024-07-19_16-09-08.png)

#### 文章详情

![Snipaste_2024-07-19_16-07-02](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/Snipaste_2024-07-19_16-09-54.png)

#### 文章评论

![Snipaste_2024-07-19_16-07-02](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/Snipaste_2024-07-19_16-11-15.png)

#### 文章搜索

![Snipaste_2024-07-19_16-07-02](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/Snipaste_2024-07-19_16-12-02.png)

文章搜索分为全局搜索、标签搜索、专栏内部搜索。具体请自行探索。



#### 个人首页

![image-20240719161702603](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/image-20240719161702603.png)

#### 修改密码

![Snipaste_2024-07-19_16-07-02](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/Snipaste_2024-07-19_16-17-52.png)

#### 文章添加

![Snipaste_2024-07-19_16-07-02](https://raw.githubusercontent.com/hhlhhhh/blog/main/images/Snipaste_2024-07-19_16-18-37.png)