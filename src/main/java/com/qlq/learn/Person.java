package com.qlq.learn;

public class Person {
    private String age = "2";


    public void setAge(String age) {
        this.age = age;
    }

    public void asp() {
        System.out.println("asp");
    }

    @Override
    public String toString() {
        return "person：" + this.age + "";
    }
}
