package epsilongtmyon.common.db.mybatis;

import java.math.BigInteger;

public class MyLog extends AbstractEntity {

	private BigInteger seq;

	private String logMessage;

	public BigInteger getSeq() {
		return seq;
	}

	public void setSeq(BigInteger seq) {
		this.seq = seq;
	}

	public String getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}

	@Override
	public String toString() {
		return "MyLog [seq=" + seq + ", logMessage=" + logMessage + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}

}
