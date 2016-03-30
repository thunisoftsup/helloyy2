--网上立案_立案预约_审核
create table [t_layy_sh] (
    [c_id]                 varchar(32)                          not null,    -- 审核id
    [c_layy_id]            varchar(32)                          not null,    -- 立案预约id
    [c_shr_id]             varchar(32)                          null,        -- 审核人id
    [c_shr_name]           varchar(200)                         null,        -- 审核人姓名
    [d_shsj]               varchar(20)                          null,        -- 审核时间
    [c_shyj]               varchar(600)                         null,        -- 审核意见
    [n_shjg]               int                                  null,        -- 审核结果
    [n_zctj]               int                                  null,        -- 再次提交
    [n_spcx]               int                                  null,        -- 审判程序
    [c_spcx]               varchar(300)                         null,        -- 审判程序
    [n_bhyy]               int                                  null,        -- 驳回原因代码
    [c_bhyy]               varchar(300)                         null,        -- 驳回原因名称
     constraint [] primary key ([c_id]) on conflict replace
);