/*
 * Copyright (C) 2018 bsyonline
 */
package com.rolex.microlabs.model;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * @author rolex
 * @since 2018
 */
@Data
public class User {
    
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄不能小于1岁")
    @Max(value = 150, message = "年龄不能大于150岁")
    private int age;
    @NotNull(message = "电话不能为空")
    @Pattern(regexp = "^1[34578]\\d{9}$", message = "电话格式不正确")
    private String phone;
    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱不正确")
    private String email;
    
}
