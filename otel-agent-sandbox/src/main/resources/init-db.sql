drop table if exists MY_TABLE;

create table MY_TABLE (
   ID             bigint auto_increment not null
  ,STRING_COL     varchar(100)
  ,BIGINT_COL     bigint
  ,INTEGER_COL    integer
  ,BIGDECIMAL_COL decimal(10, 2)
  ,DATE_COL       date
  ,TIMESTAMP_COL  timestamp
  ,CREATED_AT     timestamp
  ,UPDATED_AT     timestamp
  ,constraint PK_MY_TABLE primary key (
	ID
  )
);

-- =============================

insert into MY_TABLE (STRING_COL, BIGINT_COL, INTEGER_COL, BIGDECIMAL_COL, DATE_COL, TIMESTAMP_COL, CREATED_AT, UPDATED_AT)
  values ('あいうえお', 2000000, 5000, 12345.67, '2025-01-02', '2025-01-02 03:04:05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
insert into MY_TABLE (STRING_COL, BIGINT_COL, INTEGER_COL, BIGDECIMAL_COL, DATE_COL, TIMESTAMP_COL, CREATED_AT, UPDATED_AT)
  values ('かきくけこ', 3000000, 6000, 123456.78, '2026-01-02', '2026-01-02 03:04:05', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
