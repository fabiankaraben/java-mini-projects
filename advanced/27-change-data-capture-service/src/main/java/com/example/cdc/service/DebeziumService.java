package com.example.cdc.service;

import com.example.cdc.model.CdcEvent;
import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class DebeziumService {

    private static final Logger log = LoggerFactory.getLogger(DebeziumService.class);

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final ApplicationEventPublisher applicationEventPublisher;
    private DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;

    @Value("${mysql.host}")
    private String host;

    @Value("${mysql.port}")
    private int port;

    @Value("${mysql.user}")
    private String user;

    @Value("${mysql.password}")
    private String password;

    @Value("${debezium.offset.storage.file}")
    private String offsetStorageFile;

    @Value("${debezium.history.storage.file}")
    private String historyStorageFile;

    public DebeziumService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void start() throws IOException {
        Configuration config = Configuration.create()
                .with("name", "mysql-connector")
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", offsetStorageFile)
                .with("offset.flush.interval.ms", "60000")
                .with("database.hostname", host)
                .with("database.port", port)
                .with("database.user", user)
                .with("database.password", password)
                .with("database.server.id", "85744")
                .with("topic.prefix", "dbserver1")
                .with("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory")
                .with("schema.history.internal.file.filename", historyStorageFile)
                .with("include.schema.changes", "false")
                .build();

        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(config.asProperties())
                .notifying(this::handleChangeEvent)
                .build();

        this.executor.execute(debeziumEngine);
        log.info("Debezium Engine Started");
    }

    @PreDestroy
    public void stop() throws IOException {
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> recordChangeEvent) {
        SourceRecord sourceRecord = recordChangeEvent.record();
        Struct sourceRecordValue = (Struct) sourceRecord.value();

        if (sourceRecordValue != null) {
            Struct source = (Struct) sourceRecordValue.get("source");
            String op = (String) sourceRecordValue.get("op");
            
            // Ignore heartbeats or other events
            if (op == null || op.isEmpty()) {
                return;
            }

            Struct before = (Struct) sourceRecordValue.get("before");
            Struct after = (Struct) sourceRecordValue.get("after");
            
            CdcEvent event = CdcEvent.builder()
                    .database(source.getString("db"))
                    .table(source.getString("table"))
                    .operation(mapOperation(op))
                    .before(structToMap(before))
                    .after(structToMap(after))
                    .timestamp(source.getInt64("ts_ms"))
                    .build();

            log.info("CDC Event: {}", event);
            applicationEventPublisher.publishEvent(event);
        }
    }

    private String mapOperation(String op) {
        switch (op) {
            case "c": return "CREATE";
            case "u": return "UPDATE";
            case "d": return "DELETE";
            case "r": return "READ";
            default: return op;
        }
    }

    private Map<String, Object> structToMap(Struct struct) {
        if (struct == null) {
            return null;
        }
        return struct.schema().fields().stream()
                .map(Field::name)
                .filter(fieldName -> struct.get(fieldName) != null)
                .collect(Collectors.toMap(fieldName -> fieldName, fieldName -> struct.get(fieldName)));
    }
}
