# otel-micrometer-tracing-sandbox

micrometerからトレースを扱う。


## 依存関係

`micrometer-tracing` を依存関係に追加する。

`micrometer-tracing-bridge-otel` を使うことでOpenTelemetryにトレースの処理を委譲する。

あとは otelからotlpでエクスポートする前に `opentelemetry-exporter-otlp` を使う
