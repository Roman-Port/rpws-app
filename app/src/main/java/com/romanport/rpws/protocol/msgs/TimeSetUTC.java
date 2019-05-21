package com.romanport.rpws.protocol.msgs;

import com.romanport.rpws.protocol.PebblePacket;
import com.romanport.rpws.protocol.PebblePacketType;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

public class TimeSetUTC extends PebblePacket {

    /*
    unix_time = Uint32()
    utc_offset = Int16()
    tz_name = PascalString()
     */

    public int unixTime;
    public short utcOffset;
    public String timezoneName;

    @Override
    public PebblePacketType GetPacketType() {
        return PebblePacketType.TIME_SET_UTC;
    }

}
