package io.github.transport_tycoon.model;


public class StopTile {

    private Tile tile;

    private Zone linkedZone;


    public StopTile(Tile tile, Zone linkedZone) {
        this.tile = tile;
        this.linkedZone = linkedZone;
    }
    public StopTile() {}

    public Tile getTile() {
        return tile;
    }

    public Zone getLinkedZone() {
        return linkedZone;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public void setLinkedZone(Zone zone) {
        this.linkedZone = zone;
    }
}
