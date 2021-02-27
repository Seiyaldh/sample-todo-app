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

    public static List<String> getErrorList(BindingResult result, MessageSource msg) {
        List<String> errorList = new ArrayList<String>();
            for(ObjectError error : result.getAllErrors()) {
                if (error.getDefaultMessage().matches(".*(dueDate).*")){
                    String message = msg.getMessage("date_error_key", null, Locale.JAPAN);
                    errorList.add(message);//もしdueDateでエラーが起きたら自作のエラーメッセージをadd
                } else {
                    errorList.add(error.getDefaultMessage());
                }
            }
        return errorList;
    }

    public static String valEmptyTodoList(List<TodoApp> todoList, MessageSource msg, TodoAppService service, Model model) {
        if (todoList.size()!=0){
            model.addAttribute("todoList", todoList);// ここの"todoList"というキーがindex.htmlで参照されている
            return "index";// もしListが検索されたら検索結果を渡す
        }
        todoList = service.getTodoAppList();
        model.addAttribute("todoList", todoList);
        String noHitMsg = msg.getMessage("no_hit", null, Locale.JAPAN);
        model.addAttribute("noHitMsg", noHitMsg);
        return "index";
    }

    //update.htmlのパラメーターを設定
    public static void addParmUpdate(int todoId, String title, String detail, String dueDate, Model model){
        model.addAttribute("todoId", todoId);
        model.addAttribute("title", title);
        model.addAttribute("detail", detail);
        model.addAttribute("dueDate", dueDate);
        return;
    }

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
    String add(@Validated @ModelAttribute TodoApp todoApp, BindingResult result, Model model) {
        return "detail";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    String register(@Validated @ModelAttribute TodoApp todoApp, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<String> errorList = getErrorList(result, msg);
            model.addAttribute("validationError", errorList);
            return "detail";
        }//もしエラーが吐かれたらエラーメッセージを表示しdetailに戻る
        service.register(todoApp.getTitle(), todoApp.getDetail(), todoApp.getDueDate());
        return "redirect:index";
    }

    //解除の指示を行う
    @RequestMapping(value = "/release", method = { RequestMethod.GET, RequestMethod.POST })
    String release(@RequestParam int releaseId, Model model) {
        service.release(releaseId);
        return "redirect:index";
    }

    @RequestMapping(value = "/change", method =  {RequestMethod.GET, RequestMethod.POST })
    String change(@RequestParam int todoId, String title, String detail, String dueDate, Model model) {
        addParmUpdate(todoId, title, detail, dueDate, model);
        return "update";
    }

    @RequestMapping(value = "/updater", method = { RequestMethod.GET, RequestMethod.POST })
    String updater(@RequestParam int todoId, String title, String detail, String dueDate, @Validated @ModelAttribute TodoApp todoApp, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<String> errorList = getErrorList(result, msg);
            model.addAttribute("validationError", errorList);         
            addParmUpdate(todoId, title, detail, dueDate, model);
            return "update";
        }
        service.updater(todoId, todoApp.getTitle(), todoApp.getDetail(), todoApp.getDueDate());
        return "redirect:index";
    }

    @RequestMapping(value = "/searchTitle" , method = { RequestMethod.GET, RequestMethod.POST })
    String searchTitle(@ModelAttribute TodoApp todoApp, Model model) {
        List<TodoApp> todoList = service.getSearchTitle(todoApp.getTitle());
        return valEmptyTodoList(todoList, msg, service, model);
    }

    @RequestMapping(value = "/searchDetail" , method = { RequestMethod.GET, RequestMethod.POST })
    String searchDetail(@ModelAttribute TodoApp todoApp, Model model) {
        List<TodoApp> todoList = service.getSearchDetail(todoApp.getDetail());
        return valEmptyTodoList(todoList, msg, service, model);
    }

    @RequestMapping(value = "/searchDueDate" , method = { RequestMethod.GET, RequestMethod.POST })
    String searchDueDate(@Validated @ModelAttribute TodoApp todoApp, BindingResult result, Model model) {
        List<TodoApp> todoList = service.getSearchDueDate(todoApp.getDueDate(),todoApp.getStartDueDate());
        if (result.hasErrors()) {
            for(ObjectError error : result.getAllErrors()) {
                if (error.getDefaultMessage().matches(".*(dueDate).*") || error.getDefaultMessage().matches(".*(startDueDate).*")) {
                    todoList = service.getTodoAppList();
                    model.addAttribute("todoList", todoList);
                    String noHitMsg = msg.getMessage("date_error_key", null, Locale.JAPAN);//もしdueDateでエラーが起きたら自作のエラーメッセージを取得
                    model.addAttribute("noHitMsg", noHitMsg);
                    return "index";
                }
            }
        }
        return valEmptyTodoList(todoList, msg, service, model);
    }
}
