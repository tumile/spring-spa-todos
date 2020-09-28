package com.example.server.todo.dto;

public class UpdateTodoRequest {

    private String content;
    private Boolean completed = false;

    public UpdateTodoRequest() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
