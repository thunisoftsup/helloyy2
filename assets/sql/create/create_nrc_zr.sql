--网上立案_立案预约_证人
create table [t_layy_zr] (
    [c_id]                 varchar(32)                          not null,    -- 证人id
    [c_layy_id]            varchar(32)                          not null,    -- 立案预约id
    [c_name]               varchar(300)                         null,        -- 姓名
    [n_xb]                 int                                  null,        -- 性别
    [n_idcard_type]        int                                  null,        -- 证件类型
    [c_idcard]             varchar(50)                          null,        -- 证件号码
    [d_csrq]               varchar(20)                          null,        -- 出生日期
    [n_age]                int                                  null,        -- 年龄
    [c_gzdw]               varchar(300)                         null,        -- 工作单位
    [c_sjhm]               varchar(50)                          null,        -- 手机号码
    [n_ctzz]               int                                  null,        -- 出庭作证
    [c_ylf_id]             varchar(600)                         null,        -- 有利方
    [c_ylf_mc]             varchar(600)                         null,        -- 有利方
    [c_address]            varchar(300)                         null,        -- 经常居住地/联系地址
    [c_address_id]         varchar(100)                         null,        -- 经常居住地/联系地址编号
    [c_zsd]                varchar(300)                         null,        -- 住所地
    [c_zsd_id]             varchar(100)                         null,        -- 住所地编号
      constraint [] primary key ([c_id]) on conflict replace
);