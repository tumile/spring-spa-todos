package com.example.server.todo;

import com.example.server.todo.dto.AddTodoRequest;
import com.example.server.todo.dto.UpdateTodoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<Object> getTodos(Principal principal) {
        List<Todo> todos = todoService.getTodos(getUserId(principal));
        return ResponseEntity.ok().body(todos);
    }

    @PostMapping
    public ResponseEntity<Object> addTodo(Principal principal, @Valid @RequestBody AddTodoRequest requestBody) {
        Todo todo = todoService.addTodo(getUserId(principal), requestBody);
        return ResponseEntity.ok().body(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTodo(Principal principal, @Valid @RequestBody UpdateTodoRequest requestBody,
                                        @PathVariable Long id) {
        Todo todo = todoService.updateTodo(getUserId(principal), id, requestBody);
        return ResponseEntity.ok().body(todo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTodo(Principal principal, @PathVariable Long id) {
        todoService.deleteTodo(getUserId(principal), id);
        return ResponseEntity.ok().build();
    }

    private Long getUserId(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
