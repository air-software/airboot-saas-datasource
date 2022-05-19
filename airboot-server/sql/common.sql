-- ----------------------------
-- 关闭外键约束，避免被Quartz表中的外键影响启动。
-- 注意：如果重启了MySQL服务，要先用 select @@foreign_key_checks; 检查外键约束的值，如果值为1表示又被开启了，需要再运行下面这条SQL语句关闭约束。
-- ----------------------------
set global foreign_key_checks = 0;
set foreign_key_checks = 0;

-- ----------------------------
-- 数据源表
-- 注意：如果你准备使用的mysql-connector-java版本不支持SPI，则需要在这张表中加上driver_class_name字段。
-- ----------------------------
drop table if exists sys_data_source;
create table sys_data_source (
  id                bigint(20)      not null auto_increment    comment '数据源id',
  name              varchar(190)    default ''                 comment '数据源名称',
  host              varchar(190)    default ''                 comment '主机地址（域名或IP，支持端口号）',
  username          varchar(190)    default ''                 comment '用户名',
  password          varchar(190)    default ''                 comment '密码',
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
) engine=innodb auto_increment=100 comment = '数据源表';

insert into sys_data_source values(1, '数据源1', 'host1', 'root', '123456', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_data_source values(2, '数据源2', 'host2', 'root', '456789', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);


-- ----------------------------
-- 租户表
-- ----------------------------
drop table if exists sys_tenant;
create table sys_tenant (
  id                bigint(20)      not null auto_increment    comment '租户id',
  data_source_id    bigint(20)      not null                   comment '数据源id',
  schema_name       varchar(190)    not null                   comment 'Schema（数据库）名称',
  tenant_name       varchar(100)    not null                   comment '租户名称',
  person_name       varchar(50)     default ''                 comment '负责人姓名',
  mobile            varchar(50)     default ''                 comment '负责人手机号码',
  email             varchar(64)     default ''                 comment '负责人邮箱',
  status            tinyint(4)      default 1                  comment '租户状态（0=停用,1=正常）',
  tenant_type       smallint(6)     default 0                  comment '租户类型',
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
) engine=innodb auto_increment=100 comment = '租户表';

insert into sys_tenant values(1, 1, 'management', '管理平台', 'airoland', '18812345678', '123456@qq.com', 1, -1000, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_tenant values(2, 1, 'tenant1', '租户1', 'airoland', '18812345678', '123456@qq.com', 1, 0, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_tenant values(3, 2, 'tenant2', '租户2', 'airoland', '18812345678', '123456@qq.com', 1, 0, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);


-- ----------------------------
-- 菜单表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  id                bigint(20)      not null auto_increment    comment '菜单ID',
  menu_name         varchar(50)     not null                   comment '菜单名称',
  parent_id         bigint(20)      default 0                  comment '父菜单ID',
  order_num         int(4)          default 0                  comment '显示顺序',
  path              varchar(100)    default ''                 comment '路由地址',
  component         varchar(190)    default ''                 comment '组件路径',
  iframe            bit(1)          default 0                  comment '是否为外链（false=否,true=是）',
  menu_type         tinyint(4)      default 0                  comment '菜单类型（0=目录,1=菜单,2=按钮）',
  hidden            bit(1)          default 0                  comment '菜单状态（false=显示,true=隐藏）',
  status            tinyint(4)      default 1                  comment '菜单状态（0=停用,1=正常）',
  perms             varchar(100)    default ''                 comment '权限标识',
  icon              varchar(100)    default '#'                comment '菜单图标',
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
) engine=innodb auto_increment=2000 comment = '菜单表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into sys_menu values('1', '系统管理', '0', '1', 'system',           '',   0, 0, 0, 1, '', 'system',   1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('2', '系统监控', '0', '2', 'monitor',          '',   0, 0, 0, 1, '', 'monitor',  1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('3', '系统工具', '0', '3', 'tool',             '',   0, 0, 0, 1, '', 'tool',     1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 二级菜单
insert into sys_menu values('98',  '数据源管理', '1',   '-1', 'datasource',       'system/datasource/index',        0, 1, 0, 1, 'system:datasource:page',        'international',          1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('99',  '租户管理', '1',   '0', 'tenant',       'system/tenant/index',        0, 1, 0, 1, 'system:tenant:page',        'example',          1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('100',  '用户管理', '1',   '1', 'user',       'system/user/index',        0, 1, 0, 1, 'system:user:page',        'user',          1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('101',  '角色管理', '1',   '2', 'role',       'system/role/index',        0, 1, 0, 1, 'system:role:page',        'peoples',       1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('102',  '菜单管理', '1',   '3', 'menu',       'system/menu/index',        0, 1, 0, 1, 'system:menu:list',        'tree-table',    1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('103',  '部门管理', '1',   '4', 'dept',       'system/dept/index',        0, 1, 0, 1, 'system:dept:list',        'tree',          1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('104',  '岗位管理', '1',   '5', 'post',       'system/post/index',        0, 1, 0, 1, 'system:post:page',        'post',          1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('106',  '参数设置', '1',   '7', 'config',     'system/config/index',      0, 1, 0, 1, 'system:config:page',      'edit',          1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('107',  '通知公告', '1',   '8', 'notice',     'system/notice/index',      0, 1, 0, 1, 'system:notice:page',      'message',       1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('108',  '日志管理', '1',   '9', 'log',        'system/log/index',         0, 0, 0, 1, '',                        'log',           1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('109',  '在线用户', '2',   '1', 'online',     'monitor/online/index',     0, 1, 0, 1, 'monitor:online:page',     'online',        1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('110',  '定时任务', '2',   '2', 'job',        'monitor/job/index',        0, 1, 0, 1, 'monitor:job:page',        'job',           1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('111',  '数据监控', '2',   '3', 'druid',      'monitor/druid/index',      0, 1, 0, 1, 'monitor:druid:list',      'druid',         1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('112',  '服务监控', '2',   '4', 'server',     'monitor/server/index',     0, 1, 0, 1, 'monitor:server:list',     'server',        1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('113',  '表单构建', '3',   '1', 'build',      'tool/build/index',         0, 1, 0, 1, 'tool:build:list',         'build',         1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('114',  '代码生成', '3',   '2', 'gen',        'tool/gen/index',           0, 1, 0, 1, 'tool:gen:page',           'code',          1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('115',  '系统接口', '3',   '3', 'swagger',    'tool/swagger/index',       0, 1, 0, 1, 'tool:swagger:list',       'swagger',       1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 三级菜单
insert into sys_menu values('500',  '操作日志', '108', '1', 'operlog',    'monitor/operlog/index',    0, 1, 0, 1, 'monitor:operlog:page',    'form',          1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('501',  '登录日志', '108', '2', 'logininfor', 'monitor/logininfor/index', 0, 1, 0, 1, 'monitor:logininfor:page', 'logininfor',    1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 数据源管理按钮
insert into sys_menu values('981', '数据源查询', '98', '1',  '', '', 0, 2, 0, 1, 'system:datasource:query',          '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('982', '数据源新增', '98', '2',  '', '', 0, 2, 0, 1, 'system:datasource:add',            '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('983', '数据源修改', '98', '3',  '', '', 0, 2, 0, 1, 'system:datasource:edit',           '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('984', '数据源删除', '98', '4',  '', '', 0, 2, 0, 1, 'system:datasource:remove',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 租户管理按钮
insert into sys_menu values('991', '租户查询', '99', '1',  '', '', 0, 2, 0, 1, 'system:tenant:query',          '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('992', '租户新增', '99', '2',  '', '', 0, 2, 0, 1, 'system:tenant:add',            '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('993', '租户修改', '99', '3',  '', '', 0, 2, 0, 1, 'system:tenant:edit',           '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('994', '租户删除', '99', '4',  '', '', 0, 2, 0, 1, 'system:tenant:remove',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('995', '租户导出', '99', '5',  '', '', 0, 2, 0, 1, 'system:tenant:export',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('996', '执行SQL', '99', '6',  '', '', 0, 2, 0, 1, 'system:tenant:exesql',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 用户管理按钮
insert into sys_menu values('1001', '用户查询', '100', '1',  '', '', 0, 2, 0, 1, 'system:user:query',          '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1002', '用户新增', '100', '2',  '', '', 0, 2, 0, 1, 'system:user:add',            '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1003', '用户修改', '100', '3',  '', '', 0, 2, 0, 1, 'system:user:edit',           '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1004', '用户删除', '100', '4',  '', '', 0, 2, 0, 1, 'system:user:remove',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1005', '用户导出', '100', '5',  '', '', 0, 2, 0, 1, 'system:user:export',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1006', '用户导入', '100', '6',  '', '', 0, 2, 0, 1, 'system:user:import',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1007', '重置密码', '100', '7',  '', '', 0, 2, 0, 1, 'system:user:resetPwd',       '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 角色管理按钮
insert into sys_menu values('1008', '角色查询', '101', '1',  '', '', 0, 2, 0, 1, 'system:role:query',          '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1009', '角色新增', '101', '2',  '', '', 0, 2, 0, 1, 'system:role:add',            '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1010', '角色修改', '101', '3',  '', '', 0, 2, 0, 1, 'system:role:edit',           '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1011', '角色删除', '101', '4',  '', '', 0, 2, 0, 1, 'system:role:remove',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1012', '角色导出', '101', '5',  '', '', 0, 2, 0, 1, 'system:role:export',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 菜单管理按钮
insert into sys_menu values('1013', '菜单查询', '102', '1',  '', '', 0, 2, 0, 1, 'system:menu:query',          '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1014', '菜单新增', '102', '2',  '', '', 0, 2, 0, 1, 'system:menu:add',            '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1015', '菜单修改', '102', '3',  '', '', 0, 2, 0, 1, 'system:menu:edit',           '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1016', '菜单删除', '102', '4',  '', '', 0, 2, 0, 1, 'system:menu:remove',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 部门管理按钮
insert into sys_menu values('1017', '部门查询', '103', '1',  '', '', 0, 2, 0, 1, 'system:dept:query',          '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1018', '部门新增', '103', '2',  '', '', 0, 2, 0, 1, 'system:dept:add',            '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1019', '部门修改', '103', '3',  '', '', 0, 2, 0, 1, 'system:dept:edit',           '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1020', '部门删除', '103', '4',  '', '', 0, 2, 0, 1, 'system:dept:remove',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 岗位管理按钮
insert into sys_menu values('1021', '岗位查询', '104', '1',  '', '', 0, 2, 0, 1, 'system:post:query',          '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1022', '岗位新增', '104', '2',  '', '', 0, 2, 0, 1, 'system:post:add',            '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1023', '岗位修改', '104', '3',  '', '', 0, 2, 0, 1, 'system:post:edit',           '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1024', '岗位删除', '104', '4',  '', '', 0, 2, 0, 1, 'system:post:remove',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1025', '岗位导出', '104', '5',  '', '', 0, 2, 0, 1, 'system:post:export',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 参数设置按钮
insert into sys_menu values('1031', '参数查询', '106', '1', '#', '', 0, 2, 0, 1, 'system:config:query',        '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1032', '参数新增', '106', '2', '#', '', 0, 2, 0, 1, 'system:config:add',          '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1033', '参数修改', '106', '3', '#', '', 0, 2, 0, 1, 'system:config:edit',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1034', '参数删除', '106', '4', '#', '', 0, 2, 0, 1, 'system:config:remove',       '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1035', '参数导出', '106', '5', '#', '', 0, 2, 0, 1, 'system:config:export',       '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 通知公告按钮
insert into sys_menu values('1036', '公告查询', '107', '1', '#', '', 0, 2, 0, 1, 'system:notice:query',        '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1037', '公告新增', '107', '2', '#', '', 0, 2, 0, 1, 'system:notice:add',          '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1038', '公告修改', '107', '3', '#', '', 0, 2, 0, 1, 'system:notice:edit',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1039', '公告删除', '107', '4', '#', '', 0, 2, 0, 1, 'system:notice:remove',       '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 操作日志按钮
insert into sys_menu values('1040', '操作查询', '500', '1', '#', '', 0, 2, 0, 1, 'monitor:operlog:query',      '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1041', '操作删除', '500', '2', '#', '', 0, 2, 0, 1, 'monitor:operlog:remove',     '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1042', '日志导出', '500', '4', '#', '', 0, 2, 0, 1, 'monitor:operlog:export',     '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 登录日志按钮
insert into sys_menu values('1043', '登录查询', '501', '1', '#', '', 0, 2, 0, 1, 'monitor:logininfor:query',   '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1044', '登录删除', '501', '2', '#', '', 0, 2, 0, 1, 'monitor:logininfor:remove',  '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1045', '日志导出', '501', '3', '#', '', 0, 2, 0, 1, 'monitor:logininfor:export',  '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 在线用户按钮
insert into sys_menu values('1046', '在线查询', '109', '1', '#', '', 0, 2, 0, 1, 'monitor:online:query',       '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1047', '批量强退', '109', '2', '#', '', 0, 2, 0, 1, 'monitor:online:batchLogout', '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1048', '单条强退', '109', '3', '#', '', 0, 2, 0, 1, 'monitor:online:forceLogout', '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 定时任务按钮
insert into sys_menu values('1049', '任务查询', '110', '1', '#', '', 0, 2, 0, 1, 'monitor:job:query',          '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1050', '任务新增', '110', '2', '#', '', 0, 2, 0, 1, 'monitor:job:add',            '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1051', '任务修改', '110', '3', '#', '', 0, 2, 0, 1, 'monitor:job:edit',           '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1052', '任务删除', '110', '4', '#', '', 0, 2, 0, 1, 'monitor:job:remove',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1053', '状态修改', '110', '5', '#', '', 0, 2, 0, 1, 'monitor:job:changeStatus',   '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1054', '任务导出', '110', '7', '#', '', 0, 2, 0, 1, 'monitor:job:export',         '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
-- 代码生成按钮
insert into sys_menu values('1055', '生成查询', '114', '1', '#', '', 0, 2, 0, 1, 'tool:gen:query',             '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1056', '生成修改', '114', '2', '#', '', 0, 2, 0, 1, 'tool:gen:edit',              '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1057', '生成删除', '114', '3', '#', '', 0, 2, 0, 1, 'tool:gen:remove',            '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1058', '导入代码', '114', '2', '#', '', 0, 2, 0, 1, 'tool:gen:import',            '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1059', '预览代码', '114', '4', '#', '', 0, 2, 0, 1, 'tool:gen:preview',           '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_menu values('1060', '生成代码', '114', '5', '#', '', 0, 2, 0, 1, 'tool:gen:code',              '#', 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);


-- ----------------------------
-- 定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job (
  id                bigint(20)      not null auto_increment    comment '任务ID',
  job_name          varchar(64)     default ''                 comment '任务名称',
  job_group         varchar(64)     default 'DEFAULT'          comment '任务组名',
  invoke_target     varchar(500)    not null                   comment '调用目标字符串',
  cron_expression   varchar(190)    default ''                 comment 'cron执行表达式',
  misfire_policy    tinyint(4)      default '3'                comment '计划执行错误策略（0=默认,1=立即执行,2=执行一次,3=放弃执行）',
  concurrent        bit(1)          default 0                  comment '是否并发执行（false=禁止,true=允许）',
  status            tinyint(4)      default 1                  comment '状态（0=暂停,1=正常）',
  creator_id        bigint(20)      default 0                  comment '创建者id',
  creator_info      varchar(500)    default ''                 comment '创建时姓名_登录账号',
  create_time 	    datetime                                   comment '创建时间',
  updater_id        bigint(20)      default 0                  comment '更新者id',
  updater_info      varchar(500)    default ''                 comment '更新时姓名_登录账号',
  update_time       datetime                                   comment '更新时间',
  ext_json          varchar(3000)   default ''                 comment '扩展JSON',
  deleted           bit(1)          default 0                  comment '删除标志（false=存在,true=删除）',
  version           int(11)         default 0                  comment '乐观锁数据版本',
  primary key (id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

insert into sys_job values(1, '系统默认（无参）', 'DEFAULT', 'taskTest.noParams',        '0/10 * * * * ?', '3', 0, 0, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_job values(2, '系统默认（有参）', 'DEFAULT', 'taskTest.params(\'test\')',  '0/15 * * * * ?', '3', 0, 0, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);
insert into sys_job values(3, '系统默认（多参）', 'DEFAULT', 'taskTest.multipleParams(\'test\', true, 2000L, 316.50D, 100)',  '0/20 * * * * ?', '3', 0, 0, 1, '管理员_admin', '2021-03-15 11:56:23', 1, '管理员_admin', '2021-03-15 11:56:23', '', 0, 0);


-- ----------------------------
-- 定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log (
  id                bigint(20)      not null auto_increment    comment '任务日志ID',
  job_name          varchar(64)     not null                   comment '任务名称',
  job_group         varchar(64)     not null                   comment '任务组名',
  invoke_target     varchar(500)    not null                   comment '调用目标字符串',
  job_message       varchar(500)                               comment '日志信息',
  status            tinyint(4)      default 1                  comment '执行状态（0=失败,1=正常）',
  exception_info    varchar(2000)   default ''                 comment '异常信息',
  start_time        datetime                                   comment '任务开始时间',
  stop_time         datetime                                   comment '任务结束时间',
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
) engine=innodb comment = '定时任务调度日志表';


-- ----------------------------
-- 代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  id                bigint(20)      not null auto_increment    comment '主键',
  table_name        varchar(200)    default ''                 comment '表名称',
  table_comment     varchar(500)    default ''                 comment '表描述',
  class_name        varchar(100)    default ''                 comment '实体类名称',
  tpl_category      varchar(50)     default 'crud'             comment '使用的模板（crud单表操作 tree树表操作）',
  package_name      varchar(100)                               comment '生成包路径',
  module_name       varchar(30)                                comment '生成模块名',
  business_name     varchar(30)                                comment '生成业务名',
  function_name     varchar(50)                                comment '生成功能名',
  function_author   varchar(50)                                comment '生成功能作者',
  options           varchar(1000)                              comment '其它生成选项',
  remark            varchar(500)    default ''                 comment '备注',
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
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  id                bigint(20)      not null auto_increment    comment '主键',
  table_id          varchar(64)                                comment '归属表编号',
  column_name       varchar(200)                               comment '列名称',
  column_comment    varchar(500)                               comment '列描述',
  column_type       varchar(100)                               comment '列类型',
  java_type         varchar(500)                               comment 'JAVA类型',
  java_field        varchar(200)                               comment 'JAVA字段名',
  primary_key       bit(1)                                     comment '是否主键（false=否,true=是）',
  incremental       bit(1)                                     comment '是否自增（false=否,true=是）',
  required          bit(1)                                     comment '是否必填（false=否,true=是）',
  insertable        bit(1)          default 1                  comment '是否为插入字段（false=否,true=是）',
  edit              bit(1)          default 1                  comment '是否编辑字段（false=否,true=是）',
  list              bit(1)          default 1                  comment '是否列表字段（false=否,true=是）',
  excel_export      bit(1)          default 1                  comment '是否导出字段（false=否,true=是）',
  excel_import      bit(1)          default 1                  comment '是否导入字段（false=否,true=是）',
  query             bit(1)          default 1                  comment '是否查询字段（false=否,true=是）',
  query_type        varchar(50)     default 'EQ'               comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type         varchar(100)                               comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  enum_full_name    varchar(150)    default ''                 comment '枚举类全限定名',
  sort              int(4)                                     comment '排序',
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
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';


-- 以下为quartz定时任务默认表

-- ----------------------------
-- 1、存储每一个已配置的 jobDetail 的详细信息
-- 如果是utf8mb4编码，则作为索引的varchar字段不能大于767/4=191.75
-- ----------------------------
drop table if exists QRTZ_JOB_DETAILS;
create table QRTZ_JOB_DETAILS (
    sched_name           varchar(120)    not null,
    job_name             varchar(190)    not null,
    job_group            varchar(190)    not null,
    description          varchar(250)    null,
    job_class_name       varchar(250)    not null,
    is_durable           varchar(1)      not null,
    is_nonconcurrent     varchar(1)      not null,
    is_update_data       varchar(1)      not null,
    requests_recovery    varchar(1)      not null,
    job_data             blob            null,
    primary key (sched_name,job_name,job_group)
) engine=innodb;

-- ----------------------------
-- 2、 存储已配置的 Trigger 的信息
-- ----------------------------
drop table if exists QRTZ_TRIGGERS;
create table QRTZ_TRIGGERS (
    sched_name           varchar(120)    not null,
    trigger_name         varchar(190)    not null,
    trigger_group        varchar(190)    not null,
    job_name             varchar(190)    not null,
    job_group            varchar(190)    not null,
    description          varchar(250)    null,
    next_fire_time       bigint(13)      null,
    prev_fire_time       bigint(13)      null,
    priority             integer         null,
    trigger_state        varchar(16)     not null,
    trigger_type         varchar(8)      not null,
    start_time           bigint(13)      not null,
    end_time             bigint(13)      null,
    calendar_name        varchar(200)    null,
    misfire_instr        smallint(2)     null,
    job_data             blob            null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,job_name,job_group) references QRTZ_JOB_DETAILS(sched_name,job_name,job_group)
) engine=innodb;

-- ----------------------------
-- 3、 存储简单的 Trigger，包括重复次数，间隔，以及已触发的次数
-- ----------------------------
drop table if exists QRTZ_SIMPLE_TRIGGERS;
create table QRTZ_SIMPLE_TRIGGERS (
    sched_name           varchar(120)    not null,
    trigger_name         varchar(190)    not null,
    trigger_group        varchar(190)    not null,
    repeat_count         bigint(7)       not null,
    repeat_interval      bigint(12)      not null,
    times_triggered      bigint(10)      not null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references QRTZ_TRIGGERS(sched_name,trigger_name,trigger_group)
) engine=innodb;

-- ----------------------------
-- 4、 存储 Cron Trigger，包括 Cron 表达式和时区信息
-- ----------------------------
drop table if exists QRTZ_CRON_TRIGGERS;
create table QRTZ_CRON_TRIGGERS (
    sched_name           varchar(120)    not null,
    trigger_name         varchar(190)    not null,
    trigger_group        varchar(190)    not null,
    cron_expression      varchar(200)    not null,
    time_zone_id         varchar(80),
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references QRTZ_TRIGGERS(sched_name,trigger_name,trigger_group)
) engine=innodb;

-- ----------------------------
-- 5、 Trigger 作为 Blob 类型存储(用于 Quartz 用户用 JDBC 创建他们自己定制的 Trigger 类型，JobStore 并不知道如何存储实例的时候)
-- ----------------------------
drop table if exists QRTZ_BLOB_TRIGGERS;
create table QRTZ_BLOB_TRIGGERS (
    sched_name           varchar(120)    not null,
    trigger_name         varchar(190)    not null,
    trigger_group        varchar(190)    not null,
    blob_data            blob            null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references QRTZ_TRIGGERS(sched_name,trigger_name,trigger_group)
) engine=innodb;

-- ----------------------------
-- 6、 以 Blob 类型存储存放日历信息， quartz可配置一个日历来指定一个时间范围
-- ----------------------------
drop table if exists QRTZ_CALENDARS;
create table QRTZ_CALENDARS (
    sched_name           varchar(120)    not null,
    calendar_name        varchar(190)    not null,
    calendar             blob            not null,
    primary key (sched_name,calendar_name)
) engine=innodb;

-- ----------------------------
-- 7、 存储已暂停的 Trigger 组的信息
-- ----------------------------
drop table if exists QRTZ_PAUSED_TRIGGER_GRPS;
create table QRTZ_PAUSED_TRIGGER_GRPS (
    sched_name           varchar(120)    not null,
    trigger_group        varchar(190)    not null,
    primary key (sched_name,trigger_group)
) engine=innodb;

-- ----------------------------
-- 8、 存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息
-- ----------------------------
drop table if exists QRTZ_FIRED_TRIGGERS;
create table QRTZ_FIRED_TRIGGERS (
    sched_name           varchar(120)    not null,
    entry_id             varchar(95)     not null,
    trigger_name         varchar(190)    not null,
    trigger_group        varchar(190)    not null,
    instance_name        varchar(190)    not null,
    fired_time           bigint(13)      not null,
    sched_time           bigint(13)      not null,
    priority             integer         not null,
    state                varchar(16)     not null,
    job_name             varchar(200)    null,
    job_group            varchar(200)    null,
    is_nonconcurrent     varchar(1)      null,
    requests_recovery    varchar(1)      null,
    primary key (sched_name,entry_id)
) engine=innodb;

-- ----------------------------
-- 9、 存储少量的有关 Scheduler 的状态信息，假如是用于集群中，可以看到其他的 Scheduler 实例
-- ----------------------------
drop table if exists QRTZ_SCHEDULER_STATE;
create table QRTZ_SCHEDULER_STATE (
    sched_name           varchar(120)    not null,
    instance_name        varchar(190)    not null,
    last_checkin_time    bigint(13)      not null,
    checkin_interval     bigint(13)      not null,
    primary key (sched_name,instance_name)
) engine=innodb;

-- ----------------------------
-- 10、 存储程序的悲观锁的信息(假如使用了悲观锁)
-- ----------------------------
drop table if exists QRTZ_LOCKS;
create table QRTZ_LOCKS (
    sched_name           varchar(120)    not null,
    lock_name            varchar(40)     not null,
    primary key (sched_name,lock_name)
) engine=innodb;

drop table if exists QRTZ_SIMPROP_TRIGGERS;
create table QRTZ_SIMPROP_TRIGGERS (
    sched_name           varchar(120)    not null,
    trigger_name         varchar(190)    not null,
    trigger_group        varchar(190)    not null,
    str_prop_1           varchar(512)    null,
    str_prop_2           varchar(512)    null,
    str_prop_3           varchar(512)    null,
    int_prop_1           int             null,
    int_prop_2           int             null,
    long_prop_1          bigint          null,
    long_prop_2          bigint          null,
    dec_prop_1           numeric(13,4)   null,
    dec_prop_2           numeric(13,4)   null,
    bool_prop_1          varchar(1)      null,
    bool_prop_2          varchar(1)      null,
    primary key (sched_name,trigger_name,trigger_group),
    foreign key (sched_name,trigger_name,trigger_group) references QRTZ_TRIGGERS(sched_name,trigger_name,trigger_group)
) engine=innodb;

commit;
