package com.qlq.learn;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void person() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("aopAnnocation.xml");
        Person p = (Person) ac.getBean("per");
        p.asp();
    }
}
