package com.kn.webflux.exception;

import lombok.Data;

/**
 * @ClassName: CheckException
 * @Description TODO:
 * @Date: 2019/11/4 22:51
 * @Author: Kn
 */
@Data
public class CheckException extends RuntimeException {
    //出错字段的名字
    private String fieldName;
    //出错字段的值
    private String fieldValue;

    public CheckException(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

}
