package com.example.markitsurvey.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProjectMetaDatum implements Serializable {


    @SerializedName("metaname")
    @Expose
    private String metaname;
    @SerializedName("displaylabel")
    @Expose
    private String displaylabel;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("options")
    @Expose
    private List<Option> options = null;

    public String getMetaname() {
        return metaname;
    }

    public void setMetaname(String metaname) {
        this.metaname = metaname;
    }

    public String getDisplaylabel() {
        return displaylabel;
    }

    public void setDisplaylabel(String displaylabel) {
        this.displaylabel = displaylabel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
