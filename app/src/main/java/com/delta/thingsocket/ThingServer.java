package com.delta.thingsocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/18 9:34
 */


public class ThingServer {

    private  ServerSocket mServerSocket;
    private  Socket mAcceptSocket;
    private  BufferedReader mBufferedReader;
    private  BufferedWriter mBufferedWriter;
    private  ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private List<Socket> mSockets = Collections.synchronizedList(new ArrayList<Socket>());

    public static void main(String[] args) {

        ThingServer mThingServer = new ThingServer();
        mThingServer.startServer();

    }

    private void startServer() {
        int a ;
        try {
            mServerSocket = new ServerSocket(9090);
            System.out.println("server start");
            while (true){
                //这个是阻塞的
                mAcceptSocket = mServerSocket.accept();
                mSockets.add(mAcceptSocket);
                managerConnection(mAcceptSocket);
            }


        } catch (IOException mE) {
            mE.printStackTrace();
            try {
                mAcceptSocket.close();
                mServerSocket.close();
                mBufferedReader.close();
                mBufferedWriter.close();
            } catch (IOException mE1) {
                mE1.printStackTrace();
            }
        }
    }

    private  void managerConnection(final Socket mAcceptSocket) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                    try {
                        System.out.println("connect"+mAcceptSocket.hashCode());
                        mBufferedWriter = new BufferedWriter(new OutputStreamWriter(mAcceptSocket.getOutputStream()));
                        mBufferedReader = new BufferedReader(new InputStreamReader(mAcceptSocket.getInputStream()));
//                        TimerTask mTimerTask=new TimerTask() {
//                            @Override
//                            public void run() {
//                                try {
//
//                                    mBufferedWriter.write("heart beat once");
//                                    System.out.println(mAcceptSocket.hashCode()+ "heart beat once");
//                                    mBufferedWriter.newLine();
//                                    mBufferedWriter.flush();
//                                } catch (IOException mE) {
//                                    mE.printStackTrace();
//                                }
//                            }
//                        };
//                        Timer mTimer = new Timer();
//                        mTimer.schedule(mTimerTask,0,10000);
                        String clientMsg;
                        while ((clientMsg = mBufferedReader.readLine()) != null) {
                            System.out.println("來自客戶端："+mAcceptSocket.hashCode()+"消息："+ clientMsg);
                            for (Socket mSocket : mSockets) {
                                PrintWriter mPrintWriter = new PrintWriter(mSocket.getOutputStream());
                                //mPrintWriter.write("clientId:"+mSocket.hashCode()+"  "+clientMsg+"\r\n");
                                BufferedWriter mBufferedWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                                mBufferedWriter.write("clientId:"+mSocket.hashCode()+"  "+clientMsg);
                                mBufferedWriter.newLine();
                                mBufferedWriter.flush();
                            }
                        }

                    } catch (IOException mE) {
                        mE.printStackTrace();
                    }
                }
        });
    }
}
