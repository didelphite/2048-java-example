package dev.benjaminc.twfe;

public class Position {

    private int r,c;

    public Position(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public int getR() {
        return r;
    }
    
    public int getC() {
        return c;
    }

    public void setPos(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public void incR() {
        r++;
    }

    public void intC() {
        c++;
    }

    public void addR(int a) {
        r += a;
    }

    public void addC(int a) {
        c += a;
    }
}