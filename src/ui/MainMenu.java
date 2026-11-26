package ui;

import application.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class MainMenu implements SceneAware{
	
	private SceneManager sceneManager;
	
	@FXML
	private void openMap() {
		sceneManager.startNewGame();
	}
	
	@FXML
	private void openSettings() {
		sceneManager.showSettings();
	}
	
	@FXML
	private void quitGame() {
		Platform.exit();
	}

	@Override
	public void setSceneManager(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}
}
