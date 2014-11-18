package com.example.locationbasedtourguide;

public interface LocationData {
	
	public boolean hasText();
	public boolean hasImage();
	public boolean hasAudio();
	
	public String getText();
	public Byte[] getAudio();
	public Byte[] getImage();
	
	public double getLongitude();
	public double getLattitude();
	public double getRadius();
}
