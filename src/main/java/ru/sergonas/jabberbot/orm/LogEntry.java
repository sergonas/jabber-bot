package ru.sergonas.jabberbot.orm;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * User: Сергей
 * Date: 14.08.13
 * Time: 10:37
 */

@Entity
@Table (name = "Logs")
public class LogEntry {
    public LogEntry() {}

    public LogEntry(Date timestamp, String username, String message) {
        this.time = timestamp;
        this.name = username;
        this.message = message;
    }

    private Long id;
    private Date time;
    private String name;
    private String message;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
