package com.sample.todo.service;

import java.util.List;

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

    public void register(String title, String detail) {
        int nextId = dao.getNextId();
        dao.insert(nextId, title, detail);
    }

    //解除する
    public void release(int todoId) {
        dao.extract(todoId);
    }

}
