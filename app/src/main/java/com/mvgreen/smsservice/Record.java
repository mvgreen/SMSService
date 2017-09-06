package com.mvgreen.smsservice;

public class Record {
    public int id;
    public String tel;
    public String text;

    Record() {}

    Record(int id, String tel, String text) {
        this.id = id;
        this.tel = tel;
        this.text = text;
    }
}
