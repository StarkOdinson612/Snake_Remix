package com.starkodinson;

import java.util.*;
import java.util.stream.Collectors;

public class Snake {
    private Deque<Cell> snakeDeque;
    
    public Snake(int row, int col)
    {
        this.snakeDeque = new LinkedList<>();
        this.snakeDeque.add(new Cell(row, col));
    }
    
    public void newHead(String dir, int row, int col, int scale)
    {
        switch (dir) {
            case "up": {
                Cell newHead = new Cell(row - scale, col);
                snakeDeque.addFirst(newHead);
                break;
            }
            case "down": {
                Cell newHead = new Cell(row + scale, col);
                snakeDeque.addFirst(newHead);
                break;
            }
            case "left": {
                Cell newHead = new Cell(row, col - scale);
                snakeDeque.addFirst(newHead);
                break;
            }
            case "right": {
                Cell newHead = new Cell(row, col + scale);
                snakeDeque.addFirst(newHead);
                break;
            }
        }

    }
    
    public void removeFirst() { snakeDeque.removeFirst(); }
    
    public void removeLast() { snakeDeque.removeLast(); }
    
    public int[] getHead() { 
        Cell head = snakeDeque.getFirst();
        
        return new int[]{head.getCol(), head.getRow()};
    }
    
    public boolean checkCoords(Cell other)
    {
        return snakeDeque.getFirst().equals((Cell) other);
    }
    
    public boolean containsCell(Cell other) { return snakeDeque.contains(other); }
    
    public boolean checkBounds(int[] xBounds, int[] yBounds)
    {
        int[] headCoords = this.getHead();

        return headCoords[0] > xBounds[1] || headCoords[1] > yBounds[1] || headCoords[1] < xBounds[0] || headCoords[0] < yBounds[0];
    }

    public boolean checkContainsSelf()
    {
        Deque<Cell> dupeSnake = new LinkedList<>(snakeDeque);
        dupeSnake.removeFirst();
        return dupeSnake.contains(snakeDeque.getFirst());
    }

    public List<Cell> returnSnakeArray() { return new ArrayList<>(snakeDeque); }
}
