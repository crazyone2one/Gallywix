package cn.master.gallywix;

import cn.master.gallywix.entity.SystemUser;
import cn.master.gallywix.mapper.SystemUserMapper;
import jakarta.annotation.Resource;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootTest
class GallywixApplicationTests {

    @Resource
    SystemUserMapper systemUserMapper;
    @Resource
    PasswordEncoder passwordEncoder;
    @Test
    void contextLoads() {
        SystemUser user = SystemUser.builder()
                .username("admin")
                .nickname("Administrator")
                .password(passwordEncoder.encode("123456"))
                .email("admin@gallywix.cn")
                .status(true)
                .creator("admin").createTime(LocalDateTime.now())
                .updater("admin").updateTime(LocalDateTime.now())
                .build();
        systemUserMapper.insert(user);
    }

}
