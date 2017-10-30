package com.delta.thingsocket.lib;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/19 14:32
 */


class SendThread implements Runnable {
    private LinkedBlockingDeque<SocketPackage> mSocketPackages;
    private long lastBeatHeartTime;

    private Socket mSocket;
    public SendThread(LinkedBlockingDeque<SocketPackage> mSocketPackages, Socket mSocket) {
        this.mSocketPackages =mSocketPackages;
        this.mSocket = mSocket;

    }

    @Override
    public void run() {

            while (!mSocket.isClosed()&&!mSocket.isOutputShutdown()&&!Thread.interrupted()){
                try {
                    SocketPackage mTake = mSocketPackages.take();
                    OutputStream mOutputStream = mSocket.getOutputStream();
                    if (mTake != null) {
                        mOutputStream.write(mTake.writeByte());
                    }
                } catch (InterruptedException mE) {
                    mE.printStackTrace();
                    break;
                } catch (IOException mE) {
                    mE.printStackTrace();
                }

            }

    }
}
