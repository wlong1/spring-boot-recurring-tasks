package com.wlong.springboottododailies.controllers;

import com.wlong.springboottododailies.models.TodoItem;
import com.wlong.springboottododailies.repositories.TodoItemRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.time.ZoneId;


@Controller
public class TodoItemController {
    private final Logger logger = LoggerFactory.getLogger(TodoItemController.class);
    String weekDay;

    @Autowired
    TodoItemRepository todoItemRepository;

    @GetMapping("/")
    public ModelAndView index() {
        logger.debug("request to GET index");
        ModelAndView modelAndView = new ModelAndView("index");
        // Get all items in repository, put it into a list keyed todoItems
        modelAndView.addObject("todoItems", todoItemRepository.findAll());
        weekDay = String.valueOf(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek());
        weekDay = weekDay.toLowerCase();
        weekDay = weekDay.substring(0, 1).toUpperCase() + weekDay.substring(1);
        modelAndView.addObject("today", weekDay);
        return modelAndView;
    }

    @PostMapping("/create-todo")
    public String createTodoItem(@Valid TodoItem todoItem, BindingResult result, Model model){
        if (result.hasErrors()){
            return "add-todo-item";
        }

        todoItem.setDuration(1);
        todoItem.setModifiedDate(Instant.now());
        todoItem.setComplete(false);
        todoItemRepository.save(todoItem);
        return "redirect:/";
    }

    @PostMapping("/edit/{id}")
    public String updateTodoItem(@PathVariable("id") long id, @Valid TodoItem todoItem, BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("todo", todoItem); // Pass item into form
            return "update-todo-item"; // Return if error
        }

        todoItem.setModifiedDate(Instant.now());
        todoItemRepository.save(todoItem);
        return "redirect:/"; //  Redirect view to index
    }

    @PostMapping("/toggle/{id}")
    public String toggleComplete(@PathVariable("id") long id, Model model){
        TodoItem todoItem = todoItemRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("TodoItem id: " + id + " not found"));

        todoItem.setComplete(!todoItem.getComplete());
        todoItemRepository.save(todoItem);

        return "redirect:/";
    }



}











