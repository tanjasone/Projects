package chess;

import java.awt.Point;
import java.awt.Color;
import java.awt.Label;
import java.util.ArrayList;

public abstract class ChessPiece {

	Point pos;
	Color color;
    public Label name;
	
	public abstract ArrayList<BoardTile> calculateMove(BoardTile[][] tiles);
}

//*****************************************************

class Pawn extends ChessPiece{

    public boolean firstTurn=true;

    public Pawn(Color c){
        name = new Label("P", Label.CENTER);
        name.setBackground(c);
        if(c.equals(Color.BLACK))
            name.setForeground(Color.WHITE);
        color = c;
    }

    @Override
	public ArrayList<BoardTile> calculateMove(BoardTile[][] tiles) {

        System.out.println("Calculating: Pawn...");
        ArrayList<BoardTile> moves = new ArrayList<BoardTile>();

        if(color.equals(Color.WHITE)) {
            if(tiles[pos.y-1][pos.x].cp == null) {
                moves.add(tiles[pos.y - 1][pos.x]);
            }
            if(firstTurn && tiles[pos.y-2][pos.x].cp == null && tiles[pos.y-1][pos.x].cp == null){
                moves.add(tiles[pos.y-2][pos.x]);
            }
            if(pos.x-1>0 &&
                    tiles[pos.y-1][pos.x-1].cp != null &&
                    !tiles[pos.y-1][pos.x-1].cp.color.equals(color)) {
                moves.add(tiles[pos.y-1][pos.x-1]);
            }
            if(pos.x+1<8 &&
                    tiles[pos.y-1][pos.x+1].cp != null &&
                    !tiles[pos.y-1][pos.x+1].cp.color.equals(color)) {
                moves.add(tiles[pos.y-1][pos.x+1]);
            }
        }
        else{
            if(tiles[pos.y+1][pos.x].cp == null){
                moves.add(tiles[pos.y+1][pos.x]);
            }
            if(firstTurn && tiles[pos.y+2][pos.x].cp == null &&
                    tiles[pos.y+1][pos.x].cp == null) {
                moves.add(tiles[pos.y + 2][pos.x]);
            }
            if(pos.x-1>0 &&
                    tiles[pos.y+1][pos.x-1].cp != null &&
                    !tiles[pos.y+1][pos.x-1].cp.color.equals(color)) {
                moves.add(tiles[pos.y+1][pos.x-1]);
            }
            if(pos.x+1<8 &&
                    tiles[pos.y+1][pos.x+1].cp != null &&
                    !tiles[pos.y+1][pos.x+1].cp.color.equals(color)) {
                moves.add(tiles[pos.y + 1][pos.x + 1]);
            }
        }
        return moves;
    }
}

//*****************************************************

class King extends ChessPiece{

    public King(Color c){
        name = new Label("K", Label.CENTER);
        name.setBackground(c);
        if(c.equals(Color.BLACK))
            name.setForeground(Color.WHITE);
        color = c;
    }

	@Override
	public ArrayList<BoardTile> calculateMove(BoardTile[][] tiles) {
        System.out.println("Calculating: King...");
        ArrayList<BoardTile> moves = new ArrayList<BoardTile>();

        try{
            moves.add(tiles[pos.y-1][pos.x]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y-1][pos.x+1]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y][pos.x+1]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y+1][pos.x+1]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y+1][pos.x]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y+1][pos.x-1]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y][pos.x-1]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y-1][pos.x-1]);
        }catch(ArrayIndexOutOfBoundsException oob){}

        int i=0;
        while(i<moves.size()){
            if(moves.get(i).cp!=null && moves.get(i).cp.color.equals(color)) {
                System.out.println("Removing: "+moves.get(i).position);
                moves.remove(i);
                continue;
            }
            i++;
        }

        return moves;
	}
}

//*****************************************************

class Queen extends ChessPiece{

    public Queen(Color c){
        name = new Label("Q", Label.CENTER);
        name.setBackground(c);
        if(c.equals(Color.BLACK))
            name.setForeground(Color.WHITE);
        color = c;
    }

