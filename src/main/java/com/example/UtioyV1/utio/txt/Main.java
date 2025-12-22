package com.example.UtioyV1.utio.txt;

import org.springframework.web.util.HtmlUtils;

public class Main {
    public static void main(String[] args) {
        String specialStr ="<div id=\"testDiv\">test1;test2</div>";
        String str1 = HtmlUtils.htmlEscape(specialStr);
        System.out.println(str1);
    }
}
