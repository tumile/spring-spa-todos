package com.example.server.todo.dto;

import javax.validation.constraints.NotBlank;

public class AddTodoRequest {

    @NotBlank
    private String content;

    public AddTodoRequest() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
