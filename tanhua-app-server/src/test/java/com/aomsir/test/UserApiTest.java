package com.aomsir.test;

import com.aomsir.dubbo.api.UserApi;
import com.aomsir.model.domain.User;
import com.aomsir.server.AppServerApplication;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Aomsir
 * @Date: 2022/7/11
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class UserApiTest {
    @DubboReference
    private UserApi userApi;

    @Test
    public void testFindByMobile() {
        User user = userApi.findByMobile("1380013800");
        System.out.println(user);
    }

}
