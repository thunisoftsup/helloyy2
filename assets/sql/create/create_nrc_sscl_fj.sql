--网上立案_立案预约_诉讼材料_附件
create table [t_layy_sscl_fj] (
    [c_id]                 varchar(32)                          not null,    -- 诉讼材料_附件id
    [c_layy_id]            varchar(32)                          null,        -- 立案预约id
    [c_sscl_id]            varchar(32)                          null,        -- 诉讼材料id
    [c_origin_name]        varchar(300)                         null,        -- 原始文件名称
    [c_path]               varchar(300)                         null,        -- 存储路径
    [n_xssx]               int                                  null,        -- 显示顺序
    [n_sync]               int                                  null,        -- 是否和服务器同步
      constraint [] primary key ([c_id]) on conflict replace
);