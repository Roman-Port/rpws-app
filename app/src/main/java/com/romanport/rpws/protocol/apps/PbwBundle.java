package com.romanport.rpws.protocol.apps;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.romanport.rpws.entities.PebbleHardware;
import com.romanport.rpws.entities.pbw.PbwBinary;
import com.romanport.rpws.entities.pbw.PbwBinaryHeader;
import com.romanport.rpws.entities.pbw.PbwManifest;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class PbwBundle {

    public ZipFile zip;

    public static PbwBundle OpenBundleFromZip(File f) throws Exception {
        //Open PBW from ZIP
        PbwBundle b = new PbwBundle();
        b.zip = new ZipFile(f);

        return b;
    }

    public ZipEntry OpenZipEntry(String path) {
        return zip.getEntry(path);
    }

    public ZipEntry OpenHardwareFile(PebbleHardware hardware, String pathname) {
        //Opens a file from a specified Pebble hardware folder
        String hardwareFolder = "";
        switch(hardware) {
            case aplite:
                hardwareFolder = "aplite/";
                break;
            case basalt:
                hardwareFolder = "basalt/";
                break;
            case chalk:
                hardwareFolder = "chalk/";
                break;
            case diorite:
                hardwareFolder = "diorite/";
                break;
            case emery:
                hardwareFolder = "emery/";
                break;
        }
        return OpenZipEntry(hardwareFolder+pathname);
    }

    public byte[] ReadZipEntry(ZipEntry e) throws Exception {
        byte[] buf = new byte[(int)e.getSize()];
        zip.getInputStream(e).read(buf);
        return buf;
    }

    public PbwManifest GetManifest(PebbleHardware hardware) throws Exception {
        //Get ZIP entry
        ZipEntry file = OpenHardwareFile(hardware, "manifest.json");

        //Get JSON from this
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        PbwManifest manifest = gson.fromJson(new String(ReadZipEntry(file)), PbwManifest.class);

        return manifest;
    }

    public InputStream GetBinaryStream(PebbleHardware hardware, PbwBinary binary) throws Exception {
        ZipEntry e = OpenHardwareFile(hardware, binary.name); //Get entry
        return zip.getInputStream(e);
    }

    public PbwBinaryHeader GetBinaryHeader(PebbleHardware hardware, PbwBinary binary) throws Exception {
        InputStream is = GetBinaryStream(hardware, binary);
        return PbwBinaryHeader.OpenHeaderFromBinary(is);
    }
}
