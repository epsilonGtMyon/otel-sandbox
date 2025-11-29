package epsilongtmyon.common.db.mybatis;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

public class MyTable extends AbstractEntity {

	private BigInteger id;
	private String stringCol;
	private BigInteger bigintCol;
	private Integer integerCol;
	private BigDecimal bigdecimalCol;
	private Date dateCol;
	private Timestamp timestampCol;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getStringCol() {
		return stringCol;
	}

	public void setStringCol(String stringCol) {
		this.stringCol = stringCol;
	}

	public BigInteger getBigintCol() {
		return bigintCol;
	}

	public void setBigintCol(BigInteger bigintCol) {
		this.bigintCol = bigintCol;
	}

	public Integer getIntegerCol() {
		return integerCol;
	}

	public void setIntegerCol(Integer integerCol) {
		this.integerCol = integerCol;
	}

	public BigDecimal getBigdecimalCol() {
		return bigdecimalCol;
	}

	public void setBigdecimalCol(BigDecimal bigdecimalCol) {
		this.bigdecimalCol = bigdecimalCol;
	}

	public Date getDateCol() {
		return dateCol;
	}

	public void setDateCol(Date dateCol) {
		this.dateCol = dateCol;
	}

	public Timestamp getTimestampCol() {
		return timestampCol;
	}

	public void setTimestampCol(Timestamp timestampCol) {
		this.timestampCol = timestampCol;
	}

	@Override
	public String toString() {
		return "MyTable [id=" + id + ", stringCol=" + stringCol + ", bigintCol=" + bigintCol + ", integerCol="
				+ integerCol + ", bigdecimalCol=" + bigdecimalCol + ", dateCol=" + dateCol + ", timestampCol="
				+ timestampCol + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
