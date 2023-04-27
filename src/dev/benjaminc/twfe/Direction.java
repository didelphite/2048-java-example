package dev.benjaminc.twfe;

public enum Direction {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    int ad, bc;

    private Direction(int ad, int bc) {
        this.ad = ad;
        this.bc = bc;
    }

    public int getAD() {
        return ad;
    }

    public int getBC() {
        return bc;
    }

    public Position convertMovement(Position pos) {
        return convertMovement(pos.getR(), pos.getC());
    }

    public Position convertMovement(int r, int c) {
        return new Position((r * ad) + (c * bc), (c * ad) + (r * bc));
    }

    public Position convertMovement(int r, int c, int w, int h) {
        int nr = (r * ad) + (c * bc) + ((ad < 0) ? -1 : 0);
        int nc = (c * ad) + (r * bc) + ((bc < 0) ? -1 : 0);
        while(nc < 0) {
            nc += w;
        }
        while(nc >= w) {
            nc -= w;
        }
        while(nr < 0) {
            nr += h;
        }
        while(nr >= h) {
            nr -= h;
        }
        return new Position(nr, nc);
    }
}
