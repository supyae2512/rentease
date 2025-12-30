package com.rentease.dto;

import java.util.List;

import com.rentease.entity.Property.PropertyType;

import lombok.Data;

@Data
public class PropertyHomeResponse {

	public PropertyHomeResponse() {
		
	}
	
	private List<PropertyType> propertyTypes;
    private List<String> propertyCities; 
    
	public List<PropertyType> getPropertyTypes() {
		return propertyTypes;
	}
	public void setPropertyTypes(List<PropertyType> propertyType) {
		this.propertyTypes = propertyType;
	}

	public List<String> getPropertyCities() {
		return propertyCities;
	}

	public void setPropertyCities(List<String> propertyCities) {
		this.propertyCities = propertyCities;
	}


    
    

}
