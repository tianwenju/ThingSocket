package com.delta.thingsocket.mina;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/20 10:12
 */


public class MinaServer {

    public static void main(String[] args) {
        try {
            IoAcceptor mIoAcceptor = new NioSocketAcceptor();
            mIoAcceptor.setHandler(new MinaHandler());
            mIoAcceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new TextLineCodecFactory()));
            mIoAcceptor.getFilterChain().addLast("df",new ProtocolCodecFilter(new ThingsTextFactory(Charset.forName("utf-8"),"\r\n")));
            mIoAcceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,10);
            mIoAcceptor.bind(new InetSocketAddress(9090));

        } catch (IOException mE) {
            mE.printStackTrace();
        }

    }
}
