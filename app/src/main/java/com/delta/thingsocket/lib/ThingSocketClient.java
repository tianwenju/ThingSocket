package com.delta.thingsocket.lib;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/19 10:26
 */


public class ThingSocketClient {
    private SocketPackage mSocketPackage;
    private SocketRequest mSocketRequest;
    private Socket mSocket;
    private int mCurrentStatus = SocketStatus.DISCONNECTED;
    private ExecutorService mExecutorService ;
    private SendThread mSendThread;
    private ReceiveThread mReceiveThread;
    private LinkedBlockingDeque<SocketPackage> mSocketPackages;

    public ThingSocketClient(){

        mExecutorService = Executors.newCachedThreadPool();
        mSocketPackages = new LinkedBlockingDeque<>();
        mSendThread = new SendThread(mSocketPackages,mSocket);
        mReceiveThread = new ReceiveThread(mSocketPackages,mSocket);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initSocket();
            }
        }).start();
        mExecutorService.execute(mSendThread);
        mExecutorService.execute(mReceiveThread);
    }

    public void initSocket(){
        try {
            mSocket = new Socket(mSocketRequest.getIp(), mSocket.getPort());
            mCurrentStatus= SocketStatus.CONNECTED;
        } catch (IOException mE) {
            mE.printStackTrace();
            mCurrentStatus= SocketStatus.DISCONNECTED;
        }

    }
    void sendMessage(SocketPackage mSocketPackage){

    }


}
