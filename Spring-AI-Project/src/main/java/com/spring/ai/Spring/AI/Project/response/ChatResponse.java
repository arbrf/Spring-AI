package com.spring.ai.Spring.AI.Project.response;


public class ChatResponse {

    private String reply;

    public ChatResponse() {
    }

    public ChatResponse(String reply) {
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        // Print a clean version without <think> ... </think>
        if (reply != null) {
            // Remove the reasoning part between <think> and </think>
            return reply.replaceAll("(?s)<think>.*?</think>", "").trim();
        }
        return "";
    }
}

