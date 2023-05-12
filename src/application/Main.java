package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			Image icon = new Image("./resources/rabbit.jpg");
			stage.getIcons().add(icon);
			stage.setResizable(false);

			stage.setTitle("Cennik");
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		launch(args);
	}

	//https://www.youtube.com/watch?v=hwCbXOM4_Qc&list=PLZPZq0r_RZOM-8vJA3NQFZB7JroDcMwev&index=17 tutorial
}
