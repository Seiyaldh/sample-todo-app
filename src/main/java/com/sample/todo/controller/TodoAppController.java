package com.sample.todo.controller;

import java.util.List;

import com.sample.todo.entity.TodoApp;
import com.sample.todo.service.TodoAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ブラウザからのリクエストはここにくる
 */
@Controller
public class TodoAppController {

    @Autowired
    private TodoAppService service;

    /**
     * valueの部分がURL<br>
     * POSTを許可しているのは、{@code #register(TodoApp, Model)} からredirectしてくるため
     */
    @RequestMapping(value = { "/", "index" }, method = { RequestMethod.GET, RequestMethod.POST })
    String index(Model model) {
        List<TodoApp> todoList = service.getTodoAppList();
        model.addAttribute("todoList", todoList);// ここの"todoList"というキーがindex.htmlで参照されている
        return "index";// resources/index.htmlを指している
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    String add(Model model) {
        return "detail";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    String register(@ModelAttribute TodoApp todoApp, Model model) {
        service.register(todoApp.getTitle(), todoApp.getDetail());
        return "redirect:index";// 登録したらindexに移る
    }

    //解除の指示を行う
    @RequestMapping(value = "/release", method = { RequestMethod.GET, RequestMethod.POST })
    String release(@RequestParam int releaseId, Model model) {
        service.release(releaseId);
        return "redirect:index";// 解除したらindexに移る
    }

    @RequestMapping(value = "/change", method =  {RequestMethod.GET, RequestMethod.POST })
    String change(@RequestParam int todoId, Model model) {
        model.addAttribute("todoId", todoId);
        return "update";
    }

    //更新の指示を行う
    @RequestMapping(value = "/updater", method = { RequestMethod.GET, RequestMethod.POST })
    String release(@RequestParam int todoId, @ModelAttribute TodoApp todoApp, Model model) {
        service.updater(todoId, todoApp.getTitle(), todoApp.getDetail());
        return "redirect:index";// 登録したらindexに移る
    }

}
