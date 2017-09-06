package com.mvgreen.smsservice;

import java.util.ArrayList;

public class Data {
    public static ArrayList<Record> load(String expectedID) {
        ArrayList<Record> a = new ArrayList<Record>();
        a.add(new Record(Integer.parseInt(expectedID)));
        return a;
    }
}
