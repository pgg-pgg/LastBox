
package com.example.pgg.qboxdemo.module.weather.api.entity;

 
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Flu implements Serializable{

	private static final long serialVersionUID = 3666731280251015538L;
	@SerializedName("brf")
    @Expose
    public String brf;
    @SerializedName("txt")
    @Expose
    public String txt;

}
