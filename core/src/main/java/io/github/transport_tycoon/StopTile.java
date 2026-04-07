package io.github.transport_tycoon;


public class StopTile {

    private Tile tile;

    private Zone linkedZone;


    public StopTile(Tile tile, Zone linkedZone) {
        this.tile = tile;
        this.linkedZone = linkedZone;
    }

    public Tile getTile() {
        return tile;
    }

    public Zone getLinkedZone() {
        return linkedZone;
    }
}
