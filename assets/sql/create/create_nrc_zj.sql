--网上立案_立案预约_证据
create table [t_zj] (
    [c_id]                 varchar(32)                          not null,    -- 证据id
    [c_ywxt_zjbh]          varchar(32)                          null,        -- 业务证据编号
    [c_xh]                 varchar(32)                          null,        -- 序号
    [c_app_id]             varchar(32)                          null,        -- 关联app模块id
    [c_yw_bh]              varchar(32)                          null,        -- 关联业务编号
    [c_aj_bh]              varchar(32)                          null,        -- 关联案件编号
    [c_ywxt_ajbh]          varchar(32)                          null,        -- 业务系统案件编号
    [c_name]               varchar(100)                         null,        -- 证据名称
    [n_zjlx]               int                                  null,        -- 证据类型
    [c_ssry_id]            varchar(32)                          null,        -- 提出人编号（原所属人编号）
    [c_ssry_mc]            varchar(100)                         null,        -- 提出人名称（原所属人名称）
    [n_ssry_ssdw]          int                                  null,        -- 提出人诉讼地位（原所属人诉讼地位）
    [c_zjly]               varchar(500)                         null,        -- 证据来源
    [c_zmwt]               varchar(500)                         null,        -- 证明问题
    [n_stauts]             int                                  null,        -- 证据状态
    [c_bhyy]               varchar(300)                         null,        -- 驳回原因
    [n_fs]                 int                                  null,        -- 是否发送np
    [n_fsjg]               int                                  null,        -- 发送结果
    [d_create]             datetime                             null,        -- 创建时间
    [d_update]             datetime                             null,        -- 更新时间
    [c_zj_id]              varchar(32)                          null,        -- 证据id
    [c_zzjl_id]            varchar(32)                          null,        -- 质证记录id
    [c_ywxt_zzjl_id]       varchar(32)                          null,        -- np质证编号
    [c_zzjl_shlx]          varchar(32)                          null,        -- 质证记录审核类型
      constraint [] primary key ([c_id]) on conflict replace
);