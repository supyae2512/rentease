package com.rentease.dto;

import java.util.Map;

import com.rentease.entity.PropertyDetail.FurnishType;

public class PropertyDetailDTO {

	private Integer bedrooms;
    private Integer bathrooms;
    private Integer kitchens;
    private Double squareFeet;
    private Integer yearBuilt;

    // Amenities
    private Boolean hasParking;
    private Boolean hasSwimmingPool;
    private Boolean hasElevator;
    private Boolean hasGarden;
    private Boolean hasSecurity;
    private Boolean hasGym;

    private FurnishType furnishType;    // FULLY_FURNISHED, PARTIALLY_FURNISHED, UNFURNISHED

    private Map<String, String> additionalFeatures;
    
    public Integer getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(Integer bedrooms) {
		this.bedrooms = bedrooms;
	}

	public Integer getBathrooms() {
		return bathrooms;
	}

	public void setBathrooms(Integer bathrooms) {
		this.bathrooms = bathrooms;
	}

	public Integer getKitchens() {
		return kitchens;
	}

	public void setKitchens(Integer kitchens) {
		this.kitchens = kitchens;
	}

	public Double getSquareFeet() {
		return squareFeet;
	}

	public void setSquareFeet(Double squareFeet) {
		this.squareFeet = squareFeet;
	}

	public Integer getYearBuilt() {
		return yearBuilt;
	}

	public void setYearBuilt(Integer yearBuilt) {
		this.yearBuilt = yearBuilt;
	}

	public Boolean getHasParking() {
		return hasParking;
	}

	public void setHasParking(Boolean hasParking) {
		this.hasParking = hasParking;
	}

	public Boolean getHasSwimmingPool() {
		return hasSwimmingPool;
	}

	public void setHasSwimmingPool(Boolean hasSwimmingPool) {
		this.hasSwimmingPool = hasSwimmingPool;
	}

	public Boolean getHasElevator() {
		return hasElevator;
	}

	public void setHasElevator(Boolean hasElevator) {
		this.hasElevator = hasElevator;
	}

	public Boolean getHasGarden() {
		return hasGarden;
	}

	public void setHasGarden(Boolean hasGarden) {
		this.hasGarden = hasGarden;
	}

	public Boolean getHasSecurity() {
		return hasSecurity;
	}

	public void setHasSecurity(Boolean hasSecurity) {
		this.hasSecurity = hasSecurity;
	}

	public Boolean getHasGym() {
		return hasGym;
	}

	public void setHasGym(Boolean hasGym) {
		this.hasGym = hasGym;
	}

	public FurnishType getFurnishType() {
		return furnishType;
	}

	public void setFurnishType(FurnishType furnishType) {
		this.furnishType = furnishType;
	}

	public Map<String, String> getAdditionalFeatures() {
		return additionalFeatures;
	}

	public void setAdditionalFeatures(Map<String, String> additionalFeatures) {
		this.additionalFeatures = additionalFeatures;
	}


}
