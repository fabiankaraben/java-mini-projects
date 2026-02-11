package com.example.cdc.model;

import java.util.Map;

public class CdcEvent {
    private String database;
    private String table;
    private String operation; // c=create, u=update, d=delete, r=read
    private Map<String, Object> before;
    private Map<String, Object> after;
    private Long timestamp;

    public CdcEvent() {
    }

    public CdcEvent(String database, String table, String operation, Map<String, Object> before, Map<String, Object> after, Long timestamp) {
        this.database = database;
        this.table = table;
        this.operation = operation;
        this.before = before;
        this.after = after;
        this.timestamp = timestamp;
    }

    public static CdcEventBuilder builder() {
        return new CdcEventBuilder();
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Map<String, Object> getBefore() {
        return before;
    }

    public void setBefore(Map<String, Object> before) {
        this.before = before;
    }

    public Map<String, Object> getAfter() {
        return after;
    }

    public void setAfter(Map<String, Object> after) {
        this.after = after;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CdcEvent{" +
                "database='" + database + '\'' +
                ", table='" + table + '\'' +
                ", operation='" + operation + '\'' +
                ", before=" + before +
                ", after=" + after +
                ", timestamp=" + timestamp +
                '}';
    }

    public static class CdcEventBuilder {
        private String database;
        private String table;
        private String operation;
        private Map<String, Object> before;
        private Map<String, Object> after;
        private Long timestamp;

        CdcEventBuilder() {
        }

        public CdcEventBuilder database(String database) {
            this.database = database;
            return this;
        }

        public CdcEventBuilder table(String table) {
            this.table = table;
            return this;
        }

        public CdcEventBuilder operation(String operation) {
            this.operation = operation;
            return this;
        }

        public CdcEventBuilder before(Map<String, Object> before) {
            this.before = before;
            return this;
        }

        public CdcEventBuilder after(Map<String, Object> after) {
            this.after = after;
            return this;
        }

        public CdcEventBuilder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public CdcEvent build() {
            return new CdcEvent(database, table, operation, before, after, timestamp);
        }
    }
}
