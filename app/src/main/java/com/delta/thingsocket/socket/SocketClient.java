package com.delta.thingsocket.socket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/27 10:08
 */


public class SocketClient implements ISocket {

    private static final long RECONNECT_MAX_TIME =60*1000 ;
    private final Context mContext;
    //// TODO: 2017/10/27 1.发送心跳包2.断线重连
    private Socket mSocket;
    private List<ISocketListener> mISocketListeners;
    private ISocketListener mISocketListener;
    private PrintWriter mPrintWriter;
    private BufferedReader mBufferedReader;
    private ScheduledThreadPoolExecutor executor;
    private long pingIntervalMillis=5000;
    private Runnable writerRunnable;
    private LinkedBlockingQueue<IRequestPacket> messageQueue;
    private IDecoder mIDecoder;
    private HeartBeat mHeatBeat;
    private int MaxRetryTime;
    private java.net.SocketAddress mInetSocketAddress;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int reconnectCount;
    private long RECONNECT_INTERVAL=5000;
    private ConnectRunnable mConnectRunnable;

    public void setHeartBeat(HeartBeat mHeatBeat) {
        this.mHeatBeat = mHeatBeat;
    }

    public void setInetSocketAddress(SocketAddress mInetSocketAddress) {
        this.mInetSocketAddress = mInetSocketAddress;
    }

    public SocketClient(Context mContext) {
        mISocketListeners = new CopyOnWriteArrayList<>();
        mISocketListeners.add(new DefaultSocketListener());
        messageQueue = new LinkedBlockingQueue<>();

        this.mContext =mContext;
        writerRunnable =new Runnable() {
            @Override
            public void run() {
                while (writeFrame());
            }
        };
    }

    public void setIDecoder(IDecoder mIDecoder) {
        this.mIDecoder = mIDecoder;
    }

    private boolean writeFrame() {
        if (mSocket==null|| mSocket.isOutputShutdown()|| mSocket.isClosed()|| mPrintWriter==null){
            for (ISocketListener mSocketListener : mISocketListeners) {
                mSocketListener.onClose(this);
            }
            return false;
        }
        IRequestPacket mPoll = messageQueue.poll();
        Log.e(TAG, "writeFrame: "+Thread.currentThread().getName());
        Log.e(TAG, "writeFrame: " + executor.getQueue().size());
        for (ISocketListener mSocketListener : mISocketListeners) {
            mSocketListener.onSendMsgBefore(this,mPoll);
        }
       // System.out.println(String.valueOf(mPoll.write()));
        mPrintWriter.write(mPoll.write());

        for (ISocketListener mSocketListener : mISocketListeners) {
            mSocketListener.onSendMsgAfter(this,mPoll);
        }
        return true;
    };

    public void addSocketListener(ISocketListener mISocketListener){
        mISocketListeners.add(mISocketListener);
    }

    public void removeSocketListener(ISocketListener mISocketListener) {
        if (mISocketListeners.contains(mISocketListener)) {
            mISocketListeners.remove(mISocketListener);
        }
    }


    @Override
    public void connect() {
        this.executor = new ScheduledThreadPoolExecutor(1,threadFactory(getClass().getName(), false));
        mConnectRunnable = new ConnectRunnable();
        executor.execute(mConnectRunnable);

    }
    private void retryConnect() {
        if (!isNetworkConnected(mContext)) return;

        long delay = (long)reconnectCount * RECONNECT_INTERVAL;
        executor.schedule(mConnectRunnable,delay > RECONNECT_MAX_TIME ? RECONNECT_MAX_TIME : delay, TimeUnit.MILLISECONDS);
        reconnectCount++;

    }
    private void looperReader() throws IOException {
        InputStream mInputStream = mSocket.getInputStream();
            while (mSocket!=null&&mSocket.isInputShutdown()){
                if (mIDecoder != null) {
                    IResponsePacket mDecode = mIDecoder.decode(this, mInputStream);
                    if (mDecode != null) {

                        for (ISocketListener mSocketListener : mISocketListeners) {
                            mSocketListener.onMsg(this,mDecode);
                        }

                    }
                }
            }
    }
    public static ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            @Override public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }
    private void initReaderAndWriter() throws IOException {
        mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        mPrintWriter = new PrintWriter(mSocket.getOutputStream());
        String name ="socket";

//        if (pingIntervalMillis != 0) {
//            executor.scheduleAtFixedRate(
//                    new PingRunnable(), pingIntervalMillis, pingIntervalMillis, MILLISECONDS);
//        }
        if (!messageQueue.isEmpty()) {
            runWriter(); // Send messages that were enqueued before we were connected.
        }
        looperReader();
    }

    private void runWriter() {

        if (executor!=null){
            executor.execute(writerRunnable);
        }
    }

    @Override
    public void sendMsg(IRequestPacket mIPacket) {

        messageQueue.add(mIPacket);
        runWriter();
    }

    @Override
    public void sendMsg(final String msg, final String charset) {


        messageQueue.add(new IRequestPacket() {
            @Override
            public String write()  {

                   return msg;

            };
        });

    }

    @Override
    public void disConnect() {

        if (executor!=null){
            executor.shutdown();
            mSocket=null;
        }

    }
    private final class PingRunnable implements Runnable {
        @Override
        public void run() {
            if (mHeatBeat != null&&mHeatBeat.getMsg()!=null) {
                sendMsg(mHeatBeat.getMsg());
            }

        }
    }

    private class ConnectRunnable implements Runnable {

        @Override
        public void run() {
            if (mSocket == null) {
                mSocket = new Socket();
            }
            try {
                //这是一个阻塞方法
                mSocket.connect(mInetSocketAddress);
                for (ISocketListener mSocketListener : mISocketListeners) {
                    mSocketListener.onConnect(SocketClient.this);
                }
                initReaderAndWriter();

            } catch (IOException mE) {
                mE.printStackTrace();
                for (ISocketListener mSocketListener : mISocketListeners) {
                    mSocketListener.onFailure(SocketClient.this,mE);
                }
            }
        }
    }

    private final class DefaultSocketListener implements ISocketListener {
        @Override
        public void onConnect(SocketClient mSocketClient) {

        }

        @Override
        public void onSendMsgBefore(SocketClient mSocketClient, IRequestPacket mWrite) {

        }

        @Override
        public void onSendMsgAfter(SocketClient mSocketClient, IRequestPacket mWrite) {

        }

        @Override
        public void onMsg(SocketClient mSocketClient, IResponsePacket mDecode) {

        }

        @Override
        public void onFailure(SocketClient mSocketClient, Throwable mThrowable) {

            retryConnect();
        }

        @Override
        public void onClose(SocketClient mSocketClient) {
            retryConnect();
        }
    }

    //检查网络是否连接
    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
