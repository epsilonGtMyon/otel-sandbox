package epsilongtmyon.sandbox.sandbox01;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.opentelemetry.instrumentation.annotations.WithSpan;

public class Sandbox01Main {

	public static void main(String[] args) throws IOException {

		var main = new Sandbox01Main();
		main.start01();

		System.out.println("wait..");
		System.in.read();
	}

	@WithSpan
	private int start01() {

		sleep(2000L);

		start01Sub01();

		sleep(2000L);

		return 1;
	}

	@WithSpan
	private int start01Sub01() {

		sleep(2000L);

		return 2;
	}

	private static void sleep(long mills) {
		try {
			TimeUnit.MILLISECONDS.sleep(mills);
		} catch (InterruptedException e) {
		}
	}
}
