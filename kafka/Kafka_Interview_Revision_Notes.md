# Apache Kafka --- Interview Revision Notes

**Target:** Deloitte Java / Spring Boot Full-Stack Interview\
**Style:** Simple English, practical examples, and quick revision

------------------------------------------------------------------------

## 1. What is Apache Kafka?

Apache Kafka is a **distributed event-streaming platform**. Applications
use it to publish events and let other applications consume and process
those events.

In simple words, Kafka lets one service send a message without needing
the receiving service to process it immediately.

**Example:** A meeting service publishes a `MeetingCreated` event.
Another service can consume it and process meeting information
independently.

### Why use Kafka?

-   **Asynchronous communication:** The producer does not have to wait
    for the consumer to finish processing.
-   **Loose coupling:** Services do not need to call each other directly
    for every event.
-   **Scalability:** Consumers can process work in parallel using
    partitions and consumer groups.
-   **Durability:** Kafka stores messages according to its retention
    configuration.
-   **Fault tolerance:** Replication can help protect data if a broker
    fails.
-   **Replay:** Consumers can read retained events again by resetting or
    managing offsets.

Kafka is not a replacement for every REST API. REST is useful for direct
request/response interactions; Kafka is useful for event-driven,
asynchronous workflows.

------------------------------------------------------------------------

## 2. Important Kafka Terms

  -----------------------------------------------------------------------
  Term                                Simple meaning
  ----------------------------------- -----------------------------------
  **Producer**                        Application that publishes messages
                                      to Kafka.

  **Consumer**                        Application that reads messages
                                      from Kafka.

  **Topic**                           Named stream/category where records
                                      are stored, such as
                                      `meeting-events`.

  **Broker**                          A Kafka server. A Kafka cluster
                                      usually has multiple brokers.

  **Partition**                       A topic is split into partitions to
                                      support scale and parallel
                                      processing.

  **Offset**                          Position/sequence number of a
                                      record within a partition.

  **Consumer group**                  Consumers sharing a group ID and
                                      dividing partition work between
                                      them.

  **Record/message**                  An event sent to Kafka; it can have
                                      a key, value, timestamp, and
                                      headers.

  **Replication**                     Keeping copies of partition data on
                                      multiple brokers for fault
                                      tolerance.
  -----------------------------------------------------------------------

### Topic and partition example

Suppose a topic named `meeting-events` has three partitions:

-   Partition 0
-   Partition 1
-   Partition 2

Kafka can distribute records across partitions. A record with a key is
normally routed consistently based on that key, depending on the
producer's partitioning strategy.

**Ordering rule:** Kafka guarantees order **within a single partition**,
not across all partitions in a topic.

------------------------------------------------------------------------

## 3. Producer → Topic → Consumer: How Kafka Works

1.  A producer creates an event.
2.  The producer sends the record to a Kafka topic.
3.  A Kafka broker stores the record in one of the topic's partitions.
4.  A consumer subscribes to the topic.
5.  The consumer polls Kafka and receives records.
6.  After processing, the consumer's offset is committed according to
    its configuration and processing strategy.

A producer and consumer do not have to be running at exactly the same
time, as long as the record is retained and the consumer has access to
it.

### Quick flow

``` text
Producer
   |
   v
Kafka topic (partitioned and stored on brokers)
   |
   v
Consumer
   |
   v
Process event / update database / call another service
```

------------------------------------------------------------------------

## 4. Kafka in My Virtual Meeting Sentinel Project

My personal project, **Virtual Meeting Sentinel**, is a
microservices-based platform involving meeting-content processing, AI,
RAG-based retrieval, semantic duplicate detection, and supporting
services.

Kafka is used in the project for asynchronous communication between
services.

### Interview-ready explanation

> "In my Virtual Meeting Sentinel project, I use Apache Kafka for
> asynchronous communication between microservices. A service publishes
> an event to a Kafka topic, and another service consumes and processes
> it independently. This reduces direct dependencies between services
> and helps the system handle processing workloads more efficiently."

### How to explain a typical event flow

