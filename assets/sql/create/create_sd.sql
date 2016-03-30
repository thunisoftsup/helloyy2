-- 送达
CREATE TABLE [t_sd] (
  [c_id]          VARCHAR(32)  NOT NULL, -- 主键
  [c_ah]          VARCHAR(100), -- 案号
  [c_writname]    VARCHAR(255), -- 文书名称
  [c_ay]          VARCHAR(100),  -- 案由
  [c_sdrmc]       VARCHAR(50),  -- 送达人名称
  [c_sdrts]       VARCHAR(50),  -- 送达人庭室
  [c_sdrfy]       VARCHAR(200), -- 送达人法院
  [c_sdrbgdh]     VARCHAR(20), -- 送达人办公电话
  [c_qssj]        VARCHAR(30), -- 签收时间
  [c_fssj]        VARCHAR(30), -- 发送时间
  CONSTRAINT [] PRIMARY KEY ([c_id]) ON CONFLICT REPLACE);
