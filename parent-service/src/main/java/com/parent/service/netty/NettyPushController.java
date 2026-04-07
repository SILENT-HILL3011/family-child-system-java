package com.parent.service.netty;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NettyPushController {

    @RequestMapping("/netty/push")
    public void push(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String message
    ){
        if (userId == null || message == null) return;
        NettyServerHandler.sendToUser(userId, message);
    }
}
