package com.rentease.entity;


import java.util.Map;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonType;

import jakarta.persistence.*;

@Entity
public class PropertyDetail {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @OneToOne
    @JoinColumn(name = "property_id")
    @JsonBackReference
    private Property property;

    
    public enum FurnishType {
        FULLY_FURNISHED,
        PARTIALLY_FURNISHED,
        UNFURNISHED
    }

    @Enumerated(EnumType.STRING)
    private FurnishType furnishType;
    
    @Type(JsonType.class)
    @Column(columnDefinition = "JSON")
    private Map<String, String> additionalFeatures;


    public Map<String, String> getAdditionalFeatures() {
		return additionalFeatures;
	}

	public void setAdditionalFeatures(Map<String, String> additionalFeatures) {
		this.additionalFeatures = additionalFeatures;
	}

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

	public FurnishType getFurnishType() {
		return furnishType;
	}

	public void setFurnishType(FurnishType furnishType) {
		this.furnishType = furnishType;
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

    public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	

}