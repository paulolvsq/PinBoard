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

public class EditorWindow implements EditorInterface, ClipboardListener {

	private Stage stage;
	private Board board;
	private Tool courant;
	private MenuBar menubar;
	private ToolBar toolbar;
	private Label barre;
	private Canvas canvas;
	private Selection selection;
	private Menu file = new Menu("File");
	private Menu edit = new Menu("Edit");
	private Menu tools = new Menu("Tools");
	private Separator separator = new Separator();
	private MenuItem newF = new MenuItem("New");
	private MenuItem close = new MenuItem("Close");
	private MenuItem rectangle = new MenuItem("Rectangle");
	private MenuItem copy = new MenuItem("Copy");
	private MenuItem ellipse = new MenuItem("Ellipse");
	private MenuItem paste = new MenuItem("Paste");
	private MenuItem delete = new MenuItem("Delete");

	public EditorWindow(Stage stage) {
		this.stage = stage;
		board = new Board();

		stage.setTitle("PinBoard");

		VBox vbox = new VBox();

		configurerMenuBar();
		configurerToolBar();

		//je crée la barre de label
		barre = new Label("pas encore rempli");

		configurerCanvas();

		//j'ajoute tous les éléments dans la vbox
		vbox.getChildren().addAll(menubar, toolbar, canvas, separator, barre);

		//je crée la scène à partir de la VBox et j'affiche 
		Scene scene = new Scene(vbox);
		stage.setScene(scene);
		stage.show();
		Clipboard.getInstance().addListener(this);
		Clipboard.getInstance().clipboardChanged();
	}

	public void configurerMenuBar() {
		//je crée la barre de menus et je la remplis
		menubar = new MenuBar();

		menubar.getMenus().addAll(file, edit, tools);

		//je crée le menu déroulant New

		newF.setOnAction( (e) -> { 
			new EditorWindow(new Stage()); 
		});

		close.setOnAction( (e) -> { 
			Clipboard.getInstance().removeListener(this);
			stage.close(); 
		});

		//je crée le menu déroulant Tools

		rectangle.setOnAction( (e) -> {
			courant = new ToolRect();
			barre.setText(courant.getName(this));
		});

		ellipse.setOnAction( (e) -> {
			courant = new ToolEllipse();
			barre.setText(courant.getName(this));
		});

		//je crée le menu déroulant Edit

		copy.setOnAction( (e) -> {
			Clipboard.getInstance().copyToClipboard(selection.getContents());
			Clipboard.getInstance().clipboardChanged();
		});

		paste.setOnAction( (e) -> {
			board.addClip(Clipboard.getInstance().copyFromClipboard());
			board.draw(canvas.getGraphicsContext2D());
		});

		delete.setOnAction( (e) -> {
			board.removeClip(selection.getContents());
			board.draw(canvas.getGraphicsContext2D());
		});

		edit.getItems().addAll(copy, paste, delete);
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
		//on peut choisir la couleur par défaut ce sera noir
		ColorPicker color = new ColorPicker(Color.BLACK);
		color.setOnAction( (e) -> {
			Color choix = color.getValue();
			board.setColor(choix);
		});
		toolbar.getItems().addAll(box, ellipse, img, select, color, reset);
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
			canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		});
		img.setOnAction( (e) -> {
			FileChooser file = new FileChooser();
			file.setTitle("Select source file");
			ExtensionFilter imgFilter = new ExtensionFilter("Image files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.svg");
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

	@Override
	public void clipboardChanged() {
		if(Clipboard.getInstance().isEmpty()) paste.setDisable(true);
		else paste.setDisable(false);
	}

}
