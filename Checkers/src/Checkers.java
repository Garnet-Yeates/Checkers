import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

public class Checkers
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JFrame gameFrame = new JFrame();
				Board gamePanel = new Board(gameFrame);
				gameFrame.addKeyListener(gamePanel);
				gamePanel.setPreferredSize(new Dimension(768 - 10, 768 - 10));
				gameFrame.getContentPane().add(gamePanel);

				gameFrame.pack();
				gameFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
				gameFrame.setResizable(false);
				gameFrame.setVisible(true);
				gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				gameFrame.add(gamePanel);		
			}
		});
	}
}
