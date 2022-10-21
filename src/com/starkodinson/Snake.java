package com.starkodinson;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

public class Snake {
    // MAIN METHOD: CALLS CONSTRUCTOR
    public static void main(String[] args) {
        new Snake();
    }
    // CONSTRUCTOR: CREATES SNAKE GAME FRAME
    public Snake() {
        JFrame frame = new JFrame("Snake Game");
        JPanel panel = new MyPanel();
        frame.add(panel);
        frame.pack(); frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }
    // PANEL: RUNS THE GAME IN THE FRAME
    public static class MyPanel extends JPanel implements ActionListener {
        // CLASS VARIABLES
        private final Random rand;
        private final int width;
        private final int height;
        private final int scale;
        private String direction;
        private final Deque<Cell> snake;
        private Cell apple;
        private Cell gold;
        private final BlackHoleCell blackHole;
        private final KeyHandler keyhandler;
        private final Timer timer;
        
        // CONSTRUCTOR: SETUP PANEL COLOR, SIZE, TIMER
        public MyPanel() {
            rand = new Random();
            width = 1000;
            height = 800;
            scale = 20;
            direction = "Up";
            snake = new LinkedList<>();
            snake.add(new Cell(height/2,width/2));
            int appleRow = rand.nextInt(height/scale)*scale;
            int appleCol = rand.nextInt(width/scale)*scale;
            apple = new Cell(appleRow, appleCol);
            gold = null;
            int holeRow = rand.nextInt(height/scale)*scale;
            int holeCol = rand.nextInt(width/scale)*scale;
            int tpRow = rand.nextInt(height/scale)*scale;
            int tpCol = rand.nextInt(width/scale)*scale;
            blackHole = new BlackHoleCell(holeRow, holeCol, tpRow, tpCol);
            keyhandler = new KeyHandler();
            addKeyListener(keyhandler);
            setFocusable(true);
            setBackground(new Color(0,0,0));
            setPreferredSize(new Dimension(width,height));
            timer = new Timer(100,this);
            timer.start();
        }
        
        // ACTION PERFORMED: CALLED BY TIMER
        public void actionPerformed(ActionEvent e) {
            // GET CURRENT DIRECTION FROM KEYS
            direction = keyhandler.getDirection().toLowerCase();
            Cell head = snake.getFirst();
            int headRow = head.getRow();
            int headCol = head.getCol();

            switch (direction) {
                case "up": {
                    Cell newHead = new Cell(headRow - scale, headCol);
                    snake.addFirst(newHead);
                    break;
                }
                case "down": {
                    Cell newHead = new Cell(headRow + scale, headCol);
                    snake.addFirst(newHead);
                    break;
                }
                case "left": {
                    Cell newHead = new Cell(headRow, headCol - scale);
                    snake.addFirst(newHead);
                    break;
                }
                case "right": {
                    Cell newHead = new Cell(headRow, headCol + scale);
                    snake.addFirst(newHead);
                    break;
                }
            }


            if (snake.getFirst().getCol() == apple.getCol() && snake.getFirst().getRow() == apple.getRow())
            {
                boolean addGold = rand.nextBoolean();

                int appleRow = rand.nextInt(height/scale)*scale;
                int appleCol = rand.nextInt(width/scale)*scale;

                while ((appleRow == apple.getRow() && appleCol == apple.getCol()) || snake.contains(new Cell(appleRow, appleCol)))
                {
                    appleRow = rand.nextInt(height/scale)*scale;
                    appleCol = rand.nextInt(width/scale)*scale;
                }

                apple = new Cell(appleRow, appleCol);

                if (addGold && gold == null)
                {
                    int goldRow = rand.nextInt(height/scale)*scale;
                    int goldCol = rand.nextInt(width/scale)*scale;

                    while (snake.contains(new Cell(goldRow, goldCol)))
                    {
                        goldRow = rand.nextInt(height/scale)*scale;
                        goldCol = rand.nextInt(width/scale)*scale;
                    }


                    gold = new Cell(goldRow, goldCol);
                }
            }
            else if (gold != null && (snake.getFirst().getCol() == gold.getCol() && snake.getFirst().getRow() == gold.getRow()))
            {
                switch (direction) {
                    case "up": {
                        Cell newHead = new Cell(headRow - scale * 2, headCol);
                        snake.addFirst(newHead);
                        break;
                    }
                    case "down": {
                        Cell newHead = new Cell(headRow + scale * 2, headCol);
                        snake.addFirst(newHead);
                        break;
                    }
                    case "left": {
                        Cell newHead = new Cell(headRow, headCol - scale * 2);
                        snake.addFirst(newHead);
                        break;
                    }
                    case "right": {
                        Cell newHead = new Cell(headRow, headCol + scale * 2);
                        snake.addFirst(newHead);
                        break;
                    }
                }

                System.out.println(snake);


                System.out.println("bob");
                boolean addGold = rand.nextBoolean();


                int goldRow = rand.nextInt(height/scale)*scale;
                int goldCol = rand.nextInt(width/scale)*scale;

                while ((goldRow == gold.getRow() && goldCol == gold.getCol()) || snake.contains(new Cell(goldRow, goldCol)))
                {
                    goldRow = rand.nextInt(height/scale)*scale;
                    goldCol = rand.nextInt(width/scale)*scale;
                }

                gold = addGold ? new Cell(goldRow, goldCol) : null;
            }
            else if (!(snake.getFirst().getCol() == apple.getCol() && snake.getFirst().getRow() == apple.getRow()))
            {
                if (gold != null)
                {
                    if (!(snake.getFirst().getCol() == gold.getCol() && snake.getFirst().getRow() == gold.getRow()))
                    {
                        snake.removeLast();
                    }
                }
                else {
                    snake.removeLast();
                }
            }

            if (snake.getFirst().getCol() > 1000 || snake.getFirst().getRow() > 800 || snake.getFirst().getRow() < 0 || snake.getFirst().getCol() < 0)
            {
                timer.stop();
                return;
            }
            else {
                Deque<Cell> dupeSnake = new LinkedList<>(snake);
                dupeSnake.removeFirst();

                if (dupeSnake.contains(snake.getFirst())) {
                    timer.stop();
                    return;
                }
            }

            // REPAINT THE SCREEN
            repaint();
        }
        
