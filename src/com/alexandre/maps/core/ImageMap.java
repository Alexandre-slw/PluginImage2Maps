package com.alexandre.maps.core;

import java.awt.*;
import java.util.ArrayList;

public class ImageMap {

    private final int id;
    private final String path;
    private final ArrayList<Short> ids;
    private final Color background;
    private final short previewID;
    private final int col;
    private final int row;
    
    public ImageMap(int id, String path, ArrayList<Short> ids, short previewID, Color background, int col, int row) {
        this.id = id;
        this.path = path;
        this.ids = ids;
        this.background = background;
        this.previewID = previewID;
        this.col = col;
        this.row = row;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String getPath() {
        return this.path;
    }
    
    public ArrayList<Short> getIDs() {
        return this.ids;
    }
    
    public Color getBackground() {
        return this.background;
    }
    
    public short getPreviewID() {
        return this.previewID;
    }
    
    public int getCol() {
        return this.col;
    }
    
    public int getRow() {
        return this.row;
    }
}
