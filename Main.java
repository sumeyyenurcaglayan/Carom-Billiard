

import javax.swing.JFrame;

public class Main {

	public static void main(String [] args)
	{
		JFrame f = new JFrame("Carom Billard");
		f.setSize(815, 640);
		f.setLayout(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
				
		BallPanel b=new BallPanel();
		b.setBounds(0, 0, 815, 640);
		b.setVisible(false);
		
		OnlineBallPanel onlineBallPanel = new OnlineBallPanel();
		onlineBallPanel.setBounds(0, 0, 815, 640);
		onlineBallPanel.setVisible(false);
		StartPanel c=new StartPanel(b, onlineBallPanel);
		c.setBounds(0,0,815, 640);
	    c.setVisible(true);

	    f.add(onlineBallPanel);
	    f.add(b);
	    f.add(c);
	}
	
}
