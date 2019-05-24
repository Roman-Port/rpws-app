package com.romanport.rpws.entities.pbw;

import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.protocol.PebbleProtocolSerializedBinaryArray;
import com.romanport.rpws.protocol.PebbleProtocolSerializedObject;
import com.romanport.rpws.protocol.PebbleProtocolSerializedString;
import com.romanport.rpws.protocol.custom_types.BinaryArray;
import com.romanport.rpws.util.DecoderStream;

import java.io.InputStream;
import java.util.UUID;
import java.util.zip.ZipInputStream;

public class PbwBinaryHeader extends PebbleProtocolSerializedObject {

    //https://github.com/Roman-Port/rpws-blue/blob/071f7fae234804b4a387bbc73bc512a1e1d19561/RpwsBlue/RpwsBlue/Services/PublishApi/PublishServices/ReleaseCreator.cs
    //https://github.com/pebble/libpebble2/blob/23e2eb92cfc084e6f9e8c718711ac994ef606d18/libpebble2/util/bundle.py

    @PebbleProtocolSerialized(index = 0)
    @PebbleProtocolSerializedBinaryArray(length = 8)
    public BinaryArray sentinel;

    @PebbleProtocolSerialized(index = 1)
    public Byte struct_version_major;

    @PebbleProtocolSerialized(index = 2)
    public Byte struct_version_minor;

    @PebbleProtocolSerialized(index = 3)
    public Byte sdk_version_major;

    @PebbleProtocolSerialized(index = 4)
    public Byte sdk_version_minor;

    @PebbleProtocolSerialized(index = 5)
    public Byte app_version_major;

    @PebbleProtocolSerialized(index = 6)
    public Byte app_version_minor;

    @PebbleProtocolSerialized(index = 7)
    public Short app_size;

    @PebbleProtocolSerialized(index = 8)
    public Integer offset;

    @PebbleProtocolSerialized(index = 9)
    public Integer crc;

    @PebbleProtocolSerialized(index = 10)
    @PebbleProtocolSerializedString(length = 32)
    public String app_name;

    @PebbleProtocolSerialized(index = 11)
    @PebbleProtocolSerializedString(length = 32)
    public String company_name;

    @PebbleProtocolSerialized(index = 12)
    public Integer icon_resource_id;

    @PebbleProtocolSerialized(index = 13)
    public Integer symbol_table_address;

    @PebbleProtocolSerialized(index = 14)
    public Integer pebble_process_flags;

    @PebbleProtocolSerialized(index = 15)
    public Integer relocation_list_count;

    @PebbleProtocolSerialized(index = 16)
    public UUID uuid;

    public static PbwBinaryHeader OpenHeaderFromBinary(InputStream is) throws Exception {
        //This is 126 bytes long. Read that much and open
        byte[] buffer = new byte[126];
        is.read(buffer);

        //Deserialize
        return (PbwBinaryHeader)DeserializeObject(new DecoderStream(buffer), PbwBinaryHeader.class);
    }
}
