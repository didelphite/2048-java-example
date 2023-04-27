package dev.benjaminc.twfe;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ColorMap {

    public static Map<Integer, Color> colorMap;

    public static void initColorMap() {
        colorMap = new HashMap<Integer, Color>();
        colorMap.put(0, Color.BLACK);
        // red
        colorMap.put(1,  new Color(255, 0, 0));
        colorMap.put(2,  new Color(255, 64, 0));
        // orange
        colorMap.put(3,  new Color(255, 128, 0));
        colorMap.put(4,  new Color(255, 192, 0));
        // yellow
        colorMap.put(5,  new Color(255, 255, 0));
        colorMap.put(6,  new Color(128, 255, 0));
        // green
        colorMap.put(7,  new Color(0, 255, 0));
        colorMap.put(8,  new Color(0, 255, 128));
        // blue
        colorMap.put(9,  new Color(0, 255, 255));
        colorMap.put(10, new Color(0, 128, 255));
        // purple
        colorMap.put(11, new Color(128, 128, 255));
        colorMap.put(12, new Color(255, 0, 255));
    }

    public static Color getNumberColor(int num) {
        if(colorMap.containsKey(num)) {
            return colorMap.get(num);
        } else {
            return new Color(255, 255, 255);
        }
    }
}
