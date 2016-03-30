--中国行政区域
create table [t_regions] (
    [c_id]                  varchar(32)                          not null,    -- id
    [c_parent_id]           varchar(32)                          null,        -- 父ID
    [c_fjm]                 varchar(32)                          null,        -- 分级代码
    [c_jb]                  varchar(32)                          null,        -- 行政级别
    [c_name]                varchar(100)                         null,        -- 地点名称
    [n_order]               int                                  null,        -- 序号
    [n_valid]               int                                  null,        -- 是否有效
      constraint [] primary key ([c_id]) on conflict replace
);