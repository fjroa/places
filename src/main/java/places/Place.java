package places;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.social.facebook.api.Location;
import org.springframework.social.facebook.api.Page;

@Document

public class Place implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@GeoSpatialIndexed(name = "position")
	private double[] position;
	
	private String city;
	private String country;
	private String description;
	private double latitude;
	private double longitude;
	private String state;
	private String street;
	private String zip;
	private String name;
	private String affiliation;
	private String category;
	private String about;
	private Date insertionDate;
	
	
	@Override
	public String toString() {
		return "Place [id=" + id + ", position=" + Arrays.toString(position)
				+ ", city=" + city + ", country=" + country + ", description="
				+ description + ", latitude=" + latitude + ", longitude="
				+ longitude + ", state=" + state + ", street=" + street
				+ ", zip=" + zip + ", name=" + name + ", affiliation="
				+ affiliation + ", category=" + category + ", about=" + about
				+ ", insertionDate=" + insertionDate + "]";
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public double[] getPosition() {
		return position;
	}


	public void setPosition(double[] position) {
		this.position = position;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public double getLongitude() {
		return longitude;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getStreet() {
		return street;
	}


	public void setStreet(String street) {
		this.street = street;
	}


	public String getZip() {
		return zip;
	}


	public void setZip(String zip) {
		this.zip = zip;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAffiliation() {
		return affiliation;
	}


	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getAbout() {
		return about;
	}


	public void setAbout(String about) {
		this.about = about;
	}


	public Date getInsertionDate() {
		return insertionDate;
	}


	public void setInsertionDate(Date insertionDate) {
		this.insertionDate = insertionDate;
	}


	public Place(Page p) {
		super();
		this.affiliation = p.getAffiliation();
		this.id = p.getId();
		this.name = p.getName();
		this.category = p.getCategory();
		this.about = p.getAbout();
		this.insertionDate = new Date();
		
		Location pageLocation = p.getLocation();
		
		 
		this.city = pageLocation.getCity();
		this.country = pageLocation.getCountry();
		this.description = pageLocation.getDescription();
		this.latitude = pageLocation.getLatitude();
		this.longitude = pageLocation.getLongitude();
		this.state = pageLocation.getState();
		this.street = pageLocation.getStreet();
		this.zip = pageLocation.getZip();

		this.position = new double[]{this.longitude, this.latitude};
	}


	public Place() {
	}
	
	
	
	
}
