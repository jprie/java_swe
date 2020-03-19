package controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Photo;
import model.Photographer;
import service.PhotoService;
import service.PhotographerService;

public class BaseController {

	static PhotoService photoService = new PhotoService();

	static PhotographerService photographerService = new PhotographerService();

	static ObservableList<Photo> photoList = FXCollections.observableArrayList();

	static ObservableList<Photographer> photographerList = FXCollections.observableArrayList();

	static ObjectProperty<Photo> selectedPhoto = new SimpleObjectProperty<Photo>();
	
	static ObjectProperty<Photographer> selectedPhotographer = new SimpleObjectProperty<Photographer>();
	
	
}
