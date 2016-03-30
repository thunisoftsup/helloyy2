-- 法院
CREATE TABLE [t_court] (
c_id varchar(32)  NOT NULL,  -- 主键
c_name varchar(100) NULL,  -- 名称
c_jc varchar(50),  -- 简称
c_parent_id varchar(32), -- 父法院ID
c_fjm  varchar(30), -- 分级码
c_jb varchar(10),  -- 法院级别
n_order  int, -- 显示顺序
c_ssfw_url varchar(100),
c_wap_url varchar(100),
n_jsfs int,
CONSTRAINT [] PRIMARY KEY ([c_id]) ON CONFLICT REPLACE
);
