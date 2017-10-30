package com.delta.thingsocket.lib;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/19 10:30
 */


public class SocketPackage {
    private final SocketPackage self = this;

    private static final AtomicInteger IDAtomic = new AtomicInteger();
    private Long lastSendTime;

    /* Constructors */
    public SocketPackage(byte[] data) {
        this(data, false);
    }

    public SocketPackage(byte[] data, boolean isHeartBeat) {
        this.ID = IDAtomic.getAndIncrement();
        this.data = Arrays.copyOf(data, data.length);
        this.heartBeat = isHeartBeat;
    }

    public SocketPackage(String message) {
        this.ID = IDAtomic.getAndIncrement();
        this.message = message;
    }

    /* Public Methods */
    public void buildDataWithCharsetName(String charsetName) {
        if (getMessage() != null) {
            this.data = ChartUtil.stringToData(getMessage(), charsetName);
        }
    }

    /* Properties */
    /**
     * ID, unique
     */
    private final int ID;
    public int getID() {
        return this.ID;
    }

    /**
     * bytes data
     */
    private byte[] data;
    public byte[] getData() {
        return this.data;
    }

    /**
     * string data
     */
    private String message;
    public String getMessage() {
        return this.message;
    }

    private boolean heartBeat;
    public boolean isHeartBeat() {
        return this.heartBeat;
    }

    private byte[] headerData;
    public SocketPackage setHeaderData(byte[] headerData) {
        this.headerData = headerData;
        return this;
    }
    public byte[] getHeaderData() {
        return this.headerData;
    }

    private byte[] packetLengthData;
    public SocketPackage setPacketLengthData(byte[] packetLengthData) {
        this.packetLengthData = packetLengthData;
        return this;
    }
    public byte[] getPacketLengthData() {
        return this.packetLengthData;
    }

    private byte[] trailerData;
    public SocketPackage setTrailerData(byte[] trailerData) {
        this.trailerData = trailerData;
        return this;
    }
    public byte[] getTrailerData() {
        return this.trailerData;
    }

    public byte[] writeByte() {


        return new byte[5];
    }
}
