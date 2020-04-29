package com.springboot.entity.chenbin.personnel.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenb
 * @version 2020/4/29/029
 * description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherResponseEntity<T>{
    private String code;
    private String msg;
    private T data;
}
