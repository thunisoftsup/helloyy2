--网上立案_人员身份认证_材料
create table [t_pro_user_sfrz_cl] (
    [c_bh]                 varchar(32)                          not null,    -- 编号
    [c_layy_id]            varchar(32)                          null,        -- 立案预约编号
    [c_pro_user_sfrz_id]   varchar(32)                          null,        -- 身份认证id
    [c_pro_user_id]        varchar(32)                          null,        -- 用户编号
    [c_cl_name]            varchar(300)                         null,        -- 材料名称
    [c_cl_path]            varchar(300)                         null,        -- 材料存储路径
    [n_cl_lx]              int                                  null,        -- 材料类型
    [d_create]             varchar(30)                          null,        -- 创建时间
    [n_sync]               int                                  null,        -- 是否与服务器同步
      constraint [] primary key ([c_bh]) on conflict replace
);