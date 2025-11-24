package com.rentease.dto;


import java.util.List;

import lombok.Data;

@Data
public class PropertyWidgetResponse {
    private List<TypeCount> propertyTypes;
    private List<TypeCount> statuses;
    private List<String> amenities;

    @Data
    public static class TypeCount {
        private String name;
        public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private Long count;

        public TypeCount(String name, Long count) {
            this.name = name;
            this.count = count;
        }

		public Long getCount() {
			return count;
		}

		public void setCount(Long count) {
			this.count = count;
		}
    }

	public List<TypeCount> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(List<TypeCount> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}

	public List<TypeCount> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<TypeCount> statuses) {
		this.statuses = statuses;
	}

	public List<String> getAmenities() {
		return amenities;
	}

	public void setAmenities(List<String> amenities) {
		this.amenities = amenities;
	}
}
