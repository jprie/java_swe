/**
 * Sample Skeleton for 'PhotoLibrary.fxml' Controller Class
 */

package controller;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import exception.ServiceException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import model.Photo;
import model.Photographer;
import model.PhotographerStringConverter;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PhotoLibraryController extends BaseController {


	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="photoTable"
	private TableView<Photo> photoTable; // Value injected by FXMLLoader

	@FXML // fx:id="nameColumn"
	private TableColumn<Photo, String> nameTableColumn; // Value injected by FXMLLoader

	@FXML // fx:id="nameText"
	private TextField nameText; // Value injected by FXMLLoader

	@FXML // fx:id="urlText"
	private TextField urlText; // Value injected by FXMLLoader

	@FXML // fx:id="selectButton"
	private Button selectButton; // Value injected by FXMLLoader

	@FXML // fx:id="photographerComboBox"
	private ComboBox<Photographer> photographerComboBox; // Value injected by FXMLLoader

	@FXML // fx:id="locationText"
	private TextField locationText; // Value injected by FXMLLoader

	@FXML // fx:id="clearButton"
	private Button clearButton; // Value injected by FXMLLoader

	@FXML // fx:id="deleteButton"
	private Button deleteButton; // Value injected by FXMLLoader

	@FXML // fx:id="updateButton"
	private Button updateButton; // Value injected by FXMLLoader

	@FXML // fx:id="newButton"
	private Button newButton; // Value injected by FXMLLoader

	@FXML
	ImageView photoImageView;

	@FXML
	TableColumn<Photo, String> locationTableColumn;

	@FXML
	TextField dateTextField;

	@FXML
	TableColumn<Photo, LocalDate> dateTableColumn;

	@FXML
	void handleAddPhotoAction(ActionEvent event) throws ServiceException {

		Photo photo = new Photo(0l, nameText.getText(), urlText.getText(),
				photographerComboBox.getSelectionModel().getSelectedItem(),
				LocalDate.parse(dateTextField.getText(), formatter), locationText.getText());
		photoList.add(photo);
		
		try {
			photoService.add(photo);
		} catch (ServiceException e) {
			System.out.println("Photo could not be persistet!");
			photoList.remove(photo);
		}
		

		clearForm();
	}

	@FXML
	void handleClearPhotoAction(ActionEvent event) {

		clearForm();
	}

	private void clearForm() {

		nameText.clear();
		urlText.clear();
		photographerComboBox.getSelectionModel().clearSelection();
		locationText.setText("");
		dateTextField.clear();
		photoImageView.setImage(null);
		selectedPhoto.set(null);
	}

	@FXML
	public void handleDeletePhotoAction(ActionEvent event) throws ServiceException {
		Photo selectedPhoto = photoTable.getSelectionModel().getSelectedItem();
		photoList.remove(selectedPhoto);
		photoService.delete(selectedPhoto);
		clearForm();
	}

	@FXML
	void handleUpdatePhotoAction(ActionEvent event) throws ServiceException {
		Photo oldPhoto = photoTable.getSelectionModel().getSelectedItem();
		// create a copy with identical id
		Photo newPhoto = new Photo(oldPhoto);

		// update values
		newPhoto.setLocation(locationText.getText());
		newPhoto.setName(nameText.getText());
		newPhoto.setDate(LocalDate.parse(dateTextField.getText(), formatter));
		newPhoto.setUrl(urlText.getText());
		newPhoto.setPhotographer(photographerComboBox.getSelectionModel().getSelectedItem());

		Photo updatedPhoto = photoService.update(newPhoto);
		
		// remove and add is simpler
		photoList.remove(oldPhoto);
		photoList.add(updatedPhoto);

		photoTable.refresh();
		photoTable.getSelectionModel().clearSelection();

		clearForm();
	}

	@FXML

	void handleSelectFileAction(ActionEvent event) throws URISyntaxException {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setInitialDirectory(new File(getClass().getResource("/"
				+ "images").toURI()));

		File file = fileChooser.showOpenDialog(selectButton.getScene().getWindow());
		if (file != null) {

			Path p = Paths.get(file.getPath());

			// create path relative from resources, prepending '/' necessary for
			// getResource(...)
			urlText.setText("/images/" + p.getFileName().toString());
			Image image = new Image(getClass().getResourceAsStream(urlText.getText()));
			photoImageView.setImage(image);

		}
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert photoTable != null : "fx:id=\"photoTable\" was not injected: check your FXML file 'PhotoLibrary.fxml'.";
		assert nameTableColumn != null : "fx:id=\"nameColumn\" was not injected: check your FXML file 'PhotoLibrary.fxml'.";
		assert nameText != null : "fx:id=\"nameText\" was not injected: check your FXML file 'PhotoLibrary.fxml'.";
		assert urlText != null : "fx:id=\"urlText\" was not injected: check your FXML file 'PhotoLibrary.fxml'.";
		assert selectButton != null : "fx:id=\"selectButton\" was not injected: check your FXML file 'PhotoLibrary.fxml'.";
		assert photographerComboBox != null : "fx:id=\"photographerComboBox\" was not injected: check your FXML file 'PhotoLibrary.fxml'.";
		assert locationText != null : "fx:id=\"locationText\" was not injected: check your FXML file 'PhotoLibrary.fxml'.";
		assert clearButton != null : "fx:id=\"clearButton\" was not injected: check your FXML file 'PhotoLibrary.fxml'.";
		assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'PhotoLibrary.fxml'.";
		assert updateButton != null : "fx:id=\"updateButton\" was not injected: check your FXML file 'PhotoLibrary.fxml'.";
		assert newButton != null : "fx:id=\"newButton\" was not injected: check your FXML file 'PhotoLibrary.fxml'.";

		
		// init photos from DB
		try {
			photoList.addAll(photoService.getAll());
		} catch (ServiceException e) {

			System.out.println("Could not load photos from DB");
			e.printStackTrace();
		}
		
		// init photographers from DB
		try {
			photographerList.addAll(photographerService.getAll());
		} catch (ServiceException e) {
			
			System.out.println("Could not load photographers from DB");
			e.printStackTrace();
		}

		// configure formatter for dateTextField
		TextFormatter<LocalDate> dateFormatter = new TextFormatter<>(new StringConverter<LocalDate>() {

			@Override
			public LocalDate fromString(String str) {

				return LocalDate.parse(str, formatter);
			}

			@Override
			public String toString(LocalDate date) {

				if (date != null) {
					return date.format(formatter);
				} else
					return null;
			}
		});

		dateTextField.setTextFormatter(dateFormatter);

		// configure photographer choice box
		
		photographerComboBox.setItems(photographerList);
		photographerComboBox.setConverter(new PhotographerStringConverter());

		// configure table
		photoTable.setItems(photoList);
		photoTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// configure cells
		nameTableColumn.setCellValueFactory(new PropertyValueFactory<Photo, String>(Photo.NAME_NAME));
		dateTableColumn.setCellValueFactory(new PropertyValueFactory<>(Photo.DATE_NAME));
		locationTableColumn.setCellValueFactory(new PropertyValueFactory<Photo, String>(Photo.LOCATION_NAME));

		dateTableColumn.setCellFactory(column -> {
			return new TableCell<Photo, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						// format date as dd.MM.yyyy
						setText(formatter.format(item));
					}
				}

			};
		});

		photoTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Photo>() {

			@Override
			public void changed(ObservableValue<? extends Photo> observable, Photo oldValue, Photo newValue) {
				selectedPhoto.set(newValue);
			}
		});

		selectedPhoto.addListener(new ChangeListener<Photo>() {

			@Override
			public void changed(ObservableValue<? extends Photo> arg0, Photo arg1, Photo newPhoto) {

				// show url in webview
				if (newPhoto != null) {
					Image image = new Image(getClass().getResourceAsStream(newPhoto.getUrl()));
					photoImageView.setImage(image);

					// set values for photo
					nameText.setText(newPhoto.getName());
					locationText.setText(newPhoto.getLocation());
					dateTextField.setText(newPhoto.getDate().format(formatter));
					photographerComboBox.getSelectionModel()
							.select(newPhoto.getPhotographer());
					urlText.setText(newPhoto.getUrl());

				}

			}
		});
		
		// keep photoList and photographerList synchronized when photographer of photo was changed
		photoList.addListener(new ListChangeListener<Photo>(){

			@Override
			public void onChanged(Change<? extends Photo> c) {
				
				while (c.next()) {
					if (c.wasAdded()) {
						c.getAddedSubList();
						for (Photo p : c.getAddedSubList()) {
							for (Photographer ph : photographerList) {
								if (ph.equals(p.getPhotographer())) {
									ph.getPhotos().add(p);
								}
							}
						}
					}
					if (c.wasRemoved()) {
						for (Photo p : c.getRemoved()) {
							for (Photographer ph : photographerList) {
								if (ph.getPhotos().contains(p)) {
									ph.getPhotos().remove(p);
								}
							}
						}
					}
				}
				
			}
			
		});

		// logic when buttons are to be enabled
		newButton.disableProperty()
				.bind(nameText.textProperty().isEmpty().or(urlText.textProperty().isEmpty())
						.or(photographerComboBox.getSelectionModel().selectedItemProperty().isNull())
						.or(locationText.textProperty().isEmpty()).or(selectedPhoto.isNotNull()));

		updateButton.disableProperty()
				.bind(nameText.textProperty().isEmpty().or(urlText.textProperty().isEmpty())
						.or(photographerComboBox.getSelectionModel().selectedItemProperty().isNull())
						.or(locationText.textProperty().isEmpty()).or(selectedPhoto.isNull()));

		deleteButton.disableProperty().bind(selectedPhoto.isNull());

	}

}
