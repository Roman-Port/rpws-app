package com.romanport.rpws.protocol;

import android.util.Log;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.protocol.custom_types.BinaryArray;
import com.romanport.rpws.protocol.custom_types.FixedList;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

//Defines a object to be serialized or deserialized. Useless by itself, but can be extended with attributes with @PebbleProtocolSerialized()
public class PebbleProtocolSerializedObject {

    //Serialization
    public byte[] SerializeObjectToBytes() throws Exception {
        EncoderStream es = new EncoderStream(2048*2);
        SerializeObject(es);
        return es.ToBytes();
    }

    public void SerializeObject(EncoderStream es) throws Exception {

        //Find all fields and serialize them
        Class<? extends PebbleProtocolSerializedObject> packetClass = getClass();
        Field[] fields = packetClass.getFields();

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
        ByteOrder order = GetEndians(f);

        if(type == Integer.class) {
            es.WriteInt32((Integer)value, order);
        } else if (type == Short.class) {
            es.WriteInt16((Short)value, order);
        } else if (type == Long.class) {
            es.WriteInt64((Long)value, order);
        } else if (type == String.class) {
            //Strings have more settings. Get them
            PebbleProtocolSerializedString sett = (PebbleProtocolSerializedString)GetRequiredAttrib(f, PebbleProtocolSerializedString.class);
            if(sett.length() == -1)
                es.WriteLengthedString((String) value);
            else
                es.WriteConstString((String) value, sett.length());
        } else if (type == BinaryArray.class) {
            //Has special settings. Read.
            PebbleProtocolSerializedBinaryArray sett = (PebbleProtocolSerializedBinaryArray)GetRequiredAttrib(f, PebbleProtocolSerializedBinaryArray.class);
            int length = sett.length();

            //If the length is less than zero, this is referencing something by it's index. Kinda gross and janky
            if(length < 0)
                length = GetIntFromIndex(-length, this);

            //Read
            BinaryArray ba = (BinaryArray)value;
            ba.Write(es);
        } else if (type == Byte.class) {
            es.WriteByte((Byte) value);
        } else if (type == Boolean.class) {
            es.WriteBool((Boolean) value);
        } else if (type == FixedList.class) {
            //Has special settings. Read.
            PebbleProtocolSerializedFixedList sett = (PebbleProtocolSerializedFixedList)GetRequiredAttrib(f, PebbleProtocolSerializedFixedList.class);
            FixedList fl = (FixedList)value;

            //Get the referenced count
            Class<? extends PebbleProtocolSerializedObject> childType = sett.innerType();


            //Read this many
            for(int i = 0; i<fl.items.size(); i+=1) {
                PebbleProtocolSerializedObject so = (PebbleProtocolSerializedObject)fl.items.get(i);
                so.SerializeObject(es);
            }

        } else if(type == UUID.class) {
            //Convert to bytes
            UUID u = (UUID)value;
            es.WriteInt64(u.getMostSignificantBits(), order);
            es.WriteInt64(u.getLeastSignificantBits(), order);
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
        RpwsLog.Log("debugging", "Creating object "+packetClass.getName()+" at pos "+ds.pos);
        Constructor<?> ctor = packetClass.getConstructor();
        PebbleProtocolSerializedObject object = (PebbleProtocolSerializedObject)ctor.newInstance(new Object[] { });

        //First, find the number of fields to manage
        Field[] fields = packetClass.getFields();
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
        ByteOrder order = GetEndians(f);

        if(type == Integer.class) {
            f.set(t, (Integer)es.ReadInt(order));
        } else if (type == Short.class) {
            f.set(t, (Short)es.ReadShort(order));
        } else if (type == Long.class) {
            f.set(t, (Long)es.ReadLong(order));
        } else if (type == String.class) {
            //Strings have more settings. Get them
            PebbleProtocolSerializedString sett = (PebbleProtocolSerializedString) GetRequiredAttrib(f, PebbleProtocolSerializedString.class);
            if (sett.length() == -1)
                f.set(t, es.ReadLengthedString());
            else
                f.set(t, es.ReadConstString(sett.length()));
        } else if (type == BinaryArray.class) {
            //Has special settings. Read.
            PebbleProtocolSerializedBinaryArray sett = (PebbleProtocolSerializedBinaryArray)GetRequiredAttrib(f, PebbleProtocolSerializedBinaryArray.class);
            int length = sett.length();

            //If the length is less than zero, this is referencing something by it's index. Kinda gross and janky
            if(length < 0)
                length = GetIntFromIndex(-length, t);

            //Read
            f.set(t, BinaryArray.Read(es, sett.length()));
        } else if (type == Byte.class) {
            f.set(t, (Byte) es.ReadByte());
        } else if (type == Boolean.class) {
            Byte b = es.ReadByte();
            Boolean value = b == 1;
            f.set(t, value);
        } else if(type == UUID.class) {
            //Read UUID parts
            long most = es.ReadLong(order);
            long least = es.ReadLong(order);
            UUID u = new UUID(most, least);
            f.set(t, u);
        } else if (type == FixedList.class) {
            //Has special settings. Read.
            PebbleProtocolSerializedFixedList sett = (PebbleProtocolSerializedFixedList)GetRequiredAttrib(f, PebbleProtocolSerializedFixedList.class);

            //Get the referenced count
            int count = GetIntFromIndex(sett.attribute_count_index(), t);
            Class<? extends PebbleProtocolSerializedObject> childType = sett.innerType();

            //Read this many
            FixedList fl = new FixedList();
            fl.items = new LinkedList<>();
            for(int i = 0; i<count; i+=1) {
                fl.items.add(DeserializeObject(es, childType));
            }

            //Set on this object
            f.set(t, fl);
        } else if (PebbleProtocolSerializedObject.class.isAssignableFrom(type)) {
            //This is another serialized object. Serialize it as a child of this
            PebbleProtocolSerializedObject o = DeserializeObject(es, type);
            f.set(t, o);
        } else {
            //No idea.
            throw new Exception("Incompatible type to deserialize '"+type.toString()+"' with name '"+f.getName()+"' in class '"+t.getClass().getName()+"'.");
        }
    }

    private static Annotation GetRequiredAttrib(Field f, Class<? extends Annotation> classname) throws Exception {
        Annotation[] ans = f.getDeclaredAnnotations();
        for(int j = 0; j<ans.length; j++) {
            Annotation a = ans[j];
            if(a.annotationType() == classname) {
                return a;
            }
        }

        //Failed
        throw new Exception("Field with name '"+f.getName()+"' was missing a required annotation.");
    }

    private static ByteOrder GetEndians(Field f) throws Exception {
        Annotation[] ans = f.getDeclaredAnnotations();
        for(int j = 0; j<ans.length; j++) {
            Annotation a = ans[j];
            if(a.annotationType() == PebbleProtocolSerializedLittleEndian.class) {
                return ByteOrder.LITTLE_ENDIAN;
            }
        }

        //Assume default
        return ByteOrder.BIG_ENDIAN;
    }

    private static Field GetFieldByIndex(Class c, int index) {
        Field[] fields = c.getFields();
        for(int i = 0; i<fields.length; i+=1) {
            //Verify that this is a field we should serialize
            Annotation[] ans = fields[i].getDeclaredAnnotations();
            for(int j = 0; j<ans.length; j++) {
                Annotation a = ans[j];
                if(a.annotationType() == PebbleProtocolSerialized.class) {
                    //OK to deserialize, find index
                    PebbleProtocolSerialized as = (PebbleProtocolSerialized)a;
                    if(as.index() == index)
                        return fields[i];
                }
            }
        }
        return null;
    }

    /**
     * From an object, finds a field referenced by another object
     * @param index
     * @param source
     * @return
     */
    private static int GetIntFromIndex(int index, Object source) throws Exception {
        //Find the field
        Field field = GetFieldByIndex(source.getClass(), index);
        if(field == null)
            throw new Exception("Failed to find field for referenced integer, "+index+", in class "+source.getClass().getName()+".");

        Class type = field.getType();
        if(type == Integer.class) {
            return (int)field.get(source);
        } else if (type == Short.class) {
            return (short)field.get(source);
        } else if (type == Byte.class) {
            return (byte)field.get(source);
        } else {
            throw new Exception("Incompatible type, "+type.getName()+", when getting field with index "+index+", in class "+source.getClass().getName()+".");
        }
    }
}
