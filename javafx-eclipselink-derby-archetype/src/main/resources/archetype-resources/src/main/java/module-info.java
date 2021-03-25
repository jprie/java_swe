module ${artifactId} {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires jakarta.persistence;
	requires javafx.media;
	
	//opens ${package}.model;
	opens ${package}.controller to javafx.fxml;
	opens ${package}.application to javafx.graphics, javafx.fxml;
}
