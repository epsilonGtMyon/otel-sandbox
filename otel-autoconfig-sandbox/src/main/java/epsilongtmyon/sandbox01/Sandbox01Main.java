package epsilongtmyon.sandbox01;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.DoubleGauge;
import io.opentelemetry.api.metrics.Meter;

/*
 * メトリクスの設定
 */
public class Sandbox01Main {

	public static void main(String[] args) throws Exception {

		var main = new Sandbox01Main();
		main.setup();

		// 内部ではAutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();が呼ばれている。
		OpenTelemetry openTelemetrySdk = GlobalOpenTelemetry.get();
		main.start1(openTelemetrySdk);

		//-----------------------------
		System.out.print("wait..");
		System.in.read();

	}

	// システムプロパティ、環境変数が対応しているが
	// IDE上で設定するのが面倒なので直接設定
	// システムプロパティが優先度高そう
	private void setup() {

		Map<String, String> props = new HashMap<>();
		
		// これがセットされていると
		// GlobalOpenTelemetry.get()を実行したときに
		// AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk()が実行される。
		props.put("otel.java.global-autoconfigure.enabled", "true");

		props.put("otel.service.name", "my-service");
		props.put("otel.resource.attributes", "myKey=001");

		props.put("otel.metric.export.interval", "10000");// default:1min

		props.put("otel.metrics.exporter", "otlp"); //default:otlp

		props.entrySet().forEach(en -> System.setProperty(en.getKey(), en.getValue()));
	}

	private void start1(OpenTelemetry openTelemetry) throws IOException, InterruptedException {
		Meter meter1 = openTelemetry.getMeter("my-meter1");

		DoubleGauge m1g1 = meter1.gaugeBuilder("my-meter1-g1").build();

		for (int i = 0; i < 5; i++) {

			m1g1.set(1.5 * i);

			System.out.println("wait " + i);
			TimeUnit.SECONDS.sleep(5);
		}

	}
}
