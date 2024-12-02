package com.example.demo;

import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication /* remember this is a special annotation
, combines @Configuration, @EnableAutoConfiguration, @ComponentScan
So, what it does is it marks the below class as a config file (@config),
then configures spring-based dependencies in this proj(@enableautoconfig),
then scans the proj for controllers,svcs,etc (@compscan)
*/
public class Demo7Application {
    public static void main(String[] args) {
        SpringApplication.run(Demo7Application.class, args);
    }

}
