



# 开发文档

### 环境配置

#### 1.配置src/main/resources/application.yml

```yaml
# DataSource Config
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.NonRegisteringDriver
    url: jdbc:mysql://127.0.0.1:3306/projectcode?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai #修改数据库地址
    username: root #修改用户名
    password: 123456 #修改密码
server:
  port: 8083 #端口设置
logging:
  level:
    com.server: error
file:
  upload-path: E:/ProjectCode/MyProject/程序代写/订单编号X18968/Server/res/  #修改图片的存储位置
```

### 总体开发框架：

#### 1.Controller控制层

|         类名         |                             作用                             |
| :------------------: | :----------------------------------------------------------: |
|   LoginController    | 包含有登录相关的请求接口，如验证码生成、Token接受、用户登录信息确定 |
| ManageUserController | 包含有用户管理相关的接口，如：删除用户、修改用户信息、重置密码 |
|  RegisterController  |                    包含有用户注册相关接口                    |
|  UserItemController  |                 包含有面向用户的众筹项目接口                 |

#### 2.接口





##### 1.LoginController

（1）获取验证码

|    功能    |          接口          | 请求方式 | 请求体 |     返回     |
| :--------: | :--------------------: | :------: | :----: | :----------: |
| 获取验证码 | 127.0.0.1:8083/captcha |   get    |        | 返回一张图片 |

（2）登录

| 功能 |         接口         | 请求方式 |                            请求体                            |
| :--: | :------------------: | :------: | :----------------------------------------------------------: |
| 登录 | 127.0.0.1:8083/login |   Post   | {<br/>	"username":"lisi",<br/>	"password":"123456",<br/>	"code":"wsda"<br/>} |

**返回**

{

  "code": 10000,

  "msg": "操作成功",

  "data": {

​    "user": {

​      "id": 2,

​      "name": "lisi",

​      "nickname": "阿四",

​      "password": "",

​      "identity": "老师",

​      "email": "12323@qq.com",

​      "status": 1

​    },

​    "token": "eyJ0eXA6IjoiSldUIiwiYWxnIjoiSFMyNTYiLCJ0eXAiOiJKV1QifQ.eyJsb2dpbk5hbWUiOiJsaXNpIiwiZXhwIjoxNjc3NzYwODAyLCJ1c2VySWQiOjJ9.PZ3BwtNiNMAr_ZoXF0yvkynsV4-ijn5bSV_-QNLX4WA"

  }

}



|    功能    |          接口          | 请求方式 |                            请求体                            |     返回     |
| :--------: | :--------------------: | :------: | :----------------------------------------------------------: | :----------: |
| 获取验证码 | 127.0.0.1:8083/captcha |   get    |                                                              | 返回一张图片 |
|    登录    |  127.0.0.1:8083/login  |   Post   | {<br/>	"username":"lisi",<br/>	"password":"123456",<br/>	"code":"wsda"<br/>} |              |

```java
/*
用于获取验证码，返回的是图片信息
*/
@GetMapping("/captcha")
public void getVerifyCodeImg(HttpServletResponse response, HttpSession session);
/*
登录接口，用于验证用户
返回一个token验证吗，之后每次访问都需要在header中添加token值，才可以访问
发送的json数据格式：
{
	"username":"lisi",
	"password":"123456",
	"code":"wsda"
}
*/
@PostMapping("/login")
public Result login(@RequestBody Map<String,String> map)
```

##### 2.RegisterController

|   功能   |              接口               | 请求 |                            请求体                            |
| :------: | :-----------------------------: | :--: | :----------------------------------------------------------: |
| 注册用户 | 127.0.0.1:8083/captcha/register | post | {<br/>	"username":"lisi",<br/>	"password":"123456",<br/>	"nickname":"阿四",<br/>	"identity":"学生",<br/>	"email":"123@qq.com",<br/>} |



```java
/*
注册用户使用
发送的json数据格式：
{
	"username":"lisi",
	"password":"123456",
	"nickname":"阿四",
	"identity":"学生",
	"email":"123@qq.com",
}
*/
@PostMapping("/register")
public Result registerUser(@RequestBody Map<String,String> map)
```

##### 3.ManageUserController



**（1）.查询一个用户信息**

|          功能          |             接口             | 请求 |                  请求体                   | 返回 |
| :--------------------: | :--------------------------: | :--: | :---------------------------------------: | ---- |
| 用于查询一个用户的信息 | 127.0.0.1:8083/sys/user/info | post | {<br/>        "username":"lisi"<br/>    } |      |

