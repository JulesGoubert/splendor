package main;
import domein.DomeinController;
import gui.HoofdPaneel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StartUp extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			HoofdPaneel root = new HoofdPaneel(primaryStage, new DomeinController());
			primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/gui/images/icon.png")));
			Scene scene = new Scene(root, 620, 450);
			scene.getStylesheets().add("styles.css");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.centerOnScreen();
	        root.setId("stack-pane");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
