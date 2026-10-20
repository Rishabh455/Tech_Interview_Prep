# Observability & Distributed Tracing — Interview Handy Guide

**Java Spring Boot Microservices | 5-minute revision | Easy English**

## 1. What is Observability?

**Answer:** Observability helps us understand what is happening inside our application and identify errors, performance issues, and failures.

It has three main parts:

| Component | Meaning | Example |
|---|---|---|
| **Logs** | What happened? | API failed with an exception |
| **Metrics** | How is it performing? | Response time, error rate |
| **Traces** | Where did the request go? | Which microservice was slow? |

**Why do we need it?**

In microservices, a single request can pass through multiple services. Observability helps us identify the root cause without manually checking every service.

## 2. How do you monitor application logs?

**Interview answer:**

“We use Elasticsearch and Kibana for centralized log monitoring.”

1. Applications generate logs.
2. Logs are collected and stored in Elasticsearch.
3. We use Kibana to search and filter logs.
4. We filter by service name, time range, error level, or Trace ID.
5. We check exception messages and stack traces to identify the root cause.

- **Elasticsearch:** Stores and searches logs.
- **Kibana:** Helps us view and analyze logs.

**Example:** If document verification fails, search the relevant service logs in Kibana, check the exception, and investigate the cause.

## 3. What are Metrics?

Metrics are numerical values that show application performance over time.

Examples:
- API response time
- Request count
- Error rate
- CPU and memory usage

Prometheus and Grafana are commonly used for metrics collection and visualization, respectively. Mention these tools in an interview only if they match your actual setup.

## 4. What is Distributed Tracing?

**Answer:** Distributed tracing tracks a single request as it travels across multiple microservices. It helps us identify where the request failed or became slow.

**Example:** `API Gateway → Order Service → Inventory Service → Payment Service`

| Term | Simple meaning |
|---|---|
| **Trace ID** | Identifies the complete request journey |
| **Span ID** | Identifies one operation |
| **Parent ID** | Connects a child operation to its parent |
| **Telemetry** | Logs, metrics, and traces |
| **Instrumentation** | Enables an application to generate telemetry |

## 5. How does Distributed Tracing work?

Remember this flow:

```text
Spring Boot Microservices
   (Micrometer / OpenTelemetry instrumentation)
                  |
                  v
             OTLP protocol
                  |
                  v
       OpenTelemetry Collector
       (receives/processes/forwards)
                  |
                  v
          Zipkin / Jaeger
       (stores and visualizes traces)
```

- **Micrometer:** Provides observability APIs commonly used in Spring Boot.
- **OpenTelemetry (OTel):** Helps generate, collect, and export telemetry.
- **OTLP:** Protocol used to transfer telemetry data.
- **Collector:** Receives, processes, and forwards telemetry.
- **Tracing backend:** Displays the request journey and span timings.

**Note:** A Collector is common but optional; some applications export directly to a compatible backend. Micrometer and OpenTelemetry have overlapping roles depending on the libraries and configuration used.

## 6. How did you implement Distributed Tracing?

**Interview answer based on the video example:**

“In the implementation demonstrated in the video, we added Micrometer and OpenTelemetry dependencies to the Order and Inventory services. We configured the OTLP endpoint in `application.yml` and ran the OpenTelemetry Collector and Zipkin using Docker Compose. The Collector forwarded tracing data to Zipkin. We then sent a request through Postman and viewed the complete request journey, Trace ID, Span IDs, and execution time in the Zipkin UI.”

Example ports from the video:
- **4318:** OTLP over HTTP
- **9411:** Zipkin

*Important: This describes the video’s implementation. Present it as your own production experience only if you actually used this setup.*

## 7. Important Cross-Questions

### Q1. What is the difference between logs and traces?

Logs show individual events and errors. Traces show the complete journey of a request across services.

### Q2. What if a request is slow?

I check the trace, identify the slow span, and investigate the corresponding service logs.

### Q3. Why do we need a Trace ID?

It helps us find related operations across multiple services for the same request.

### Q4. What is sampling?

Sampling decides which requests should be traced, helping reduce overhead and storage.

### Q5. What is the difference between OpenTelemetry and Zipkin?

OpenTelemetry helps generate and export telemetry; Zipkin stores and visualizes traces.

### Q6. Do logs, metrics, and traces replace each other?

No. They provide different information and work together to troubleshoot issues.

## 8. Final Revision — Remember These Lines

- **Observability:** Understand application health.
- **Logs:** Find error details.
- **Metrics:** Monitor performance.
- **Tracing:** Follow one request across services.
- **Trace ID:** Complete request journey.
- **Span ID:** Individual operation.
- **Elasticsearch + Kibana:** Log monitoring.
- **Micrometer + OpenTelemetry:** Instrumentation and telemetry.
- **OTLP:** Transfers telemetry data.
- **Collector:** Receives and forwards data.
- **Zipkin / Jaeger:** Trace visualization.

## One-line Interview Summary

“Logs tell us what happened, metrics tell us how the application is performing, and distributed tracing tells us where a request spent its time. Together, they help us troubleshoot microservices faster.”
