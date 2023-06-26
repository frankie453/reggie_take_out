package com.itheima.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class R<T> {
    private Integer code;
    private String msg;
    private T data;
    private Map map=new HashMap();

    public static <T> R<T> success(T data) {
        R<T> r= new R<T> ();
        r.code=1;
        r.data=data;
        return r;
    }
    public static <T> R<T> error(String message) {
        R<T> r= new R<T> ();
        r.code=0;
        r.msg=message;
        return r;
    }

}
