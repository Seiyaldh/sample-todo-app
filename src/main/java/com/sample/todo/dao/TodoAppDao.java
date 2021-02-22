package com.sample.todo.dao;

import java.util.Date;
import java.util.List;

import com.sample.todo.entity.TodoApp;
import com.sample.todo.entity.TodoAppRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * データアクセスオブジェクト（DataAccessObject=Dao）<br>
 * データアクセス関連を記述するクラス
 */
@Component
public class TodoAppDao {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public List<TodoApp> getTodoAppList() {
        List<TodoApp> resultList = jdbcTemplate.query("SELECT * FROM TODO_APP", new MapSqlParameterSource(null),
                new TodoAppRowMapper());
        return resultList;
    }

    //レコードが0の時にnullではなく、0を返すように変更
    public int getNextId() {
        int maxTodoId = jdbcTemplate.queryForObject("SELECT NVL(MAX(TODO_ID),0) FROM TODO_APP;",
                new MapSqlParameterSource(null), Integer.class);
        return ++maxTodoId;
    }

    public <T> void insert(int todoId, String title, String detail, Date dueDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("todoId", todoId);
        paramMap.addValue("title", title);
        paramMap.addValue("detail", detail);
        paramMap.addValue("dueDate", dueDate);
        jdbcTemplate.update("INSERT INTO TODO_APP VALUES(:todoId, :title, :detail, :dueDate)", paramMap);
    }

    /*テーブル削除命令を追加*/
    public <T> void extract(int todoId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("todoId", todoId);
        jdbcTemplate.update("DELETE FROM TODO_APP WHERE TODO_ID = :todoId",paramMap);
    }

    //テーブル更新命令を追加
    public <T> void update(int todoId, String title, String detail, Date dueDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("todoId", todoId);
        paramMap.addValue("title", title);
        paramMap.addValue("detail", detail);
        paramMap.addValue("dueDate", dueDate);
        jdbcTemplate.update("UPDATE TODO_APP SET TITLE = :title, DETAIL = :detail, DUE_DATE = :dueDate WHERE TODO_ID = :todoId", paramMap);
    }

    //titleによるテーブル検索命令を行う
    public List<TodoApp> getSearchTitle(String title) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        String qer = "";
        qer = "SELECT * FROM TODO_APP WHERE TITLE LIKE :title";
        if (title != ""){
            paramMap.addValue("title", "%" + title + "%");
        } else {
            paramMap.addValue("title", null);
        }
        List<TodoApp> resultList = jdbcTemplate.query(qer,paramMap,new TodoAppRowMapper());
        return resultList;
    }

    //detailによるテーブル検索命令を行う
    public List<TodoApp> getSearchDetail(String detail) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        String qer = "";
        qer = "SELECT * FROM TODO_APP WHERE DETAIL LIKE :detail";
        if (detail != ""){
            paramMap.addValue("detail", "%" + detail + "%");
        } else {
            paramMap.addValue("detail", null);
        }
        List<TodoApp> resultList = jdbcTemplate.query(qer,paramMap,new TodoAppRowMapper());
        return resultList;
    }

    //dueDateによるテーブル検索命令を行う
    public List<TodoApp> getSearchDueDate(Date dueDate, Date startDueDate) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        String qer = "";
        paramMap.addValue("startDueDate", startDueDate);
        paramMap.addValue("dueDate", dueDate);
        if (startDueDate != null && dueDate == null) {
            qer = "SELECT * FROM TODO_APP WHERE DUE_DATE >= :startDueDate";
        } else {
            if (startDueDate == null && dueDate != null) {
                qer = "SELECT * FROM TODO_APP WHERE DUE_DATE <= :dueDate";
            } else {
                qer = "SELECT * FROM TODO_APP WHERE DUE_DATE >= :startDueDate AND DUE_DATE <= :dueDate";
            }
        }
        List<TodoApp> resultList = jdbcTemplate.query(qer,paramMap,new TodoAppRowMapper());
        return resultList;
    }
}
