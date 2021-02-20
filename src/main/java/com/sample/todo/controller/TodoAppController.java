package com.sample.todo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.sample.todo.entity.TodoApp;
import com.sample.todo.service.TodoAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.context.MessageSource;

/**
 * ブラウザからのリクエストはここにくる
 */
@Controller
public class TodoAppController {

    @Autowired
    private TodoAppService service;
    
    //エラーメッセージを取得
    @Autowired
    private MessageSource msg;

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

    @RequestMapping(value = "/new", method = { RequestMethod.GET, RequestMethod.POST })
    String add(@Validated @ModelAttribute("todoApp") TodoApp todoApp, BindingResult result, Model model) {
        return "detail";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    String register(@Validated @ModelAttribute TodoApp todoApp, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<String> errorList = new ArrayList<String>();
            for(ObjectError error : result.getAllErrors()) {
                if (error.getDefaultMessage().matches(".*(dueDate).*")){
                    String message = msg.getMessage("date_error_key", null, Locale.JAPAN);
                    errorList.add(message);//もしdueDateでエラーが起きたら自作のエラーメッセージをadd
                } else {
                    errorList.add(error.getDefaultMessage());
                }
            }
            model.addAttribute("validationError", errorList);
            return "detail";
        }//もしエラーが吐かれたらエラーメッセージを表示しdetailに戻る
        service.register(todoApp.getTitle(), todoApp.getDetail(), todoApp.getDueDate());
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
    String updater(@RequestParam int todoId, @Validated @ModelAttribute("TodoApp")  TodoApp todoApp, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<String> errorList = new ArrayList<String>();
            for(ObjectError error : result.getAllErrors()) {
                if (error.getDefaultMessage().matches(".*(dueDate).*")){
                    String message = msg.getMessage("date_error_key", null, Locale.JAPAN);
                    errorList.add(message);//もしdueDateでエラーが起きたら自作のエラーメッセージをadd
                } else {
                    errorList.add(error.getDefaultMessage());
                }
            }
            model.addAttribute("validationError", errorList);         
            model.addAttribute("todoId", todoId);
            return "update";
        }//もしエラーが吐かれたらエラ〜メッセージを表示してupdateに戻る
        service.updater(todoId, todoApp.getTitle(), todoApp.getDetail(), todoApp.getDueDate());
        return "redirect:index";// 更新したらindexに移る
    }

}
