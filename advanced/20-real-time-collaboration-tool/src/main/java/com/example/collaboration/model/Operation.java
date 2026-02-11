package com.example.collaboration.model;

public class Operation {
    private OperationType type;
    private int position;
    private String text; // For INSERT: text to insert; For DELETE: ignored (or length could be used)
    private int length;  // For DELETE: number of chars to delete
    private int revision; // The revision this operation is based on
    private String clientId;

    public Operation() {}

    public Operation(OperationType type, int position, String text, int length, int revision, String clientId) {
        this.type = type;
        this.position = position;
        this.text = text;
        this.length = length;
        this.revision = revision;
        this.clientId = clientId;
    }

    public OperationType getType() { return type; }
    public void setType(OperationType type) { this.type = type; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }
    public int getRevision() { return revision; }
    public void setRevision(int revision) { this.revision = revision; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    
    @Override
    public String toString() {
        return "Operation{" +
                "type=" + type +
                ", position=" + position +
                ", text='" + text + '\'' +
                ", length=" + length +
                ", revision=" + revision +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
