package com.rentease.utils;

public class Helper {

	
	
	public enum AsianCurrency {
	    SGD("Singapore Dollar", "SGD"),
	    MYR("Malaysian Ringgit", "MYR"),
	    THB("Thai Baht", "THB"),
	    IDR("Indonesian Rupiah", "IDR"),
	    PHP("Philippine Peso", "PHP"),
	    VND("Vietnamese Dong", "VND"),
	    KHR("Cambodian Riel", "KHR"),
	    LAK("Lao Kip", "LAK"),
	    MMK("Myanmar Kyat", "MMK"),
	    BND("Brunei Dollar", "BND"),

	    CNY("Chinese Yuan", "CNY"),
	    HKD("Hong Kong Dollar", "HKD"),
	    TWD("New Taiwan Dollar", "TWD"),
	    JPY("Japanese Yen", "JPY"),
	    KRW("South Korean Won", "KRW"),

	    INR("Indian Rupee", "INR"),
	    PKR("Pakistani Rupee", "PKR"),
	    BDT("Bangladeshi Taka", "BDT"),
	    LKR("Sri Lankan Rupee", "LKR"),
	    NPR("Nepalese Rupee", "NPR"),
	    MVR("Maldivian Rufiyaa", "MVR"),
	    AFN("Afghan Afghani", "AFN"),

	    AED("UAE Dirham", "AED"),
	    SAR("Saudi Riyal", "SAR"),
	    QAR("Qatari Riyal", "QAR"),
	    KWD("Kuwaiti Dinar", "KWD"),
	    BHD("Bahraini Dinar", "BHD"),
	    OMR("Omani Rial", "OMR"),
	    ILS("Israeli Shekel", "ILS");

	    private final String fullName;
	    private final String code;

	    AsianCurrency(String fullName, String code) {
	        this.fullName = fullName;
	        this.code = code;
	    }

	    public String getFullName() {
	        return fullName;
	    }

	    public String getCode() {
	        return code;
	    }
	}
}
