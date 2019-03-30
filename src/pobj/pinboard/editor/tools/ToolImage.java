package pobj.pinboard.editor.tools;

import java.io.File;
import java.io.IOException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.ClipImage;
import pobj.pinboard.editor.EditorInterface;

public class ToolImage implements Tool {
	
	private File file;
	private ClipImage image;
	private boolean verif = false;
	
	public ToolImage(File file) {
		this.file = file;
	}

	@Override
	public void press(EditorInterface i, MouseEvent e) {
		Board b = i.getBoard();
		try {
			image = new ClipImage(e.getX(), e.getY(), file);
		} catch (IOException e1) {
			e1.getMessage();
		}
		b.addClip(image);
		verif = true;
	}

	@Override
	public void drag(EditorInterface i, MouseEvent e) {
		Board b = i.getBoard();
		if(image != null) b.removeClip(image);
		try {
			image = new ClipImage(e.getX(), e.getY(), file);
		} catch (IOException e1) {
			e1.getMessage();
		}
		b.addClip(image);
	}

	@Override
	public void release(EditorInterface i, MouseEvent e) {
		Board b = i.getBoard();
		b.removeClip(image);
		try {
			image = new ClipImage(e.getX(), e.getY(), file);
		} catch (IOException e1) {
			e1.getMessage();
		}
		b.addClip(image);
		image = null;
		verif = false;
	}

	@Override
	public void drawFeedback(EditorInterface i, GraphicsContext gc) {
		i.getBoard().draw(gc);
		if(verif) {
			//image = new ClipImage(image.getLeft(), image.getTop(), Math.abs(image.getLeft() - image.getRight()), Math.abs(image.getBottom() - image.getTop()));
			/*try {
				image = new ClipImage(image.getLeft(), image.getTop(), file);
			} catch (IOException e) {
				e.getMessage();
			}*/
			//image.draw(gc);

			gc.strokeRect(image.getLeft(), image.getTop(), Math.abs(image.getLeft() - image.getRight()), Math.abs(image.getBottom() - image.getTop()));
		}
	}

	@Override
	public String getName(EditorInterface editor) {
		return "Image tool";
	}

}


