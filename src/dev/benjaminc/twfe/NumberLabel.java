package dev.benjaminc.twfe;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class NumberLabel extends JLabel {
    private int num;
    private boolean inited = false;
    private Color markerColor = null;

    public NumberLabel() {
        super("", SwingConstants.CENTER);
        inited = true;
        setNum(0);
    }

    public void setMarkerColor(Color c) {
        markerColor = c;
        if(getGraphics() != null) {
            this.paint(getGraphics());
        }
    }

    public void setNum(int num) {
        this.num = num;
        if(num > 0) {
            super.setText(Integer.toString((int) Math.pow(2, num)));
        } else {
            super.setText("");
        }
    }

    public int getNum() {
        return num;
    }

    @Override
    public void setText(String text) {
        if(inited) {
            throw new UnsupportedOperationException("Please use setNum to set the number of a NumberLabel (attempted text was \"" + text + "\")");
        } else {
            super.setText(text);
        }
    }

    public void paint(Graphics g) {
        int width  = getWidth();
        int height = getHeight();
        Color c = ColorMap.getNumberColor(num);
        if(c == Color.WHITE) {
            System.out.println("Found invalid color");
        }
        g.setColor(c);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, width, height);
        if(markerColor != null) {
            g.setColor(markerColor);
            for(int i = 0; i < 5; i++) {
                g.drawRect(i, i, width-(2*i), height-(2*i));
            }
        }
        super.paint(g);
    }
}
