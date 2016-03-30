
-- 省份
CREATE TABLE [t_province] (
c_id varchar(32)  NOT NULL,
c_name varchar(32),
c_jc int,
c_fjm  varchar(30),
c_jp varchar(10),
n_order int,
CONSTRAINT [] PRIMARY KEY ([c_id]) ON CONFLICT REPLACE
);