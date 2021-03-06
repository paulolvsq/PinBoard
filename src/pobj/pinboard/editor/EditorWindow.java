package pobj.pinboard.editor;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.commands.CommandGroup;
import pobj.pinboard.editor.commands.CommandUngroup;
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
	private CommandStack commands = new CommandStack();
	private Selection selection = new Selection();
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
	private MenuItem group = new MenuItem("Group");
	private MenuItem ungroup = new MenuItem("Ungroup");
	private MenuItem redo = new MenuItem("Redo");
	private MenuItem undo = new MenuItem("Undo");
	private MenuItem save = new MenuItem("Save");
	private MenuItem open = new MenuItem("Open");
	private ImageView iv = new ImageView();

	public EditorWindow(Stage stage) {
		this.stage = stage;
		configurerCanvas();
		board = new Board();

		stage.setTitle("PinBoard");

		VBox vbox = new VBox();

		configurerMenuBar();
		configurerToolBar();

		//je crée la barre de label
		barre = new Label("Empty");

		//j'ajoute tous les éléments dans la vbox
		vbox.getChildren().addAll(menubar, toolbar, canvas, separator, barre, iv);

		//je crée la scène à partir de la VBox et j'affiche 
		Scene scene = new Scene(vbox);
		this.stage.setScene(scene);
		this.stage.show();
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
		group.setOnAction( (e) -> {
			selection.clear(); //ôter les éléments sélectionnés
			ClipGroup c = new ClipGroup(); //créer un nouveau groupe
			for(Clip clip : board.getContents()) c.addClip(clip); //ajouter les éléments sélectionnés au groupe
			board.removeClip(board.getContents()); //on supprime les anciens éléments de la planche
			board.addClip(c); //on ajoute le groupe de clip à la planche
			commands.addCommand(new CommandGroup(this, c));
		});
		ungroup.setOnAction( (e) -> {
			selection.clear(); //on supprime les éléments sélectionnés
			List<ClipGroup> groupes = new ArrayList<>(); //liste qui sert à stocker les groupes
			for(Clip clip : board.getContents()) {
				if(clip instanceof ClipGroup) { //on regarde dans le contenu de la planche s'il y a des groupes de Clip
					board.addClip(((ClipGroup) clip).getClips()); //on ajoute tous les clips du groupe de clip
					board.removeClip(clip); //on supprime le groupe de clip
					board.addClip(clip); //on ajoute en dernier le groupe de clip complet
				}
			}
			commands.addCommand(new CommandUngroup(this, groupes));
		});
		undo.setOnAction( (e) -> {
			commands.undo();
		});
		redo.setOnAction( (e) -> {
			commands.redo();
		});
		save.setOnAction( (e) -> { //je n'ai pas utilisé les outils proposés dans le tme pour la sauvegarde d'une image
			FileChooser fc = new FileChooser();
			FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.svg");
			fc.getExtensionFilters().add(ef);
			File file = fc.showSaveDialog(stage);
			if(file != null) {
				try {
					WritableImage saveImage = new WritableImage((int) (canvas.getWidth()), (int) (canvas.getHeight()));
					canvas.snapshot(null, saveImage);
					RenderedImage rendu = SwingFXUtils.fromFXImage(saveImage, null);
					ImageIO.write(rendu, "png", file);
				} catch (IOException ioe) {
					ioe.getMessage();
				}
			}
		});
		open.setOnAction( (e) -> { //ne fonctionne pas correctement
			FileChooser fc = new FileChooser();
			FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.svg");
			fc.getExtensionFilters().add(ef);
			File file = fc.showOpenDialog(null);
			try {
				BufferedImage bi = ImageIO.read(file);
				Image image = SwingFXUtils.toFXImage(bi, null);
				iv.setImage(image);
			} catch (IOException ioe) {
				ioe.getMessage();
			}
		});
		
		edit.getItems().addAll(copy, paste, delete, redo, undo, group, ungroup);
		file.getItems().addAll(newF, close, save, open);
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
			board.getContents().clear();
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
		return commands;
	}

	@Override
	public void clipboardChanged() {
		if(Clipboard.getInstance().isEmpty()) paste.setDisable(true);
		else paste.setDisable(false);
	}

}