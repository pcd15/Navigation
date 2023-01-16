import java.security.InvalidAlgorithmParameterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Models a weighted graph of latitude-longitude points
 * and supports various distance and routing operations.
 * To do: Add your name(s) as additional authors
 * @author Brandon Fain
 * @author David Ntim
 * @author Paul Dilly
 */
public class GraphProcessor {

    // Instance Variables (can change, however): 
    private static Map<Point, Set<Point>> adjacents;
    private static Map<Integer, Point> pointIndices;
    //private static Map<String, Point> cities;

    /**
     * Creates and initializes a graph from a source data
     * file in the .graph format. Should be called
     * before any other methods work.
     * @param file a FileInputStream of the .graph file
     * @throws Exception if file not found or error reading
     */

    public void initialize(FileInputStream file) throws Exception {
        if (file == null) {
            throw new Exception("Could not read .graph file");
        }
        adjacents = new HashMap<>();
        pointIndices = new HashMap<>();
        //cities = new HashMap<>();
        Scanner sc = new Scanner(file);
        int vertices = sc.nextInt();
        int edges = sc.nextInt();
        int counter = 0;
        while (counter < vertices) {
            sc.next();
            double lat = sc.nextDouble();
            double lon = sc.nextDouble();
            Point p = new Point(lat, lon);
            pointIndices.put(counter, p);
            adjacents.put(p, new HashSet<Point>());
            counter++;
        }
        counter = 0;
        while (counter < edges) {
            int point1 = sc.nextInt();
            int point2 = sc.nextInt();
            Point one = pointIndices.get(point1);
            Point two = pointIndices.get(point2);
            adjacents.get(one).add(two);
            adjacents.get(two).add(one);
            if (!sc.hasNextInt() && counter != edges - 1) {
                sc.nextLine();
            }
            counter++;
        }
        sc.close();
    }

    /**
     * Searches for the point in the graph that is closest in
     * straight-line distance to the parameter point p
     * @param p A point, not necessarily in the graph
     * @return The closest point in the graph to p
     */
    public Point nearestPoint(Point p) {
        double min = Double.MAX_VALUE;
        Point nearMe = p;
        for (Point pt : adjacents.keySet()) {
            double dist = p.distance(pt);
            if (dist < min) {
                min = dist;
                nearMe = pt;
            }
        }
        return nearMe;
    }

    /**
     * Calculates the total distance along the route, summing
     * the distance between the first and the second Points, 
     * the second and the third, ..., the second to last and
     * the last. Distance returned in miles.
     * @param start Beginning point. May or may not be in the graph.
     * @param end Destination point May or may not be in the graph.
     * @return The distance to get from start to end
     */
    public double routeDistance(List<Point> route) {
        double retVal = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            retVal += route.get(i).distance(route.get(i + 1));
        }
        return retVal;
    }

    /**
     * Checks if input points are part of a connected component
     * in the graph, that is, can one get from one to the other
     * only traversing edges in the graph
     * @param p1 one point
     * @param p2 another point
     * @return true if p2 is reachable from p1 (and vice versa)
     */
    public boolean connected(Point p1, Point p2) {
        if (!adjacents.containsKey(p1) || !adjacents.containsKey(p2)) { return false; }
        Set<Point> visited = new HashSet<>();
        Stack<Point> s = new Stack<>();
        s.push(p1);
        visited.add(p1);
        while (!s.isEmpty()) {
            Point current = s.pop();
            for (Point p : adjacents.get(current)) {
                if (p.equals(p2)) { return true; }
                if (!visited.contains(p)) {
                    visited.add(p);
                    s.push(p);
                }
            }
        }
        return false;
    }

    /**
     * Returns the shortest path, traversing the graph, that begins at start
     * and terminates at end, including start and end as the first and last
     * points in the returned list. If there is no such route, either because
     * start is not connected to end or because start equals end, throws an
     * exception.
     * @param start Beginning point.
     * @param end Destination point.
     * @return The shortest path [start, ..., end].
     * @throws InvalidAlgorithmParameterException if there is no such route, 
     * either because start is not connected to end or because start equals end.
     */
    public List<Point> route(Point start, Point end) throws InvalidAlgorithmParameterException {
        if (!connected(start, end) || start.equals(end) || !adjacents.containsKey(start) || !adjacents.containsKey(end)) {
            throw new InvalidAlgorithmParameterException("No path between start and end");
        }
        Map<Point, Point> previous = new HashMap<>();
        Map<Point, Double> distance = new HashMap<>();
        Comparator<Point> comp = (a, b) -> Double.compare(distance.get(a), distance.get(b));
        PriorityQueue<Point> pq = new PriorityQueue<>(comp);
        distance.put(start, (double) 0);
        pq.add(start);
        while (!pq.isEmpty()) {
            Point current = pq.poll();
            for (Point adj : adjacents.get(current)) {
                double dist = current.distance(adj) + distance.get(current);
                if ((!distance.containsKey(adj) || (dist + 0.1 < distance.get(adj)))) {
                    distance.put(adj, dist);
                    previous.put(adj, current);
                    pq.add(adj);
                }
            }
        }
        List<Point> list = new LinkedList<>();
        Point p = end;
        list.add(end);
        while (!p.equals(start)) {
            p = previous.get(p);
            list.add(0, p);
        }
        return new ArrayList<>(list);
    }
}
