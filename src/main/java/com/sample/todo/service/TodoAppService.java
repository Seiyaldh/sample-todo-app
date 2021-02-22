package com.sample.todo.service;

import java.util.List;
import java.util.Date;

import com.sample.todo.dao.TodoAppDao;
import com.sample.todo.entity.TodoApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ロジックを記述するクラス<br>
 *
 * @Componentと書いておくと、他からはは@Autowiredと記述すれば利用できる。Spring Beanという概念。
 */
@Component
public class TodoAppService {

    /**
     * TodoAppDaoは@Componentを持っているので、@Autowiredで利用できる（裏でSpringがこっそりセットしています）
     */
    @Autowired
    private TodoAppDao dao;

    public List<TodoApp> getTodoAppList() {
        return dao.getTodoAppList();
    }

    public void register(String title, String detail, Date dueDate) {
        int nextId = dao.getNextId();
        dao.insert(nextId, title, detail, dueDate);
    }

    //解除する
    public void release(int todoId) {
        dao.extract(todoId);
    }

    //更新する
    public void updater(int todoId, String title, String detail, Date dueDate) {
        dao.update(todoId, title, detail, dueDate);
    }

    //タイトルで検索する
    public List<TodoApp> getSearchTitle(String title) {
        return dao.getSearchTitle(title);
    }

    //締切日で検索する
    public List<TodoApp> getSearchDetail(String detail) {
        return dao.getSearchDetail(detail);
    }

    //締切日で検索する
    public List<TodoApp> getSearchDueDate(Date dueDate, Date stratDueDate) {
        return dao.getSearchDueDate(dueDate, stratDueDate);
    }

}
