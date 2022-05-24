-- ----------------------------
-- 部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept (
  id                bigint(20)      not null auto_increment    comment '部门id',
  parent_id         bigint(20)      default 0                  comment '父部门id',
  ancestors         varchar(1000)   default ''                 comment '祖级列表（为兼容Snowflake算法的长ID，此字段长度设为1000）',
  dept_name         varchar(100)    default ''                 comment '部门名称',
  order_num         int(4)          default 0                  comment '显示顺序',
  leader            varchar(50)     default ''                 comment '负责人',
  mobile            varchar(50)     default ''                 comment '负责人手机号码',
  email             varchar(64)     default ''                 comment '邮箱',
  status            tinyint(4)      default 1                  comment '部门状态（0=停用,1=正常）',
  creator_id        bigint(20)      default 0                  comment '创建者id',
  creator_info      varchar(500)    default ''                 comment '创建时姓名_登录账号',
  create_time 	    datetime                                   comment '创建时间',
  updater_id        bigint(20)      default 0                  comment '更新者id',
  updater_info      varchar(500)    default ''                 comment '更新时姓名_登录账号',
  update_time       datetime                                   comment '更新时间',
  ext_json          varchar(3000)   default ''                 comment '扩展JSON',
  deleted           bit(1)          default 0                  comment '删除标志（false=存在,true=删除）',
  version           int(11)         default 0                  comment '乐观锁数据版本',
  primary key (id)
) engine=innodb auto_increment=200 comment = '部门表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
insert into sys_dept values(100,  0,   '0',          '集团总部',   0, 'airoland', '15888888888', '123456@qq.com', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_dept values(101,  100, '0,100',      '北京分公司', 1, 'airoland', '15888888888', '123456@qq.com', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_dept values(102,  100, '0,100',      '长沙分公司', 2, 'airoland', '15888888888', '123456@qq.com', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_dept values(103,  101, '0,100,101',  '研发部门',   1, 'airoland', '15888888888', '123456@qq.com', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_dept values(104,  101, '0,100,101',  '市场部门',   2, 'airoland', '15888888888', '123456@qq.com', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_dept values(105,  101, '0,100,101',  '测试部门',   3, 'airoland', '15888888888', '123456@qq.com', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_dept values(106,  101, '0,100,101',  '财务部门',   4, 'airoland', '15888888888', '123456@qq.com', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_dept values(107,  101, '0,100,101',  '运维部门',   5, 'airoland', '15888888888', '123456@qq.com', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_dept values(108,  102, '0,100,102',  '市场部门',   1, 'airoland', '15888888888', '123456@qq.com', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_dept values(109,  102, '0,100,102',  '财务部门',   2, 'airoland', '15888888888', '123456@qq.com', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);


-- ----------------------------
-- 用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
  id                bigint(20)      not null auto_increment    comment '用户ID',
  dept_id           bigint(20)      not null                   comment '部门ID',
  username          varchar(50)     not null                   comment '用户名',
  person_name       varchar(50)     default ''                 comment '用户姓名',
  email             varchar(64)     default ''                 comment '用户邮箱',
  mobile            varchar(50)     default ''                 comment '手机号码',
  gender            tinyint(4)       default 0                 comment '用户性别（0=保密,1=男,2=女）',
  avatar            varchar(100)    default ''                 comment '头像地址',
  password          varchar(100)    default ''                 comment '密码',
  id_card           varchar(50)     default ''                 comment '证件号码',
  card_type         tinyint(4)      default 1                  comment '证件类型',
  nickname          varchar(50)     default ''                 comment '用户昵称',
  status            tinyint(4)       default 1                 comment '帐号状态（0=停用,1=正常）',
  login_ip          varchar(50)     default ''                 comment '最后登录IP',
  login_location    varchar(190)    default ''                 comment '最后登录地点',
  login_date        datetime                                   comment '最后登录时间',
  creator_id        bigint(20)      default 0                  comment '创建者id',
  creator_info      varchar(500)    default ''                 comment '创建时姓名_登录账号',
  create_time 	    datetime                                   comment '创建时间',
  updater_id        bigint(20)      default 0                  comment '更新者id',
  updater_info      varchar(500)    default ''                 comment '更新时姓名_登录账号',
  update_time       datetime                                   comment '更新时间',
  ext_json          varchar(3000)   default ''                 comment '扩展JSON',
  deleted           bit(1)          default 0                  comment '删除标志（false=存在,true=删除）',
  version           int(11)         default 0                  comment '乐观锁数据版本',
  primary key (id),
  KEY `idx_mobile` (`mobile`)
) engine=innodb auto_increment=100 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user values(1,  103, 'admin', '管理员', '123456@qq.com', '15888888888', '1', '', '41ae2142375ca87970787369850d8790330734b21cb8ee4e', '', 1, '', 1, '127.0.0.1', '内网地址', '2021-01-22 11:56:00', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_user values(2,  105, 'airboot', '业务员', '456789@qq.com',  '15666666666', '1', '', '41ae2142375ca87970787369850d8790330734b21cb8ee4e', '', 1, '', 1, '127.0.0.1', '内网地址', '2021-01-22 11:56:00', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);


-- ----------------------------
-- 岗位信息表
-- ----------------------------
drop table if exists sys_post;
create table sys_post (
  id                bigint(20)      not null auto_increment    comment '岗位ID',
  post_code         varchar(64)     not null                   comment '岗位编码',
  post_name         varchar(50)     not null                   comment '岗位名称',
  post_sort         int(4)          not null                   comment '显示顺序',
  status            tinyint(4)      not null default 1         comment '状态（0=停用,1=正常）',
  creator_id        bigint(20)      default 0                  comment '创建者id',
  creator_info      varchar(500)    default ''                 comment '创建时姓名_登录账号',
  create_time 	    datetime                                   comment '创建时间',
  updater_id        bigint(20)      default 0                  comment '更新者id',
  updater_info      varchar(500)    default ''                 comment '更新时姓名_登录账号',
  update_time       datetime                                   comment '更新时间',
  ext_json          varchar(3000)   default ''                 comment '扩展JSON',
  deleted           bit(1)          default 0                  comment '删除标志（false=存在,true=删除）',
  version           int(11)         default 0                  comment '乐观锁数据版本',
  primary key (id)
) engine=innodb comment = '岗位信息表';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
insert into sys_post values(1, 'ceo',  '董事长',    1, 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_post values(2, 'se',   '项目经理',  2, 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_post values(3, 'hr',   '人力资源',  3, 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_post values(4, 'user', '普通员工',  4, 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);


-- ----------------------------
-- 角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
  id                bigint(20)      not null auto_increment    comment '角色ID',
  role_name         varchar(30)     not null                   comment '角色名称',
  role_type         smallint(6)     default 0                  comment '角色类型',
  role_sort         int(4)          not null                   comment '显示顺序',
  data_scope        tinyint(4)      default 1                  comment '数据范围（1=全部数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限）',
  status            tinyint(4)      not null default 1         comment '角色状态（0=停用,1=正常）',
  creator_id        bigint(20)      default 0                  comment '创建者id',
  creator_info      varchar(500)    default ''                 comment '创建时姓名_登录账号',
  create_time 	    datetime                                   comment '创建时间',
  updater_id        bigint(20)      default 0                  comment '更新者id',
  updater_info      varchar(500)    default ''                 comment '更新时姓名_登录账号',
  update_time       datetime                                   comment '更新时间',
  ext_json          varchar(3000)   default ''                 comment '扩展JSON',
  deleted           bit(1)          default 0                  comment '删除标志（false=存在,true=删除）',
  version           int(11)         default 0                  comment '乐观锁数据版本',
  primary key (id)
) engine=innodb auto_increment=100 comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role values('1', '管理员',   -500,  1, 1, 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_role values('2', '普通角色', 0, 2, 2, 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);


-- ----------------------------
-- 用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
  user_id   bigint(20) not null comment '用户ID',
  role_id   bigint(20) not null comment '角色ID',
  primary key(user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role values ('1', '1');
insert into sys_user_role values ('2', '2');


-- ----------------------------
-- 角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
  role_id   bigint(20) not null comment '角色ID',
  menu_id   bigint(20) not null comment '菜单ID',
  primary key(role_id, menu_id)
) engine=innodb comment = '角色和菜单关联表';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
-- 管理员
insert into sys_role_menu values ('1', '1');
insert into sys_role_menu values ('1', '2');
insert into sys_role_menu values ('1', '3');
insert into sys_role_menu values ('1', '100');
insert into sys_role_menu values ('1', '101');
insert into sys_role_menu values ('1', '103');
insert into sys_role_menu values ('1', '104');
insert into sys_role_menu values ('1', '106');
insert into sys_role_menu values ('1', '107');
insert into sys_role_menu values ('1', '108');
insert into sys_role_menu values ('1', '109');
insert into sys_role_menu values ('1', '111');
insert into sys_role_menu values ('1', '112');
insert into sys_role_menu values ('1', '113');
insert into sys_role_menu values ('1', '115');
insert into sys_role_menu values ('1', '500');
insert into sys_role_menu values ('1', '501');
insert into sys_role_menu values ('1', '1001');
insert into sys_role_menu values ('1', '1002');
insert into sys_role_menu values ('1', '1003');
insert into sys_role_menu values ('1', '1004');
insert into sys_role_menu values ('1', '1005');
insert into sys_role_menu values ('1', '1006');
insert into sys_role_menu values ('1', '1007');
insert into sys_role_menu values ('1', '1008');
insert into sys_role_menu values ('1', '1009');
insert into sys_role_menu values ('1', '1010');
insert into sys_role_menu values ('1', '1011');
insert into sys_role_menu values ('1', '1012');
insert into sys_role_menu values ('1', '1017');
insert into sys_role_menu values ('1', '1018');
insert into sys_role_menu values ('1', '1019');
insert into sys_role_menu values ('1', '1020');
insert into sys_role_menu values ('1', '1021');
insert into sys_role_menu values ('1', '1022');
insert into sys_role_menu values ('1', '1023');
insert into sys_role_menu values ('1', '1024');
insert into sys_role_menu values ('1', '1025');
insert into sys_role_menu values ('1', '1031');
insert into sys_role_menu values ('1', '1032');
insert into sys_role_menu values ('1', '1033');
insert into sys_role_menu values ('1', '1034');
insert into sys_role_menu values ('1', '1035');
insert into sys_role_menu values ('1', '1036');
insert into sys_role_menu values ('1', '1037');
insert into sys_role_menu values ('1', '1038');
insert into sys_role_menu values ('1', '1039');
insert into sys_role_menu values ('1', '1040');
insert into sys_role_menu values ('1', '1041');
insert into sys_role_menu values ('1', '1042');
insert into sys_role_menu values ('1', '1043');
insert into sys_role_menu values ('1', '1044');
insert into sys_role_menu values ('1', '1045');
insert into sys_role_menu values ('1', '1046');
insert into sys_role_menu values ('1', '1047');
insert into sys_role_menu values ('1', '1048');
-- 业务员
insert into sys_role_menu values ('2', '1');
insert into sys_role_menu values ('2', '100');
insert into sys_role_menu values ('2', '101');
insert into sys_role_menu values ('2', '103');
insert into sys_role_menu values ('2', '104');
insert into sys_role_menu values ('2', '1001');
insert into sys_role_menu values ('2', '1002');
insert into sys_role_menu values ('2', '1003');
insert into sys_role_menu values ('2', '1004');
insert into sys_role_menu values ('2', '1005');
insert into sys_role_menu values ('2', '1006');
insert into sys_role_menu values ('2', '1007');
insert into sys_role_menu values ('2', '1008');
insert into sys_role_menu values ('2', '1009');
insert into sys_role_menu values ('2', '1010');
insert into sys_role_menu values ('2', '1011');
insert into sys_role_menu values ('2', '1012');
insert into sys_role_menu values ('2', '1017');
insert into sys_role_menu values ('2', '1018');
insert into sys_role_menu values ('2', '1019');
insert into sys_role_menu values ('2', '1020');
insert into sys_role_menu values ('2', '1021');
insert into sys_role_menu values ('2', '1022');
insert into sys_role_menu values ('2', '1023');
insert into sys_role_menu values ('2', '1024');
insert into sys_role_menu values ('2', '1025');


-- ----------------------------
-- 角色和部门关联表  角色1-N部门
-- ----------------------------
drop table if exists sys_role_dept;
create table sys_role_dept (
  role_id   bigint(20) not null comment '角色ID',
  dept_id   bigint(20) not null comment '部门ID',
  primary key(role_id, dept_id)
) engine=innodb comment = '角色和部门关联表';

-- ----------------------------
-- 初始化-角色和部门关联表数据
-- ----------------------------
insert into sys_role_dept values ('2', '105');


-- ----------------------------
-- 用户与岗位关联表  用户1-N岗位
-- ----------------------------
drop table if exists sys_user_post;
create table sys_user_post
(
  user_id   bigint(20) not null comment '用户ID',
  post_id   bigint(20) not null comment '岗位ID',
  primary key (user_id, post_id)
) engine=innodb comment = '用户与岗位关联表';

-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------
insert into sys_user_post values ('1', '1');
insert into sys_user_post values ('2', '2');


-- ----------------------------
-- 操作日志记录
-- ----------------------------
drop table if exists sys_oper_log;
create table sys_oper_log (
  id                bigint(20)      not null auto_increment    comment '日志主键',
  title             varchar(50)     default ''                 comment '模块标题',
  operation_type    tinyint(4)      default 0                  comment '操作类型（0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据）',
  method            varchar(100)    default ''                 comment '方法名称',
  request_method    varchar(10)     default ''                 comment '请求方式',
  device            tinyint(4)      default 10                 comment '操作设备（10=PC端,20=手机APP,30=微信小程序）',
  browser           varchar(50)     default ''                 comment '浏览器类型',
  os                varchar(50)     default ''                 comment '操作系统',
  oper_account      varchar(190)    default ''                 comment '操作人员账号',
  oper_name         varchar(190)    default ''                 comment '操作人员姓名',
  oper_user_id      bigint(20)      default 0                  comment '操作人员ID',
  dept_name         varchar(50)     default ''                 comment '部门名称',
  oper_url          varchar(500)    default ''                 comment '请求URL',
  oper_ip           varchar(50)     default ''                 comment '主机地址',
  oper_location     varchar(190)    default ''                 comment '操作地点',
  oper_param        varchar(2000)   default ''                 comment '请求参数',
  json_result       varchar(2000)   default ''                 comment '返回参数',
  status            tinyint(4)      default 1                  comment '操作状态（0=失败,1=成功）',
  error_msg         varchar(2000)   default ''                 comment '错误消息',
  oper_time         datetime                                   comment '操作时间',
  creator_id        bigint(20)      default 0                  comment '创建者id',
  creator_info      varchar(500)    default ''                 comment '创建时姓名_登录账号',
  create_time 	    datetime                                   comment '创建时间',
  updater_id        bigint(20)      default 0                  comment '更新者id',
  updater_info      varchar(500)    default ''                 comment '更新时姓名_登录账号',
  update_time       datetime                                   comment '更新时间',
  ext_json          varchar(3000)   default ''                 comment '扩展JSON',
  deleted           bit(1)          default 0                  comment '删除标志（false=存在,true=删除）',
  version           int(11)         default 0                  comment '乐观锁数据版本',
  primary key (id)
) engine=innodb auto_increment=100 comment = '操作日志记录';


-- ----------------------------
-- 参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  id                bigint(20)      not null auto_increment    comment '参数主键',
  config_name       varchar(100)    default ''                 comment '参数名称',
  config_key        varchar(100)    default ''                 comment '参数键名',
  config_value      varchar(2000)   default ''                 comment '参数键值',
  built_in          bit(1)          default 0                  comment '是否系统内置（false=否,true=是）',
  need_login        bit(1)          default 1                  comment '是否需要登录验证（false=否,true=是）',
  status            tinyint(4)      default 1                  comment '状态（0=停用,1=正常）',
  creator_id        bigint(20)      default 0                  comment '创建者id',
  creator_info      varchar(500)    default ''                 comment '创建时姓名_登录账号',
  create_time 	    datetime                                   comment '创建时间',
  updater_id        bigint(20)      default 0                  comment '更新者id',
  updater_info      varchar(500)    default ''                 comment '更新时姓名_登录账号',
  update_time       datetime                                   comment '更新时间',
  ext_json          varchar(3000)   default ''                 comment '扩展JSON',
  deleted           bit(1)          default 0                  comment '删除标志（false=存在,true=删除）',
  version           int(11)         default 0                  comment '乐观锁数据版本',
  primary key (id)
) engine=innodb auto_increment=100 comment = '参数配置表';

insert into sys_config values(1, '主框架页-默认皮肤样式名称', 'sys.index.skinName',     'skin-blue',     1, 1, 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '{"remark":"蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow"}', 0, 0);
insert into sys_config values(2, '用户管理-账号初始密码',     'sys.user.initPassword',  '123456',        1, 1, 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '{"remark":"初始化密码 123456"}', 0, 0);
insert into sys_config values(3, '主框架页-侧边栏主题',       'sys.index.sideTheme',    'theme-dark',    1, 1, 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '{"remark":"深色主题theme-dark，浅色主题theme-light"}', 0, 0);


-- ----------------------------
-- 系统访问记录
-- ----------------------------
drop table if exists sys_logininfor;
create table sys_logininfor (
  id                bigint(20)      not null auto_increment    comment '访问ID',
  account           varchar(190)    default ''                 comment '用户登录账号',
  user_id           bigint(20)      default 0                  comment '用户ID',
  ipaddr            varchar(50)     default ''                 comment '登录IP地址',
  login_location    varchar(190)    default ''                 comment '登录地点',
  device            tinyint(4)      default 10                 comment '登录设备（10=PC端,20=手机APP,30=微信小程序）',
  browser           varchar(50)     default ''                 comment '浏览器类型',
  os                varchar(50)     default ''                 comment '操作系统',
  login_result      tinyint(4)      default 1                  comment '登录结果（1=登录成功,-1=登录失败,11=退出成功）',
  msg               varchar(190)    default ''                 comment '提示消息',
  login_time        datetime                                   comment '登录时间',
  creator_id        bigint(20)      default 0                  comment '创建者id',
  creator_info      varchar(500)    default ''                 comment '创建时姓名_登录账号',
  create_time 	    datetime                                   comment '创建时间',
  updater_id        bigint(20)      default 0                  comment '更新者id',
  updater_info      varchar(500)    default ''                 comment '更新时姓名_登录账号',
  update_time       datetime                                   comment '更新时间',
  ext_json          varchar(3000)   default ''                 comment '扩展JSON',
  deleted           bit(1)          default 0                  comment '删除标志（false=存在,true=删除）',
  version           int(11)         default 0                  comment '乐观锁数据版本',
  primary key (id)
) engine=innodb auto_increment=100 comment = '系统访问记录';


-- ----------------------------
-- 通知公告表
-- ----------------------------
drop table if exists sys_notice;
create table sys_notice (
  id                bigint(20)      not null auto_increment    comment '公告ID',
  notice_title      varchar(50)     not null                   comment '公告标题',
  notice_type       tinyint(4)      not null                   comment '公告类型（1=通知,2=公告）',
  notice_content    varchar(2000)   default ''                 comment '公告内容',
  status            tinyint(4)      default 1                  comment '公告状态（0=关闭,1=正常）',
  creator_id        bigint(20)      default 0                  comment '创建者id',
  creator_info      varchar(500)    default ''                 comment '创建时姓名_登录账号',
  create_time 	    datetime                                   comment '创建时间',
  updater_id        bigint(20)      default 0                  comment '更新者id',
  updater_info      varchar(500)    default ''                 comment '更新时姓名_登录账号',
  update_time       datetime                                   comment '更新时间',
  ext_json          varchar(3000)   default ''                 comment '扩展JSON',
  deleted           bit(1)          default 0                  comment '删除标志（false=存在,true=删除）',
  version           int(11)         default 0                  comment '乐观锁数据版本',
  primary key (id)
) engine=innodb auto_increment=10 comment = '通知公告表';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------
insert into sys_notice values('1', '温馨提醒：新版本发布啦', '2', '新版本内容', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_notice values('2', '维护通知：系统凌晨维护', '1', '维护内容', 1, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);


commit;
