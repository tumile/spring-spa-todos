package com.example.server.todo;

import com.example.server.todo.dto.AddTodoRequest;
import com.example.server.todo.dto.UpdateTodoRequest;
import com.example.server.user.User;
import com.example.server.user.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public List<Todo> getTodos(Long userId) {
        return todoRepository.findAllByUserId(userId);
    }

    public Todo addTodo(Long userId, AddTodoRequest requestBody) {
        User user = userRepository.findById(userId).orElseThrow();
        Todo todo = new Todo(requestBody.getContent());
        todo.setUser(user);
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long userId, Long todoId, UpdateTodoRequest requestBody) {
        Todo todo = todoRepository.findById(todoId).orElseThrow();
        if (!todo.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException();
        }
        todo.setContent(requestBody.getContent());
        todo.setCompleted(requestBody.getCompleted());
        return todoRepository.save(todo);
    }

    public void deleteTodo(Long userId, Long todoId) {
        Todo todo = todoRepository.findById(todoId).orElseThrow();
        if (!todo.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException();
        }
        todoRepository.deleteById(todoId);
    }
}
