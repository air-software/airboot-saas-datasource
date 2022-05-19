-- ----------------------------
-- 设计实体表的时候可以基于下面这个例子进行修改。
-- 新增字段时最好把注释都写上，这样可以直接生成到实体里。
-- ----------------------------

drop table if exists sys_table;
create table sys_table (
-- 主键类型建议使用bigint，如果有分布式需求，可以设置mybatis-plus的id生成策略为assign_id
  id                bigint(20)      not null auto_increment    comment 'id',
-- 如果是树表，则以下三个字段 parent_id, ancestors, order_num 为必须
  parent_id         bigint(20)      default 0                  comment '父级id',
  ancestors         varchar(190)    default ''                 comment '祖级列表',
  order_num         int(4)          default 0                  comment '显示顺序',
-- 实体字段里，如果是boolean类型，可以参考下面的例子，注释中的（false=XX,true=XX）必须要有，在代码生成和导入导出时会进行自动转换，XX要替换成你所定义的描述文字。
  boolean_example   bit(1)          default 0                  comment '布尔举例（false=XX,true=XX）',
-- 在代码生成时tinyint和smallint都会被解析为枚举类型，并且会根据一定的命名规则来自动匹配项目**.model.enums包中已有的枚举类，如status_example字段，会匹配到StatusExampleEnum枚举类。
-- 因此建议先将StatusExampleEnum枚举类建好（枚举类的写法可参考本项目中的StatusEnum，注意不要有重名的枚举类），再从代码生成那里导入表，这样在生成代码时就会自动匹配到。
-- 否则就只能在导入后，在前端编辑生成字段时，将枚举类的全限定名手动填写到枚举字段右侧的“枚举类全限定名”输入框里了。
  status_example    tinyint(4)      default 0                  comment '枚举举例1',
  type_example      smallint(6)     default 100                comment '枚举举例2',
-- 以下为父类BaseEntity的字段，继承BaseEntity的表都要有这些字段（关系表可以不继承）
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
) engine=innodb auto_increment=100 comment = '表注释';
