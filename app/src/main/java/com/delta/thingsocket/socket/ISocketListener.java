package com.delta.thingsocket.socket;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/27 10:11
 */


public interface  ISocketListener {
    void onConnect(SocketClient mSocketClient);

    void onSendMsgBefore(SocketClient mSocketClient, IRequestPacket mWrite);

    void onSendMsgAfter(SocketClient mSocketClient, IRequestPacket mWrite);

    void onMsg(SocketClient mSocketClient, IResponsePacket mDecode);

    void onFailure(SocketClient mSocketClient, Throwable mThrowable);

    void onClose(SocketClient mSocketClient);
}
