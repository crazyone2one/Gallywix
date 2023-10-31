package cn.master.gallywix;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author the2n
 */
@SpringBootApplication
@MapperScan("cn.master.gallywix.mapper")
public class GallywixApplication {

    public static void main(String[] args) {
        SpringApplication.run(GallywixApplication.class, args);
    }

}
