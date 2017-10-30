package com.delta.thingsocket.socket;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/27 17:05
 */


public class HeartBeat {
    private IRequestPacket mMsg;


    public IRequestPacket getMsg() {
        return mMsg;
    }

    public void setMsg(IRequestPacket mMsg) {
        this.mMsg = mMsg;
    }
}
