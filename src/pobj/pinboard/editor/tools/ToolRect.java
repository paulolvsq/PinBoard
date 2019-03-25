package pobj.pinboard.editor.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.ClipRect;
import pobj.pinboard.editor.EditorInterface;

public class ToolRect implements Tool {
	
	private double x1;
	private double y1; //coordonnées de là où on va cliquer la première fois
	private double x2;
	private double y2; //coordonnées de là où on relâche la souris
	
	private boolean verif = false; //vérifie si on dessine 
	//vaut true quand on dessine false sinon
	private ClipRect rectangle; 
	
	@Override
	public void press(EditorInterface i, MouseEvent e) {
		x1 = e.getX();
		y1 = e.getY();
		x2 = e.getX();
		y2 = e.getY(); //pour le moment on initialise comme ça on changera plus tard
		verif = true;
	}

	@Override
	public void drag(EditorInterface i, MouseEvent e) {
		x2 = e.getX();
		y2 = e.getY();
		//on laisse le verif à true vu qu'on dessine à ce moment
	}

	@Override
	public void release(EditorInterface i, MouseEvent e) {
		double relacheX = e.getX();
		double relacheY = e.getY();
		Board b = i.getBoard();
		Color color = b.getColor();
		if(relacheX == x1 || relacheY == y1) return; //dans ce cas c'est qu'on a rien à faire puisque les coordonnées ne correspondent pas à un rectangle
		rectangle = new ClipRect(Math.min(x1, relacheX), Math.min(y1 ,relacheY), Math.max(x1, relacheX), Math.max(y1, relacheY), color);
		b.addClip(rectangle);
		verif = false;
	}

	@Override
	public void drawFeedback(EditorInterface i, GraphicsContext gc) {
		if(verif) {
			rectangle = new ClipRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2-x1), Math.abs(y2-y1), i.getBoard().getColor());
			rectangle.draw(gc);	
		}
	}

	@Override
	public String getName(EditorInterface editor) {
		return "Rectangle tool";
	}
	
	

}
