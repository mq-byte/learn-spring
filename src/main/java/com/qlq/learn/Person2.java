package com.qlq.learn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

@Component(value="per2")
public class Person2 {

    @Autowired
    protected int value;

//    public void setAge(int value){
//        this.value = value;
//    }
}
