package com.wlong.springboottododailies.repositories;

import com.wlong.springboottododailies.models.TodoItem;
import org.springframework.data.repository.CrudRepository;

public interface TodoItemRepository extends CrudRepository<TodoItem, Long> {

}
