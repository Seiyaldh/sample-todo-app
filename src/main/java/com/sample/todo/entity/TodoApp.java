package com.sample.todo.entity;

import java.io.Serializable;

import javax.validation.constraints.Size;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * TODO_APPテーブルに該当するエンティティクラス<br>
 * JavaBeansのルールに従っています。
 */
public class TodoApp implements Serializable {
    /**
     * おまじない
     */
    private static final long serialVersionUID = 1L;

    private int todoId;

    @Size(min = 1, max = 30, message="Titleは{min}文字以上{max}文字以下です。") //titleの文字数制限
    private String title;

    @Size(min = 1, max = 30, message="Detailは{min}文字以上{max}文字以下です。") //detailの文字数制限
    private String detail;

    @DateTimeFormat(pattern = "yyyy-M-d")//dateの入力パターンを制限
    private Date dueDate;

    @DateTimeFormat(pattern = "yyyy-M-d")//dateの入力パターンを制限
    private Date startDueDate;

    public TodoApp() {
    }

    public int getTodoId() {
        return this.todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date startDueDate) {
        this.dueDate = startDueDate;
    }

    public Date getStartDueDate() {
        return this.startDueDate;
    }

    public void setStartDueDate(Date startDueDate) {
        this.startDueDate = startDueDate;
    }
}
