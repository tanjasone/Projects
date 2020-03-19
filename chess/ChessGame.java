import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import java.awt.*;

public class ChessGame{
	
	public static void main(String[] args) {
		
		int i, j;
		
		JFrame gameFrame = new JFrame();
		JPanel gameWindow = new JPanel();
		JPanel gameBoard = new JPanel(new GridLayout(8, 8));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//JPanel gameUI = BuildUI();
		
		GameController gc = new GameController();
		BoardTile[][] tiles = new BoardTile[8][8];
		for(i=0; i<8; i++) {
			for(j=0; j<8; j++) {
				tiles[i][j] = new BoardTile();
				tiles[i][j].position = new Point(j, i);
				if(j%2==1) {
					Color c = i%2==0 ? new Color(100,100,100) : new Color(200,200,200);
					tiles[i][j].setBackground(c);
				}
				else {
					Color c = i % 2 == 0 ? new Color(200, 200, 200) : new Color(100, 100, 100);
					tiles[i][j].setBackground(c);
				}
				tiles[i][j].cp = null;
				gameBoard.add(tiles[i][j]);
			}
		}
		initPieces(tiles);
		
		gameFrame.setVisible(true);
		gameFrame.setSize(screenSize);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gameBoard.setEnabled(true);
		gameBoard.setVisible(true);
		gameBoard.setSize(600, 600);
		gameBoard.setBackground(Color.BLACK);
		gameBoard.addMouseListener(gc);
		
		gc.setBoard(gameBoard);
		gc.setTileArray(tiles);

		

		gameWindow.add(gameBoard);
		//gameWindow.add(gameUI);
		
		gameFrame.add(gameBoard);
	}

	private static void initPieces(BoardTile[][] bt){

		int i;

		bt[0][0].cp = new Rook(Color.BLACK);
		bt[0][1].cp = new Knight(Color.BLACK);
		bt[0][2].cp = new Bishop(Color.BLACK);
		bt[0][3].cp = new Queen(Color.BLACK);
		bt[0][4].cp = new King(Color.BLACK);
		bt[0][5].cp = new Bishop(Color.BLACK);
		bt[0][6].cp = new Knight(Color.BLACK);
		bt[0][7].cp = new Rook(Color.BLACK);

		bt[7][0].cp = new Rook(Color.WHITE);
		bt[7][1].cp = new Knight(Color.WHITE);
		bt[7][2].cp = new Bishop(Color.WHITE);
		bt[7][3].cp = new Queen(Color.WHITE);
		bt[7][4].cp = new King(Color.WHITE);
		bt[7][5].cp = new Bishop(Color.WHITE);
		bt[7][6].cp = new Knight(Color.WHITE);
		bt[7][7].cp = new Rook(Color.WHITE);

		for(i=0; i<8; i++){
			bt[1][i].cp = new Pawn(Color.BLACK);
			bt[6][i].cp = new Pawn(Color.WHITE);
		}

		for(i=0; i<8; i++){
			for(int j=0; j<8; j++) {
				if (bt[i][j].cp != null) {
					bt[i][j].add(bt[i][j].cp.name);
					bt[i][j].cp.pos = bt[i][j].position;
				}
			}
		}

	}

	private static JPanel BuildUI(){

		JPanel ui = new JPanel(new BorderLayout());


		//ui.add(, BorderLayout.NORTH);

		return ui;
	}

}
