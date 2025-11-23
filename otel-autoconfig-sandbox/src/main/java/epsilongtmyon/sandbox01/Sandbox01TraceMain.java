package epsilongtmyon.sandbox01;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

/*
 * トレーサーの設定
 */
public class Sandbox01TraceMain {

	public static void main(String[] args) throws Exception {

		var main = new Sandbox01TraceMain();
		main.setup();

		// 内部ではAutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();が呼ばれている。
		OpenTelemetry openTelemetrySdk = GlobalOpenTelemetry.get();
		main.start1(openTelemetrySdk);
		main.start2(openTelemetrySdk);

		//-----------------------------
		System.out.print("wait..");
		System.in.read();

	}

	private void setup() {

		Map<String, String> props = new HashMap<>();

		// これがセットされていると
		// GlobalOpenTelemetry.get()を実行したときに
		// AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk()が実行される。
		props.put("otel.java.global-autoconfigure.enabled", "true");

		props.put("otel.service.name", "my-service");
		props.put("otel.resource.attributes", "myKey=001");

		props.put("otel.traces.exporter", "otlp"); //default:otlp

		props.entrySet().forEach(en -> System.setProperty(en.getKey(), en.getValue()));
	}

	// -----------------------------------------------------------
	// 同じtracerを使う

	private void start1(OpenTelemetry openTelemetry) throws IOException, InterruptedException {

		Tracer tracer = openTelemetry.getTracer("my-tracer1");

		Span span = tracer.spanBuilder(getClass().getSimpleName() + "#start1").startSpan();

		// makeCurrentをしておくとstartSpan時に紐づけが自動で行われる。
		try (Scope ignored = span.makeCurrent()) {

			withSleep(1000L, () -> {
				start1Sub1(tracer);
			});

		} finally {
			span.end();
		}
	}

	private void start1Sub1(Tracer tracer) {

		Span span = tracer.spanBuilder(getClass().getSimpleName() + "#start1Sub1").startSpan();
		try (Scope ignored = span.makeCurrent()) {

			withSleep(1000L, () -> {
				System.out.println("xxx");
			});

		} finally {
			span.end();
		}

	}

	// -----------------------------------------------------------
	// 異なるtracerを使う

	private void start2(OpenTelemetry openTelemetry) throws IOException, InterruptedException {

		Tracer tracer = openTelemetry.getTracer("my-tracer2");

		Span span = tracer.spanBuilder(getClass().getSimpleName() + "#start2").startSpan();

		// makeCurrentをしておくとstartSpan時に紐づけが自動で行われる。
		try (Scope ignored = span.makeCurrent()) {

			withSleep(1000L, () -> {
				start2Sub1(openTelemetry);
			});

		} finally {
			span.end();
		}
	}

	private void start2Sub1(OpenTelemetry openTelemetry) {

		Tracer tracer = openTelemetry.getTracer("my-tracer2-A");

		Span span = tracer.spanBuilder(getClass().getSimpleName() + "#start2Sub1").startSpan();
		try (Scope ignored = span.makeCurrent()) {

			withSleep(1000L, () -> {
				System.out.println("xxx");
			});

		} finally {
			span.end();
		}

	}
	
	
	/**
	 * 実行前後に待機を行う。
	 * @param sleepMills
	 * @param action
	 */
	private static void withSleep(long sleepMills, Runnable action) {
		try {
			Thread.sleep(sleepMills);

			action.run();

			Thread.sleep(sleepMills);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
