package pobj.pinboard.editor;

import java.io.File;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import pobj.pinboard.document.Board;
import pobj.pinboard.editor.tools.Tool;
import pobj.pinboard.editor.tools.ToolEllipse;
import pobj.pinboard.editor.tools.ToolImage;
import pobj.pinboard.editor.tools.ToolRect;
import pobj.pinboard.editor.tools.ToolSelection;

public class EditorWindow implements EditorInterface {

	private Stage stage;
	private Board board;
	private Tool courant;
	private MenuBar menubar;
	private ToolBar toolbar;
	private Label barre;
	private Canvas canvas;
	private Selection selection;
	
	public EditorWindow(Stage stage) {
		this.stage = stage;
		board = new Board();

		stage.setTitle("PinBoard");

		VBox vbox = new VBox();

		configurerMenuBar();
		configurerToolBar();

		//je crée le séparateur
		Separator separator = new Separator();
		//on peut choisir la couleur par défaut ce sera noir
		ColorPicker color = new ColorPicker(Color.BLACK);
		color.setOnAction( (e) -> {
			Color choix = color.getValue();
			board.setColor(choix);
		});
		//je crée la barre de label
		barre = new Label("pas encore rempli");
		
		configurerCanvas();
		
		//j'ajoute tous les éléments dans la vbox
		vbox.getChildren().addAll(menubar, toolbar, canvas, separator, barre);

		//je crée la scène à partir de la VBox et j'affiche 
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.show();
	}

	public void configurerMenuBar() {
		//je crée la barre de menus et je la remplis
		menubar = new MenuBar();
		Menu file = new Menu("File");
		Menu edit = new Menu("Edit");
		Menu tools = new Menu("Tools");
		menubar.getMenus().addAll(file, edit, tools);

		//je crée le menu déroulant New
		MenuItem newF = new MenuItem("New");
		newF.setOnAction( (e) -> { 
			new EditorWindow(new Stage()); 
		});
		MenuItem close = new MenuItem("Close");
		close.setOnAction( (e) -> { 
			stage.close(); 
		});
		
		//je crée le menu déroulant Tools
		MenuItem rectangle = new MenuItem("Rectangle");
		rectangle.setOnAction( (e) -> {
			courant = new ToolRect();
			barre.setText(courant.getName(this));
		});
		MenuItem ellipse = new MenuItem("Ellipse");
		ellipse.setOnAction( (e) -> {
			courant = new ToolEllipse();
			barre.setText(courant.getName(this));
		});
		
		file.getItems().addAll(newF, close);
		tools.getItems().addAll(rectangle, ellipse);
	}

	public void configurerToolBar() {
		//je crée la barre de boutons et je la remplis 
		toolbar = new ToolBar();
		Button box = new Button("Box");
		Button ellipse = new Button("Ellipse");
		Button img = new Button("Img...");
		Button reset = new Button("Reset");
		Button select = new Button("Select");
		toolbar.getItems().addAll(box, ellipse, img, select, reset);
		//j'associe un bouton à un événement
		box.setOnAction( (e) -> {
			courant = new ToolRect();
			barre.setText(courant.getName(this));
		});

		ellipse.setOnAction( (e) -> {
			courant = new ToolEllipse();
			barre.setText(courant.getName(this));
		});
		reset.setOnAction( (e) -> {
			canvas.getGraphicsContext2D().clearRect(0, 0, 800, 600);
		});
		img.setOnAction( (e) -> {
			FileChooser file = new FileChooser();
			file.setTitle("Select source file");
			ExtensionFilter imgFilter = new ExtensionFilter("Image files", "*.jpg", "*.png", "*.gif", "*.svg");
			file.getExtensionFilters().add(imgFilter);
			File file2 = file.showOpenDialog(stage);
			System.out.println(file);
			courant = new ToolImage(file2);
			barre.setText(courant.getName(this));
		});
		select.setOnAction( (e) -> {
			courant = new ToolSelection();
			barre.setText(courant.getName(this));
		});

	}
	
	public void configurerCanvas () {
		//je crée la zone de dessin
		canvas = new Canvas(800, 600);
		canvas.setOnMousePressed( (e) -> {
			courant.press(this, e);
			courant.drawFeedback(this, canvas.getGraphicsContext2D());
		});
		canvas.setOnMouseDragged( (e) -> {
			courant.drag(this, e);
			courant.drawFeedback(this, canvas.getGraphicsContext2D());
		});
		canvas.setOnMouseReleased( (e) -> {
			courant.release(this, e);
			courant.drawFeedback(this, canvas.getGraphicsContext2D());
		});
	}

	public Board getBoard() {
		return this.board;
	}

	public Selection getSelection() {
		return this.selection;
	}

	public CommandStack getUndoStack() {
		return this.getUndoStack();
	}

}
