package com.kn.webflux.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserService
 * @Description TODO:
 * @Date: 2019/11/6 10:18
 * @Author: Kn
 */
@Service
public class UserService {
    public ResponseEntity<String> run1() {
        throw new NullPointerException();
    }
}
