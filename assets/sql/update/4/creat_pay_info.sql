
-- 省份
CREATE TABLE [t_province_new] (
c_id varchar(32)  NOT NULL,
c_jfje varchar(32),
c_jfzh varchar(32),
c_sfzh varchar(32),
c_dzph varchar(32),
c_jffs varchar(10),
n_success int,
CONSTRAINT [] PRIMARY KEY ([c_id]) ON CONFLICT REPLACE
);