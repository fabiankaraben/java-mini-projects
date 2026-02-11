package com.example.collaboration.model;

import java.util.ArrayList;
import java.util.List;

public class Document {
    private StringBuilder content;
    private int revision;
    private List<Operation> history;

    public Document() {
        this.content = new StringBuilder();
        this.revision = 0;
        this.history = new ArrayList<>();
    }

    public synchronized String getContent() {
        return content.toString();
    }

    public synchronized int getRevision() {
        return revision;
    }

    public synchronized void apply(Operation op) {
        if (op.getType() == OperationType.INSERT) {
            content.insert(op.getPosition(), op.getText());
        } else if (op.getType() == OperationType.DELETE) {
            content.delete(op.getPosition(), op.getPosition() + op.getLength());
        }
        history.add(op);
        revision++;
    }

    public synchronized List<Operation> getHistorySince(int rev) {
        if (rev >= history.size()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(history.subList(rev, history.size()));
    }
}