Use this sequence only if it matches the actual implementation:

1.  A service generates a meeting-related event.
2.  The producer publishes the event to a Kafka topic.
3.  Kafka stores the event.
4.  A consumer service receives the event.
5.  The consumer processes it---for example, preparing or analyzing
    meeting content---and may store the result or call another service.
6.  The producer does not need to wait for the consumer's work to
    finish.

**Important:** Be ready to name the exact event/topic and the two
services connected by Kafka in your implementation. Do not claim that a
specific service, AI step, or topic is connected through Kafka unless it
is actually implemented that way.

### If the interviewer asks, "Why did you use Kafka?"

> "Kafka allows services to communicate asynchronously, reduces direct
> service-to-service dependency, and supports scalable event processing.
> It also retains events based on configuration, so consumers can
> process retained records later."

### If the interviewer asks, "Why not just use REST?"

> "REST is useful when a caller needs an immediate response. Kafka is
> useful when the sender can publish an event and another service can
> process it independently. Kafka can reduce synchronous coupling, but
> it adds operational and message-processing complexity, so I would
> choose it when asynchronous event processing is useful."

------------------------------------------------------------------------

## 5. Kafka vs REST API

  -----------------------------------------------------------------------
  REST API                            Kafka
  ----------------------------------- -----------------------------------
  Commonly used for request/response  Commonly used for asynchronous
                                      event streaming

  Caller usually waits for a response Producer can publish without
                                      waiting for consumer processing

  Caller directly addresses a service Producer publishes to a topic
  endpoint                            

  Often used to query or change a     Often used to communicate that an
  resource                            event occurred

  Consumer service must be available  Consumer can process retained
  for a successful synchronous call   records later, within retention and
                                      availability limits
  -----------------------------------------------------------------------

**Interview tip:** They solve different problems and are often used
together in one system.

------------------------------------------------------------------------

## 6. Kafka Consumer Groups

A consumer group lets multiple consumers share the work for a topic.

For a topic with **three partitions** and **three consumers in the same
consumer group**, Kafka can assign one partition to each consumer.

``` text
Topic: meeting-events
  Partition 0 ---> Consumer A (group meeting-workers)
  Partition 1 ---> Consumer B (group meeting-workers)
  Partition 2 ---> Consumer C (group meeting-workers)
```

Key points: - Within a consumer group, a partition is assigned to at
most one consumer at a time. - If there are more consumers than
partitions, some consumers may be idle. - Consumers in **different
groups** can independently read the same topic. - Kafka rebalances
partition assignments when group membership or topic partition
assignments change.

**Example:** One group processes meeting summaries, while another
independent group performs analytics---if both are subscribed to the
relevant event topic.

------------------------------------------------------------------------

## 7. What Is an Offset?

An offset identifies a record's position within a particular partition.

Consumers use offsets to track progress.

-   If an offset is committed, Kafka can use that progress when the
    consumer restarts.
-   If a consumer restarts, it resumes based on its committed offset and
    the configured offset-reset behavior.
-   Committing too early can cause records to be skipped after a
    failure.
-   Committing only after processing can cause a record to be processed
    again if the application fails after processing but before
    committing.

This is why consumer processing should be designed with retries and
duplicate handling in mind.

------------------------------------------------------------------------

## 8. Delivery Semantics: At-Most-Once, At-Least-Once, Exactly-Once

### At-most-once

A record is processed zero or one time. A failure can cause a record to
be lost from the application's processing flow if progress was committed
before successful processing.

### At-least-once

A record is processed one or more times. A failure can cause the same
record to be processed again.

### Exactly-once

Kafka supports exactly-once processing patterns for certain
Kafka-to-Kafka workflows using transactions and suitable configuration.
End-to-end exactly-once behavior involving external databases or APIs
requires additional design; it is not automatic.

**Interview-safe answer:** "In many consumer applications, at-least-once
delivery is common, so I design handlers to be idempotent and safe
against duplicate processing."

------------------------------------------------------------------------

## 9. What Is Idempotency?

