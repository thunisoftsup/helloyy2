--网上立案_立案预约_证据材料
create table [t_zjcl] (
    [c_id]                 varchar(32)                          not null,    -- 证据材料id
    [c_zj_bh]              varchar(32)                          null,        -- 关联证据编号
    [c_sh_bh]              varchar(32)                          null,        -- 关联审核编号
    [n_sh_type]            int                                  null,        -- 审核类型 1-真实性审核，2-证明问题性审核
    [c_origin_name]        varchar(300)                         null,        -- 原始文件名称
    [c_path]               varchar(300)                         null,        -- 存储路径
    [d_create]             datetime                             null,        -- 创建时间
    [n_sync]               int                                  null,        -- 是否同步服务器
      constraint [] primary key ([c_id]) on conflict replace
);