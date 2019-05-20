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

    public TimeSetUTC() {
        this.UpdateInfoFromType(PebblePacketType.TIME_SET_UTC);

        unixTime = 1558244769;
        utcOffset = 0;
        timezoneName = "America/Chicago";
    }

    @Override
    public void DecodePayload(PebblePacketType type, DecoderStream ds) {
        //Will never be used.
    }

    @Override
    public byte[] EncodePayload() {
        EncoderStream es = new EncoderStream(256);
        es.WriteInt32(unixTime);
        es.WriteInt16(utcOffset);
        es.WriteLengthedString(timezoneName);
        return es.ToBytes(  );
    }

}
