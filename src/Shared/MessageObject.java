package Shared;

public class MessageObject {

    private MessageType messageType;
    private String payload;

    public MessageObject(MessageType messageType, String payload)
    {
        this.messageType = messageType;
        this.payload = payload;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getPayload() {
        return payload;
    }
}
