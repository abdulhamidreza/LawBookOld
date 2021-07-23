package com.lb.lawbook.pojos;

import java.util.HashMap;
import java.util.List;

public class Service {
    private String location;
    private String service_type;
    private HashMap modes;
    private String exp;
    private List<String> lang;
    private String docId;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getLang() {
        return lang;
    }

    public void setLang(List<String> lang) {
        this.lang = lang;
    }

    public HashMap getModes() {
        return modes;
    }

    public void setModes(HashMap modes) {
        this.modes = modes;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getLocations() {
        return location;
    }

    public void setLocations(String location) {
        this.location = location;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

}
