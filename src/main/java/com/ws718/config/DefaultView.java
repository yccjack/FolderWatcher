package com.ws718.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author :MysticalYcc
 * @date :11:12 2019/2/20
 */
@Controller
@RequestMapping()
public class DefaultView  {

    @RequestMapping("/")
    public String index(){
        return "forward:/root.html";
    }
}
