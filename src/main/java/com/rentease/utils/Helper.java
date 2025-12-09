package com.rentease.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.stereotype.Component;

@Component
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
	
	public byte[] compressMedia(byte[] originalBytes) throws IOException {
	    final long MAX_SIZE = 500 * 1024; // 500 KB


	    BufferedImage image = ImageIO.read(new ByteArrayInputStream(originalBytes));

	    BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = rgbImage.createGraphics();
	    g.drawImage(image, 0, 0, null);
	    g.dispose();

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    float quality = 0.8f;

	    System.out.println("Baos size :: " + baos.size());
	    System.out.println("Baos size :: " + MAX_SIZE);
	    
	    while (baos.size() < MAX_SIZE && quality > 0.1f) {
	        baos.reset();
	        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
	        ImageWriteParam param = writer.getDefaultWriteParam();
	        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	        param.setCompressionQuality(quality);

	        try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
	            writer.setOutput(ios);
	            writer.write(null, new IIOImage(rgbImage, null, null), param);
	        } finally {
	            writer.dispose();
	        }

	        quality -= 0.1f;
	    }
	    return baos.toByteArray();
	}

}
