package com.delta.thingsocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.delta.thingsocket.socket.HeartBeat;
import com.delta.thingsocket.socket.IDecoder;
import com.delta.thingsocket.socket.IRequestPacket;
import com.delta.thingsocket.socket.IResponsePacket;
import com.delta.thingsocket.socket.ISocketListener;
import com.delta.thingsocket.socket.SocketClient;

import java.io.InputStream;
import java.net.InetSocketAddress;

public class MainActivity extends AppCompatActivity implements ISocketListener {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocketClient mSocketClient = new SocketClient(this);
        InetSocketAddress mInetSocketAddress = new InetSocketAddress("10.0.2.2",9090);
        mSocketClient.setInetSocketAddress(mInetSocketAddress);
        mSocketClient.setIDecoder(new IDecoder() {
            @Override
            public IResponsePacket decode(SocketClient mSocketClient, InputStream mInputStream) {
                return null;
            }
        });
        HeartBeat mHeartBeat = new HeartBeat();
        mHeartBeat.setMsg(new IRequestPacket() {
            @Override
            public String write() {
                return "heart beat\r\n";
            }
        });
        mSocketClient.setHeartBeat(mHeartBeat);
        mSocketClient.addSocketListener(this);
        mSocketClient.connect();
    }

    @Override
    public void onConnect(SocketClient mSocketClient) {
        Log.d(TAG, "onConnect() called with: mSocketClient = [" + mSocketClient + "]");
        mSocketClient.sendMsg(new IRequestPacket() {
            @Override
            public String write() {
                return "msgr\r\n";
            }
        });
    }

    @Override
    public void onSendMsgBefore(SocketClient mSocketClient, IRequestPacket mWrite) {

        Log.d(TAG, "onSendMsgBefore() called with: mSocketClient = [" + mSocketClient + "], mWrite = [" + mWrite + "]");
    }

    @Override
    public void onSendMsgAfter(SocketClient mSocketClient, IRequestPacket mWrite) {

        Log.d(TAG, "onSendMsgAfter() called with: mSocketClient = [" + mSocketClient + "], mWrite = [" + mWrite + "]");
    }

    @Override
    public void onMsg(SocketClient mSocketClient, IResponsePacket mDecode) {

        Log.d(TAG, "onMsg() called with: mSocketClient = [" + mSocketClient + "], mDecode = [" + mDecode + "]");
    }

    @Override
    public void onFailure(SocketClient mSocketClient, Throwable mThrowable) {

        Log.d(TAG, "onFailure() called with: mSocketClient = [" + mSocketClient + "], mThrowable = [" + mThrowable + "]");
    }

    @Override
    public void onClose(SocketClient mSocketClient) {

        Log.d(TAG, "onClose() called with: mSocketClient = [" + mSocketClient + "]");
    }
}
