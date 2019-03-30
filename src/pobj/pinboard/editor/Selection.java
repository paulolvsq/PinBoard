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
			if(c.isSelected(x, y)) this.selected.add(c);
		}
	}
	
	public void toogleSelect(Board board, double x, double y) {
		for(Clip c : board.getContents()) {
			if(c.isSelected(x, y)) {
				if(this.selected.contains(c)) this.selected.remove(c);
				else this.selected.add(c);
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
		/*for(Clip c : selected) {
			c.draw(gc);
		}*/ //stupide, ce n'est pas ce qui est demandé dans la question
		//je commence par initialiser les contours de ce que je veux sélectionner
		double top = 0.;
		double bottom = 0.;
		double left = 0.; 
		double right = 0.;
		//je regarde si la sélection n'est pas vide
		if(!this.selected.isEmpty()) {
			//je récupère les coordonnées du premier objet sélectionné -> c'est un clip
			top = this.selected.get(0).getTop();
			bottom = this.selected.get(0).getBottom();
			left= this.selected.get(0).getLeft();
			right = this.selected.get(0).getRight();
		}
		//ensuite je parcours la liste des selected et je mets à jour les positions si la sélection est plus grande que pour le clip précédent
		//c'est à dire si j'ai bougé ma souris pour agrandir la zone de sélection
		for(Clip c : this.selected) {
			if(c.getTop() < top) top = c.getTop();
			if(c.getBottom() > bottom) bottom = c.getBottom();
			if(c.getLeft() < left) left = c.getLeft();
			if(c.getRight() > right) right = c.getRight();
 		}
		//une fois qu'on a parcouru tous les clips et récupéré les bonnes coordonnées on dessine le rectangle de sélection
		//ce rectangle va contenir les clips sélectionnés 
		gc.strokeRect(left, top, right - left, bottom - top);
	}

}
