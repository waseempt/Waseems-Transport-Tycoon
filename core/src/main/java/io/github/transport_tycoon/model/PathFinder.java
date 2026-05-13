package io.github.transport_tycoon.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class PathFinder {

    public static List<Tile> findPath(GameMap map, Tile start, Tile end) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fScore));
        Map<Tile, Node> allNodes = new HashMap<>();

        Node startNode = new Node(start, null, 0, getHeuristic(start, end));
        openSet.add(startNode);
        allNodes.put(start, startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.tile == end) {
                return reconstructPath(current);
            }

            for (Tile neighbor : getNeighbors(map, current.tile)) {
                // Valid tiles are roads, or the start/end stop tiles themselves
                if (!neighbor.hasRoad() && neighbor != end && neighbor != start) continue;

                double tentativeGScore = current.gScore + 1;

                Node neighborNode = allNodes.getOrDefault(neighbor, new Node(neighbor));

                if (tentativeGScore < neighborNode.gScore) {
                    neighborNode.cameFrom = current;
                    neighborNode.gScore = tentativeGScore;
                    neighborNode.fScore = tentativeGScore + getHeuristic(neighbor, end);

                    if (!openSet.contains(neighborNode)) {
                        openSet.add(neighborNode);
                        allNodes.put(neighbor, neighborNode);
                    }
                }
            }
        }
        return new ArrayList<>(); // No path found
    }

    private static List<Tile> getNeighbors(GameMap map, Tile tile) {
        List<Tile> neighbors = new ArrayList<>();
        int x = tile.getGridX();
        int y = tile.getGridY();

        if (map.getTile(x + 1, y) != null) neighbors.add(map.getTile(x + 1, y));
        if (map.getTile(x - 1, y) != null) neighbors.add(map.getTile(x - 1, y));
        if (map.getTile(x, y + 1) != null) neighbors.add(map.getTile(x, y + 1));
        if (map.getTile(x, y - 1) != null) neighbors.add(map.getTile(x, y - 1));

        return neighbors;
    }

    private static double getHeuristic(Tile a, Tile b) {
        // Manhattan distance calculation
        return Math.abs(a.getGridX() - b.getGridX()) + Math.abs(a.getGridY() - b.getGridY());
    }

    private static List<Tile> reconstructPath(Node endNode) {
        List<Tile> path = new ArrayList<>();
        Node current = endNode;
        while (current != null) {
            path.add(current.tile);
            current = current.cameFrom;
        }
        Collections.reverse(path);
        return path;
    }

    private static class Node {
        Tile tile;
        Node cameFrom;
        double gScore = Double.MAX_VALUE;
        double fScore = Double.MAX_VALUE;

        Node(Tile tile) { this.tile = tile; }
        Node(Tile tile, Node cameFrom, double gScore, double fScore) {
            this.tile = tile;
            this.cameFrom = cameFrom;
            this.gScore = gScore;
            this.fScore = fScore;
        }
    }
}
