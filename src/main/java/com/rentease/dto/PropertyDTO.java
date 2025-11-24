package com.rentease.dto;

import java.util.List;

import com.rentease.entity.Property.PropertyStatus;
import com.rentease.entity.Property.PropertyType;
import com.rentease.utils.Helper.AsianCurrency;

public class PropertyDTO {

	private Long id;  // For update operations

    private String title;
    private String description;

    private PropertyType propertyType;   // ENUM value as string (HDB, CONDO…)
    private PropertyStatus propertyStatus; // FOR_RENT, FOR_SALE
    
    private AsianCurrency currency;

    private double price;

    private String address;
    private String city;
    private String postalCode;
    private String country;
    
    private Float latitude;
    public AsianCurrency getCurrency() {
		return currency;
	}

	public void setCurrency(AsianCurrency currency) {
		this.currency = currency;
	}

	private Float longitude;

    private Long advertiserId;  // User ID

    private PropertyDetailDTO propertyDetail;

    // Optional: for update operations (keep images or remove)
    private List<String> existingImages;

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public PropertyStatus getPropertyStatus() {
		return propertyStatus;
	}

	public void setPropertyStatus(PropertyStatus propertyStatus) {
		this.propertyStatus = propertyStatus;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Long getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(Long advertiserId) {
		this.advertiserId = advertiserId;
	}

	public PropertyDetailDTO getPropertyDetail() {
		return propertyDetail;
	}

	public void setPropertyDetail(PropertyDetailDTO propertyDetail) {
		this.propertyDetail = propertyDetail;
	}

	public List<String> getExistingImages() {
		return existingImages;
	}

	public void setExistingImages(List<String> existingImages) {
		this.existingImages = existingImages;
	}
}
