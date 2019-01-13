package com.example.pc.gallerytest;

public class MessageSet {
    public MessageSet(){};

    public MessageSet(boolean resultSet) {this.resultSet = resultSet;}

    private boolean resultSet;

    public boolean isResultSet() {
        return resultSet;
    }

    public void setResultSet(boolean resultSet) {
        this.resultSet = resultSet;
    }
}
