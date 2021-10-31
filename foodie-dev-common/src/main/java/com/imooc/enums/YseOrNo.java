package com.imooc.enums;

public enum YseOrNo {
    NO(0,"否"),
    YES(1,"是")
    ;
    public final Integer type;
    public final String value;

    YseOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
