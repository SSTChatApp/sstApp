package com.example.mine.mychatapplication;



class ChatMessage {

    public String body;
    public String writer;
    private String photoUrl;
    public String sendTime;
    public ChatMessage(String body, String writer,String sendTime, String photoUrl) {
        this.body = body;
        this.writer = writer;
        this.sendTime = sendTime;
        this.photoUrl = photoUrl;
    }

    public ChatMessage() {
    }
}
