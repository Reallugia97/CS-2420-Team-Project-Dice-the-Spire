package application;
	
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Dice the Spire");
		new SceneManager(primaryStage).showMainMenu();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
