
package com.example.pgg.qboxdemo.module.weather.api.entity;

 
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Wind implements Serializable{

	private static final long serialVersionUID = -4630866105753746574L;
	@SerializedName("deg")
    @Expose
    public String deg;
    @SerializedName("dir")
    @Expose
    public String dir;
    @SerializedName("sc")
    @Expose
    public String sc;
    @SerializedName("spd")
    @Expose
    public String spd;

}
