package com.romanport.rpws.protocol;

import com.romanport.rpws.RpwsLog;
import com.romanport.rpws.util.ByteDecoder;
import com.romanport.rpws.util.DecoderStream;
import com.romanport.rpws.util.EncoderStream;

import java.lang.reflect.Constructor;

//Class for all PebblePackets. Useless for the most part on it's own
public abstract class PebblePacket {

    //Updates the metadata of the packet from a type
    public void UpdateInfoFromType(PebblePacketType type) {
        this.endpoint = type.GetId();
        this.endpointCode = type.GetSubId();
    }

    //Decoding
    //Decodes a framed packet WITHOUT IT'S LENGTH!
    public static PebblePacket DecodePacket(byte[] b) {
        //Check if the packet is long enough
        if(b.length < 5) {
            RpwsLog.Log("pbl-protocol-fail-length", "Failed to read Pebble protocol packet because it was too short.");
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
            //Decode the payload for this type (yuck)
            Constructor<?> ctor = type.GetTypeClass().getConstructor();
            PebblePacket object = (PebblePacket)ctor.newInstance(new Object[] { });
            object.type = type;
            object.endpoint = type.GetId();
            object.endpointCode = type.GetSubId();
            object.DecodePayload(type, ds);

            return object;
        } catch (Exception ex) {
            RpwsLog.Log("pbl-protocol-fail-instantiate", "Failed to instantiate Pebble protocol packet with endpoint "+endpoint+" ("+endpointCode+") and payload length "+(b.length - 3)+"! Dropping...");
            return null;
        }
    }

    //Decodes just the packet data itself without the frame
    public abstract void DecodePayload(PebblePacketType type, DecoderStream ds);

    //Instance data
    public PebblePacketType type;
    public int endpoint; //The endpoint ID, an unsigned short.
    public int endpointCode; //The 1-byte code following an endpoint

    //Encoding
    //Encodes a packet with a frame INCLUDING the length.
    public byte[] EncodePacket() {
        //Encode the packet
        byte[] packetPayload = EncodePayload();

        //Produce the final area
        EncoderStream es = new EncoderStream(packetPayload.length + 5);
        es.WriteInt16((short)packetPayload.length);
        es.WriteInt16((short)endpoint);
        es.WriteByte((byte)endpointCode);
        es.WriteBytes(packetPayload);

        //Submit this message
        return es.source;
    }

    //Encodes just the packet data itself without the frame
    public abstract byte[] EncodePayload();
}
