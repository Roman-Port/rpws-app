package com.romanport.rpws.protocol;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public abstract class PebblePacket extends PebbleProtocolSerializedObject {

    public abstract PebblePacketType GetPacketType();

}
