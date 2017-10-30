package com.delta.thingsocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/18 9:34
 */


public class ThingSocket  {

    private BufferedReader mBufferedReader;
    private  ISocketlistener mISocketlistener;

    public void setISocketlistener(ISocketlistener mISocketlistener) {
        this.mISocketlistener = mISocketlistener;
    }

    interface ISocketlistener{
        void close();
    }


    public static void main(String[] args) {

        //new ThingSocket().connect();
        ArrayList<String> date = new ArrayList<>();
        date.add("df");
        List<String> mStrings = date.subList(0, 1);
        mStrings.add("te");
        mStrings.size();
        System.out.println(mStrings.size());
//        try {
//            RandomAccessFile mRandomAccessFile = new RandomAccessFile("test", "rw");
//            FileChannel inchannel = mRandomAccessFile.getChannel();
//            ByteBuffer mAllocate = ByteBuffer.allocate(48);
//            int mRead = inchannel.read(mAllocate);
//            while (mRead != -1) {
//                mAllocate.flip();
//                inchanne
//            }
//        } catch (FileNotFoundException mE) {
//            mE.printStackTrace();
//        } catch (IOException mE) {
//            mE.printStackTrace();
//        }



    }

    private void connect() {
        try {
            Socket mSocket =new Socket("127.0.0.1",80);
            System.out.println("connect");
            mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            BufferedReader systemReader = new BufferedReader(new InputStreamReader(System.in));
            startServerReplyListener(mBufferedReader);
            BufferedWriter mBufferWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
            String systemMsg;
            while ((systemMsg=systemReader.readLine() )!= "\r\n") {
                mBufferWriter.write(systemMsg);
                System.out.println(systemMsg);
                mBufferWriter.newLine();
                mBufferWriter.flush();
            }
        } catch (IOException mE) {
            mE.printStackTrace();
            if (mISocketlistener!=null){
                connect();
            }
        }
    }

    private void startServerReplyListener(final BufferedReader mBufferedReader) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String receivedMsg ;
                try {
                    while ((receivedMsg = mBufferedReader.readLine()) != "fdsg") {
                        System.out.println(receivedMsg);
                    }
                } catch (IOException mE) {
                    System.out.println(mE.getMessage());
                    mE.printStackTrace();
                }
            }
        }).start();
    }

}
