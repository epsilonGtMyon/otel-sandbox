package epsilongtmyon.sandbox.sandbox01;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.registry.otlp.OtlpConfig;
import io.micrometer.registry.otlp.OtlpMeterRegistry;

public class Sandbox01Main {

	private final MeterRegistry meterRegistry;

	public Sandbox01Main(MeterRegistry meterRegistry) {
		super();
		this.meterRegistry = meterRegistry;
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		MeterRegistry mr = createMeterRegistry();
		var main = new Sandbox01Main(mr);

//		main.start1();
//		main.start2();
		main.start3();

	}

	private static MeterRegistry createMeterRegistry() {

		OtlpConfig config = new OtlpConfig() {
			@Override
			public String get(final String key) {
				//System.out.println(key);
				return null;
			}
		};

		MeterRegistry registry = new OtlpMeterRegistry(config, Clock.SYSTEM);

		File file = Paths.get(System.getProperty("user.home")).toFile();
		DiskSpaceMetrics dsMetrics = new DiskSpaceMetrics(file);
		dsMetrics.bindTo(registry);

		ProcessorMetrics psMetrics = new ProcessorMetrics();
		psMetrics.bindTo(registry);

		UptimeMetrics utMetrics = new UptimeMetrics();
		utMetrics.bindTo(registry);

		return registry;
	}

	private void start1() throws IOException {

		System.out.println("ready...");
		System.in.read();
	}

	private void start2() throws IOException {
		meterRegistry.gauge("myGauge", 8);
		meterRegistry.gauge("myGauge", 15);
		System.out.println("ready...");
		System.in.read();

	}

	private void start3() throws InterruptedException, IOException {

		ObservationRegistry observationRegistry = ObservationRegistry.create();

		observationRegistry.observationConfig().observationHandler(new DefaultMeterObservationHandler(meterRegistry));

		Observation obs = Observation.createNotStarted(getClass().getSimpleName() + ".start3", observationRegistry);

		obs.observeChecked(() -> {
			TimeUnit.SECONDS.sleep(2L);
		});

		obs.observeChecked(() -> {
			TimeUnit.SECONDS.sleep(1L);
		});


		obs.observeChecked(() -> {
			TimeUnit.SECONDS.sleep(3L);
		});
		
		System.in.read();
	}
}
