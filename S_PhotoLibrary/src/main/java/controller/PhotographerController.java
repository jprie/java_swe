package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ListIterator;
import java.util.ResourceBundle;

import application.Constants;
import exception.ServiceException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Photo;
import model.Photographer;
import javafx.scene.control.ListView;

public class PhotographerController extends BaseController {

	private BooleanProperty selectedPhotographerHasNoPhotos = new SimpleBooleanProperty(true);
	
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField firstNameTextField;

	@FXML
	private TextField lastNameTextField;

	@FXML
	private Button addButton;

	@FXML
	private Button updateButton;

	@FXML
	private Button deleteButton;

	@FXML
	private Button clearButton;

	@FXML
	private ListView<Photographer> listView;

	@FXML Button slideShowButton;

	@FXML
	void handleAddAction(ActionEvent event) {

		String firstName = firstNameTextField.getText();
		String lastName = lastNameTextField.getText();

		if (!firstName.equals("") && !lastName.equals("")) {

			Photographer photographer = new Photographer(0, firstName, lastName);

			photographerList.add(photographer);

			try {
				photographerService.add(photographer);
			} catch (Exception e) {
				e.printStackTrace();
			}

			clearForm();
		}
	}

	@FXML
	void handleClearAction(ActionEvent event) {

		clearForm();
		selectedPhotographer.set(null);
	}

	private void clearForm() {

		firstNameTextField.clear();
		lastNameTextField.clear();

	}

	@FXML
	void handleDeleteAction(ActionEvent event) {

		Photographer photographer = selectedPhotographer.get();
		photographerList.remove(photographer);
		
		try {
			photographerService.delete(photographer);
			
		} catch (ServiceException e) {
			// could not be deleted from DB
			System.out.println("Could not be deleted: Constraint violation");
			photographerList.add(photographer);
		}
		
		
		listView.getSelectionModel().clearSelection();
		
	}

	@FXML
	void handleUpdateAction(ActionEvent event) {

		Photographer photographer = selectedPhotographer.get();
		
		photographerList.remove(photographer);
		
		photographer.setFirstName(firstNameTextField.getText());
		photographer.setLastName(lastNameTextField.getText());
		
		try {
			photographer = photographerService.update(photographer);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		photographerList.add(photographer);
		
		
	}

	@FXML
	void initialize() {
		assert firstNameTextField != null : "fx:id=\"firstNameTextField\" was not injected: check your FXML file 'PhotographerView.fxml'.";
		assert lastNameTextField != null : "fx:id=\"lastNameTextField\" was not injected: check your FXML file 'PhotographerView.fxml'.";
		assert addButton != null : "fx:id=\"addButton\" was not injected: check your FXML file 'PhotographerView.fxml'.";
		assert updateButton != null : "fx:id=\"updateButton\" was not injected: check your FXML file 'PhotographerView.fxml'.";
		assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'PhotographerView.fxml'.";
		assert clearButton != null : "fx:id=\"clearButton\" was not injected: check your FXML file 'PhotographerView.fxml'.";

		listView.setItems(photographerList);

		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Photographer>() {

			@Override
			public void changed(ObservableValue<? extends Photographer> observable, Photographer oldValue,
					Photographer newValue) {

				selectedPhotographer.set(newValue);
				if (newValue != null) {
					selectedPhotographerHasNoPhotos.set(newValue.getPhotos().isEmpty());
				}

			}
		});

		selectedPhotographer.addListener(new ChangeListener<Photographer>() {

			@Override
			public void changed(ObservableValue<? extends Photographer> observable, Photographer oldValue,
					Photographer newValue) {
				if (newValue != null) {
					firstNameTextField.setText(newValue.getFirstName());
					lastNameTextField.setText(newValue.getLastName());
				}

			}
		});

		// disable when photographer selected OR one of the text fields is empty
		addButton.disableProperty().bind(selectedPhotographer.isNotNull()
				.or(firstNameTextField.textProperty().isEmpty().or(lastNameTextField.textProperty().isEmpty())));
		updateButton.disableProperty().bind(selectedPhotographer.isNull()
				.or(firstNameTextField.textProperty().isEmpty().or(lastNameTextField.textProperty().isEmpty())));
		deleteButton.disableProperty().bind(selectedPhotographer.isNull());
		slideShowButton.disableProperty().bind(selectedPhotographer.isNull().or(selectedPhotographerHasNoPhotos));
		
		// keep photographersList and photoList synchronized
		// remove photos when photographer was deleted
		photographerList.addListener(new ListChangeListener<Photographer>() {

			@Override
			public void onChanged(Change<? extends Photographer> c) {
				
				while (c.next()) {
					
					if (c.wasRemoved()) {
						for (Photographer ph : c.getRemoved()) {
						
							ListIterator<Photo> iter = photoList.listIterator();
							
							while (iter.hasNext()) {
								
								Photo p = iter.next();
								if (p.getPhotographer().equals(ph)) {
									iter.remove();
								}
								
							}
						}
					}
				}
				
			}
			
		});
		
	}

	@FXML
	public void handleStartSlideShowAction(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.PATH_TO_SLIDESHOW_VIEW_FXML));
		Parent root = loader.load();
		
		Stage slideshowStage = new Stage();
		slideshowStage.initModality(Modality.APPLICATION_MODAL);
		
		Scene scene = new Scene(root);
		
		slideshowStage.setScene(scene);
		slideshowStage.showAndWait();
	}
}
