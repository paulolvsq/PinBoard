package pobj.pinboard.document;

import java.io.File;
import java.io.IOException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ClipImage implements Clip {
	
	private double left;
	private double top;
	private Image image;
	private File filename;
	
	public ClipImage(double left, double top, File filename) throws IOException {
		this.left = left;
		this.top = top;
		this.filename = filename;
		image = new Image(filename.toURI().toURL().toExternalForm());
	}

	@Override
	public void draw(GraphicsContext ctx) {
		ctx.drawImage(image, left, top);
	}

	@Override
	public double getTop() {
		return this.top;
	}

	@Override
	public double getLeft() {
		return this.left;
	}

	@Override
	public double getBottom() {
		return this.top + image.getHeight();
	}

	@Override
	public double getRight() {
		return this.left + image.getWidth();
	}

	@Override
	public void setGeometry(double left, double top, double right, double bottom) {
		this.left = left;
		this.top = top;
	}

	@Override
	public void move(double x, double y) {
		this.left += x;
		this.top += y;
	}

	@Override
	public boolean isSelected(double x, double y) {
		if(x >= left && x <= this.getRight() && y <= this.getBottom() && y >= top) return true;
		return false;
	}

	@Override
	public void setColor(Color c) {
		
	}

	@Override
	public Color getColor() {
		return null;
	}

	@Override
	public Clip copy() {
		try {
			return new ClipImage(this.left, this.top, this.filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}
