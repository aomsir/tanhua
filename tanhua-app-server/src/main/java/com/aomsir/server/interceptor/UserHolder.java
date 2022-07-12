package com.aomsir.server.interceptor;

import com.aomsir.model.domain.User;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description: 工具类,实现向ThreadLocal存储数据
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

public class UserHolder {
    private static ThreadLocal<User> tl = new ThreadLocal<>();

    /**
     * 将用户对象存入ThreadLocal
     * @param user
     */
    public static void set(User user) {
        tl.set(user);
    }

    /**
     * 从当前线程获取对象
     * @return
     */
    public static User get() {
        return tl.get();
    }

    /**
     * 从当前线程获取用户的对象id
     * @return
     */
    public static Long getUserId() {
        return tl.get().getId();
    }

    /**
     * 从当前线程获取用户的对象手机号
     * @return
     */
    public static String getUserMobile() {
        return tl.get().getMobile();
    }


    /**
     * 清空
     */
    public static void remove() {
        tl.remove();
    }
}
