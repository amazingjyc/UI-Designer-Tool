package cmp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @web http://java-buddy.blogspot.com/
 */
public class UIDesignerTool extends Application {

	private static double orgSceneX, orgSceneY;
	private static double orgTranslateX, orgTranslateY;

	private Label contentLabel;
	private Button labelButton;
	private Button textFieldButton;
	private Button buttonButton;
	private Button renderButton;
	private Button clearAllButton;
	private Label resizeLabel;
	private Label widthLabel;
	private Label heightLabel;
	private static TextField resizeWidth;
	private static TextField resizeHeight;
	private Button resizeButton;
	private Button loadJSON;

	private static ArrayList<Button> buttons = new ArrayList<Button>();
	private static ArrayList<Label> labels = new ArrayList<Label>();
	private static ArrayList<TextField> textFields = new ArrayList<TextField>();

	// Pointers..
	private static TextField currTextField;
	private static Button currButton;
	private static Label currLabel;
	// 0 - TextField, 1 - Button, 2 - Label..
	private static int pointer = -1;

	@Override
	public void start(Stage primaryStage) {

		Group root = new Group();

		contentLabel = new Label("Choose Your UI Elements");
		contentLabel.setTranslateX(320);
		contentLabel.setTranslateY(20);
		contentLabel.setFont(new Font("Arial", 30));

		resizeLabel = new Label("Resize UI Element");
		resizeLabel.setTranslateX(30);
		resizeLabel.setTranslateY(50);

		widthLabel = new Label("Width:");
		widthLabel.setTranslateX(30);
		widthLabel.setTranslateY(80);

		heightLabel = new Label("Height:");
		heightLabel.setTranslateX(30);
		heightLabel.setTranslateY(115);

		resizeWidth = new TextField();
		resizeWidth.setTranslateX(80);
		resizeWidth.setTranslateY(80);
		resizeWidth.setPrefHeight(10);
		resizeWidth.setPrefWidth(120);

		resizeHeight = new TextField();
		resizeHeight.setTranslateX(80);
		resizeHeight.setTranslateY(115);
		resizeHeight.setPrefHeight(10);
		resizeHeight.setPrefWidth(120);

		resizeButton = new Button("Resize");
		resizeButton.setTranslateX(80);
		resizeButton.setTranslateY(150);
		resizeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (pointer == 0) {
					currTextField.setPrefHeight(Double.parseDouble(resizeHeight.getText()));
					currTextField.setPrefWidth(Double.parseDouble(resizeWidth.getText()));
					System.out.println("Resized TextField");
				} else if (pointer == 1) {
					currButton.setPrefHeight(Double.parseDouble(resizeHeight.getText()));
					currButton.setPrefWidth(Double.parseDouble(resizeWidth.getText()));
					System.out.println("Resized Button");
				} else if (pointer == 2) {
					currLabel.setPrefHeight(Double.parseDouble(resizeHeight.getText()));
					currLabel.setPrefWidth(Double.parseDouble(resizeWidth.getText()));
					System.out.println("Resized Label");
				}
			}
		});

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
				clearUIScreen(root);
			}
		});

		loadJSON = new Button("Load JSON File");
		loadJSON.setTranslateX(760);
		loadJSON.setTranslateY(80);
		loadJSON.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open JSON File");
				fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
				File chosenJSONFile = fileChooser.showOpenDialog(primaryStage);

				// JSON parser object to parse read file
				JSONParser jsonParser = new JSONParser();

				try (FileReader reader = new FileReader(chosenJSONFile)) {
					// Read JSON file
					Object obj = jsonParser.parse(reader);

					JSONArray uiList = (JSONArray) obj;
					System.out.println(uiList);
					System.out.println();

					JSONObject labelsList = null;
					JSONObject buttonsList = null;
					JSONObject textFieldsList = null;

					for (int i = 0; i < uiList.size(); i++) {
						JSONObject object = (JSONObject) uiList.get(i);
						String check = object + "";
						if (check.contains("Labels")) {
							labelsList = object;
						} else if (check.contains("Buttons")) {
							buttonsList = object;
						} else if (check.contains("Text Fields")) {
							textFieldsList = object;
						}
					}

					if (uiList.size() > 0) {
						loadUI(labelsList, buttonsList, textFieldsList, root);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});

		root.getChildren().addAll(contentLabel, labelButton, textFieldButton, buttonButton, renderButton,
				clearAllButton, resizeLabel, loadJSON, resizeButton, widthLabel, heightLabel, resizeWidth,
				resizeHeight);

		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(root, 1000, 750));

		primaryStage.setTitle("UI Designer Tool");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	static EventHandler<MouseEvent> LabelOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			orgTranslateX = ((Label) (t.getSource())).getTranslateX();
			orgTranslateY = ((Label) (t.getSource())).getTranslateY();
			Label label = (Label) (t.getSource());
			resizeWidth.setText(label.getWidth() + "");
			resizeHeight.setText(label.getHeight() + "");
			currLabel = label;
			pointer = 2;

		}
	};

	static EventHandler<MouseEvent> LabelOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

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

	static EventHandler<MouseEvent> TextFieldOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			orgTranslateX = ((TextField) (t.getSource())).getTranslateX();
			orgTranslateY = ((TextField) (t.getSource())).getTranslateY();
			TextField textField = (TextField) (t.getSource());
			resizeWidth.setText(textField.getWidth() + "");
			resizeHeight.setText(textField.getHeight() + "");
			currTextField = textField;
			pointer = 0;
		}
	};

	static EventHandler<MouseEvent> TextFieldOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

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

	static EventHandler<MouseEvent> ButtonOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {
			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();
			orgTranslateX = ((Button) (t.getSource())).getTranslateX();
			orgTranslateY = ((Button) (t.getSource())).getTranslateY();
			Button button = (Button) (t.getSource());
			resizeWidth.setText(button.getWidth() + "");
			resizeHeight.setText(button.getHeight() + "");
			currButton = button;
			pointer = 1;
		}
	};

	static EventHandler<MouseEvent> ButtonOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

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

	public static void clearUIScreen(Group root) {
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

	public static void loadUI(JSONObject labelsList, JSONObject buttonsList, JSONObject textFieldsList, Group root) {

		// Clear UI Screen first
		clearUIScreen(root);

		if (labelsList != null) {
			JSONArray lList = (JSONArray) labelsList.get("Labels");
			for (int i = 0; i < lList.size(); i++) {
				Label newLabel = new Label();
				JSONObject obj = (JSONObject) lList.get(i);
				newLabel.setTranslateX((double) obj.get("PositionX"));
				newLabel.setTranslateY((double) obj.get("PositionY"));
				newLabel.setText((String) obj.get("Text"));
				newLabel.setPrefHeight((double) obj.get("Height"));
				newLabel.setPrefWidth((double) obj.get("Width"));
				newLabel.setCursor(Cursor.HAND);
				newLabel.setOnMousePressed(LabelOnMousePressedEventHandler);
				newLabel.setOnMouseDragged(LabelOnMouseDraggedEventHandler);
				labels.add(newLabel);
				root.getChildren().add(newLabel);
			}
		}

		if (buttonsList != null) {

			JSONArray bList = (JSONArray) buttonsList.get("Buttons");
			for (int i = 0; i < bList.size(); i++) {
				Button newButton = new Button();
				JSONObject obj = (JSONObject) bList.get(i);
				newButton.setTranslateX((double) obj.get("PositionX"));
				newButton.setTranslateY((double) obj.get("PositionY"));
				newButton.setText((String) obj.get("Text"));
				newButton.setPrefHeight((double) obj.get("Height"));
				newButton.setPrefWidth((double) obj.get("Width"));
				newButton.setCursor(Cursor.HAND);
				newButton.setOnMousePressed(ButtonOnMousePressedEventHandler);
				newButton.setOnMouseDragged(ButtonOnMouseDraggedEventHandler);
				buttons.add(newButton);
				root.getChildren().add(newButton);
			}
		}

		if (textFieldsList != null) {
			JSONArray tList = (JSONArray) textFieldsList.get("Text Fields");
			for (int i = 0; i < tList.size(); i++) {
				TextField newTextField = new TextField();
				JSONObject obj = (JSONObject) tList.get(i);
				newTextField.setTranslateX((double) obj.get("PositionX"));
				newTextField.setTranslateY((double) obj.get("PositionY"));
				newTextField.setText((String) obj.get("Text"));
				newTextField.setPrefHeight((double) obj.get("Height"));
				newTextField.setPrefWidth((double) obj.get("Width"));
				newTextField.setCursor(Cursor.HAND);
				newTextField.setOnMousePressed(TextFieldOnMousePressedEventHandler);
				newTextField.setOnMouseDragged(TextFieldOnMouseDraggedEventHandler);
				textFields.add(newTextField);
				root.getChildren().add(newTextField);
			}
		}
	}
}