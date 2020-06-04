package net.snortum.javafx.background;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrimesInBackground extends Application {
	
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox vBox = new VBox();
		vBox.setPadding(new Insets(5));
		vBox.setSpacing(5.0);

		TextArea textArea = new TextArea();
		vBox.getChildren().add(textArea);

		HBox hBox = new HBox();
		hBox.setSpacing(5.0);
		Label noOfPrimesLabel = new Label("Enter number of primes");
		hBox.getChildren().add(noOfPrimesLabel);
		TextField noOfPrimesText = new TextField();
		hBox.getChildren().add(noOfPrimesText);
		vBox.getChildren().add(hBox);
		
		ProgressBar progress = new ProgressBar(0.0);
		vBox.getChildren().add(progress);

		HBox buttonBox = new HBox();
		buttonBox.setSpacing(5.0);
		Button startButton = new Button("Start");
		Button cancelButton = new Button("Cancel");
		startButton.setOnAction(event -> {
			int noOfPrimes;
			
			try {
				noOfPrimes = Integer.parseInt(noOfPrimesText.getText());
			} catch (NumberFormatException e) {
				return;
			}
			
			PrimeNumbersTask task = new PrimeNumbersTask(noOfPrimes);
			task.messageProperty().addListener((observable, oldValue, newValue) -> {
				// newValue is the prime number set in the updateMessage() method of Task
				textArea.appendText(newValue + System.lineSeparator());
			});
			Thread thread = new Thread(task);
			thread.setDaemon(true);
			thread.start();
			
			// The following line will work if you only need to update text, not append it
			//textArea.textProperty().bind(task.messageProperty());
			
			// progressProperty() is updated by updatedProgress() in the task
			progress.progressProperty().bind(task.progressProperty());
			
			cancelButton.setOnAction(cancelEvent -> {
				task.setStopTask(true);
			});
		});
		buttonBox.getChildren().addAll(startButton, cancelButton);
		vBox.getChildren().add(buttonBox);

		Scene scene = new Scene(vBox);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Generate Primes In Background");
		primaryStage.show();
	}

}
