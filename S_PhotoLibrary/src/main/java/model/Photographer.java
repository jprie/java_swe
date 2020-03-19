package model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Access(AccessType.PROPERTY)
public class Photographer {

	private LongProperty id;
	private StringProperty firstName;
	private StringProperty lastName;
	private List<Photo> photos = new ArrayList<Photo>();

	@OneToMany(mappedBy = "photographer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public List<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}

	// default constructor
	public Photographer() {
		
		this.id = new SimpleLongProperty();
		this.firstName = new SimpleStringProperty();
		this.lastName = new SimpleStringProperty();
	}
	
	public Photographer(long id, String firstName, String lastName) {
		this.id = new SimpleLongProperty(id);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
	}

	public final LongProperty idProperty() {
		return this.id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return this.idProperty().get();
	}
	

	public void setId(final long id) {
		this.idProperty().set(id);
	}

	@Column(name = "first_name")
	public String getFirstName() {
		return this.firstName.get();
	}
	
	public void setFirstName(final String firstName) {
		this.firstName.set(firstName);
	}
	
	public StringProperty firstNameProperty() {
		return this.firstName;
	}
	
	@Column(name = "last_name")
	public String getLastName() {
		return this.lastName.get();
	}
	
	public void setLastName(final String lastName) {
		this.lastName.set(lastName);
	}
	
	
	public StringProperty lastNameProperty() {
		return this.lastName;
	}
	
	@Override
	public String toString() {
		
		return firstName.get() + " " + lastName.get();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Photographer ph = (Photographer) obj;
		return ph.getId() == this.getId();
	}

	
	
	
}
