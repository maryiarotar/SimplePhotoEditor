package com.rotar.PhotoEditorWeb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authorization")
public class DaoController {

    public String returnAuthorize(){
        return "authorization";
    }

}
