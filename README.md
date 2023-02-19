# 仿12306网站


####  项目所用技术点 

```

1. 访问控制过滤器Filter的使用

2. HttpSession的使用

3. servlet传递数据至Jsp

4. JDBC连接池的使用

5. Jsp EL表达式 
https://www.cnblogs.com/czs1982/p/3966748.html

6. Jsp核心标签库的运用
http://how2j.cn/k/jsp/jsp-jstl/578.html

7. ajax输出xml使用

8. Md5加密技术

9. 图形验证码技术

10. Cookie的使用

11. 图片上传

12. 服务端表格分页

13. 导出一个xls Excel表格

```

#### 项目所导入的Jar包

1. jstl-1.2.jar (JSP标准标签库)

2. [jxl.jar](https://bbs.csdn.net/topics/90494976)(Java操作Excel或创建Excel) 

3. ojdbc6.jar Oracle数据驱动

#### 本项目的思维导向图草图

![](WebContent/bug/12306.png)

#### 总体架构

![](WebContent/bug/JavaEE应用的标准层次结构.png)

#### 配置环境

Jdk 1.8 

Tomcat 8.0.47 

Oracle 11.2.0.1.0

#### 开发工具

Eclipse 4.11.0

PL/SQL 11.2.0.1.0 

Vscode 1.34.0

#### 数据库设计

##### tickets_user

|列名|数据类型|可否为空|说明|
| -- | -- | -- | -- |
| id | number(11)    | not null  | id(Parimary主键)  |  
| username   | varchar2(30)   | not null | 用户名(Unique唯一)   | 
| password   | varchar2(50)   | not null | 密码   |  
| rule   | varchar2(2)  | not null | 权限(1 管理员 2 普通用户) |
| realname   | varchar2(50)   | not null | 真实姓名   |  
| sex   | char(1)    | not null | 性别(1 男 2 女)   |  
| city   | number(11)    | not null | 城市信息id值((Foreign外键tickets_tab_city))   |  
| cert_type   | number(11)    | not null | 证件类型(1二代身份证2港澳通行证3台湾通行证4护照)(Foreign外键tickets_tab_certtype)   |  
| cert   | varchar2(50)    | not null | 证件号码   |  
| birthday   | date   | not null | 生日   |  
| user_type   | number(11)   | not null | 旅客类型(1成人2儿童3学生4残疾军人、伤残人民警察)((Foreign外键tickets_tab_usertype))   |  
| content   | varchar2(3000)    | null   | 备注信息   |  
| status   | char(1)   | not null | 用户状态(0 无效 1 有效)   | 
| login_ip   | varchar2(50)   | not null   | 登陆IP   |  
| image_path   | varchar2(200)    |  not null | 用户头像路径   |   

##### tickets_usertype

|列名|数据类型|可否为空|说明|
| -- | -- | -- | -- |
| id | number(11)    | not null  | id (主键)  |  
| content   | varchar2(40)   | not null | 旅客类型(1成人2儿童3学生4残疾军人、伤残人民警察)    | 

##### tickets_province

|列名|数据类型|可否为空|说明|
| -- | -- | -- | -- |
| id | number(11) | not null  | id (主键)  |  
| provinceid   | varchar2(6)   | not null | 省份标识   |
| province | varchar2(40) | not null  | 省份名称  |  

##### tickets_city

|列名|数据类型|可否为空|说明|
| -- | -- | -- | -- |
| id | number(11) | not null  | id (主键)  |  
| cityid   | varchar2(6)   | not null | 城市标识   |
| city | varchar2(50) | not null  | 城市名称  |  
| father | varchar2(6) | not null  | 省份标识  |  

#### tickets_certtype

|列名|数据类型|可否为空|说明|
| -- | -- | -- | -- |
| id | number(11)    | not null  | id (主键)  |  
| content   | varchar2(20)   | not null | 证件类型(1二代身份证2港澳通行证3台湾通行证4护照)| 



#### 项目所遇部分bug总结

1. ORA-02291: 违反完整约束条件 (SCOTT.tickets_2_USER_CITY_FK) - 未找到父项关键字

![](WebContent/bug/bug1.png)

原因:赋值错误 ：赋值city时关联的是id不是cityid

解决: 取得City实体内的id值正确赋值即可

[参考链接](https://blog.csdn.net/jihuanliang/article/details/7205968)

2. bug For input string: "" (其中双引号内可能为C也可能为G等任何字母)

![](WebContent/bug/bug2.png)

原因：前端JSP页面value里的值为C或G或者任何字母,不能转换成数值。

解决：将前端JSP页面中的value里的值更改为数字类型的字符串即可

3. 数据库能根据用户名密码能够查到而页面却不能登陆

![](WebContent/bug/bug3.png)

![](WebContent/bug/bug3_1.png)

完全是因为自己事务没有学好，特么小废物不知道插入数据之后提交造成的。。。。

4. 跳转jsp页面url后带中文出现乱码

解决：两行Java代码

```
String mes = URLEncoder.encode("注册成功","utf-8");//加码
response.sendRedirect(request.getContextPath() + "/login.jsp?message=" + mes);

String message = URLDecoder.decode(request.getParameter("message"), "utf-8");//解码
```

