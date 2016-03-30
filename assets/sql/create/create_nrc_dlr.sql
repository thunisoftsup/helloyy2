--网上立案_立案预约_代理人
create table [t_layy_dlr] (
    [c_id]                 varchar(32)                          not null,    -- 代理人id
    [c_layy_id]            varchar(32)                          not null,    -- 立案预约id
    [n_dlr_type]           int                                  null,        -- 代理人类型代码
    [n_dlr_dlzl]           int                                  null,        -- 代理种类
    [c_name]               varchar(300)                         null,        -- 姓名/单位名称
    [c_bdlr_id]            varchar(600)                         null,        -- 被代理人id（1个或多个原告的id）
    [c_bdlr_mc]            varchar(600)                         null,        -- 被代理人名称（1个或多个原告的名称）
    [n_idcard_type]        int                                  null,        -- 证件类型
    [c_idcard]             varchar(50)                          null,        -- 证件号码
    [c_zyzh]               varchar(50)                          null,        -- 执业证号
    [c_szdw]               varchar(150)                         null,        -- 所在单位
    [c_sjhm]               varchar(50)                          null,        -- 手机号码
    [n_xh]                 int                                  null,        -- 显示顺序
      constraint [] primary key ([c_id]) on conflict replace
);