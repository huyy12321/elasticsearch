package com.example.search.controller;

import com.example.search.DTO.User;
import com.example.search.service.SearchService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author hyy
 */
@RestController
public class UserController {
    @Resource
    private SearchService searchService;

    @RequestMapping("/save")
    public String save(User user) {
        searchService.save(user);
        return "save";
    }

    @RequestMapping("/del/{id}")
    public String del(@PathVariable("id")Long id) {
        searchService.del(id);
        return "del";
    }

    @RequestMapping("/update")
    public String update(User user) {
        if(user.getId() == null) {
            return "null";
        }
        searchService.update(user);
        return "update";
    }

    @RequestMapping("/get/{id}")
    public String get(@PathVariable("id")Long id) {
        User user = searchService.get(User.builder().id(id).build());
        return user == null ? "null" : user.toString();
    }

    @RequestMapping("/search")
    public List<User> search(User user) {
       return searchService.search(user);
    }
}
