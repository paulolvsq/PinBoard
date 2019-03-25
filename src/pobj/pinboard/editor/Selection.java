package pobj.pinboard.editor;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;

public class Selection {
	
	private List<Clip> selected = new ArrayList<>();
	
	public void select(Board b, double x, double y) {
		this.clear();
		for(Clip c : b.getContents()) {
			if(c.isSelected(x, y)) selected.add(c);
		}
	}
	
	public void toogleSelect(Board board, double x, double y) {
		for(Clip c : board.getContents()) {
			if(c.isSelected(x, y)) {
				if(selected.contains(c)) selected.remove(c);
				else selected.add(c);
			}
		}
	}
	
	public void clear() {
		this.selected.clear();
	}
	
	public List<Clip> getContents(){
		return this.selected;
	}
	
	public void drawFeedback(GraphicsContext gc) {
		for(Clip c : selected) {
			c.draw(gc);
		}
	}

}
