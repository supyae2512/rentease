package com.rentease.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.rentease.utils.Helper.AsianCurrency;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "properties")
public class Property {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // e.g. “Cozy Room near MRT”
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

	private double price;

    private String address;
    private String city;
    private String postalCode;
    private String country;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private Boolean isActive = true;
    
    @Column(name="latitude", nullable=true)
    private float latitude;
    
    @Column(name="longitude", nullable=true)
    private float longitude;
    
    // Enum Section;
    public enum PropertyStatus {
        FOR_RENT,
        FOR_SALE
    }
    
    public enum PropertyType {
        HDB,
        CONDO,
        APARTMENT,
        VILLA,
        LANDED_HOUSE,
        ROOM,
        STUDIO
    }
    
    public enum LeaseTerm {
    	YEARLY,
    	MONTHLY,
    	WEEKLY
    }
    
    @Enumerated(EnumType.STRING)
    private PropertyStatus propertyStatus;
    
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;
    
    @Enumerated(EnumType.STRING)
    private LeaseTerm leaseTerm;
    
    
    @Column(name = "min_lease_months", nullable = true, columnDefinition = "int default 0")
    private Integer minLeaseMonths;

	@Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private AsianCurrency currency;

	@ManyToOne
    @JoinColumn(name = "advertiser_id", nullable = false)
	@JsonIgnore
    private User advertiser;
	
	@Column(name = "available_date", nullable = true)
	private LocalDate availableDate;


	 public Long getId() {
		return id;
	}


	 public Long getAdvertiserId() {
	    return advertiser != null ? advertiser.getId() : null;
	 }
    
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PropertyImage> images;

	@OneToOne(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonManagedReference
    private PropertyDetail propertyDetail;
	
	
	public List<PropertyImage> getImages() {
		return images;
	}

	public void setImages(List<PropertyImage> images) {
		this.images = images;
	}
    
    public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}
    
    public float getLatitude() {
		return latitude;
	}

	public PropertyStatus getPropertyStatus() {
		return propertyStatus;
	}

	public AsianCurrency getCurrency() {
		return currency;
	}

	public void setCurrency(AsianCurrency currency) {
		this.currency = currency;
	}

	public void setPropertyStatus(PropertyStatus propertyStatus) {
		this.propertyStatus = propertyStatus;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getTitle() {
		return title;
	}

	public PropertyDetail getPropertyDetail() {
		return propertyDetail;
	}

	public void setPropertyDetail(PropertyDetail propertyDetail) {
		this.propertyDetail = propertyDetail;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setAdvertiser(User user) {
		// TODO Auto-generated method stub
		this.advertiser = user;
	}
	
	public LeaseTerm getLeaseTerm() {
		return leaseTerm;
	}

	public void setLeaseTerm(LeaseTerm leaseTerm) {
		this.leaseTerm = leaseTerm;
	}
	
	public Integer getMinLeaseMonths() {
		return minLeaseMonths;
	}

	public void setMinLeaseMonths(Integer minLeaseMonths) {
		this.minLeaseMonths = minLeaseMonths;
	}
	
	public LocalDate getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(LocalDate availableDate) {
		this.availableDate = availableDate;
	}
}
