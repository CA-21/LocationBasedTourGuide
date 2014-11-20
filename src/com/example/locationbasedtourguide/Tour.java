package com.example.locationbasedtourguide;

public class Tour implements Comparable<Tour> {
	
	private String name;

	@Override
	public int compareTo(Tour another) {
		
		return name.compareTo(another.name);
	}
	
	public void tester();

}
