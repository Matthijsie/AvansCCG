package client;

import Shared.MessageObject;

public interface ServerResponseCallback {

    void OnServerResponse(MessageObject messageObject);
    void OnServerError(MessageObject messageObject);
}
