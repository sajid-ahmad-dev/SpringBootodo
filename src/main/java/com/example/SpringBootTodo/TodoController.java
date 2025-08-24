package com.example.SpringBootTodo;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class TodoController {


    private static List<Todo> todoList;

    public TodoController(){
        todoList=new ArrayList<>();

        todoList.add(new Todo(1L,false,"coding",1L));
        todoList.add(new Todo(2L,true,"lectures",2L));
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getTodo(){
        return ResponseEntity.status(HttpStatus.OK).body(todoList);

    }
    @PostMapping("/todos")
    public ResponseEntity<Todo> createtodo(@RequestBody Todo newTodo){
        todoList.add(newTodo);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTodo);
    }
    @GetMapping("/todos/{todoId}")
    public ResponseEntity<Todo> getTodoId(@PathVariable Long todoId){
        for(Todo todo:todoList){
            if(todo.getId()==todoId){
                return ResponseEntity.status(HttpStatus.FOUND).body(todo);
            }

        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/todos/{todoId}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long todoId, @RequestBody Todo newTodo) {
        for (Todo todo : todoList) {
            if (Objects.equals(todo.getId(), todoId)) {
                todo.setTitle(newTodo.getTitle());
                todo.setUserId(newTodo.getUserId());
                todo.setCompleted(newTodo.isCompleted());
                return new ResponseEntity<>(todo,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long todoId){
        for(Todo todo: todoList){
            Iterator<Todo> iterator=todoList.iterator();
            while(iterator.hasNext()){
                Todo it=iterator.next();
                if(Objects.equals(it.getId(),todoId)){
                    iterator.remove();
                    return new ResponseEntity<>("Todo Deleted Successfully",HttpStatus.OK);
                }

            }
        }
        return new ResponseEntity<>("Not Found",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/todos/filter")
    public ResponseEntity<List<Todo>> getTodos(@RequestParam(required = false) Boolean completed) {
        List<Todo> result = todoList;   // start with all todos

        if (completed != null) {        // if a filter is requested
            result = todoList.stream()
                    .filter(todo -> todo.isCompleted() == completed) // keep only matching todos
                    .collect(Collectors.toList());                 // collect as List
        }

        return ResponseEntity.ok(result); // return the filtered (or all) todos
    }
}
