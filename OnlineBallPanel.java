import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;


public class OnlineBallPanel extends JPanel implements MouseListener, BallListener, MouseMotionListener, OnlineListener {

	private Online online;
	
	private static final long serialVersionUID = 1L;
	public static final int tableWidth = 800, tableHeight = 500;
	Stick stick;
	Ball whiteBall;
	Ball yellowBall;
	Ball redBall;
	
	private JButton exitButton;
	
	private GameClock gameClock;
	private String time;
	
	private int score = 0;
	private int hittedToWall = 0;
	private int hittedToBall = 0;
	
	Thread onlineThread;
	
	public OnlineBallPanel() {
		super();
		setLayout(null);
		Color bs=new Color(107,142,35);
		setBackground(bs);
		addMouseListener(this);
		addMouseMotionListener(this);

		whiteBall = new Ball(50, 50, this);
		yellowBall = new Ball(200, 200, this);
		redBall = new Ball(300, 300, this);
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(600,540,100,50);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameClock.stopClock();
				time = "00:00";
			}
		});
		add(exitButton);
		
		time = "00:00";
		gameClock = new GameClock(new ClockListener() {
			@Override
			public void onClockTick(String s) {
				time = s;
			}
		});

		stick = new Stick(whiteBall.getCenterPoint().getX(), whiteBall
				.getCenterPoint().getY());

	}
	
	public void setOnline(Online ol) {
		this.online = ol;
		if(online.isServer()) {
			online.getServer().setOnlineListener(this);
			onlineThread = new Thread(new Runnable() {
				@Override
				public void run() {
					online.getServer().startServer();
				}
			});
			online.setReady(false);
		}
		else {
			online.getClient().setOnlineListener(this);	
			onlineThread = new Thread(new Runnable() {
				@Override
				public void run() {
					online.getClient().startClient();
				}
			});
			online.setReady(true);
		}
		
		onlineThread.start();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		Color as=new Color(0,100,0);
		g2d.setColor(as);
		g2d.fillRect(0, 0, tableWidth, tableHeight);
		
		g2d.setColor(Color.white);
		g2d.fill(whiteBall.getBall());
		g2d.setColor(Color.yellow);
		g2d.fill(yellowBall.getBall());
		g2d.setColor(Color.red);
		g2d.fill(redBall.getBall());
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Speed: " + whiteBall.getSpeed() + " km/h", 20, 560);
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Score: " + score, 200, 560);
		
		g2d.setColor(Color.WHITE);
		g2d.drawString(time, 400, 560);
		
		g2d.setColor(Color.black);
		if (!whiteBall.getTimer().isRunning() && online.isReady()) {
			Line2D.Double stickline = stick.getLine(
					whiteBall.getCenterPoint().x, whiteBall.getCenterPoint().y);
			g2d.drawLine((int) stickline.getX1(), (int) stickline.getY1(),
					(int) stickline.getX2(), (int) stickline.getY2());
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(!online.isReady())
			return;
		
		stick.pull(e.getPoint());
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!online.isReady())
			return;
		
		Point mousePos = e.getPoint();
		Point centerPos = whiteBall.getCenterPoint();
		Point vector = new Point((int) (mousePos.getX() - centerPos.getX()),
				(int) (mousePos.getY() - centerPos.getY()));
		double angle = Math.atan2(vector.getY(), vector.getX());

		stick.setAngle(angle);
		repaint();

	}

	@Override
	public void onBallTick() {
		repaint();
	}

	@Override
	public void onBallCollidingCalculated() {
		double distanceFromYellow = whiteBall.getCenterPoint().distance(
				yellowBall.getCenterPoint());
		double distanceFromRed = whiteBall.getCenterPoint().distance(
				redBall.getCenterPoint());
		double redDistanceFromYellow = redBall.getCenterPoint().distance(
				yellowBall.getCenterPoint());
		
		if (distanceFromYellow <= Ball.size) {
			collide(whiteBall, yellowBall);
		}
		if (distanceFromRed <= Ball.size) {
			collide(whiteBall, redBall);
		}
		if (redDistanceFromYellow <= Ball.size) {
			collide(yellowBall, redBall);
		}
	}
	
	private void collide(Ball b1,Ball b2){
		Point b1Center=b1.getCenterPoint();
		Point b2Center=b2.getCenterPoint();
		Point distanceVector=new Point (b2Center.x-b1Center.x,b2Center.y-b1Center.y);
		
		double distance=Math.sqrt(distanceVector.x*distanceVector.x+distanceVector.y*distanceVector.y);
		double angle=Math.atan2(distanceVector.y, distanceVector.x);
		
		//SEPERATE BALLS
		double df=Math.ceil((Ball.size-distance)/2);
		int dfx=(int)(df*Math.cos(angle));
		int dfy=(int)(df*Math.sin(angle));
		b1.setX(b1.getX()-dfx);
		b1.setY(b1.getY()-dfy);
		b2.setX(b2.getX()+dfx);
		b2.setY(b2.getY()+dfy);
		
		//COLLISION
		double scalerProductForV1 = b1.getVx() * distanceVector.x + b1.getVy() * distanceVector.y;
		double normDistanceVector = Math.hypot(distanceVector.x, distanceVector.y);
		
		double projectionOfV1 = scalerProductForV1 / normDistanceVector;
		
		double scalerProductForV2 = b2.getVx() * distanceVector.x + b2.getVy() * distanceVector.y;
		double projectionOfV2 = scalerProductForV2 / normDistanceVector;
		
		double resultant = projectionOfV1 - projectionOfV2;
		
		double newVx1 = b1.getVx() + Math.cos(Math.PI + angle) * resultant;
		double newVy1 = b1.getVy() + Math.sin(Math.PI + angle) * resultant;
		
		double newVx2 = b2.getVx() + Math.cos(angle) * resultant;
		double newVy2 = b2.getVy() + Math.sin(angle) * resultant;
		
		b1.setVx(newVx1);
		b1.setVy(newVy1);
		
		b2.setVx(newVx2);
		b2.setVy(newVy2);
		
		if(!b1.getTimer().isRunning())
			b1.getTimer().start();
		if(!b2.getTimer().isRunning())
			b2.getTimer().start();
		
		hittedToBall++;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if(online.isReady())
			stick.setMouseFirstPosition(arg0.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(!online.isReady())
			return;
		
		try {
			if(online.isServer())
				online.getServer().sendShoot(stick.getAngle(), stick.getOffset());
			else
				online.getClient().sendShoot(stick.getAngle(), stick.getOffset());
			
			online.setReady(false);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		whiteBall.shoot(stick.getAngle(), stick.getOffset());
		stick.setOffset(0);
		repaint();
	}

	@Override
	public void onDataReceived(double angle, double force) {
		whiteBall.shoot(angle, force);
		online.setReady(true);
	}

	@Override
	public void onWallHitted(Ball b) {
		if(b == whiteBall)
			hittedToWall++;
	}

	@Override
	public void onBallStopped(Ball b) {
		if(b != whiteBall)
			return;
		
		if(hittedToWall == 0 && hittedToBall == 2)
			score += 10;
		
		hittedToWall = 0;
		hittedToBall = 0;
	}

	public GameClock getGameClock() {
		return gameClock;
	}

	@Override
	public void onConnectionSuccess() {
		gameClock.startClock();
		repaint();
	}
}
