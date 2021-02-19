package com.sample.todo.entity;

import java.io.Serializable;

import javax.validation.constraints.Size;

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

    @Size(min = 0, max = 30, message="titleは{min}文字以上{max}文字以下です。") //titleの文字数制限
    private String title;

    @Size(min = 0, max = 30, message="detailは{min}文字以上{max}文字以下です。") //detailの文字数制限
    private String detail;

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
}
