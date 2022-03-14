package com.example.iplogger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LinkShortenerController обеспечивает переход по короткой ссылке и логирование информации из запроса
 */

@Controller
@RequestMapping("/link")
public class LinkShortenerController {

    @GetMapping("/{id}")
    public void getLink(@PathVariable String id,  HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteIp = request.getRemoteAddr();
        //TODO: взять ссылку из репы, если ссылки нет - сделать редирект на заглушку
        //TODO: написать заглушку??? хотя это REST, так что пофиг - HTTP.404
        System.out.println("requested id: " + id + " from ip " + remoteIp);
        response.sendRedirect("https://3dnews.ru/");
    }
}
