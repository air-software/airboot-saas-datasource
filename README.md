# Airboot-SaaS-DataSource

### 注意：如需多租户非动态数据源版本请移步至 <a href="https://github.com/air-software/airboot-saas" target="_blank">Airboot-SaaS</a> 。

### 注意：如需非多租户版本请移步至 <a href="https://github.com/air-software/airboot" target="_blank">Airboot</a> 。

### 正式使用前请务必仔细阅读[功能说明及注意事项](#功能说明及注意事项)

---

## 介绍

Airboot-SaaS-DataSource是轻量级通用多租户管理系统 <a href="https://github.com/air-software/airboot-saas" target="_blank">Airboot-SaaS</a> 的动态数据源版本。在主要功能上将与 <a href="https://github.com/air-software/airboot-saas" target="_blank">Airboot-SaaS</a> 和 <a href="https://github.com/air-software/airboot" target="_blank">Airboot</a> 保持一致。

Airboot-SaaS-DataSource支持【**共享数据源，独立Schema**】和【**独立数据源**】的SaaS模式，完美支持**混合云**。租户之间**物理隔离**，理论上租户数量可无限扩展，不会遭遇【**共享Schema，共享数据表**】模式的性能瓶颈，即使某个租户的数据量巨大也不会影响其他租户的体验，客户可随时将自己的Schema私有化。

本系统支持`数据源 --> Schema`一对多的灵活配置，你可以根据部署成本、租户数据量或客户要求等因素，来决定某租户是跟其他租户共享数据源（租户自己的Schema都是专属独立的互不影响），还是完全独享一个数据源，以此来达到混合云的效果。如果某个租户数据量巨大，你可以对其单独进行分库分表等优化，最后映射出一个统一的VIP或域名配置到本系统内即可。如果数据源配置有变更，本系统支持**不停机动态切换**。

关于Schema的定义可参考 <a href="https://wenku.baidu.com/view/8882036ecb50ad02de80d4d8d15abe23482f0316.html" target="_blank">数据库中的Schema是什么？</a> ，**如果你使用的是MySQL，则Schema = Database**。

### 相关技术

- 使用 <a href="https://github.com/air-software/saas-datasource-spring-boot-starter" target="_blank">saas-datasource-spring-boot-starter</a> 来动态新增和切换数据源
- 前端使用 Vue + Element
- 后端使用 Spring Boot + Mybatis-Plus
- 缓存使用 Redis，数据库使用 MySQL
- 登录验证使用 JWT，支持切换唯一登录
- 使用代码生成器可以一键生成前后端代码

### 项目背景

本系统初期基于 <a href="https://gitee.com/y_project/RuoYi-Vue" target="_blank">若依（RuoYi-Vue）</a> 二次开发，启动开发时对应的若依版本在2.2至2.3之间，之后的功能路线与若依不同，基本没有再合并若依的更新。

若依本身是一个优秀的开源项目，我根据**个人开发习惯**做了一些架构和细节上的改动，其中后端改动较大，前端改动较小，UI风格没有变化（若依也是直接采用了vue-admin-element），欢迎有**类似开发习惯**的小伙伴们尝试使用。

主要改动如下：

