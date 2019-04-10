package com.oceam.controller.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("StateInfo")
    private String stateInfo = "OK";
    @JsonProperty("StateCode")
    private int stateCode = 200;

    @Override
    public String toString() {
        return "BaseEntity{" +
                "stateInfo='" + stateInfo + '\'' +
                ", stateCode=" + stateCode +
                '}';
    }

    public BaseEntity(int stateCode) {
        this.stateCode = stateCode;
        switch (stateCode) {
            case 200:
                this.stateInfo = "OK";
                break;
            case 404:
                this.stateInfo = "接口地址不正确";
                break;
            case 405:
                this.stateInfo = "请求方法不支持,request method not allowed";
                break;
            case 500:
                this.stateInfo = "服务内部错误";
                break;
            case 1001:
                this.stateInfo = "暂无数据";
                break;
            case 1002:
                this.stateInfo = "参数错误";
                break;
            default:
                break;
        }
    }

    public BaseEntity(int stateCode, String stateInfo) {
        this.stateCode = stateCode;
        this.stateInfo = stateInfo;
    }

}
