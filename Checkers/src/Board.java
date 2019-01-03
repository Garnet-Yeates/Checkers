import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Board extends JPanel implements MouseListener, KeyListener
{
	private static final long serialVersionUID = 902033213014961959L;
	
	int numTiles = 8;
	int tileSize = 96;
	
	public Tile[][] board = new Tile[numTiles][numTiles];
	
	private Player p1 = new Player(new Color(242, 36, 36), this, 1);
	private Player p2 = new Player(new Color(100, 100, 100), this, -1);
	
	private Player turnPlayer = p1;
	
	public Board(JFrame container)
	{
		initBoard();
		initPieces(8);
		addMouseListener(this);
	}
	
	public void initPieces(int piecesPerTeam)
	{
		{ // BLOCK: Init Red Pieces
			int numP1PiecesAdded = 0;
			o: for (int y = 0; y < numTiles; y++)
			{
				for (int x = 0; x < numTiles; x++)
				{
					Location loc = new Location(y, x);
					Tile t = tileAt(loc);
					if (t.getColor().equals(Color.BLACK))
					{
						t.setPiece(new Piece(p1.getColor(), false));
						numP1PiecesAdded++;
						if (numP1PiecesAdded == piecesPerTeam)
						{
							break o;
						}
					}
				}
			}	
		}
		
		{ // BLOCK: Init Black Pieces
			int numP2PiecesAdded = 0;
			o: for (int y = numTiles - 1; y >= 0; y--)
			{
				for (int x = numTiles - 1; x >= 0; x--)
				{
					Location loc = new Location(y, x);
					Tile t = tileAt(loc);
					if (t.getColor().equals(Color.BLACK))
					{
						t.setPiece(new Piece(p2.getColor(), false));
						numP2PiecesAdded++;
						if (numP2PiecesAdded == piecesPerTeam)
						{
							break o;
						}
					}
				}
			}	
		}
		
	}
	
	public void initBoard()
	{
		boolean white = true;
		for (int y = 0; y < numTiles; y++)
		{
			for (int x = 0; x < numTiles; x++, white = ((white) ? false : true))
			{
				Location loc = new Location(y, x);
				Tile t = new Tile(((white == true) ? Color.WHITE : Color.BLACK ), loc, this);
				board[y][x] = t;
				if (x == 7)
				{
					white = ((white) ? false : true);
				}
			}
		}
	}
	
	@Override
	public void paint(Graphics g)
	{
		// BLOCK: Draw Tiles
		{
			int yPos = 0;
			int xPos = 0;
			for (int y = 0; y < numTiles; y++)
			{
				xPos = 0;
				for (int x = 0; x < numTiles; x++)
				{
					Location loc = new Location(y, x);
					Tile t = tileAt(loc);
					g.setColor(t.getColor());
					g.fillRect(xPos, yPos, tileSize, tileSize);
					xPos += tileSize;
				}
				yPos += tileSize;
			}	
		}
		
		// BLOCK: Draw Highlights
		{
			for (Tile t : turnPlayer.getSelectedTiles())
			{
				Location loc = t.getLocation();
				int x = loc.x;
				int y = loc.y;
				int yPos = tileSize * y;
				int xPos = tileSize * x;

				g.setColor(turnPlayer.getColor());
				int sizeOffset = 64;
				int posOffset = sizeOffset / 2;
				g.drawRect(xPos + posOffset, yPos + posOffset, tileSize - sizeOffset - 1, tileSize - sizeOffset - 1);
				g.drawRect(xPos + posOffset - 1, yPos + posOffset - 1, tileSize - sizeOffset + 1, tileSize - sizeOffset + 1);
				
				if (t.hasPiece()) // When a piece is selected it becomes thicc
				{
					g.drawRect(xPos + posOffset - 2, yPos + posOffset - 2, tileSize - sizeOffset + 3, tileSize - sizeOffset + 3);
					g.drawRect(xPos + posOffset - 3, yPos + posOffset - 3, tileSize - sizeOffset + 5, tileSize - sizeOffset + 5);
					g.drawRect(xPos + posOffset - 4, yPos + posOffset - 4, tileSize - sizeOffset + 7, tileSize - sizeOffset + 7);
				}
			}
		}
		
		// BLOCK: Draw Pieces
		{
			int yPos = 0;
			for (int y = 0; y < numTiles; y++)
			{
				int xPos = 0;
				for (int x = 0; x < numTiles; x++)
				{
					Piece p;
					if ((p = pieceAt(new Location(y, x))) != null)
					{
						g.setColor(p.getColor());
						int sizeOffset = 64;
						int posOffset = sizeOffset / 2;
						g.fillRect(xPos + posOffset, yPos + posOffset, tileSize - sizeOffset, tileSize - sizeOffset);
						
						if (p.isKing())
						{
							g.setColor(Color.WHITE);
							sizeOffset = 80;
							posOffset = sizeOffset / 2;
							g.fillRect(xPos + posOffset, yPos + posOffset, tileSize - sizeOffset, tileSize - sizeOffset);
						}
					}
					xPos += tileSize;
				}
				yPos += tileSize;
			}	
		}
	}
	
	public Tile tileAt(Location loc)
	{
		try
		{
			return board[loc.y][loc.x];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
	public boolean hasPieceAt(Location loc)
	{
		return (tileAt(loc) != null) ? true : false;
	}
	
	public Piece pieceAt(Location loc)
	{
		return tileAt(loc).getPiece();
	}
	
	public Piece getPieceBetween(Location start, Location end)
	{
		int sX = start.x;
		int sY = start.y;
		
		int eX = end.x;
		int eY = end.y;
		
		if ((sX + 2 == eX || sX - 2 == eX) && (sY + 2 == eY || sY - 2 == eY))
		{
			int x = (sX + eX) / 2;
			int y = (sY + eY) / 2;
			Tile t = tileAt(new Location(y, x));
			if (t.hasPiece())
			{
				return t.getPiece();
			}
		}
		
		return null;
	}
	
	public boolean isPieceBetween(Location start, Location end)
	{
		return getPieceBetween(start, end) != null;
	}
	
	public Tile tileBetween(Location start, Location end)
	{
		int sX = start.x;
		int sY = start.y;
		
		int eX = end.x;
		int eY = end.y;
		
		if ((sX + 2 == eX || sX - 2 == eX) && (sY + 2 == eY || sY - 2 == eY))
		{
			int x = (sX + eX) / 2;
			int y = (sY + eY) / 2;
			Tile t = tileAt(new Location(y, x));
			return t;
		}
		return null;
	}
	
	public int calculateDirection(Location start, Location end)
	{
		if (start.y > end.y)
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		int x = e.getX() / (tileSize);
		int y = e.getY() / (tileSize);
		Tile t = tileAt(new Location(y, x));
		if (!t.getColor().equals(Color.WHITE))
		{
			if ((t.hasPiece() && t.getPiece().getColor().equals(turnPlayer.getColor())) || !t.hasPiece())
			{
				if (turnPlayer.getSelectedTiles().isEmpty())
				{
					if (t.hasPiece() && t.getPiece().getColor().equals(turnPlayer.getColor()))
					{
						turnPlayer.selectTile(t);
					}
					else
					{
						// CONDITION first selected tile must be one of your pieces!
					}
				}
				else
				{
					turnPlayer.selectTile(t);
				}
			}
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			turnPlayer.preMove();
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			turnPlayer.clearSelectedTiles();
			repaint();
		}
	}

	/**
	 * Switches turns in the game to the other player
	 */
	public void switchTurns()
	{
		if (p1.equals(turnPlayer))
		{
			turnPlayer = p2;
			System.out.println("PLAYER 2'S TURN");
		}
		else
		{
			turnPlayer = p1;
			System.out.println("PLAYER 1'S TURN");
		}
		
	}

	// Unused Implemented Methods
	
	@Override
	public void keyReleased(KeyEvent arg0) { }
	
	@Override
	public void keyTyped(KeyEvent arg0) { }
	
	@Override
	public void mouseEntered(MouseEvent arg0) { }

	@Override
	public void mouseExited(MouseEvent arg0) { }

	@Override
	public void mousePressed(MouseEvent arg0) { }

	@Override
	public void mouseReleased(MouseEvent arg0) { }	
}
