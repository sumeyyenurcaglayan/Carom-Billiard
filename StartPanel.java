import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class StartPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BallPanel ballPanel;
	OnlineBallPanel onlineBallPanel;
	JButton startButton;
	JButton startOnline;
	

	public StartPanel(BallPanel a, OnlineBallPanel ob) {
		super();
		ballPanel = a;
		onlineBallPanel = ob;
		
		setLayout(null);
		setBackground(new Color(70, 30, 140));

		startButton = new JButton(" START ");
		startButton.setBounds(350, 100, 80, 80);
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				ballPanel.setVisible(true);
				ballPanel.getGameClock().startClock();
			}
		});

		startOnline = new JButton("ONLINE");
		startOnline.setBounds(350, 250, 80, 80);
		startOnline.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				
				Online online = new Online();
				onlineBallPanel.setOnline(online);
				onlineBallPanel.setVisible(true);
			}
		});

		add(startButton);
		add(startOnline);
	}

}
