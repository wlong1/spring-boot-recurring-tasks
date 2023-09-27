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
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;


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

        // Check and refresh items in repository
        refreshRepo();

        // Get all items in repository, put it into a list keyed todoItems
        modelAndView.addObject("todoItems", todoItemRepository.findAll());

        // Get weekday and format it
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

        // Set time
        Calendar temp = todoItem.getModifiedDate();
        temp.set(Calendar.HOUR_OF_DAY, 0);
        temp.set(Calendar.MINUTE, 0);
        temp.set(Calendar.SECOND, 0);
        temp.set(Calendar.MILLISECOND, 0);
        todoItem.setModifiedDate(temp);

        logger.debug("Duration is: " + todoItem.getDuration());

        todoItemRepository.save(todoItem);
        return "redirect:/";
    }

    @PostMapping("/edit/{id}")
    public String updateTodoItem(@PathVariable("id") long id, @Valid TodoItem todoItem, BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("todo", todoItem); // Pass item into form
            return "update-todo-item"; // Return if error
        }


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

    private void refreshRepo(){
        Iterable<TodoItem> temp = todoItemRepository.findAll();
        Calendar today = new GregorianCalendar();
        today.setTime(new Date());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);


        for (TodoItem todoItem : temp) {
            Calendar todoDate = todoItem.getModifiedDate();
            long diff = ChronoUnit.DAYS.between(todoDate.toInstant(), today.toInstant());
            if (today.toInstant().isBefore(todoDate.toInstant()) || !todoItem.getComplete()){
                continue; // If haven't reached, move to next
            }

            switch(Integer.valueOf(todoItem.getDuration())){
                case 1: // Daily
                    if (diff >= 1){
                        saveAndGo(todoItem, today);
                    }
                    break;
                case 2: // Weekly
                    if (diff >= 7){
                        saveAndGo(todoItem, today);
                    }
                    break;
                case 3: // Monthly
                    if (todoDate.get(Calendar.MONTH) < today.get(Calendar.MONTH)){
                        saveAndGo(todoItem, today);
                    }
                    break;
                case 4: // Yearly
                    if (todoDate.get(Calendar.YEAR) < today.get(Calendar.YEAR)){
                        saveAndGo(todoItem, today);
                    }
                    break;
            }
        }
    }

    private void saveAndGo(TodoItem todoItem, Calendar today){
        todoItem.setModifiedDate(today);
        todoItem.setComplete(false);
        todoItemRepository.save(todoItem);
    }



}











