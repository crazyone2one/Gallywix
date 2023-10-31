package cn.master.gallywix;

import cn.master.gallywix.mapper.SystemUserMapper;
import jakarta.annotation.Resource;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GallywixApplicationTests {

    @Resource
    SystemUserMapper systemUserMapper;
    @Test
    void contextLoads() {
        val systemUsers = systemUserMapper.selectAll();
        System.out.println(systemUsers);
    }

}
