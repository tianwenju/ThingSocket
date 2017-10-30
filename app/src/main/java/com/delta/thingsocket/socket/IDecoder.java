package com.delta.thingsocket.socket;

import java.io.InputStream;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/27 10:05
 */


public interface IDecoder {


    IResponsePacket decode(SocketClient mSocketClient, InputStream mInputStream);
}
