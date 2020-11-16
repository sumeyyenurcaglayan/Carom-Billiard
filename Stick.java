import java.awt.Point;
import java.awt.geom.Line2D;

public class Stick {
	private final int maxOffset=100;
	private final int defaultOffset=50;
	private int offset ;
	private double xs, ys;
	private int size = 100;
	private double angle;
	private Point mouseFirstPosition;

	public Stick(double xs, double ys) {
		this.setXs(xs);
		this.setYs(ys);
        this.setOffset(0);
		this.setAngle(0);
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
		setCoordinates();
	}


	public double getYs() {
		return ys;
	}

	public void setYs(double ys) {
		this.ys = ys;
	}

	public double getXs() {
		return xs;
	}

	public void setXs(double xs) {
		this.xs = xs;
	}

	public void setCoordinates() {
		
		xs = (int) ((offset+defaultOffset) * Math.cos(angle));
		
		ys = (int) ((offset+defaultOffset) * Math.sin(angle));
		
		
	}

	public Line2D.Double getLine(int ballx,int bally) {
		int fx = (int) ((offset + size+defaultOffset) * Math.cos(angle));
		int fy = (int) ((offset + size+defaultOffset) * Math.sin(angle));
		
        return new Line2D.Double(xs+ballx,ys+bally,fx+ballx,fy+bally);
	}

	public Point getMouseFirstPosition() {
		return mouseFirstPosition;
	}

	public void setMouseFirstPosition(Point mouseFirstPosition) {
		this.mouseFirstPosition = mouseFirstPosition;
	}
	public void pull(Point mousePos){
		double distance=mousePos.distance(mouseFirstPosition);
		offset=Math.min((int)distance,maxOffset );
		setCoordinates();
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
		setCoordinates();
	}
}
