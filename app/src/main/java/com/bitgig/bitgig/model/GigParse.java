package com.bitgig.bitgig.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("GigParse")
public class GigParse extends ParseObject {
    public GigParse() {
        super();
    }

    public void setAudio(byte[] audio) {
        ParseFile file = new ParseFile(audio);
        put("audio", file);
    }

    public void setText(String str){
        put("bitcoin", str);
    }
}