	@Override
	public ArrayList<BoardTile> calculateMove(BoardTile[][] tiles) {
        System.out.println("Calculating: Queen...");
        ArrayList<BoardTile> moves = new ArrayList<BoardTile>();
        int i=1;
        boolean N=true, NE=true, E=true, SE=true, S=true, SW=true, W=true, NW=true;

        while(N || NE || E || SE || S || SW || W || NW){

            if(N){
                try{
                    if(tiles[pos.y-i][pos.x].cp!=null) N = false;
                    moves.add(tiles[pos.y-i][pos.x]);
                }catch(ArrayIndexOutOfBoundsException oob){N=false;}
            }
            if(NE){
                try{
                    if(tiles[pos.y-i][pos.x+i].cp!=null) NE = false;
                    moves.add(tiles[pos.y - i][pos.x + i]);
                }catch(ArrayIndexOutOfBoundsException oob){NE=false;}
            }
            if(E){
                try{
                    if(tiles[pos.y][pos.x+i].cp!=null) E = false;
                    moves.add(tiles[pos.y][pos.x+i]);
                }catch(ArrayIndexOutOfBoundsException oob){E=false;}
            }
            if(SE){
                try{
                    if(tiles[pos.y+i][pos.x+i].cp!=null) SE = false;
                    moves.add(tiles[pos.y+i][pos.x+i]);
                }catch(ArrayIndexOutOfBoundsException oob){SE=false;}
            }
            if(S){
                try{
                    if(tiles[pos.y+i][pos.x].cp!=null) S = false;
                    moves.add(tiles[pos.y+i][pos.x]);
                }catch(ArrayIndexOutOfBoundsException oob){S=false;}
            }
            if(SW){
                try{
                    if(tiles[pos.y+i][pos.x-i].cp!=null) SW = false;
                    moves.add(tiles[pos.y+i][pos.x-i]);
                }catch(ArrayIndexOutOfBoundsException oob){SW=false;}
            }
            if(W){
                try{
                    if(tiles[pos.y][pos.x-i].cp!=null) W = false;
                    moves.add(tiles[pos.y][pos.x-i]);
                }catch(ArrayIndexOutOfBoundsException oob){W=false;}
            }
            if(NW){
                try{
                    if(tiles[pos.y-i][pos.x-i].cp!=null) NW = false;
                    moves.add(tiles[pos.y-i][pos.x-i]);
                }catch(ArrayIndexOutOfBoundsException oob){NW=false;}
            }
            i++;
        }

        i=0;
        while(i<moves.size()){
            if(moves.get(i).cp!=null && moves.get(i).cp.color.equals(color)) {
                System.out.println("Removing: "+moves.get(i).position);
                moves.remove(i);
                continue;
            }
            i++;
        }


        return moves;
	}
}

//*****************************************************

class Bishop extends ChessPiece{

    public Bishop(Color c){
        name = new Label("B", Label.CENTER);
        name.setBackground(c);
        if(c.equals(Color.BLACK))
            name.setForeground(Color.WHITE);
        color = c;
    }

