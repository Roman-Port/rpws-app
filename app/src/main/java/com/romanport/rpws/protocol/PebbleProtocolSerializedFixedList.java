package com.romanport.rpws.protocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PebbleProtocolSerializedFixedList {

    public Class<? extends PebbleProtocolSerializedObject> innerType();
    public int attribute_count_index(); //Index to check when reading
    public int data_length_index(); //Index to check when reading

}
