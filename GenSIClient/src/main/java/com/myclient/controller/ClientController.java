package com.myclient.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ：楼兰
 * @date ：Created in 2020/10/7
 * @description:
 **/
@Controller
@RequestMapping("/client")
public class ClientController {

    @RequestMapping("/receiveMessage")
    public void receiveMessage(@RequestBody String message){
        System.out.println("received message from gensi "+message);
    }
}
