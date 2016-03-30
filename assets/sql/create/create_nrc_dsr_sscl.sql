--网上立案_立案预约_当事人_诉讼材料
create table [t_layy_dsr_sscl] (
    [c_id]                 varchar(32)                          not null,    -- 当事人_诉讼材料id
    [c_layy_id]            varchar(32)                          null,        -- 立案预约id
    [c_dsr_id]             varchar(32)                          null,        -- 当事人id
    [c_dsr_name]           varchar(32)                          null,        -- 当事人名称
    [c_sscl_id]            varchar(32)                          null,        -- 诉讼材料id
    [c_sscl_name]          varchar(32)                          null,        -- 诉讼材料名称
      constraint [] primary key ([c_id]) on conflict replace
);