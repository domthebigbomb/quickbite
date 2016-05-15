package com.cmsc436.quickbite.slidingtab.ListElements;

public class RowItem {

	 private String location_name;
	 private int icon_id;
	 private String address;
	 private String distance;
	 private String location_id;

	 public RowItem(String location_name, int icon_id, String address,
	   String distance, String location_id ) {

	  this.location_name = location_name;
	  this.icon_id = icon_id;
	  this.address = address;
	  this.distance = distance;
		 this.location_id = location_id;
	 }

	 public String getlocation_name() {
	  return location_name;
	 }

	 public void setlocation_name(String location_name) {
	  this.location_name = location_name;
	 }

	 public int geticon_id() {
	  return icon_id;
	 }

	 public void seticon_id(int icon_id) {
	  this.icon_id = icon_id;
	 }

	 public String getaddress() {
	  return address;
	 }

	 public void setaddress(String address) {
	  this.address = address;
	 }

	 public String getdistance() {
	  return distance;
	 }

	 public void setdistance(String distance) {
	  this.distance = distance;
	 }

	public String getLocation_id() {
		return location_id;
	}

	public void setLocation_id(String location_id) {
		this.location_id = location_id;
	}

	}

