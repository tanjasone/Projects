import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameController implements MouseListener{
	
	private JPanel mainBoard;
	public int turns=0;
	private int turnPhase=0;
	BoardTile[][] tiles;
	private BoardTile currentSelect;
	private ArrayList<BoardTile> moves;


	public void setBoard(JPanel p) {
		mainBoard = p;
	}

	public void setTileArray(BoardTile[][] t){
	    tiles = t;
	}


	@Override
	public void mouseClicked(MouseEvent e) {

        Point mp = mainBoard.getMousePosition();
        BoardTile b = (BoardTile) mainBoard.getComponentAt(mp);
        System.out.println("Selecting: "+b.position);


        if(currentSelect == null && b.cp == null) return;

        if(currentSelect != null && b.cp != null && !b.equals(currentSelect) &&
        	b.cp.color.equals(currentSelect.cp.color))
        {
            System.out.println("Resetting");
        	currentSelect.setBorder(new EmptyBorder(0,0,0,0));
            for(int i=0; i<moves.size(); i++){
                moves.get(i).setBorder(new EmptyBorder(0,0,0,0));
            }
            currentSelect = null;
            turnPhase = 0;
        }

		if(turnPhase==0) {
			System.out.println("Turn phase 0");

            if(turns%2==0 && b.cp != null && b.cp.color.equals(Color.BLACK)) return;
            if(turns%2!=0 && b.cp != null && b.cp.color.equals(Color.WHITE)) return;
            if(b.cp == null) return;

			moves =  b.cp.calculateMove(tiles);
			currentSelect = b;
			b.setBorder(new LineBorder(Color.blue, 2));

			for(int i=0; i<moves.size(); i++){
				moves.get(i).setBorder(new LineBorder(Color.GREEN, 2));
				if(moves.get(i).cp != null && !moves.get(i).cp.color.equals(b.cp.color)){
					moves.get(i).setBorder(new LineBorder(Color.RED, 2));
				}
			}
			turnPhase = 1;
			System.out.println();
			return;
		}
		else if(turnPhase==1){

            if(!moves.contains(b)) return;

            System.out.println("Turn phase 1");
            boolean pawnChange = false;
            Label oldLabel=null;
            
            if(b.cp!=null)
				b.remove(b.cp.name);
            if(currentSelect.cp instanceof Pawn)
            {
            	oldLabel = currentSelect.cp.name;
            	pawnChange = true;
            	if(currentSelect.cp.color.equals(Color.BLACK) && b.position.y == 7)
            		currentSelect.cp = convertPawn(currentSelect.cp.color);
            	if(currentSelect.cp.color.equals(Color.WHITE) && b.position.y == 0)
            		currentSelect.cp = convertPawn(currentSelect.cp.color);
            }
			b.cp = currentSelect.cp;
			b.add(b.cp.name);
			b.cp.pos = b.position;


			currentSelect.setBorder(new EmptyBorder(0,0,0,0));
			for(int i=0; i<moves.size(); i++){
				moves.get(i).setBorder(new EmptyBorder(0,0,0,0));
			}
			
			if(pawnChange)
				currentSelect.remove(oldLabel);
			else
				currentSelect.remove(currentSelect.cp.name);
			currentSelect.cp = null;
			currentSelect = null;
			if(b.cp instanceof Pawn && ((Pawn) b.cp).firstTurn){
				((Pawn) b.cp).firstTurn = false;
			}

            turnPhase = 0;
            turns++;
		}
		System.out.println("----------------");
	}

	private ChessPiece convertPawn(Color color) 
	{
		String[] selectionValues = {"Queen", "Bishop", "Rook", "Knight"};
		ChessPiece cp = null;
		String choice;
		
		while(cp == null) 
		{
			choice = (String) JOptionPane.showInputDialog(null, "What type of piece do want to exchange the Pawn with?", null, 
				JOptionPane.QUESTION_MESSAGE, null, selectionValues, "[select]");
			if(choice.toUpperCase().equals("QUEEN"))
				cp = new Queen(color);
			else if(choice.toUpperCase().equals("BISHOP"))
				cp = new Bishop(color);
			else if(choice.toUpperCase().equals("ROOK"))
				cp = new Rook(color);
			else if(choice.toUpperCase().equals("KNIGHT"))
				cp = new Knight(color);
			else
				JOptionPane.showInternalMessageDialog(null, "That is not a valid option", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return cp;
	}

//*****************************************************

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
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
	
	
}
