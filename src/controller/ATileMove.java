package controller;

import java.awt.Point;

/**
 * Groups information regarding the past and present position and value for a Tile as well as if the Tile was formed from merging Tiles, or if the Tile is to be
 * deleted. Convention assumed: The point information is to be in (x,y) coordinates where (0,0) refers to the top-left cell, while (1,2) would be one cell down and
 * two cells to the right.
 */
public class ATileMove {
    private Point curLoc, prevLoc;
    private int curVal, prevVal;
    private boolean merged, deleted;

    /**
     * Constructs this tile from the given point and value.
     * @param loc The location of this Tile.
     * @param val The value of this Tile.
     */
    public ATileMove(Point loc, int val) {
      curLoc = loc;
      curVal = val;
      prevLoc = null;
      prevVal = 0;
      merged = deleted = false;
    }

    /**
     * Returns the current location of this Tile.
     * @return The current location of this Tile.
     */
    public Point getCurLoc() {
	return curLoc;
    }

    /**
     * Returns the current value of this Tile.
     * @return The current value of this Tile.
     */
    public int getCurVal() {
	return curVal;
    }

    /**
     * Returns the previous location of this Tile.
     * @return The previous location of this Tile
     */
    public Point getPrevLoc() {
	return prevLoc;
    }

    /**
     * Returns the previous value of this Tile.
     * @return The previous value of this Tile
     */
    public int getPrevVal() {
	return prevVal;
    }

    /**
     * Updates the current location of this Tile to the one provided.
     * @param target The new location for this Tile.
     */
    public void move(Point target) {
      prevLoc = curLoc;
      curLoc = new Point(target.x, target.y);
      prevVal = curVal;
    }

    /**
     * Updates the current location and value of this Tile to the information provided and marks the Tile as being merged.
     * @param target The new location of this Tile.
     * @param val The new value of this Tile.
     */
    public void merge(Point target, int val) {
      prevLoc = curLoc;
      curLoc = new Point(target.x, target.y);
      prevVal = curVal;
      curVal = val;
      merged = true;
    }

    /**
     * Marks this Tile as being deleted.
     */
    public void delete() {
      prevVal = curVal;
      curVal = 0;
      deleted = true;
    }

    /**
     * Mark this Tile as being merged.
     */
    public void merged() {
	merged = true;
    }

    /**
     * Returns whether or not this Tile has been marked as merged or not.
     * @return True if this Tile has been marked as merged, false otherwise.
     */
    public boolean isMerged() {
	return merged;
    }

    /**
     * Returns whether or not this Tile has been marked as deleted or not.
     * @return True if this Tile has been marked as deleted, false otherwise.
     */
    public boolean isDeleted() {
	return deleted;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof ATileMove)) return false;
      ATileMove that = (ATileMove) obj;
      if (this.curVal != that.curVal) return false;
      if (!this.curLoc.equals(that.curLoc)) return false;
      if (this.prevVal != that.prevVal) return false;
      if (this.prevLoc == null) {
          if (that.prevLoc != null) return false;
      } else if (!this.prevLoc.equals(that.prevLoc)) return false;
      return true;
    }

}