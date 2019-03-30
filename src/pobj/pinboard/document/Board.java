package pobj.pinboard.document;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Board implements Clip {
	
	private List<Clip> conteneur;
	private Color color;
	
	public Board() {
		conteneur = new ArrayList<>();
		color = Color.BLACK;
	}

	@Override
	public void draw(GraphicsContext ctx) {
		double height = ctx.getCanvas().getHeight();
		double width = ctx.getCanvas().getWidth();
		ctx.setFill(Color.WHITE);
		ctx.fillRect(0.0, 0.0, width, height);
		for(Clip c : conteneur)
			c.draw(ctx);
	}

	@Override
	public double getTop() {
		return 0;
	}

	@Override
	public double getLeft() {
		return 0.0;
	}

	@Override
	public double getBottom() {
		return 0.0;
	}

	@Override
	public double getRight() {
		return 0;
	}

	@Override
	public void setGeometry(double left, double top, double right, double bottom) {
		
	}

	@Override
	public void move(double x, double y) {
		
	}

	@Override
	public boolean isSelected(double x, double y) {
		for(Clip c : conteneur)
			if(c.isSelected(x, y)) return true;
		return false;
	}

	@Override
	public void setColor(Color c) {
		this.color = c;
	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public Clip copy() {
		Board res = new Board();
		for(Clip c : this.conteneur) {
			res.conteneur.add(c.copy());
		}
		res.color = this.color;
		return res;
	}
	
	public List<Clip> getContents() {
		return this.conteneur;
	}
	
	public void addClip(Clip clip) {
		this.conteneur.add(clip);
	}
	
	public void addClip(List<Clip> clip) {
		for(Clip c : clip)
			this.conteneur.add(c);
	}
	
	public void removeClip(Clip clip) {
		this.conteneur.remove(clip);
	}
	
	public void removeClip(List<Clip> clip) {
		for(Clip c : clip)
			this.conteneur.remove(c);
	}
	
	
}
