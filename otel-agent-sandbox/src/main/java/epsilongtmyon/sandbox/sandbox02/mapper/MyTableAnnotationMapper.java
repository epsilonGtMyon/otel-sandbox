package epsilongtmyon.sandbox.sandbox02.mapper;

import java.math.BigInteger;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import epsilongtmyon.common.db.mybatis.MyTable;

public interface MyTableAnnotationMapper {

	@Select("""
			select
			   ID
			  ,STRING_COL
			  ,BIGINT_COL
			  ,INTEGER_COL
			  ,BIGDECIMAL_COL
			  ,DATE_COL
			  ,TIMESTAMP_COL
			  ,CREATED_AT
			  ,UPDATED_AT
			from
			  MY_TABLE
			where
			 ID = #{id}
						""")
	MyTable findByKey(@Param("id") BigInteger id);

	@Select("""
			select
			   ID
			  ,STRING_COL
			  ,BIGINT_COL
			  ,INTEGER_COL
			  ,BIGDECIMAL_COL
			  ,DATE_COL
			  ,TIMESTAMP_COL
			  ,CREATED_AT
			  ,UPDATED_AT
			from
			  MY_TABLE
			order by
			  ID
						""")
	List<MyTable> findAll();
}