**返回**

{

  "code": 10000,

  "msg": "操作成功",

  "data": {

​    "user": {

​      "id": 2,

​      "name": "lisi",

​      "nickname": "阿四",

​      "password": "",

​      "identity": "老师",

​      "email": "12323@qq.com",

​      "status": 1

​    }

  }

}

(2).查询所有用户

|                          功能                          |             接口             | 请求 | 请求体 | 返回 |
| :----------------------------------------------------: | :--------------------------: | :--: | :----: | ---- |
| 用于查询所有用户的信息，返回用户信息，除去了用户的密码 | 127.0.0.1:8083/sys/user/list | post |   无   |      |

**返回：**

{

  "code": 10000,

  "msg": "操作成功",

  "data": {

​    "records": [

​      {

​        "id": 1,

​        "name": "zhangshan",

​        "nickname": "小珊",

​        "password": **null**,

​        "identity": "教师",

​        "email": "11@qq.com",

​        "status": 1

​      },

​      {

​        "id": 2,

​        "name": "lisi",

​        "nickname": "阿四",

​        "password": **null**,

​        "identity": "老师",

​        "email": "12323@qq.com",

​        "status": 1

​      },

​    ]

  }

}

|                             功能                             |                接口                | 请求 |                            请求体                            | 返回 |
| :----------------------------------------------------------: | :--------------------------------: | :--: | :----------------------------------------------------------: | ---- |
|                      用于更新用户的信息                      |   127.0.0.1:8083/sys/user/update   | post | {<br/>	"id":"",<br/>	"username":"",<br/>	"nickname":"",<br/>	"identity":"",<br/>	"email":""<br/>} |      |
| 用于批量删除用户，删除是逻辑删除，在数据库中不会物理删除数据，只是把将删除标志位置0 | 127.0.0.1:8083/sys/user/deletelist | post | {<br/>	"id1":"",<br/>	"id2":"",<br/>	"id3":""<br/>	.......,<br/>	"idn":""<br/>} |      |
|                     重置用户密码88888888                     |  127.0.0.1:8083/sys/user/resetpwd  | post |                   {<br/>	"id":"",<br/>}                   |      |
|                         修改用户密码                         |      127.0.0.1:8083/sys/user/      | post | {<br/>	"id":"",<br/>	"oldPwd":"",<br/>	"newPwd":“”<br/>} |      |

以上返回都是：

{

  "code": 10000,

  "msg": "操作成功",

  "data": ""(操作信息)

}



```java
//注意在url有/sys/user
@RequestMapping("/sys/user")
public class ManageUserController{
    
/*
用于查询一个用户的信息，
json数据结构
    {
        "username":"lisi"
    }
*/
    @PostMapping("/info")
    public Result ret_Info(@RequestBody Map<String,String> map);
/*
用于查询所有用户的信息，
返回用户信息，除去了用户的密码
*/  
    @PostMapping("/list")
    public Result retUserList();
/*
用于更新用户的信息，
json数据结构：
{
	"id":"",
	"username":"",
	"nickname":"",
	"identity":"",
	"email":""
}
*/  
    @PostMapping("update")
    public Result updateUser(@RequestBody Map<String,String> map);
/*
用于批量删除用户，删除是逻辑删除，在数据库中不会物理删除数据，只是把将删除标志位置0
json数据结构：
{
	"id1":"",
	"id2":"",
	"id3":""
	.......,
	"idn":""
}
*/  
    @PostMapping("/deletelist")
    public Result deleteList(@RequestBody Map<String,String> map);
/*
重置用户密码
json数据结构：
{
	"id":"",
}
*/  
    @PostMapping("/resetpwd")
    public Result resetPwd(@RequestBody Map<String,String> map);
/*
修改用户密码
json数据结构：
{
	"id":"",
	"oldPwd":"",
	"newPwd":“”
}
失败返回错误代码
*/  
    @PostMapping("/updatetpwd")
    public Result updatePwd(@RequestBody Map<String,String> map)
}

```

##### 4.UserItemController

