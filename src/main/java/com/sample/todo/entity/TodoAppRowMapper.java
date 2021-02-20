package com.sample.todo.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * データベースから取出したReesultSetをTodoAppクラスに変換するクラス
 */
public class TodoAppRowMapper implements RowMapper<TodoApp> {

    @Override
    public TodoApp mapRow(ResultSet rs, int rowNum) throws SQLException {
        TodoApp app = new TodoApp();
        app.setTodoId(rs.getInt("TODO_ID"));
        app.setTitle(rs.getString("TITLE"));
        app.setDetail(rs.getString("DETAIL"));
        app.setDueDate(rs.getDate("DUE_DATE"));
        return app;
    }
}
