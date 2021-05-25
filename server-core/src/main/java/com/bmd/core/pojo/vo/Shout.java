package com.bmd.core.pojo.vo;

import lombok.Data;

import java.io.Serializable;


@Data
public class Shout implements Serializable {
private String message;

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

}