        // PAINT COMPONENT: DRAW SNAKE AND FOOD
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // DRAW ALL BLACK
            g.setColor(new Color(2, 90, 0, 188));
            g.fillRect(0, 0, width, height);
            
            // DRAW GREEN SQUARES
            g.setColor(new Color(3, 101, 0));
            for(int r=0; r<height; r+=scale) {
                for(int c=0; c<width; c+=scale) {
                    g.fillRect(c, r, scale-1, scale-1);
                }
            }
            
            // DRAW SNAKE
            g.setColor(new Color(255,255,255));
            for (Cell cell : snake) {
                g.fill3DRect(cell.getCol(), cell.getRow(), scale - 1, scale - 1, true);
            }
            
            // DRAW APPLE
            g.setColor(new Color(198,0,0));
            g.fill3DRect(apple.getCol(), apple.getRow(), scale-1, scale-1, true);
            g.setColor(new Color(37, 154, 0));
            g.fill3DRect(apple.getCol() + (scale - 2)/4 + 2, apple.getRow() + (scale - 2)/4 + 2, (scale-1)/3, (scale-1)/3, true);
            g.setColor(new Color(255, 255, 255, 109));
            g.fillRect(apple.getCol() + (scale - 2)/6 - 2, apple.getRow() + (scale - 1)/6 - 2, 7, 2);
            g.fillRect(apple.getCol() + (scale - 2)/6 - 2, apple.getRow() + (scale - 1)/6, 2, 4);

            if (gold != null)
            {
                g.setColor(new Color(255, 185,0));
                g.fill3DRect(gold.getCol(), gold.getRow(), scale-1, scale-1, true);
                g.setColor(new Color(255, 255, 255, 153));
                g.fill3DRect(gold.getCol() + (scale - 2)/4 + 2, gold.getRow() + (scale - 2)/4 + 2, (scale-1)/3, (scale-1)/3, true);
                g.setColor(new Color(255, 255, 255, 109));
                g.fillRect(gold.getCol() + (scale - 2)/6 - 2, gold.getRow() + (scale - 1)/6 - 2, 7, 2);
                g.fillRect(gold.getCol() + (scale - 2)/6 - 2, gold.getRow() + (scale - 1)/6, 2, 4);
            }
            
            if (blackHole != null)
            {
                g.setColor(new Color(14, 0, 45));
                g.fill3DRect(blackHole.getCol(), blackHole.getRow(), scale-1, scale-1, true);
                g.setColor(new Color(0, 21, 93, 153));
                g.fill3DRect(blackHole.getCol() + (scale - 2)/4 + 2, blackHole.getRow() + (scale - 2)/4 + 2, (scale-1)/3, (scale-1)/3, true);
                g.setColor(new Color(44, 0, 70, 109));
                g.fillRect(blackHole.getCol() + (scale - 2)/6 - 2, blackHole.getRow() + (scale - 1)/6 - 2, 7, 2);
                g.fillRect(blackHole.getCol() + (scale - 2)/6 - 2, blackHole.getRow() + (scale - 1)/6, 2, 4);
            }
        }
    }
}