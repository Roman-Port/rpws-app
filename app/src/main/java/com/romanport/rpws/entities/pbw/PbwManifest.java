package com.romanport.rpws.entities.pbw;

public class PbwManifest {

    public int manifestVersion;
    public String generatedBy;
    public int generatedAt;
    public PbwBinary worker;
    public PbwBinary application;
    public String type;
    public PbwResourceBinary resources;

}
