package com.delta.thingsocket.lib;

/**
 * description :
 *
 * @author :  Wenju.Tian
 * @version date : 2017/10/19 9:28
 */


public class SocketRequest {

    private String ip ;
    private int port;
    private long heartBeatTime;
    private int retryTime;

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public long getHeartBeatTime() {
        return heartBeatTime;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public SocketRequest(Builder mBuilder) {
        ip=mBuilder.ip;
        port=mBuilder.port;
        heartBeatTime=mBuilder.heartBeatTime;
        retryTime =mBuilder.retryTime;
    }

    public static class Builder{

        private String ip ;
        private int port;
        private long heartBeatTime;
        private int retryTime;

        public Builder setIp(String mip) {
            ip = mip;
            return this;
        }

        public Builder setPort(int mPort) {
            port = mPort;
            return this;
        }

        public Builder setHeartBeatTime(long mHeartBeatTime) {
            heartBeatTime = mHeartBeatTime;
            return this;
        }

        public Builder setRetryTime(int mRetryTime) {
            retryTime = mRetryTime;
            return this;
        }

        public SocketRequest build(){

            if (ip==null|| port ==0){
                throw new IllegalArgumentException("Ip or port is Empty");
            }
            return new SocketRequest(this);
        }
    }

}
