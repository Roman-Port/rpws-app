package com.romanport.rpws.protocol;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.util.ByteDecoder;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

import java.lang.reflect.Constructor;
import java.nio.ByteOrder;

//Class for all PebblePackets. Useless for the most part on it's own
public abstract class PebblePacketTool {

    //Decoding
    //Decodes a framed packet WITHOUT IT'S LENGTH!
    public static PebblePacket DecodePacket(byte[] b) {
        //Check if the packet is long enough
        if(b.length < 2) {
            RpwsLog.Log("pbl-protocol-fail-length", "Failed to read Pebble protocol packet because it was too short. Packets after this WILL fail.");
            //Todo: Handle this
            return null;
        }

        //Open decoder stream
        DecoderStream ds = new DecoderStream(b);

        //Read endpoint and check if we have one registered
        int endpoint = ds.ReadUShort(ByteOrder.BIG_ENDIAN);
        if(!PebblePacketType.CheckIfEndpointRegistered(endpoint)) {
            RpwsLog.Log("pbl-protocol-fail-lookup", "Failed to find Pebble protocol packet with endpoint "+endpoint+" and any code! Dropping...");
            return null;
        }

        //Check if this type must be read with a sub endpoint
        PebblePacketType type = PebblePacketType.fromId(endpoint, -1);
        byte endpointCode;
        if(type == null && b.length >= 3)
            endpointCode = ds.ReadByte();
        else
            endpointCode = -1;

        //Find the type if we can
        type = PebblePacketType.fromId(endpoint, endpointCode);
        if(type == null) {
            RpwsLog.Log("pbl-protocol-fail-lookup", "Failed to find Pebble protocol packet with endpoint "+endpoint+" ("+endpointCode+")! Dropping...");
            return null;
        }

        try {
            //Decode the payload for this type
            PebblePacket packet = (PebblePacket)PebblePacket.DeserializeObject(ds, type.GetTypeClass());
            return packet;
        } catch (Exception ex) {
            RpwsLog.LogException("pbl-protocol-fail-instantiate", "Failed to instantiate Pebble protocol packet with endpoint "+endpoint+" ("+endpointCode+") and payload length "+(b.length - 3)+"! Dropping with error: ",ex);
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
        es.WriteInt16((short)(length+1), ByteOrder.BIG_ENDIAN);
        es.WriteInt16((short)t.GetId(), ByteOrder.BIG_ENDIAN);
        es.WriteByte((byte)t.GetSubId());
        es.WriteBytes(bes.ToBytes());

        //Submit this message
        return es.source;
    }
}
