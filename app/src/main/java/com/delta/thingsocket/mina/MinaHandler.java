package com.delta.thingsocket.mina;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/20 10:39
 */


class MinaHandler implements IoHandler {
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("server start"+session.hashCode());
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {

        System.out.println("server start  11"+session.hashCode());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {

    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {

        System.out.println("status"+status.toString());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

        String text = ((String) message);
        System.out.println(session.hashCode()+text);
        session.write("replay:"+text+"\r\n");
    }


    @Override
    public void messageSent(IoSession session, Object message) throws Exception {

    }

    @Override
    public void inputClosed(IoSession session) throws Exception {

    }
}
