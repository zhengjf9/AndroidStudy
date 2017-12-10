package com.example.zhengjiafeng.lab2;

/**
 * Created by zhengjiafeng on 2017/10/23.
 */

public class FirstEvent {
    private String namemsg;
    private String pricemsg;
    public FirstEvent(String str1, String str2) {
        namemsg = str1;
        pricemsg = str2;
    }
    public String getNamemsg(){
        return namemsg;
    }
    public String getPricemsg(){
        return pricemsg;
    }
}
