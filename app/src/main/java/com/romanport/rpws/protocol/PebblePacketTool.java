package com.romanport.rpws.protocol;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.util.ByteDecoder;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

import java.lang.reflect.Constructor;

//Class for all PebblePackets. Useless for the most part on it's own
public abstract class PebblePacketTool {

    //Decoding
    //Decodes a framed packet WITHOUT IT'S LENGTH!
    public static PebblePacket DecodePacket(byte[] b) {
        //Check if the packet is long enough
        if(b.length < 3) {
            RpwsLog.Log("pbl-protocol-fail-length", "Failed to read Pebble protocol packet because it was too short.");
            //Todo: Handle this
            return null;
        }

        //Open decoder stream
        DecoderStream ds = new DecoderStream(b);

        //Read endpoint and code
        int endpoint = ds.ReadUShort();
        byte endpointCode = ds.ReadByte();

        //Find the type if we can
        PebblePacketType type = PebblePacketType.fromId(endpoint, endpointCode);
        if(type == null) {
            RpwsLog.Log("pbl-protocol-fail-lookup", "Failed to find Pebble protocol packet with endpoint "+endpoint+" ("+endpointCode+")! Dropping...");
            return null;
        }

        try {
            //Decode the payload for this type
            PebblePacket packet = (PebblePacket)PebblePacket.DeserializeObject(ds, type.GetTypeClass());
            return packet;
        } catch (Exception ex) {
            RpwsLog.Log("pbl-protocol-fail-instantiate", "Failed to instantiate Pebble protocol packet with endpoint "+endpoint+" ("+endpointCode+") and payload length "+(b.length - 3)+"! Dropping...");
            return null;
        }
    }

    //Encoding
    //Encodes a packet with a frame INCLUDING the length.
    public static byte[] EncodePacket(PebblePacket p) throws Exception {
        //Encode the packet
        EncoderStream bes = new EncoderStream(2048);
        p.SerializeObject(bes);
        int length = bes.len;

        //Get type
        PebblePacketType t = p.GetPacketType();

        //Produce the final area
        EncoderStream es = new EncoderStream(length + 5);
        es.WriteInt16((short)(length+1));
        es.WriteInt16((short)t.GetId());
        es.WriteByte((byte)t.GetSubId());
        es.WriteBytes(bes.ToBytes());

        //Submit this message
        return es.source;
    }
}
