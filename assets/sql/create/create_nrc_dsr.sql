--网上立案_立案预约_当事人
create table [t_layy_dsr] (
    [c_id]                 varchar(32)                          not null,    -- 当事人id
    [c_layy_id]            varchar(32)                          not null,    -- 立案预约id
    [c_ywxt_dsrbh]         varchar(32)                          null,        -- 业务系统当事人编号
    [c_ys_rybh]            varchar(32)                          null,        -- 原审案件人员编号
    [n_xh]                 int                                  null,        -- 显示序号
    [c_ssdw]               varchar(50)                          null,        -- 诉讼地位
    [n_type]               int                                  null,        -- 当事人类型
    [c_name]               varchar(300)                         null,        -- 自然人姓名/法人单位名称/非法人组织的单位名称
    [n_idcard_type]        int                                  null,        -- 自然人证件类型/法人的法定代表人证件类型/非法人组织的主要负责人证件类型
    [c_idcard]             varchar(50)                          null,        -- 自然人证件号码/法人的法定代表人证件号码/非法人组织的主要负责人证件号码
    [n_xb]                 int                                  null,        -- 性别
    [d_csrq]               datetime                             null,        -- 出生日期
    [n_age]                int                                  null,        -- 年龄
    [n_mz]                 int                                  null,        -- 民族
    [n_zy]                 int                                  null,        -- 职业
    [c_gzdw]               varchar(300)                         null,        -- 工作单位
    [c_sjhm]               varchar(50)                          null,        -- 手机号码
    [c_lxdh]               varchar(50)                          null,        -- 联系电话（固话）
    [c_address]            varchar(300)                         null,        -- 经常居住地/联系地址
    [c_address_id]         varchar(100)                         null,        -- 经常居住地/联系地址编号
    [c_zsd]                varchar(300)                         null,        -- 住所地
    [c_zsd_id]             varchar(100)                         null,        -- 住所地编号
    [n_dwxz]               int                                  null,        -- 单位性质
    [c_zzjgdm]             varchar(100)                         null,        -- 组织机构代码
    [c_dwdz]               varchar(300)                         null,        -- 单位地址
    [c_dwdz_id]            varchar(100)                         null,        -- 单位地址编号
    [c_fddbr]              varchar(100)                         null,        -- 法定代表人
    [c_fddbr_zw]           varchar(50)                          null,        -- 法定代表人职务
    [c_fddbr_sjhm]         varchar(50)                          null,        -- 法定代表人手机号码
    [c_fddbr_lxdh]         varchar(50)                          null,        -- 法定代表人联系电话(固话)
      constraint [] primary key ([c_id]) on conflict replace
);