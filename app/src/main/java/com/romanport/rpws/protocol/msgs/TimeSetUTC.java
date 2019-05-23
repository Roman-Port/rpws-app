package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.protocol.PebbleProtocolSerialized;
import com.romanport.rpws.protocol.PebbleProtocolSerializedString;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

public class TimeSetUTC extends PebblePacket {

    /*
    unix_time = Uint32()
    utc_offset = Int16()
    tz_name = PascalString()
     */

    @PebbleProtocolSerialized(index = 0)
    public Integer unixTime;

    @PebbleProtocolSerialized(index = 1)
    public Short utcOffset;

    @PebbleProtocolSerialized(index = 2)
    @PebbleProtocolSerializedString(length = -1)
    public String timezoneName;

    @Override
    public PebblePacketType GetPacketType() {
        return PebblePacketType.TIME_SET_UTC;
    }

    public TimeSetUTC() {

    }
}
