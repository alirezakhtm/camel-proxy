package com.khtm.demo.camel.restservicetest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static final String TAG = "UserController";
    private Logger logger = LoggerFactory.getLogger(TAG);

    @RequestMapping(value = "/user-sample", method = RequestMethod.GET)
    public User getSampleUser(){
        logger.info("SAMPLE USER REST API HAS BEEN CALLED.");
        User user = new User();
        user.setFname("Tom");
        user.setLname("Patter");
        user.setId(1L);
        return user;
    }

    @RequestMapping(value = "/user-echo", method = RequestMethod.POST)
    public User getUserEcho(@RequestBody User user){
        user.setFname(user.getFname() + " - edited");
        user.setLname(user.getLname() + " - edited");
        return user;
    }
}
