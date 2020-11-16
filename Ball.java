import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.Timer;



public class Ball implements ActionListener {

    public static int size = 50;
    private double speedFriction=0.995;
	private int x;
	private int y;
    private BallListener ballListener;
	private double Vx;
	private double Vy;
	
	private boolean isColliding;
	
	private Timer timer; 

	public Ball(int x, int y,BallListener bl) {
        ballListener=bl;
		this.setX(x);
		this.setY(y);
		this.setColliding(false);
		timer=new Timer(16,this);
	}

	public int getRadius() {
		return size;
	}

	public double getVx() {
		return Vx;
	}

	public void setVx(double vx) {
		Vx = vx;
	}

	public double getVy() {
		return Vy;
	}

	public void setVy(double vy) {
		Vy = vy;
	}

	public int getX() {
		return x;
    }

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
    public Point getCenterPoint(){
    	int centerX=this.x+(size/2);
    	int centerY=this.y+(size/2);
    	
    	return new Point(centerX,centerY);
    }
    
    public int getSpeed() {
    	return (int)Math.hypot(Vx, Vy);
    }
    
    public Ellipse2D.Double getBall(){
    	Ellipse2D.Double ball=new Ellipse2D.Double(this.x,this.y,size,size);
    	return ball;
    	
    }
    
    public void shoot(double angle,double force){
    	
    	force=force/5*3;
    	double Vx2=force*Math.cos(angle)*-1;
    	double Vy2=force*Math.sin(angle)*-1;
    	setVx(Vx2);
    	setVy(Vy2);
    	timer.start();
    	
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		//WALL COLLISION
		if(x<=0||x>=BallPanel.tableWidth-size){
			if(x<=0)
				setX(2);
			else
				setX(BallPanel.tableWidth-size-2);
			
			Vx=-Vx;
			ballListener.onWallHitted(this);
		}
		if(y<=0||y>=BallPanel.tableHeight-size){
			if(y<=0)
				setY(2);
			else
				setY(BallPanel.tableHeight-size-2);
			
			Vy=-Vy;
			ballListener.onWallHitted(this);
		}
		
		ballListener.onBallCollidingCalculated();
		
	    double s=Math.sqrt(Vx*Vx+Vy*Vy);
	    s*=speedFriction;
	    if(s<=0.3){
	    	timer.stop();
	    	ballListener.onBallStopped(this);
	    }
	    double angle=Math.atan2(Vy, Vx);
	    int sx=(int)(Math.cos(angle)*s);
	    int sy=(int)(Math.sin(angle)*s);
	    setVx(sx);
	    setVy(sy);
	    setX(x+sx);
	    setY(y+sy);
	    
	    ballListener.onBallTick();
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public boolean isColliding() {
		return isColliding;
	}

	public void setColliding(boolean isColliding) {
		this.isColliding = isColliding;
	}
}
