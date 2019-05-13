package com.zereao.apphunter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@PropertySource({"classpath:mail.properties"})
@SpringBootApplication(scanBasePackages = {"com.zereao.apphunter"})
public class AppHunter {

    public static void main(String[] args) {
        SpringApplication.run(AppHunter.class, args);
    }

}
