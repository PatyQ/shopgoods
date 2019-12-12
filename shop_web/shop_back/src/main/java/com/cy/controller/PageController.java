package com.cy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "goto")
public class PageController {

    @RequestMapping("{page}")
    public String toGoPage(@PathVariable String page) {
        return page;
    }
}
