package model;

import lombok.Data;

import java.sql.Date;

@Data
public class Log {
    private Long id;
    private Date date;
    private String text;

    public Log() {
    }

    public Log(Date date, String text) {
        this.date = date;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }
}
