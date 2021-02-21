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
    String updater(@RequestParam int todoId, @Validated @ModelAttribute TodoApp todoApp, BindingResult result, Model model) {
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
        }//もしエラーが吐かれたらエラ-メッセージを表示してupdateに戻る
        service.updater(todoId, todoApp.getTitle(), todoApp.getDetail(), todoApp.getDueDate());
        return "redirect:index";// 更新したらindexに移る
    }

    //titleによる検索の指示を行う
    @RequestMapping(value = "/searchTitle" , method = { RequestMethod.GET, RequestMethod.POST })
    String searchTitle(@ModelAttribute TodoApp todoApp, Model model) {
        List<TodoApp> todoList = service.getSearchTitle(todoApp.getTitle());
        if (todoList.size()!=0){
            model.addAttribute("todoList", todoList);// ここの"todoList"というキーがindex.htmlで参照されている
            return "index";// もしListが検索されたら検索結果を渡す
        }
        todoList = service.getTodoAppList();
        model.addAttribute("todoList", todoList);
        String no_hit_msg = msg.getMessage("no_hit", null, Locale.JAPAN);//検索結果がない時のメッセージを取得
        model.addAttribute("no_hit_msg", no_hit_msg);
        return "index";// 検索されなければ全件渡してindexに移る
    }

    //detailによる検索の指示を行う
    @RequestMapping(value = "/searchDetail" , method = { RequestMethod.GET, RequestMethod.POST })
    String searchDetail(@ModelAttribute TodoApp todoApp, Model model) {
        List<TodoApp> todoList = service.getSearchDetail(todoApp.getDetail());
        if (todoList.size()!=0){
            model.addAttribute("todoList", todoList);// ここの"todoList"というキーがindex.htmlで参照されている
            return "index";// もしListが検索されたら検索結果を渡す
        }
        todoList = service.getTodoAppList();
        model.addAttribute("todoList", todoList);
        String no_hit_msg = msg.getMessage("no_hit", null, Locale.JAPAN);//検索結果がない時のメッセージを取得
        model.addAttribute("no_hit_msg", no_hit_msg);
        return "index";// 検索されなければ全件渡してindexに移る
    }

    //dueDateによる検索の指示を行う
    @RequestMapping(value = "/searchDueDate" , method = { RequestMethod.GET, RequestMethod.POST })
    String searchDueDate(@Validated @ModelAttribute TodoApp todoApp, BindingResult result, Model model) {
        List<TodoApp> todoList = service.getSearchDueDate(todoApp.getDueDate(),todoApp.getStartDueDate());
        if (result.hasErrors()) {
            for(ObjectError error : result.getAllErrors()) {
                if (error.getDefaultMessage().matches(".*(dueDate).*") || error.getDefaultMessage().matches(".*(startDueDate).*")) {
                    todoList = service.getTodoAppList();
                    model.addAttribute("todoList", todoList);
                    String no_hit_msg = msg.getMessage("date_error_key", null, Locale.JAPAN);//もしdueDateでエラーが起きたら自作のエラーメッセージを取得
                    model.addAttribute("no_hit_msg", no_hit_msg);
                    return "index";// duedDateでエラーが起これば全件渡してindexに移る
                }
            }
        }
        if (todoList.size()!=0){
            model.addAttribute("todoList", todoList);// ここの"todoList"というキーがindex.htmlで参照されている
            return "index";// もしListが検索されたら検索結果を渡す
        }
        todoList = service.getTodoAppList();
        model.addAttribute("todoList", todoList);
        String no_hit_msg = msg.getMessage("no_hit", null, Locale.JAPAN);//検索結果がない時のメッセージを取得
        model.addAttribute("no_hit_msg", no_hit_msg);
        return "index";// 検索されなければ全件渡してindexに移る
    }
}
