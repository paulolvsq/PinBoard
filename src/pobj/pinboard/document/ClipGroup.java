package pobj.pinboard.document;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClipGroup implements Composite {
	
	private List<Clip> clips = new ArrayList<>(); //liste de clips qui correspond à un groupe de Clip
	
	//dessiner un groupe se réduit à dessiner tous ses éléments
	public void draw(GraphicsContext ctx) {
		for(Clip c : this.clips) c.draw(ctx);
	}
	
	//on récupère les coordonnées du rectangle englobant d'un groupe
	//il s'agit du plus petit élément englobant tous les autres éléments englobants
	//on récupère le plus petit left, le plus petit top, le plus grand bottom, le plus grand right
	//pour avoir le rectangle englobant qui contient tous les autres éléments
	public double getTop() {
		double top = this.clips.get(0).getTop();
		for(Clip c : this.clips) {
			if(c.getTop() < top) top = c.getTop();
		}
		return top;
	}
	
	public double getLeft() {
		double left = this.clips.get(0).getLeft();
		for(Clip c : this.clips) {
			if(c.getLeft() < left) left = c.getLeft();
		}
		return left;
	}
	
	public double getBottom() {
		double bottom = this.clips.get(0).getBottom();
		for(Clip c : this.clips) {
			if(c.getBottom() > bottom) bottom = c.getBottom();
		}
		return bottom;
	}
	
	public double getRight() {
		double right = this.clips.get(0).getRight();
		for(Clip c : this.clips) {
			if(c.getRight() > right) right = c.getRight();
		}
		return right;
	}
	
	//déplacer un groupe déplace tous ses éléments
	public void move(double x, double y) {
		for(Clip c : this.clips) c.move(x, y);
	}
	
	//setGeometry ne change pas la taille d'un groupe mais seulement sa position
	public void setGeometry(double left, double top, double right, double bottom) {
		for(Clip c : this.clips) c.setGeometry(left, top, right, bottom);
	}
	
	//copie en profondeur 
	public Clip copy() {
		ClipGroup res = new ClipGroup();
		for(Clip c : this.clips) res.addClip(c.copy());
		return res;
	}
	
	public List<Clip> getClips() {
		return this.clips;
	}
	
	public void addClip(Clip toAdd) {
		this.clips.add(toAdd);
	}
	
	public void removeClip(Clip toRemove) {
		this.clips.remove(toRemove);
	}

	@Override
	public boolean isSelected(double x, double y) {
		double cx = (this.getLeft() + this.getRight())/2.;
		double cy = (this.getTop() + this.getBottom())/2.;
		double rx = (this.getLeft() - this.getRight())/2.;
		double ry = (this.getTop() - this.getBottom())/2.;
		double tmpX = ((x - cx)/rx)*((x - cx)/rx);
		double tmpY = ((y - cy)/ry)*((y - cy)/ry);
		return tmpX + tmpY <= 1;
	}

	@Override
	public void setColor(Color c) {
		for(Clip clip : this.clips) clip.setColor(c);
	}

	@Override
	public Color getColor() {
		return this.clips.get(0).getColor();
	}

}
