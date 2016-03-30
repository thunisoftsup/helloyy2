--网上立案_立案预约_诉讼材料
create table [t_layy_sscl] (
    [c_id]                 varchar(32)                          not null,    -- 诉讼材料id
    [c_layy_id]            varchar(32)                          not null,    -- 立案预约id
    [n_type]               int                                  null,        -- 材料类型
    [c_name]               varchar(100)                         null,        -- 材料名称
    [c_ssry_id]            varchar(32)                          null,        -- 所属人id
    [c_ssry_mc]            varchar(100)                         null,        -- 所属人名称
    [c_ly]                 varchar(100)                         null,        -- 来源
    [c_origin_name]        varchar(100)                         null,        -- 原始文件名称
    [c_path]               varchar(300)                         null,        -- 存储路径
    [n_xssx]               int                                  null,        -- 显示顺序
      constraint [] primary key ([c_id]) on conflict replace
);