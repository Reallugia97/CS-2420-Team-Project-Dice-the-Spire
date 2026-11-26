package application;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import map.MapGenerator;
import ui.MapView;
import ui.SceneAware;

public class SceneManager {
	private static final double DESIGN_W = 1920;
    private static final double DESIGN_H = 1080;

    private final Stage stage;
    private final Group scaler = new Group();
    private MapGenerator mapGenerator;
    private int currentNode = -1;
    private final Set<Integer> visited = new LinkedHashSet<>();
	
    public SceneManager(Stage stage) {
        this.stage = stage;

        StackPane letterbox = new StackPane(scaler);

        Scene scene = new Scene(letterbox, 1280, 720);

        // Scale the screen to fit the window, keeping the aspect ratio
        scaler.scaleXProperty().bind(Bindings.min(
            scene.widthProperty().divide(DESIGN_W),
            scene.heightProperty().divide(DESIGN_H)));
        scaler.scaleYProperty().bind(scaler.scaleXProperty());

        stage.setScene(scene);
        stage.centerOnScreen();
    }
    
    public void startNewGame() {
    	mapGenerator = new MapGenerator();
    	mapGenerator.generate();
    	currentNode = -1;
    	visited.clear();
    	showMap();
    }
	
	public void showMainMenu() {
		switchTo("MainMenu");
	}
	public void showMap() {
		setRoot(new MapView(mapGenerator, currentNode, visited, this::onNodeSelected, this::showMainMenu));
	}
	public void showSettings() {
		switchTo("Settings");
	}
	
	private void onNodeSelected(int vertex) {
		currentNode = vertex;
		visited.add(vertex);
		System.out.println("Entered node " + vertex + ": " + mapGenerator.getType(vertex));
		showMap();
	}
	
	public void switchTo(String fxmlName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlName + ".fxml"));
			Parent root = loader.load();
			
			Object controller = loader.getController();
			if (controller instanceof SceneAware aware) {
				aware.setSceneManager(this);
			}
			
			setRoot(root);
		} catch(IOException e) {
			throw new RuntimeException("Could not load " + fxmlName + ".fxml", e);
		}
	}
	
    private void setRoot(Parent root) {
        if (root instanceof Region region) {
            region.setMinSize(DESIGN_W, DESIGN_H);
            region.setPrefSize(DESIGN_W, DESIGN_H);
            region.setMaxSize(DESIGN_W, DESIGN_H);
        }
        scaler.getChildren().setAll(root);   // NOT scene.setRoot(...)
        stage.show();
    }
}