1. 添加多租户支持，兼容【**共享数据源，独立Schema**】和【**独立数据源**】的模式；
2. 将`Mybatis`替换为 <a href="https://baomidou.com/" target="_blank">Mybatis-Plus</a> ，简化了Mapper代码；
3. 去掉了`字典管理`功能，全部改为枚举实现。这东西见仁见智，只是我个人更习惯用枚举，没有绝对的好坏。非要说原因的话，请看：[为什么去掉字典管理？](#为什么去掉字典管理)
4. 去掉了较重的`Spring-Security`，改为自定义拦截器实现，原因请看：[为什么去掉Spring-Security？](#为什么去掉Spring-Security)
5. 其他细节改动，如代码风格、框架配置、工具类封装、代码生成等等。

---

## 如何开始

### 环境准备

- JDK 8+
- Node.js 10+
- MySQL 5.5+
- Redis

### 后端启动

以MySQL为例（字符集utf8mb4）：
1. 打开`airboot-server/sql/common.sql`，将数据源表中的初始化数据改成你自己的数据源配置，租户表中的初始化数据务必确保有一个id为1的租户（只需保证id为1即可，租户名称等都可以改动），其他租户可以之后在系统中创建；
2. 新建一个数据库作为公共库，将`common.sql`导入数据库中建表，随后在`application-dev.yml`配置文件中，将`spring.datasource.dynamic.datasource.common`里的配置改为你建好的公共库配置；
3. 查看租户表中id为1的租户的`schema_name`，并以此为名称新建一个数据库作为管理平台租户，随后将`airboot-server/sql/tenant.sql`导入此库中建表；
2. 检查`application.yml`及`application-dev.yml`中各项配置，比如Redis的链接地址；
3. 在IDE中启动开发调试，观察日志输出，启动成功后会出现`Airboot Server启动成功！`的提示。

### 前端启动

在`airboot-web`目录下打开命令行：

```
# 使用淘宝源来加快下载速度
npm config set registry http://registry.npm.taobao.org/

# 解决node-sass可能下载出错的问题
npm config set sass_binary_site https://npm.taobao.org/mirrors/node-sass/

# 安装依赖
npm install

# 启动服务
npm run dev
```

启动成功后浏览器访问 http://localhost/

管理员账号 admin，密码 admin123

如果你选择了管理平台租户登录，则管理员账号具有`超级租户管理员`权限，是系统内的最高权限。

如果登录其他租户，则管理员只是租户内部的管理员权限。

---

## 功能说明及注意事项

### 租户切换

本系统已经在拦截器中自动处理了租户切换逻辑，如果你想自己手动切换租户，或想了解租户切换原理，请前往 <a href="https://github.com/air-software/saas-datasource-spring-boot-starter" target="_blank">saas-datasource-spring-boot-starter</a> 仔细阅读说明文档。

### 关于事务

**谨记一点：不要在事务内部做任何切换数据源的操作**！无论是`SaaSDataSource.switchTo(dsKey)`方法、`@SaaS`注解还是`@DS`注解，都必须保证他们在事务开启前生效，否则在事务内部无法正常切换数据源。

具体事务相关注意事项请仔细阅读 <a href="https://github.com/air-software/saas-datasource-spring-boot-starter" target="_blank">saas-datasource-spring-boot-starter</a> 的说明文档。

### 关于登录

从大的方向上来讲，SaaS的登录一般有两类方案：

1. 使用用户名或工号等租户内唯一但整个系统内不唯一的字段作为登录账号，这种场景需要在登录时即确定是哪个租户，否则无法知道该去哪个租户里校验账号和密码。而登录时确定租户一般有两种方式：下拉选择或填写租户名称。选择的用户体验较好但会暴露你目前的租户数量，填写的用户体验较差但数据安全性更高。
2. 使用手机号等整个系统内唯一的字段作为登录账号，这种场景在登录时无需选择租户，用户体验上最好。如果一个账号对应多个租户，则一般在登录后让用户选择或切换。这种方案在不同的SaaS模式下开发难度不同：
    1. 如果你使用的是 <a href="https://github.com/air-software/airboot-saas" target="_blank">Airboot-SaaS</a> 的【**共享Schema，共享数据表**】模式，则用户都在同一张表中靠租户ID区分，直接查用户表即可；
    2. 如果你使用的是本系统的【**共享数据源，独立Schema**】和【**独立数据源**】模式，则用户都存在自己租户的Schema中，即使登录账号系统唯一我们也无法提前知晓在哪个租户中。此时就需要考虑其他解决方式，比如我们可以在公共库中建一张仅供登录使用的全量用户表，登录时先在此表中查询出用户对应的租户Schema，随后切换至对应租户完成登录。但这样一来如果租户中的用户发生CRUD，也需要同步至公共库的此表中，这种数据一致性的问题也是需要认真考虑的。

综上所述，具体采用什么登录方案，是要根据用户体验及技术难度等多方面因素来取舍的。**本系统为简便起见，采用了上述第一种登录方案，开发者可自行更改**。

### 数据源和租户管理

系统运行过程中可在`数据源管理`页面随时添加新的数据源，在新增租户时可以选择已经建好的数据源。

**注意：本系统为演示方便，默认在页面上明文展示数据源的用户名和密码，开发者正式使用时务必根据自身安全策略自行修改展示方式。**

新增租户会直接动态新建Schema并执行初始化脚本，默认的初始化脚本为`airboot-server/sql/tenant.sql`，你也可以修改`tenant.sql-script-name`配置来指定其他脚本（目录仍然是`airboot-server/sql/`）。或者修改`SqlUtil.executeScript`方法来指定其他目录。

### 批量刷库

假设你在某次迭代后产生了数据库变更，而此时已经有很多租户正在使用了，手动去给每个Schema执行变更是很繁琐且存在风险的。

此时可以使用`租户管理页面`中的`执行SQL`功能来进行自动批量刷库，可以选择全部租户（不含公共库）、只刷公共库或指定租户。密钥默认airboot，你可以修改密钥或自定义其他安全策略。

### 超级租户管理员

id为1的租户中的管理员是超级租户管理员，拥有系统中的最高权限。

超级租户管理员可以在右上角随意切换至其他租户。

其他租户的管理员的权限实质上由超级租户管理员来分配，你也可以在租户初始化脚本中预设管理员的权限。假设你新增了某个功能，想给所有现有租户的管理员都分配此功能的权限，可以使用批量刷库。

### 关于代码生成

注意`代码生成`功能默认只识别公共库中的数据表，使用时需要先在公共库中建新的表，或者修改代码以指向其他数据源（如专门用于代码生成的库），由开发者自行决定。

---

## 演示截图

![登录页](https://air-software.github.io/static/image/airboot-saas-datasource/login.png)

![登录页-选择租户](https://air-software.github.io/static/image/airboot-saas-datasource/login-select.png)

![首页](https://air-software.github.io/static/image/airboot-saas-datasource/index.png)

![数据源管理](https://air-software.github.io/static/image/airboot-saas-datasource/datasource.png)

![租户管理](https://air-software.github.io/static/image/airboot-saas-datasource/tenant.png)

![执行SQL](https://air-software.github.io/static/image/airboot-saas-datasource/execute-sql.png)

![执行SQL-选择租户](https://air-software.github.io/static/image/airboot-saas-datasource/execute-sql-select.png)

![用户管理](https://air-software.github.io/static/image/airboot-saas-datasource/user.png)

![角色管理](https://air-software.github.io/static/image/airboot-saas-datasource/role.png)

![菜单管理](https://air-software.github.io/static/image/airboot-saas-datasource/menu.png)

![代码生成-字段编辑](https://air-software.github.io/static/image/airboot-saas-datasource/table-column.png)

![代码生成-生成信息](https://air-software.github.io/static/image/airboot-saas-datasource/gen-info.png)

![代码生成-预览](https://air-software.github.io/static/image/airboot-saas-datasource/gen-preview.png)

---

## FAQ

### 为什么切换租户没有成功？

请按以下步骤排查问题：

1. 确保你没有在事务内进行切换租户的操作，原因参见 [关于事务](#关于事务)；
2. 对于异步操作、定时任务等情况，必须使用`SaaSDataSource.switchTo(dsKey)`手动切换租户；
3. 如果你在数据库中对正在使用中的租户进行数据源配置的更改，那么由于数据源缓存池的存在，你的更改并不会生效。请参考`数据源管理`和`租户管理`的后端更新逻辑，移除相应的缓存即可；
4. 再次检查你的数据源各项配置是否正确，如主机地址、密码等；
5. 如果问题仍未解决，请提ISSUE，完整描述你的问题场景。

### 为什么去掉字典管理？

1. 字典看起来灵活，能够随时新增和修改，但如果没有业务关联就是没有意义的数据。而如果想要做业务关联，大部分情况下还是要去修改代码的，**字典并没有减轻你的工作**。比如在 <a href="https://gitee.com/y_project/RuoYi-Vue" target="_blank">RuoYi-Vue</a> 的源代码中，虽然字典定义了`0`是正常状态，`1`是停用状态，但依然要在代码里用常量类 <a href="https://gitee.com/y_project/RuoYi-Vue/blob/0a75dcdd85c2c4921c7f3997f8c90214f5202a25/ruoyi-common/src/main/java/com/ruoyi/common/constant/UserConstants.java" target="_blank">UserConstants.java</a> 来为`0`和`1`赋予具体的业务含义，然后再拿来比对用户或部门的状态等，其实是比添加和使用枚举更繁琐了一点。
2. 我认为最适合使用字典的是像`新闻类别`这样的数据，只要我预先写好代码，就可以动态增改新闻类别，因为处理逻辑都是一样的。但要注意，像这样的字典，**恰恰不应该放进像`字典管理`这样统一的功能中**。原因如下：
   1. 首先从权限上就不好控制，我想授权一个人管理新闻类别，但不想让他看到其他的字典，这是很正常的权限需求，但如果所有字典数据全都放在`字典管理`里，这个权限就很难控制，<a href="https://gitee.com/y_project/RuoYi-Vue" target="_blank">RuoYi-Vue</a> 并没有在字典管理上实现这种权限细分，而如果你想自己实现也是很繁琐的。
   2. 其次，我的`新闻类别`不一定是只要有`字典名称`和`字典值`这两个字段就能满足了，我可能还需要其他属性，但我总不能去给字典表随便添加字段吧？
   
   所以正确的做法是：单独建一张`新闻类别表`，单独加一个`新闻类别管理`功能，这样不但业务清晰，也便于管理。
   
3. 如果我们在系统中大量使用字典，就意味着我们在所有用到字典数据的地方都需要去后端获取，否则用户会无法使用查询条件，列表数据里的字典值也无法转义。这在请求量少的时候没多大问题，但如果请求非常多，**对数据库的压力是显而易见的**。可如果把字典数据拉到缓存里，又会面临缓存和数据库双写的问题。或者我们可以设置一个按钮来手动刷新缓存，但也会存在忘记刷新的风险。

   而如果我们使用枚举，这些问题都是不会存在的。

4. `字典管理`如果想要做的完善，还需要考虑很多细节。比如是否需要对修改和删除进行控制，如果不控制，那**字典值修改或删除后，使用到此字典值的旧数据怎么办**？如果要控制，那就需要遍历可能用到字典值的数据表，并作出相应处理。在 <a href="https://gitee.com/y_project/RuoYi-Vue" target="_blank">RuoYi-Vue</a> 的源代码中是直接就可以修改和删除的，并没有做控制处理，而如果我们自己实现的话也需要花很多时间和精力。相比之下枚举就不存在这样的问题。

综上所述，我并非反对使用字典，而是认为使用字典一定要有合适的情况和明确的目的（比如单独的`新闻类别管理`是合适且明确的），像统一的`字典管理`其实是没有必要的。


### 为什么去掉Spring-Security？

`Spring-Security`的确是功能强大的权限框架，但一方面是较重，不符合Airboot-SaaS轻量级的理念。另一方面，如果想要用好`Spring-Security`的强大功能，需要对其有一定的熟练度，**这无疑会给想使用Airboot-SaaS进行二次开发的人设置了门槛**，所以我将其替换成了更易于理解和改造的自定义拦截器。

个人认为，权限这个东西，说通用也通用，说不通用也不通用，很多公司在权限上其实都会有自己的特殊需求。除非你的目的是做一个庞大且复杂的管理系统，否则没有必要考虑用`Spring-Security`。

### 为什么本项目中的枚举几乎都是中文？

1. 增加了代码的可读性，尤其对于业务类枚举来说（在某些专业领域的业务中更是如此），不必再绞尽脑汁想合适的专业英文翻译或拼音简写。除非你所在的项目团队有外国开发人员，或者你的开发/部署环境不支持中文字符；
2. 中文枚举可以直接返回给前端作为字符串展示，大部分情况下不需要再次翻译给用户看。

综上所述，我一直推荐使用中文来命名业务相关的枚举、常量或局部变量，但对于类似实体类的属性等不推荐使用中文，因为会导致getter和setter的方法名看起来很怪。
