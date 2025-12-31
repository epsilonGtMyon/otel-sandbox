package epsilongtmyon.sandbox.sandbox01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.observation.DefaultMeterObservationHandler;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.registry.otlp.OtlpMeterRegistry;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingReceiverTracingObservationHandler;
import io.micrometer.tracing.handler.PropagatingSenderTracingObservationHandler;
import io.micrometer.tracing.otel.bridge.OtelBaggageManager;
import io.micrometer.tracing.otel.bridge.OtelCurrentTraceContext;
import io.micrometer.tracing.otel.bridge.OtelPropagator;
import io.micrometer.tracing.otel.bridge.OtelTracer;
import io.micrometer.tracing.otel.bridge.Slf4JBaggageEventListener;
import io.micrometer.tracing.otel.bridge.Slf4JEventListener;
import io.micrometer.tracing.propagation.Propagator;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;

public class Sandbox01Main {

	private final ObservationRegistry observationRegistry;

	public Sandbox01Main(ObservationRegistry observationRegistry) {
		super();
		this.observationRegistry = observationRegistry;
	}

	public static void main(String[] args) throws InterruptedException, IOException {

		ObservationRegistry observationRegistry = createMeterRegistry();
		var main = new Sandbox01Main(observationRegistry);

		main.start1();

	}

	private static ObservationRegistry createMeterRegistry() {

		// ---------------------
		// https://docs.micrometer.io/tracing/reference/configuring.html
		OpenTelemetry openTelemetry = createOpenTelemetry();

		// [OTel component] Tracer is a component that handles the life-cycle of a span
		io.opentelemetry.api.trace.Tracer otelTracer = openTelemetry.getTracerProvider()
				.get("io.micrometer.micrometer-tracing");

		// [Micrometer Tracing component] A Micrometer Tracing wrapper for OTel
		OtelCurrentTraceContext otelCurrentTraceContext = new OtelCurrentTraceContext();

		// [Micrometer Tracing component] A Micrometer Tracing listener for setting up MDC
		Slf4JEventListener slf4JEventListener = new Slf4JEventListener();

		// [Micrometer Tracing component] A Micrometer Tracing listener for setting
		// Baggage in MDC. Customizable
		// with correlation fields (currently we're setting empty list)
		Slf4JBaggageEventListener slf4JBaggageEventListener = new Slf4JBaggageEventListener(Collections.emptyList());

		// [Micrometer Tracing component] A Micrometer Tracing wrapper for OTel's Tracer.
		// You can consider
		// customizing the baggage manager with correlation and remote fields (currently
		// we're setting empty lists)
		OtelTracer tracer = new OtelTracer(otelTracer, otelCurrentTraceContext, event -> {
			slf4JEventListener.onEvent(event);
			slf4JBaggageEventListener.onEvent(event);
		}, new OtelBaggageManager(otelCurrentTraceContext, Collections.emptyList(), Collections.emptyList()));

		// ---------------------
		//https://docs.micrometer.io/tracing/reference/configuring.html

		ContextPropagators contextPropagators = ContextPropagators
				.create(TextMapPropagator.composite(new ArrayList<>()));

		Propagator propagator = new OtelPropagator(contextPropagators, otelTracer);
		; // The real propagator will come from
			// your tracer implementation (Brave /
			// OTel)
		MeterRegistry meterRegistry = new OtlpMeterRegistry();

		ObservationRegistry registry = ObservationRegistry.create();
		registry.observationConfig()
				// assuming that micrometer-core is on the classpath
				.observationHandler(new DefaultMeterObservationHandler(meterRegistry))
				// we set up a first matching handler that creates spans - it comes from
				// Micrometer
				// Tracing. We set up spans for sending and receiving data over the wire
				// and a default one
				.observationHandler(new ObservationHandler.FirstMatchingCompositeObservationHandler(
						new PropagatingSenderTracingObservationHandler<>(tracer, propagator),
						new PropagatingReceiverTracingObservationHandler<>(tracer, propagator),
						new DefaultTracingObservationHandler(tracer)));

		return registry;
	}

	private static OpenTelemetry createOpenTelemetry() {
		Map<String, String> props = new HashMap<>();
		props.put("otel.java.global-autoconfigure.enabled", "true");

		props.put("otel.service.name", "my-service");
		props.put("otel.resource.attributes", "myKey=001");

		// metrics
		props.put("otel.metric.export.interval", "10000");// default:1min
		props.put("otel.metrics.exporter", "otlp"); //default:otlp

		// trace
		props.put("otel.traces.exporter", "otlp"); //default:otlp

		props.entrySet().forEach(en -> System.setProperty(en.getKey(), en.getValue()));

		OpenTelemetry openTelemetry = GlobalOpenTelemetry.get();

		return openTelemetry;
	}

	private void start1() throws InterruptedException, IOException {
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

		System.out.println("wait....");
		System.in.read();
	}
}
