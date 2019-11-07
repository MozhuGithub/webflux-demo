package com.kn.webflux.entity;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: User
 * @Description TODO:
 * @Date: 2019/11/3 21:50
 * @Author: Kn
 */
@Data
@Builder(toBuilder = true)
@Document("system_user")
public class User {
    @Id
    private String id;
    @NotBlank
    private String name;
    @Range(min = 0, max = 100)
    private Integer age;
}
