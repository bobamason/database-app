package com.bobamason.database1;
import android.os.*;

public class Entry {
	private long id = 0;

	private String category = "";

	private String name = "";

	private float cost = 0f;

	private float value = 0f;

	private String description = "";

	public Entry() {

	}

	public void setValuefromDB(int v) {
		value = (float) v / 100f;
	}

	public void setCostfromDB(int c) {
		cost = (float) c / 100f;
	}

	public int getDBvalue() {
		return Math.round(value * 100);
	}

	public int getDBcost() {
		return Math.round(cost * 100);
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getCost() {
		return cost;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}

	public void setDescription(String decsription) {
		this.description = decsription;
	}

	public String getDescription() {
		return description;
	}

	public static Bundle entryToBundle(Entry entry) {
		Bundle b = new Bundle();
		b.putLong("id", entry.getId());
		b.putString("name", entry.getName());
		b.putString("category", entry.getCategory());
		b.putString("desc", entry.getDescription());
		b.putFloat("cost", entry.getCost());
		b.putFloat("value", entry.getValue());
		return b;	
	}

	public static Entry bundleToEntry(Bundle b) {
		Entry entry = new Entry();
		entry.setId(b.getLong("id"));
		entry.setName(b.getString("name"));
		entry.setCategory(b.getString("category"));
		entry.setDescription(b.getString("desc"));
		entry.setCost(b.getFloat("cost"));
		entry.setValue(b.getFloat("value"));
		return entry;
	}
}
