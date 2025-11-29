package epsilongtmyon.sandbox.sandbox02.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import epsilongtmyon.common.db.mybatis.MyLog;


public interface MyLogAnnotationMapper {

	@Options(useGeneratedKeys = true, keyProperty = "seq", keyColumn = "SEQ")
	@Insert("""
			insert into MY_LOG (
			   LOG_MESSAGE
			  ,CREATED_AT
			  ,UPDATED_AT
			) values (
			   #{logMessage, jdbcType=VARCHAR}
			  ,#{createdAt, jdbcType=TIMESTAMP}
			  ,#{updatedAt, jdbcType=TIMESTAMP}
			)
						""")
	int insert(MyLog myLog);
}
