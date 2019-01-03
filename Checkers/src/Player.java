import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Player
{
	private ArrayList<Tile> selectedTiles = new ArrayList<>();
	private Color team;
	private Board board;
	private int direction;
	
	Timer timer = null;
	
	public Player(Color team, Board b, int dir)
	{
		this.team = team;
		this.board = b;
		direction = dir;
	}
	
	public void clearSelectedTiles()
	{
		selectedTiles.clear();
	}
	
	public boolean selectTile(Tile t)
	{
		if (selectedTiles.contains(t))
		{
			clearSelectedTiles();
			return false;
		}
		else
		{
			selectedTiles.add(t);
			return true;
		}
	}
	
	public Color getColor()
	{
		return team;
	}
	
	public ArrayList<Tile> getSelectedTiles()
	{
		ArrayList<Tile> selected = new ArrayList<>();
		for (Tile t : selectedTiles)
		{
			selected.add(t);
		}
		return selected;
	}
	
	public boolean checkJumpValidity(Location startLoc, Location endLoc, boolean mustHavePiece, Piece startPiece)
	{
		Tile startTile = board.tileAt(startLoc);
		Tile endTile = board.tileAt(endLoc);
		int dir = board.calculateDirection(startLoc, endLoc);
		
		if (startTile.hasPiece() || !mustHavePiece)
		{
			if (startPiece.king || dir == this.direction)
			{
				if (!endTile.hasPiece())
				{					
					if (endTile.isAdjacentTo(startTile))
					{
						return true;
					}
					else
					{
						if (board.isPieceBetween(startLoc, endLoc))
						{
							return true;
						}
						else
						{
							System.out.println("You cannot jump this far");
							return false;
							// They tried to jump too far (i.e over a gap)
						}
					}
				}
				else
				{
					System.out.println("You can't jump on another piece");
					return false;
					// CONDITION they are trying to jump on top of another piece
				}
			}
			else
			{
				System.out.println("You can't move in this direction");
				return false;
				// CONDITION the piece is not a king and they can't move in this dir
			}
		}
		else
		{
			System.out.println("No piece at first selection");
			return false;
		}
		
	}

	public void preMove()
	{
		if (selectedTiles.size() > 1 && selectedTiles.size() < 4)
		{
			Tile startTile = selectedTiles.get(0);
			if (startTile.hasPiece())
			{
				switch (selectedTiles.size())
				{
				case 2:
					Tile endTile = selectedTiles.get(1);
					if (checkJumpValidity(startTile.getLocation(), endTile.getLocation(), true, startTile.getPiece()))
					{
						move(startTile, endTile);
					}
					else
					{
						// Cant jump
						selectedTiles.clear();
						board.repaint();
					}
					break;
				case 3:
					Tile midTile = selectedTiles.get(1);
					Tile lastTile = selectedTiles.get(2);
					Location sLoc = startTile.getLocation();
					Location mLoc = midTile.getLocation();
					Location lLoc = lastTile.getLocation();
					if (board.isPieceBetween(sLoc, mLoc) && board.isPieceBetween(mLoc, lLoc))
					{
						if (checkJumpValidity(sLoc, mLoc, true, startTile.getPiece()) && checkJumpValidity(mLoc, lLoc, false, startTile.getPiece()))
						{
							move(startTile, midTile);
							timer = new Timer(300, new ActionListener()
							{
								@Override
								public void actionPerformed(ActionEvent e)
								{
									move(midTile, lastTile);
									timer.stop();
								}
							});
							timer.start();
							selectedTiles.clear();
							board.repaint();
							board.switchTurns();
						}
						else
						{
							// CONDITION invalid jump
							clearSelectedTiles();
							board.repaint();
						}
					}
					else
					{
						System.out.println("Invalid Double Jump (there needs to be a piece between every selected piece)");
						// CONDITION invalid double jump
						clearSelectedTiles();
						board.repaint();
					}
				}
			}
			else
			{
				System.out.println("No piece selected at starting point");
				// CONDITION no piece selected at starting point
				clearSelectedTiles();
				board.repaint();
			}
		}
		else if (selectedTiles.size() <= 1)
		{
			System.out.println("Not enough tiles selected");

			// CONDITION only one or less tiles selected
			selectedTiles.clear();
			board.repaint();
		}
		else
		{
			System.out.println("Too many tiles selected");
			// CONDITION too many tiles selected (double jump (3 tiles) is the max)
			selectedTiles.clear();
			board.repaint();
		}
	}

	private void move(Tile startTile, Tile endTile)
	{
		Piece p = startTile.getPiece();
		startTile.setPiece(null);
		endTile.setPiece(p);
		if (board.isPieceBetween(startTile.getLocation(), endTile.getLocation()))
		{
			if (!board.getPieceBetween(startTile.getLocation(), endTile.getLocation()).getColor().equals(getColor()))
			{
				board.tileBetween(startTile.getLocation(), endTile.getLocation()).setPiece(null);
			}
		}
		selectedTiles.clear();
		board.repaint();
		board.switchTurns();
	
		if ((direction == -1 && endTile.getLocation().y == 0) || (direction == 1 && endTile.getLocation().y == board.numTiles - 1))
		{
			endTile.kingPiece();
			System.out.println("King Me!");
		}
	}
}