An operation is **idempotent** if repeating it produces the same final
result as performing it once.

For example, saving a meeting event using a unique event ID and checking
whether that ID has already been processed can prevent duplicate work.

Why it matters: - Kafka consumers may process a record again after a
retry or restart. - Idempotency helps avoid duplicate database entries
or repeated business actions.

------------------------------------------------------------------------

## 10. Failure Handling and Reliability

Common techniques include:

-   **Retries:** Try processing a failed record again, with sensible
    limits and backoff.
-   **Dead-letter topic (DLT/DLQ):** Send records that repeatedly fail
    to a separate topic for investigation or later handling.
-   **Idempotent processing:** Make duplicate delivery safe.
-   **Offset management:** Commit offsets at an appropriate point
    relative to processing.
-   **Replication:** Configure replicas to improve availability and
    durability.
-   **Monitoring:** Track consumer lag, errors, throughput, and broker
    health.

A dead-letter topic and retry strategy need to be
implemented/configured; they do not automatically solve every failure.

------------------------------------------------------------------------

## 11. Kafka with Spring Boot

Spring for Apache Kafka provides integration through `KafkaTemplate` for
producing records and `@KafkaListener` for consuming them.

### Maven dependency

Add the Spring Kafka dependency to `pom.xml` (choose a version
compatible with your Spring Boot dependency management):

``` xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### Basic configuration

Example `application.properties` for a local Kafka broker:

``` properties
spring.kafka.bootstrap-servers=localhost:9092

spring.kafka.consumer.group-id=meeting-workers
spring.kafka.consumer.auto-offset-reset=earliest

spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
```

`auto-offset-reset=earliest` applies when the group has no valid
committed offset; it does not mean every restart rereads all messages.

### Producer

``` java
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MeetingEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public MeetingEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String event) {
        kafkaTemplate.send("meeting-events", event);
    }
}
```

### Consumer

``` java
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MeetingEventConsumer {

    @KafkaListener(
        topics = "meeting-events",
        groupId = "meeting-workers"
    )
    public void consume(String event) {
        System.out.println("Received event: " + event);
        // Process the event
    }
}
```

These snippets demonstrate the basic idea. They assume Kafka is running
and the topic is available or topic creation is configured. Production
code should also consider structured event schemas, validation, error
handling, retries, idempotency, logging, and offset/transaction
strategy.

### Optional REST endpoint to publish an event

``` java
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meetings")
public class MeetingEventController {

    private final MeetingEventProducer producer;

