-- 送达文书
CREATE TABLE [t_sd_writ] (
  [c_id]         VARCHAR(32)  NOT NULL,  -- 主键
  [c_sd_id]      VARCHAR(32),
  [c_name]       VARCHAR(40), -- 文书名称
  [c_path]       VARCHAR(255), -- 文书存放路径
  CONSTRAINT [] PRIMARY KEY ([c_id]) ON CONFLICT REPLACE);

