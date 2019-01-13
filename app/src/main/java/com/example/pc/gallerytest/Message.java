package com.example.pc.gallerytest;

import java.util.ArrayList;

public class Message {
    private ArrayList<String> list;

    public Message(){}

    public Message(ArrayList<String> list){ this.list = list;}

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