    public MeetingEventController(MeetingEventProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/events")
    public String publish(@RequestBody String event) {
        producer.publish(event);
        return "Event submitted";
    }
}
```

This is a minimal demonstration, not a complete production API.
Returning a response immediately after calling `send()` does not
necessarily mean the broker has acknowledged the record. Production code
should handle the send result and errors appropriately.

------------------------------------------------------------------------

## 12. Does Kafka Update the Angular UI Directly?

No. Kafka is generally used for communication between backend services,
not as a browser UI update mechanism by itself.

A common architecture is:

``` text
Angular
   |
   | HTTP request
   v
Spring Boot API
   |
   | Publish event
   v
Kafka topic
   |
   v
Consumer microservice
   |
   | Save result
   v
Database
   |
   v
Spring Boot API
   |
   | Polling, SSE, or WebSocket
   v
Angular UI updated
```

Possible ways to update Angular: - **Polling:** Angular periodically
calls an API to check for updated data. - **SSE:** Server sends updates
to the browser over a one-way stream. - **WebSocket:** Persistent,
bidirectional communication between browser and server.

Use whichever mechanism the actual project implements; Kafka alone does
not automatically push an update to Angular.

------------------------------------------------------------------------

## 13. Common Kafka Interview Questions

### Q1. What is Kafka?

Kafka is a distributed event-streaming platform used to publish, store,
and consume event records.

### Q2. What is a producer?

An application that publishes records to Kafka topics.

### Q3. What is a consumer?

An application that reads records from Kafka topics.

### Q4. What is a topic?

A named stream/category of records.

### Q5. What is a broker?

A Kafka server that stores and serves partition data.

### Q6. What is a partition?

A division of a topic that enables scalability and parallel processing.
Ordering is guaranteed within a partition.

### Q7. What is a consumer group?

A group of consumers that share topic partitions and divide processing
work.

### Q8. What is an offset?

A record's position within a partition, used to track consumer progress.

### Q9. Can two consumer groups read the same topic?

Yes. Each group maintains its own progress and can process the same
records independently.

### Q10. Can two consumers in the same group read the same partition at the same time?

Normally, a partition is assigned to at most one consumer in a group at
a time.

### Q11. What happens if a consumer fails?

Kafka can reassign its partitions to another consumer in the group.
Processing may repeat depending on when offsets were committed.

### Q12. How does Kafka help scalability?

Topics can have multiple partitions, and consumers in a group can
process different partitions in parallel, up to the available partition
parallelism.

### Q13. Does Kafka guarantee ordering?

Ordering is guaranteed within a partition, not across all partitions in
a topic.

### Q14. Kafka vs REST?

REST is typically used for direct request/response; Kafka is typically
used for asynchronous event-driven communication.

### Q15. What is a dead-letter topic?

A separate topic where records that cannot be processed successfully
after the configured handling/retry process can be sent for
investigation.

### Q16. What is consumer lag?

The gap between the latest available records and the consumer group's
processing/committed position. The exact measurement depends on the
monitoring tool and offset state.

### Q17. What is idempotency?

Repeating an operation does not change the final result beyond the first
successful execution. It helps handle duplicate processing safely.

### Q18. How do you use Kafka in Spring Boot?

Use `KafkaTemplate` to publish records and `@KafkaListener` to consume
records, with the appropriate bootstrap-server, serializer/deserializer,
and consumer-group configuration.

### Q19. Does Kafka replace a database?

No. Kafka stores event records according to retention/compaction
configuration. A database is commonly used for application queries and
durable business state.

### Q20. Does Kafka automatically retry every failed message?

No. Retry and dead-letter behavior must be designed/configured in the
producer/consumer application and Kafka integration.

------------------------------------------------------------------------

## 14. Quick Revision Sheet

Remember these one-liners:

-   **Kafka:** Distributed event-streaming platform.
-   **Producer:** Sends records.
-   **Consumer:** Reads records.
-   **Topic:** Named stream of records.
-   **Broker:** Kafka server.
-   **Partition:** Enables scale and parallelism.
-   **Offset:** Position of a record in a partition.
-   **Consumer group:** Consumers sharing partition work.
-   **Ordering:** Within a partition.
-   **Consumer lag:** How far behind a consumer group is.
-   **Idempotency:** Duplicate execution has no additional effect on the
    final result.
-   **Dead-letter topic:** Holds records that cannot be processed
    successfully under the configured policy.
-   **REST:** Direct request/response.
-   **Kafka:** Asynchronous event communication.
-   **Spring producer:** `KafkaTemplate`.
-   **Spring consumer:** `@KafkaListener`.

### Project answer to remember

> "In my Virtual Meeting Sentinel project, I use Kafka for asynchronous
> communication between microservices. A service publishes an event to a
> Kafka topic, and another service consumes and processes it
> independently. This reduces direct dependencies and supports scalable
> event processing. I can explain the exact event flow based on the
> services connected in my implementation."

------------------------------------------------------------------------

## 15. Final Interview Advice

1.  Explain Kafka in simple terms first.
2.  Describe the actual event and the services connected in Virtual
    Meeting Sentinel.
3.  Be prepared to explain why Kafka was preferable to a synchronous
    REST call for that flow.
4.  Know producers, consumers, topics, partitions, offsets, and consumer
    groups.
5.  Do not claim exactly-once processing, retries, dead-letter handling,
    or a particular Angular update mechanism unless your implementation
    supports it.
6.  If asked a project-specific question, explain what you implemented
    and distinguish it from what could be added in a production design.