	@Override
	public ArrayList<BoardTile> calculateMove(BoardTile[][] tiles) {
        System.out.println("Calculating: Bishop...");
        ArrayList<BoardTile> moves = new ArrayList<BoardTile>();
        int i=1;
        boolean NE=true, SE=true, SW=true, NW=true;

        while(NE || SE || SW || NW){

            if(NE){
                try{
                    if(tiles[pos.y-i][pos.x+i].cp!=null) NE = false;
                    moves.add(tiles[pos.y - i][pos.x + i]);
                }catch(ArrayIndexOutOfBoundsException oob){NE=false;}
            }
            if(SE){
                try{
                    if(tiles[pos.y+i][pos.x+i].cp!=null) SE = false;
                    moves.add(tiles[pos.y+i][pos.x+i]);
                }catch(ArrayIndexOutOfBoundsException oob){SE=false;}
            }
            if(SW){
                try{
                    if(tiles[pos.y+i][pos.x-i].cp!=null) SW = false;
                    moves.add(tiles[pos.y+i][pos.x-i]);
                }catch(ArrayIndexOutOfBoundsException oob){SW=false;}
            }
            if(NW){
                try{
                    if(tiles[pos.y-i][pos.x-i].cp!=null) NW = false;
                    moves.add(tiles[pos.y-i][pos.x-i]);
                }catch(ArrayIndexOutOfBoundsException oob){NW=false;}
            }
            i++;
        }
        i=0;
        while(i<moves.size()){
            if(moves.get(i).cp!=null && moves.get(i).cp.color.equals(color)) {
                System.out.println("Removing: "+moves.get(i).position);
                moves.remove(i);
                continue;
            }
            i++;
        }

        return moves;
	}
}

//*****************************************************

class Knight extends ChessPiece{

    public Knight(Color c){
        name = new Label("N", Label.CENTER);
        name.setBackground(c);
        if(c.equals(Color.BLACK))
            name.setForeground(Color.WHITE);
        color = c;
    }

	@Override
	public ArrayList<BoardTile> calculateMove(BoardTile[][] tiles) {
        System.out.println("Calculating: Knight...");
        ArrayList<BoardTile> moves = new ArrayList<BoardTile>();

        try{
            moves.add(tiles[pos.y-2][pos.x+1]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y-1][pos.x+2]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y+1][pos.x+2]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y+2][pos.x+1]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y+2][pos.x-1]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y+1][pos.x-2]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y-1][pos.x-2]);
        }catch(ArrayIndexOutOfBoundsException oob){}
        try{
            moves.add(tiles[pos.y-2][pos.x-1]);
        }catch(ArrayIndexOutOfBoundsException oob){}

        int i=0;
        while(i<moves.size()){
            if(moves.get(i).cp!=null && moves.get(i).cp.color.equals(color)) {
                System.out.println("Removing: "+moves.get(i).position);
                moves.remove(i);
                continue;
            }
            i++;
        }

        return moves;
	}
}

//*****************************************************

class Rook extends ChessPiece{

    public Rook(Color c){
        name = new Label("R", Label.CENTER);
        name.setBackground(c);
        if(c.equals(Color.BLACK))
            name.setForeground(Color.WHITE);
        color = c;
    }

	@Override
	public ArrayList<BoardTile> calculateMove(BoardTile[][] tiles) {
        ArrayList<BoardTile> moves = new ArrayList<BoardTile>();
        int i=1;
        boolean N=true, E=true, S=true, W=true;

        while(N || E || S || W){

            if(N){
                try{
                    if(tiles[pos.y-i][pos.x].cp!=null) N = false;
                    moves.add(tiles[pos.y-i][pos.x]);
                }catch(ArrayIndexOutOfBoundsException oob){N=false;}
            }
            if(E){
                try{
                    if(tiles[pos.y][pos.x+i].cp!=null) E = false;
                    moves.add(tiles[pos.y][pos.x+i]);
                }catch(ArrayIndexOutOfBoundsException oob){E=false;}
            }
            if(S){
                try{
                    if(tiles[pos.y+i][pos.x].cp!=null) S = false;
                    moves.add(tiles[pos.y+i][pos.x]);
                }catch(ArrayIndexOutOfBoundsException oob){S=false;}
            }
            if(W){
                try{
                    if(tiles[pos.y][pos.x-i].cp!=null) W = false;
                    moves.add(tiles[pos.y][pos.x-i]);
                }catch(ArrayIndexOutOfBoundsException oob){W=false;}
            }
            i++;
        }

        i=0;
        while(i<moves.size()){
            if(moves.get(i).cp!=null && moves.get(i).cp.color.equals(color)) {
                System.out.println("Removing: "+moves.get(i).position);
                moves.remove(i);
                continue;
            }
            i++;
        }

        return moves;
	}
}