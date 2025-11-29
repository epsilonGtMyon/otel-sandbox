# otel-agent-sandbox

javaagentを使ったゼロコード計装について調べる。

## agentのダウンロード


[opentelemetry-javaagent.jar](https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar) をダウンロード

``` powershell
mkdir ./.agent
Invoke-WebRequest -Uri "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar" -OutFile ".agent/opentelemetry-javaagent.jar"
```

## 依存関係の追加

`@WithSpan`, `@SpanAttribute` などを使えるようにするために依存関係に`opentelemetry-instrumentation-annotations`を追加


## Eclipseからの実行時の設定

「実行の構成」->「引数」->「VM 引数」に以下を追加


```
-Dotel.service.name=my-service
-Dotel.instrumentation.mybatis.enabled=true
-javaagent:.agent/opentelemetry-javaagent.jar
```

MyBatisはデフォルトで無効のようなので明示的に有効化


いろいろ渡すことができる。

```
-Dotel.service.name=my-service
-Dotel.instrumentation.mybatis.enabled=true
-Dotel.resource.attributes=myKey=001,myKey=002
-Dotel.metric.export.interval=10000
-javaagent:.agent/opentelemetry-javaagent.jar
```

特に設定しなくてデフォルトでotlpでローカルにエクスポートされている。
