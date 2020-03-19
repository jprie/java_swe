package model;

import java.time.LocalDate;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
@Access(AccessType.PROPERTY)
public class Photo {

	public static final String NAME_NAME = "name";
	public static final String DATE_NAME = "date";
	public static final String LOCATION_NAME = "location";

	private LongProperty id = new SimpleLongProperty();
	private StringProperty name;
	private StringProperty url;
	private ObjectProperty<Photographer> photographer;
	private ObjectProperty<LocalDate> date;
	private StringProperty location;

	public Photo() {
		this.name = new SimpleStringProperty();
		this.url = new SimpleStringProperty();
		this.date = new SimpleObjectProperty<LocalDate>();
		this.photographer = new SimpleObjectProperty<Photographer>();
		this.location = new SimpleStringProperty();
	}

	public Photo(long id, String name, String url, Photographer photographer, LocalDate date, String location) {

		super();
		this.id.set(id);
		this.name = new SimpleStringProperty(name);
		this.url = new SimpleStringProperty(url);
		this.date = new SimpleObjectProperty<LocalDate>(date);
		this.photographer = new SimpleObjectProperty<Photographer>(photographer);
		this.location = new SimpleStringProperty(location);
	}

	// copy constructor
	public Photo(Photo photo) {
		this.id.set(photo.getId());
		this.name = new SimpleStringProperty(photo.getName());
		this.url = new SimpleStringProperty(photo.getUrl());
		this.date = new SimpleObjectProperty<LocalDate>(photo.getDate());
		this.photographer = new SimpleObjectProperty<Photographer>(photo.getPhotographer());
		this.location = new SimpleStringProperty(photo.getLocation());
	}

	public final LongProperty idProperty() {
		return this.id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL requires identity, using internal AUTO_INCREMENT
	public long getId() {
		return this.idProperty().get();
	}

	public void setId(final long id) {
		this.idProperty().set(id);
	}

	public final StringProperty nameProperty() {
		return this.name;
	}

	public String getName() {
		return this.nameProperty().get();
	}

	public void setName(final String name) {
		this.nameProperty().set(name);
	}

	public final StringProperty locationProperty() {
		return this.location;
	}

	public String getLocation() {
		return this.locationProperty().get();
	}

	public void setLocation(final String location) {
		this.locationProperty().set(location);
	}

	public final StringProperty urlProperty() {
		return this.url;
	}

	public String getUrl() {
		return this.urlProperty().get();
	}

	public void setUrl(final String url) {
		this.urlProperty().set(url);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((photographer == null) ? 0 : photographer.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Photo p = (Photo) obj;
		return p.getId() == this.getId();
	}

	public final ObjectProperty<LocalDate> dateProperty() {
		return this.date;
	}

	public LocalDate getDate() {
		return this.dateProperty().get();
	}

	public void setDate(final LocalDate date) {
		this.dateProperty().set(date);
	}

	public final ObjectProperty<Photographer> photographerProperty() {
		return this.photographer;
	}

	
	@ManyToOne
	@JoinColumn(name = "photographer_id")
	public Photographer getPhotographer() {
		return this.photographerProperty().get();
	}

	public void setPhotographer(final Photographer newPhotographer) {

		this.photographerProperty().set(newPhotographer);
	}

}