|                             功能                             |              接口               | 请求 |                            请求体                            |
| :----------------------------------------------------------: | :-----------------------------: | :--: | :----------------------------------------------------------: |
| 添加一个众筹项目<br/>    	/add/{id}中的id是当前用户id<br/>    	如用户id是1，则为/sys/item/add/1 |   127.0.0.1/sys/item/add/{id}   | post | {<br/>            "identity":"",<br/>            "price":"",<br/>            "title":"",<br/>            "content":"",<br/>        } |
| 为众筹项目添加图片，<br/>    	/setting/{id}中的id是众筹项目id<br/>    	如众筹项目id是1，则为/sys/item/setting/1 | 127.0.0.1/sys/item/setting/{id} | post |                             文件                             |
|                    用户更新众筹项目的信息                    |    127.0.0.1/sys/item/update    | post | {<br/>            "identity":"",<br/>            "price":"",<br/>            "title":"",<br/>            "id":"",<br/>            "content":"",<br/>        } |
|                     返回所有众筹项目信息                     |   127.0.0.1/sys/item/listItem   | post |                              无                              |
|                        管理员审核项目                        |       127.0.0.1/sys/item/       | post | {<br/>			"item_id":"",<br/>			"status":""//-1为审核不通过，1是审核通过<br/>		} |
| 返回一张众筹项目图片"/retpic/{id}"中的id是众筹项目id<br/>		若没有图片则返回一张白色背景图 | 127.0.0.1/sys/item/retpic/{id}  | post |                              无                              |



```JAVA
//注意在url有/sys/item
@RequestMapping("/sys/item")
public class UserItemController {
    /*
		添加一个众筹项目
    	/add/{id}中的id是当前用户id
    	如用户id是1，则为/sys/item/add/1
        json数据结构：
        {
            "identity":"",
            "price":"",
            "title":"",
            "content":"",
        }
        成功返回一个众筹项目的所有数据
    */ 
    @PostMapping("/add/{id}")
    public Result addItem(@PathVariable("id") int id,@RequestBody Map<String,String> map);
    /*
		为众筹项目添加图片，
    	/setting/{id}中的id是众筹项目id
    	如众筹项目id是1，则为/sys/item/setting/1
		发送的是一个文件
    */
    @PostMapping("/setting/{id}")
    public Result setImg(@PathVariable("id") int id, @RequestBody MultipartFile file);
    /*
		用户更新众筹项目的信息
        json数据结构：
        {
            "identity":"",
            "price":"",
            "title":"",
            "id":"",
            "content":"",
        }
    */
    @PostMapping("/update")
    public Result updateItem(@RequestBody Map<String,String> map);
    /*
		返回一张众筹项目图片
		"/retpic/{id}"中的id是众筹项目id
		若没有图片则返回一张白色背景图
    */
    @PostMapping("/retpic/{id}")
    public ResponseEntity<byte[]> getImg(@PathVariable("id") int id);
    /*
		返回所有众筹项目信息
    */
    @PostMapping("/listItem}")
    public List<SysItem> retlist();
    /*
		管理员审核项目
		json格式：
		{
			"item_id":"",
			"status":""//-1为审核不通过，1是审核通过
		}
    */
    @PostMapping("/verify")
    public void examineItem(@RequestBody Map<String,String> map)
}
```

#### 3.Config

|       类名        |              说明              |
| :---------------: | :----------------------------: |
|    DruidConfig    |           数据池配置           |
| MybatisPlusConfig |        MybatisPlus配置         |
|     WebConfig     | 内部主要有过滤器、静态资源设置 |

#### 4.Entity

|  类名   |      说明      |
| :-----: | :------------: |
| SysItem | 众筹项目的参数 |
| SysUser |   用户的参数   |

#### 5.Interceptor

|       类名       |      说明       |
| :--------------: | :-------------: |
| TokenInterceptor | Token验证拦截器 |

#### 6.Param

|      类名       |          说明           |
| :-------------: | :---------------------: |
|   ApiResponse   |   验证Token的返回信息   |
| ApiResponseEnum | 验证Token的返回信息枚举 |
|     Result      |      接口回复信息       |

#### 7.Service

这里分接口和实现类，接口在service目录下，接口实现类在service/impl目录下。

|       类名        |       说明        |
| :---------------: | :---------------: |
| SysItemServiceImp | SysItem的相关操作 |
| SysUserServiceImp | SysUser的相关操作 |

#### 8.Utils

存放相关使用工具的

|      类名       |        说明        |
| :-------------: | :----------------: |
| ApiResponseUtil |     响应的API      |
|    JwtUtils     | 一个token加密的API |
| VerifyCodeUtil  |   验证token的API   |

