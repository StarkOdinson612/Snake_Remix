package com.starkodinson;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;

public class GameLogic {
    // CONSTRUCTOR: CREATES SNAKE GAME FRAME
    public GameLogic() {
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
        private final Snake snake;
        private Cell apple;
        private Cell gold;
        private final BlackHoleCell blackHole1, blackHole2;
        private final KeyHandler keyhandler;
        private final Timer timer;
        
        // CONSTRUCTOR: SETUP PANEL COLOR, SIZE, TIMER
        public MyPanel() {
            rand = new Random();
            width = 1000;
            height = 800;
            scale = 20;
            direction = "Up";
            
            snake = new Snake(height/2,width/2);
            
            int appleRow = rand.nextInt(height/scale)*scale;
            int appleCol = rand.nextInt(width/scale)*scale;
            apple = new Cell(appleRow, appleCol);
            
            gold = null;
            
            int holeRow = rand.nextInt(height/scale)*scale;
            int holeCol = rand.nextInt(width/scale)*scale;
            blackHole1 = new BlackHoleCell(holeRow, holeCol);

            int tpRow = rand.nextInt(height/scale)*scale;
            int tpCol = rand.nextInt(width/scale)*scale;
            blackHole2 = new BlackHoleCell(tpRow, tpCol);
            
            BlackHoleCell.initializePair(blackHole1, blackHole2);
            
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
            int[] head = snake.getHead();
            int headRow = head[1];
            int headCol = head[0];

            snake.newHead(direction, headRow, headCol, scale);


            if (snake.checkCoords(apple))
            {
                boolean addGold = rand.nextBoolean();

                int appleRow = rand.nextInt(height/scale)*scale;
                int appleCol = rand.nextInt(width/scale)*scale;

                while ((appleRow == apple.getRow() && appleCol == apple.getCol()) || snake.containsCell(new Cell(appleRow, appleCol)))
                {
                    appleRow = rand.nextInt(height/scale)*scale;
                    appleCol = rand.nextInt(width/scale)*scale;
                }

                apple = new Cell(appleRow, appleCol);

                if (addGold && gold == null)
                {
                    int goldRow = rand.nextInt(height/scale)*scale;
                    int goldCol = rand.nextInt(width/scale)*scale;

                    while (snake.containsCell(new Cell(goldRow, goldCol)))
                    {
                        goldRow = rand.nextInt(height/scale)*scale;
                        goldCol = rand.nextInt(width/scale)*scale;
                    }


                    gold = new Cell(goldRow, goldCol);
                }
            }
            else if (gold != null && (snake.checkCoords(gold)))
            {
                snake.newHead(direction, snake.getHead()[1], snake.getHead()[0], scale);

                System.out.println(snake);


                System.out.println("bob");
                boolean addGold = rand.nextBoolean();


                int goldRow = rand.nextInt(height/scale)*scale;
                int goldCol = rand.nextInt(width/scale)*scale;

                while ((goldRow == gold.getRow() && goldCol == gold.getCol()) || snake.containsCell(new Cell(goldRow, goldCol)) || apple.equals(gold))
                {
                    goldRow = rand.nextInt(height/scale)*scale;
                    goldCol = rand.nextInt(width/scale)*scale;
                }

                gold = addGold ? new Cell(goldRow, goldCol) : null;
            }
            else if (snake.checkCoords(blackHole1) || snake.checkCoords(blackHole2))
            {
                BlackHoleCell collided = snake.checkCoords(blackHole1) ? blackHole1 : blackHole2;
                
                int tpr = collided.getTPCoords()[1];
                int tpc = collided.getTPCoords()[0];

                snake.removeFirst();

                snake.newHead(direction, tpr, tpc, scale);

                snake.removeLast();

                int ntpr = rand.nextInt(height/scale)*scale;
                int ntpc = rand.nextInt(width/scale)*scale;

                while ((collided.getRow() == tpr && collided.getCol() == tpc) || snake.containsCell(new Cell(tpr, tpc)) || apple.equals(new Cell(tpr, tpc)) || collided.getOtherReference().equals(new Cell(tpr, tpc)))
                {
                    System.out.println(collided.getRow() == tpr && collided.getCol() == tpc);
                    System.out.println(snake.containsCell(new Cell(tpr, tpc)));
                    System.out.println(apple.equals(new Cell(tpr, tpc)));
                    System.out.println(collided.getOtherReference().equals(new Cell(tpr, tpc)));

                    if (gold != null)
                    {
                        if (gold.equals(new Cell(tpr, tpc)))
                        {
                            continue;
                        }
                    }

                    ntpr = rand.nextInt(height/scale)*scale;
                    ntpc = rand.nextInt(width/scale)*scale;
                }

                collided.getOtherReference().setCoords(ntpr, ntpc);
            }
            else if (!(snake.checkCoords(apple)))
            {
                if (gold != null)
                {
                    if (!(snake.checkCoords(gold)))
                    {
                        snake.removeLast();
                    }
                }
                else {
                    snake.removeLast();
                }
            }

            if (snake.checkBounds(new int[]{0, 1000}, new int[]{0, 800}))
            {
                timer.stop();
                System.out.println("Game Over!");
                return;
            }
            else {
                if (snake.checkContainsSelf()) {
                    timer.stop();
                    System.out.println("Game Over!");
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
            for (Cell cell : snake.returnSnakeArray()) {
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

            g.setColor(new Color(14, 0, 45));
            g.fill3DRect(blackHole1.getCol(), blackHole1.getRow(), scale-1, scale-1, true);
            g.setColor(new Color(0, 29, 130, 82));
            g.fill3DRect(blackHole1.getCol() + (scale - 2)/4 + 2, blackHole1.getRow() + (scale - 2)/4 + 2, (scale-1)/3, (scale-1)/3, false);
            g.setColor(new Color(97, 0, 151, 82));
            g.fillRect(blackHole1.getCol() + (scale - 2)/6 - 2, blackHole1.getRow() + (scale - 1)/6 - 2, 7, 2);
            g.fillRect(blackHole1.getCol() + (scale - 2)/6 - 2, blackHole1.getRow() + (scale - 1)/6, 2, 4);

            g.setColor(new Color(14, 0, 45));
            g.fill3DRect(blackHole2.getCol(), blackHole2.getRow(), scale-1, scale-1, true);
            g.setColor(new Color(0, 29, 130, 82));
            g.fill3DRect(blackHole2.getCol() + (scale - 2)/4 + 2, blackHole2.getRow() + (scale - 2)/4 + 2, (scale-1)/3, (scale-1)/3, false);
            g.setColor(new Color(97, 0, 151, 82));
            g.fillRect(blackHole2.getCol() + (scale - 2)/6 - 2, blackHole2.getRow() + (scale - 1)/6 - 2, 7, 2);
            g.fillRect(blackHole2.getCol() + (scale - 2)/6 - 2, blackHole2.getRow() + (scale - 1)/6, 2, 4);
        }
    }
}