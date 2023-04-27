package dev.benjaminc.twfe;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Game {

    public static void main(String args[]) {
        new Game();
    }

    private int width = 4;
    private int height = 4;
    private int numbers[][];
    private NumberLabel boxes[][];
    private int boxSize = 256;
    private double twoChance = 0.75;
    private int debugAnimSpeed = 1/60;
    private Object gosyncro = new Object();
    private Object kpsyncro = new Object();
    private Direction move;
    
    public Game() {
        ColorMap.initColorMap();
        numbers = new int[height][width];
        boxes = new NumberLabel[height][width];

        JFrame jf = new JFrame();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jp =  new JPanel();
        jp.setLayout(new GridLayout(4, 4));
        // Init visual boxes and number array
        int next = 0;
        for(int r = 0; r < height; r++) {
            for(int c = 0; c < width; c++) {
                NumberLabel jl = new NumberLabel();
                boxes[r][c] = jl;
                jp.add(jl);
                numbers[r][c] = 0;
                // next *= 2;
            }
        }
        restartGame();

        regenBoxes();

        jf.add(jp);
        jf.pack();
        jf.setVisible(true);

        jf.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                // System.out.println("keyPressed");
            }
        
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_RIGHT) {
                    move = Direction.RIGHT;
                } else if(e.getKeyCode()== KeyEvent.VK_LEFT) {
                    move = Direction.LEFT;
                } else if(e.getKeyCode()== KeyEvent.VK_DOWN) {
                    move = Direction.DOWN;
                } else if(e.getKeyCode()== KeyEvent.VK_UP) {
                    move = Direction.UP;
                } else {
                    return;
                }
                synchronized(kpsyncro) {
                    kpsyncro.notify();
                }
            }
            public void keyTyped(KeyEvent e) {
                // System.out.println("keyTyped");
            }
        });

        System.out.println("Hello world");

        while(true) {
            playGame();
        }

        // while(true) {
        //     int fn = 0;
        //     boolean dead = false;
        //     while(!dead) {
        //         slide(Direction.RIGHT);
        //         dead = addNumber();
        //         regenBoxes();
        //         BufferedImage img = new BufferedImage(jf.getWidth(), jf.getHeight(), BufferedImage.TYPE_INT_RGB);
        //         jf.paint(img.getGraphics());
        //         File outputfile = new File("sc/sc" + fn++ + ".png");
        //         try {
        //             ImageIO.write(img, "png", outputfile);
        //         } catch (IOException e) {
        //             // TODO Auto-generated catch block
        //             e.printStackTrace();
        //         }
        //         try {
        //             Thread.sleep(2000);
        //         } catch (InterruptedException e) {
        //             e.printStackTrace();
        //         }
        //         clearMarkers();
        //     }

        //     gameOver();

        //     synchronized(gosyncro) {
        //         try {
        //             gosyncro.wait();
        //         } catch (InterruptedException e) {
        //             // TODO Auto-generated catch block
        //             e.printStackTrace();
        //         }
        //     }
            
        //     restartGame();
        // }
    }

    public void playGame() {
        boolean alive = true;
        while(alive) {
            // wait for key
            synchronized(kpsyncro) {
                try {
                    kpsyncro.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(move != null) {
                clearMarkers();
                slide(move);
                alive = addNumber();
                regenBoxes();
            }
        }
        gameOver();
        synchronized(gosyncro) {
            try {
                gosyncro.wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void clearMarkers() {
        for(int r = 0; r < height; r++) {
            for(int c = 0; c < width; c++) {
                boxes[r][c].setMarkerColor(null);
            }
        }
    }

    public void regenBoxes() {
        for(int r = 0; r < height; r++) {
            for(int c = 0; c < width; c++) {
                NumberLabel jl = boxes[r][c];
                jl.setNum(numbers[r][c]);
                jl.setFont(jl.getFont().deriveFont(64.f));
                jl.setSize(boxSize, boxSize);
                jl.setMinimumSize(new Dimension(boxSize, boxSize));
                jl.setPreferredSize(new Dimension(boxSize, boxSize));
                jl.setMaximumSize(new Dimension(boxSize, boxSize));
            }
        }
    }

    public int numPos(Position p) {
        return numbers[p.getR()][p.getC()];
    }

    public void repaintAll() {
        for(int r = 0; r < height; r++) {
            for(int c = 0; c < width; c++) {
                boxes[r][c].repaint();
            }
        }
    }

    public boolean slide(Direction dir) {
        // Scan each row
        for(int sc = 0; sc < height; sc++) {
            for(int sr = width-1; sr > 0; sr--) {
                for(int st = sr-1; st >= 0; st--) {
                    boolean slid = false;
                    // Apply rotation
                    int r = dir.convertMovement(sr, sc, width, height).getR();
                    int c = dir.convertMovement(sr, sc, width, height).getC();
                    int tr = dir.convertMovement(st, sc, width, height).getR();
                    int tc = dir.convertMovement(st, sc, width, height).getC();
                    if(debugAnimSpeed > 0) {
                        boxes[r][c].setMarkerColor(Color.MAGENTA);
                        boxes[tr][tc].setMarkerColor(Color.BLUE);
                        repaintAll();
                        regenBoxes();
                    }
                    try {
                        Thread.sleep(debugAnimSpeed);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(numbers[r][c] > 0) {
                        if(numbers[tr][tc] == numbers[r][c]) {
                            // Merge numbers
                            numbers[r][c]++;
                            numbers[tr][tc] = 0;
                            boxes[r][c].setMarkerColor(Color.GREEN);
                            System.out.println("Merged");
                            if(debugAnimSpeed > 0) {
                                boxes[r][c].setMarkerColor(null);
                                boxes[tr][tc].setMarkerColor(null);
                                repaintAll();
                                regenBoxes();
                            }
                            break;
                        } else if(numbers[tr][tc] > 0) {
                            System.out.println(numbers[tr][tc] + "?" + numbers[r][c]);
                            if(debugAnimSpeed > 0) {
                                boxes[r][c].setMarkerColor(null);
                                boxes[tr][tc].setMarkerColor(null);
                                repaintAll();
                                regenBoxes();
                            }
                            break;
                        }
                    } else {
                        if(numbers[tr][tc] > 0) {
                            // Shift numbers
                            numbers[r][c] = numbers[tr][tc];
                            numbers[tr][tc] = 0;
                            boxes[r][c].setMarkerColor(Color.LIGHT_GRAY);
                            slid = true;
                        }
                    }
                    if(debugAnimSpeed > 0) {
                        boxes[r][c].setMarkerColor(null);
                        boxes[tr][tc].setMarkerColor(null);
                        repaintAll();
                        regenBoxes();
                    }
                }
            }
        }

        return false;
    }

    public void restartGame() {
        for(int r = 0; r < height; r++) {
            for(int c = 0; c < width; c++) {
                numbers[r][c] = 0;
                boxes[r][c].setMarkerColor(null);
            }
        }
        regenBoxes();
    }

    public void gameOver() {
        System.out.println("Game Over");
        JDialog jd = new JDialog();
        JButton jb = new JButton("Game over!");
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
                synchronized(gosyncro) {
                    gosyncro.notify();
                }
                jd.dispose();
            }
        });
        jd.add(jb);
        jd.pack();
        jd.setVisible(true);
    }

    public int countFilledBoxes() {
        int count = 0;
        for(int r = 0; r < height; r++) {
            for(int c = 0; c < width; c++) {
                if(numbers[r][c] > 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean addNumber() {
        // Find all possible places to put a number
        List<Position> opts = new ArrayList<Position>();
        for(int r = 0; r < height; r++) {
            for(int c = 0; c < width; c++) {
                if(numbers[r][c] <= 0) {
                    opts.add(new Position(r, c));
                }
            }
        }

        // Put in a number, or say you can't
        if(opts.size() > 0) {
            Random rand = new Random();
            int num = (rand.nextDouble() < twoChance) ? 1 : 2;
            int choice = rand.nextInt(opts.size());
            numbers[opts.get(choice).getR()][opts.get(choice).getC()] = num;
            boxes[opts.get(choice).getR()][opts.get(choice).getC()].setMarkerColor(Color.MAGENTA);;
            return true;
        } else {
            return false;
        }
    }
}
