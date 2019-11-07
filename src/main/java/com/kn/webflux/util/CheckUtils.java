package com.kn.webflux.util;

import com.kn.webflux.exception.CheckException;

import java.util.Arrays;

/**
 * @ClassName: CheckUtils
 * @Description TODO:
 * @Date: 2019/11/4 22:49
 * @Author: Kn
 */
public class CheckUtils {
    //不允许含有这些名词  (是否可以考虑从数据库读取 解决重名的问题)
    private static final String[] INVALID_NAMES = {"admin", "管理员"};

    /**
     * @Description TODO: 校验姓名 不成功时抛出校验异常
     * @Param: [name]
     * @Return: void
     * @Author: Kn
     * @Date: 2019/11/4 22:50
     */
    public static void CheckName(String name) {
        Arrays.stream(INVALID_NAMES)
                .filter(value -> value.equalsIgnoreCase(name))
                .findAny()
                .ifPresent(value -> {
                    throw new CheckException("name", value);
                });
    }
}
