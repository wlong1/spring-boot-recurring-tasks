package com.wlong.springboottododailies.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Calendar modifiedDate;

    @Getter
    @Setter
    private String duration; // 1, 2, 3, 4 = Daily, Weekly, Monthly, Yearly respectively

    public TodoItem() {
    }

    public TodoItem(String description) {
        this.description = description;
        this.complete = false;
        this.duration = "1";
        Calendar temp = new GregorianCalendar();
        temp.setTime(new Date());
        temp.set(Calendar.HOUR_OF_DAY, 0);
        temp.set(Calendar.MINUTE, 0);
        temp.set(Calendar.SECOND, 0);
        temp.set(Calendar.MILLISECOND, 0);
        this.modifiedDate = temp;
    }

    @Override
    public String toString() {
        return String.format("TodoItem{id='%d', description='%s', complete='%s', duration='%s', modifiedDate='%s'}",
                id, description, complete, duration, modifiedDate);
    }

}
