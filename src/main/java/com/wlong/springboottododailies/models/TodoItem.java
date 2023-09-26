package com.wlong.springboottododailies.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "todo_item")
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotEmpty(message = "Description cannot be empty.")
    private String description;

    @Getter
    @Setter
    private Boolean complete;

    @Getter
    @Setter
    private Instant modifiedDate;

    @Getter
    @Setter
    private Integer duration; // 1, 2, 3, 4 = Daily, Weekly, Monthly, Yearly respectively

    public TodoItem() {
    }

    public TodoItem(String description) {
        this.description = description;
        this.complete = false;
        this.duration = 1;
        this.modifiedDate = Instant.now();
    }

    @Override
    public String toString() {
        return String.format("TodoItem{id='%d', description='%s', complete='%s', duration='%s', modifiedDate='%s'}",
                id, description, complete, duration, modifiedDate);
    }

}
