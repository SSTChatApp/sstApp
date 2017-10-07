package com.example.mine.mychatapplication;



class Position {
    public String name;
    public String positionCode;
    public String closed;
    public String levelCode;

    public Position(String name, String positionCode, String closed, String levelCode) {
        this.name = name;
        this.positionCode = positionCode;
        this.closed = closed;
        this.levelCode = levelCode;
    }

    public Position() {
        this.name = " ";
        this.positionCode = " ";
        this.closed = " ";
        this.levelCode = " ";
    }
}
