
package com.example.pgg.qboxdemo.module.weather.api.entity;

 
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Tmp implements Serializable{

	private static final long serialVersionUID = 2217337469703819836L;
	@SerializedName("max")
    @Expose
    public String max;
    @SerializedName("min")
    @Expose
    public String min;

}
