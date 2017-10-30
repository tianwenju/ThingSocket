package com.delta.thingsocket.socket;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/27 9:59
 */


public interface ISocket {

    //连接socket
    void connect();

    //发送消息
    void sendMsg(IRequestPacket mIPacket);


    void sendMsg(String msg, String charset);

    //失去连接
    void disConnect();

}
