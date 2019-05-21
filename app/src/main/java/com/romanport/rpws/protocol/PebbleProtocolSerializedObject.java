package com.romanport.rpws.protocol;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

//Defines a object to be serialized or deserialized. Useless by itself, but can be extended with attributes with @PebbleProtocolSerialized()
public class PebbleProtocolSerializedObject {

    //Serialization

    public void SerializeObject(EncoderStream es) throws Exception {

        //Find all fields and serialize them
        Class<? extends PebbleProtocolSerializedObject> packetClass = getClass();
        Field[] fields = packetClass.getDeclaredFields();

        //First, find the number of fields to manage
        Field[] fieldsToMange = new Field[64]; //Maximum number of fields possible. Doubt it'll ever need to be increased, but maybe....
        for(int i = 0; i<fields.length; i+=1) {
            Field f = fields[i];

            //Verify that this is a field we should serialize
            Annotation[] ans = f.getDeclaredAnnotations();
            for(int j = 0; j<ans.length; j++) {
                Annotation a = ans[j];
                if(a.annotationType() == PebbleProtocolSerialized.class) {
                    //OK to serialize, find index
                    PebbleProtocolSerialized as = (PebbleProtocolSerialized)a;
                    fieldsToMange[as.index()] = f;
                }
            }
        }

        //Now, begin reading fields from zero
        for(int i = 0; i<fieldsToMange.length; i+=1) {
            Field f = fieldsToMange[i];
            if(f != null) {
                //Serialize now
                EncodeFieldToBytes(f, es);
                //RpwsLog.Log("test", f.getName());
            }
        }
    }

    private void EncodeFieldToBytes(Field f, EncoderStream es) throws Exception {
        Class type = f.getType();
        Object value = f.get(this);

        if(type == Integer.class) {
            es.WriteInt32((Integer)value);
        } else if (type == Short.class) {
            es.WriteInt16((Short)value);
        } else if (type == Long.class) {
            es.WriteInt64((Long)value);
        } else if (type == String.class) {
            es.WriteLengthedString((String) value);
        } else if (type == Byte.class) {
            es.WriteByte((Byte)value);
        } else if (PebbleProtocolSerializedObject.class.isAssignableFrom(type)) {
            //This is another serialized object. Serialize it as a child of this
            PebbleProtocolSerializedObject child = (PebbleProtocolSerializedObject)value;
            child.SerializeObject(es);
        } else {
            //No idea.
            throw new Exception("Incompatible type to serialize '"+type.toString()+"' with name '"+f.getName()+"' in class '"+getClass().getName()+"'.");
        }
    }


    //Deserialization
    public static PebbleProtocolSerializedObject DeserializeObject(DecoderStream ds, Class<? extends PebbleProtocolSerializedObject> packetClass) throws Exception {

        //Instantiate new
        Constructor<?> ctor = packetClass.getConstructor();
        PebbleProtocolSerializedObject object = (PebbleProtocolSerializedObject)ctor.newInstance(new Object[] { });

        //First, find the number of fields to manage
        Field[] fields = packetClass.getDeclaredFields();
        Field[] fieldsToMange = new Field[64]; //Maximum number of fields possible. Doubt it'll ever need to be increased, but maybe....
        for(int i = 0; i<fields.length; i+=1) {
            Field f = fields[i];

            //Verify that this is a field we should serialize
            Annotation[] ans = f.getDeclaredAnnotations();
            for(int j = 0; j<ans.length; j++) {
                Annotation a = ans[j];
                if(a.annotationType() == PebbleProtocolSerialized.class) {
                    //OK to deserialize, find index
                    PebbleProtocolSerialized as = (PebbleProtocolSerialized)a;
                    fieldsToMange[as.index()] = f;
                }
            }
        }

        //Now, begin reading fields from zero
        for(int i = 0; i<fieldsToMange.length; i+=1) {
            Field f = fieldsToMange[i];
            if(f != null) {
                //Deserialize now
                DecodeFieldToBytes(f, ds, object);
                //RpwsLog.Log("test-deser", f.getName());
            }
        }

        return object;
    }

    private static void DecodeFieldToBytes(Field f, DecoderStream es, PebbleProtocolSerializedObject t) throws Exception {
        Class type = f.getType();

        if(type == Integer.class) {
            f.set(t, (Integer)es.ReadInt());
        } else if (type == Short.class) {
            f.set(t, (Short)es.ReadShort());
        } else if (type == Long.class) {
            f.set(t, (Long)es.ReadLong());
        } else if (type == String.class) {
            f.set(t, es.ReadLengthedString());
        } else if (type == Byte.class) {
            f.set(t, (Byte)es.ReadByte());
        } else if (PebbleProtocolSerializedObject.class.isAssignableFrom(type)) {
            //This is another serialized object. Serialize it as a child of this
            PebbleProtocolSerializedObject o = DeserializeObject(es, type);
            f.set(t, o);
        } else {
            //No idea.
            throw new Exception("Incompatible type to deserialize '"+type.toString()+"' with name '"+f.getName()+"' in class '"+t.getClass().getName()+"'.");
        }
    }
}
