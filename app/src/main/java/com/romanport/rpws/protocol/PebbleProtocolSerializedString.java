package com.romanport.rpws.protocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PebbleProtocolSerializedString {

    //If -1, this is a dynamically sized string
    public int length();

}
