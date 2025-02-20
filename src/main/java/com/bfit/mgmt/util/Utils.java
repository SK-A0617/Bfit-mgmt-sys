package com.bfit.mgmt.util;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class Utils {
	
	@Value("${category.price.general}")
	public String generalCategory;
	
	@Value("${category.price.cardio}")
	public String cardioCategory;
	
	@Value("${category.price.general-and-cardio}")
	public String generalAndCardioCategory;

}
