package cmp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * @web http://java-buddy.blogspot.com/
 */
public class UIDesignerTool extends Application {

	private double orgSceneX, orgSceneY;
	private double orgTranslateX, orgTranslateY;

	private Label contentLabel;
	private Button labelButton;
	private Button textFieldButton;
	private Button buttonButton;
	private Button renderButton;
	private Button clearAllButton;
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private ArrayList<Label> labels = new ArrayList<Label>();
	private ArrayList<TextField> textFields = new ArrayList<TextField>();

	@Override
	public void start(Stage primaryStage) {

		Group root = new Group();

		contentLabel = new Label("Choose Your UI Elements");
		contentLabel.setTranslateX(320);
		contentLabel.setTranslateY(20);
		contentLabel.setFont(new Font("Arial", 30));

		labelButton = new Button("Add Label");
		labelButton.setTranslateX(240);
		labelButton.setTranslateY(80);
		labelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Label label = new Label("New Label");
				label.setCursor(Cursor.HAND);
				label.setOnMousePressed(LabelOnMousePressedEventHandler);
				label.setOnMouseDragged(LabelOnMouseDraggedEventHandler);
				labels.add(label);
				root.getChildren().add(label);
			}
		});

		textFieldButton = new Button("Add TextField");
		textFieldButton.setTranslateX(340);
		textFieldButton.setTranslateY(80);
		textFieldButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TextField textField = new TextField();
				textField.setCursor(Cursor.HAND);
				textField.setOnMousePressed(TextFieldOnMousePressedEventHandler);
				textField.setOnMouseDragged(TextFieldOnMouseDraggedEventHandler);
				textFields.add(textField);
				root.getChildren().add(textField);
			}
		});

		buttonButton = new Button("Add Button");
		buttonButton.setTranslateX(465);
		buttonButton.setTranslateY(80);
		buttonButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button button = new Button("New Button");
				button.setCursor(Cursor.HAND);
				button.setOnMousePressed(ButtonOnMousePressedEventHandler);
				button.setOnMouseDragged(ButtonOnMouseDraggedEventHandler);
				buttons.add(button);
				root.getChildren().add(button);
			}
		});

		renderButton = new Button("Render UI");
		renderButton.setTranslateX(575);
		renderButton.setTranslateY(80);
		renderButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				writeToJSON();
			}
		});

		clearAllButton = new Button("Clear UI");
		clearAllButton.setTranslateX(675);
		clearAllButton.setTranslateY(80);
		clearAllButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (int i = 0; i < buttons.size(); i++) {
					root.getChildren().remove(buttons.get(i));
				}
				for (int i = 0; i < labels.size(); i++) {
					root.getChildren().remove(labels.get(i));
				}
				for (int i = 0; i < textFields.size(); i++) {
					root.getChildren().remove(textFields.get(i));
				}
				buttons.clear();
				textFields.clear();
				labels.clear();
			}
		});

		root.getChildren().addAll(contentLabel, labelButton, textFieldButton, buttonButton, renderButton,
				clearAllButton);

		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(root, 1000, 750));

		primaryStage.setTitle("UI Designer Tool");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	EventHandler<MouseEvent> LabelOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			orgTranslateX = ((Label) (t.getSource())).getTranslateX();
			orgTranslateY = ((Label) (t.getSource())).getTranslateY();
		}
	};

	EventHandler<MouseEvent> LabelOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			double offsetX = t.getSceneX() - orgSceneX;
			double offsetY = t.getSceneY() - orgSceneY;
			double newTranslateX = orgTranslateX + offsetX;
			double newTranslateY = orgTranslateY + offsetY;

			((Label) (t.getSource())).setTranslateX(newTranslateX);
			((Label) (t.getSource())).setTranslateY(newTranslateY);
		}
	};

	EventHandler<MouseEvent> TextFieldOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			orgTranslateX = ((TextField) (t.getSource())).getTranslateX();
			orgTranslateY = ((TextField) (t.getSource())).getTranslateY();
		}
	};

	EventHandler<MouseEvent> TextFieldOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			double offsetX = t.getSceneX() - orgSceneX;
			double offsetY = t.getSceneY() - orgSceneY;
			double newTranslateX = orgTranslateX + offsetX;
			double newTranslateY = orgTranslateY + offsetY;

			((TextField) (t.getSource())).setTranslateX(newTranslateX);
			((TextField) (t.getSource())).setTranslateY(newTranslateY);
		}
	};

	EventHandler<MouseEvent> ButtonOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			orgTranslateX = ((Button) (t.getSource())).getTranslateX();
			orgTranslateY = ((Button) (t.getSource())).getTranslateY();
		}
	};

	EventHandler<MouseEvent> ButtonOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			double offsetX = t.getSceneX() - orgSceneX;
			double offsetY = t.getSceneY() - orgSceneY;
			double newTranslateX = orgTranslateX + offsetX;
			double newTranslateY = orgTranslateY + offsetY;

			((Button) (t.getSource())).setTranslateX(newTranslateX);
			((Button) (t.getSource())).setTranslateY(newTranslateY);
		}
	};

	@SuppressWarnings("unchecked")
	public void writeToJSON() {

		JSONArray uiList = new JSONArray();
		JSONArray labelsList = new JSONArray();
		JSONArray buttonsList = new JSONArray();
		JSONArray textFieldsList = new JSONArray();

		for (int i = 0; i < labels.size(); i++) {
			JSONObject labelDetails = new JSONObject();
			labelDetails.put("PositionX", labels.get(i).getTranslateX());
			labelDetails.put("PositionY", labels.get(i).getTranslateY());
			labelDetails.put("Text", labels.get(i).getText());
			labelDetails.put("Width", labels.get(i).getWidth());
			labelDetails.put("Height", labels.get(i).getHeight());
			labelsList.add(labelDetails);
		}

		for (int i = 0; i < buttons.size(); i++) {
			JSONObject buttonDetails = new JSONObject();
			buttonDetails.put("PositionX", buttons.get(i).getTranslateX());
			buttonDetails.put("PositionY", buttons.get(i).getTranslateY());
			buttonDetails.put("Text", buttons.get(i).getText());
			buttonDetails.put("Width", buttons.get(i).getWidth());
			buttonDetails.put("Height", buttons.get(i).getHeight());
			buttonsList.add(buttonDetails);
		}

		for (int i = 0; i < textFields.size(); i++) {
			JSONObject textFieldDetails = new JSONObject();
			textFieldDetails.put("PositionX", textFields.get(i).getTranslateX());
			textFieldDetails.put("PositionY", textFields.get(i).getTranslateY());
			textFieldDetails.put("Text", textFields.get(i).getText());
			textFieldDetails.put("Width", textFields.get(i).getWidth());
			textFieldDetails.put("Height", textFields.get(i).getHeight());
			textFieldsList.add(textFieldDetails);
		}

		if (labels.size() > 0) {
			JSONObject labelsUI = new JSONObject();
			labelsUI.put("Labels", labelsList);
			uiList.add(labelsUI);
		}
		if (buttons.size() > 0) {
			JSONObject buttonsUI = new JSONObject();
			buttonsUI.put("Buttons", buttonsList);
			uiList.add(buttonsUI);
		}
		if (textFields.size() > 0) {
			JSONObject textFieldsUI = new JSONObject();
			textFieldsUI.put("Text Fields", textFieldsList);
			uiList.add(textFieldsUI);
		}

		JSONObject uiJson = new JSONObject();
		uiJson.put("UI Elements", uiList);

		// Write JSON file
		try (FileWriter file = new FileWriter(
				"C:\\Users\\jarre\\eclipse-workspace\\UI Designer Tool\\output\\UI.json")) {

			file.write(uiList.toJSONString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}