package com.delta.thingsocket.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/20 14:55
 */


class ThingsTextFactory implements ProtocolCodecFactory {


    private ThingsEnCoder mThingsEnCoder;
    private ThingsDecoder mThingsDecoder;

    public ThingsTextFactory(Charset chartset, String delimiter) {
        this.mThingsEnCoder = new ThingsEnCoder(chartset,delimiter);
        this.mThingsDecoder = new ThingsDecoder(chartset,delimiter);
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return mThingsEnCoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return mThingsDecoder;
    }
}
