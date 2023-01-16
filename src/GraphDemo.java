import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidAlgorithmParameterException;
import java.util.List;
import java.util.Scanner;

/**
 * Demonstrates the calculation of shortest paths in the US Highway
 * network, showing the functionality of GraphProcessor and using
 * Visualize
 * 
 * @author David Ntim
 * @author Paul Dilly
 */

//import java.util.*;

public class GraphDemo {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        /*System.out.println("Enter the name of a city followed by its state's abbreviation, separated by a space (e.g., Durham NC): ");
        String c1 = input.nextLine();
        System.out.println("Enter the name of another city followed by its state's abbreviation, separated by a space (e.g., Durham NC): ");
        String c2 = input.nextLine();*/
        System.out.println("Enter Latitude of City 1: ");
        double lat1 = input.nextDouble();
        System.out.println("Enter Longitude of City 1: ");
        double lon1 = input.nextDouble();
        System.out.println("Enter Latitude of City 2: ");
        double lat2 = input.nextDouble();
        System.out.println("Enter Longitude of City 2: ");
        double lon2 = input.nextDouble();
        Point city1 = new Point(lat1, lon1);
        Point city2 = new Point(lat2, lon2);
        GraphProcessor graph = new GraphProcessor();
        FileInputStream usa;
        //Point city1;
        //Point city2;
        try { 
            usa = new FileInputStream("data/usa.graph");
            graph.initialize(usa); 
            //graph.cityMap("data/uscities.csv");
            //city1 = graph.cityCoordinates(c1);
            //city2 = graph.cityCoordinates(c2);

        }
        catch (Exception e1) { System.out.println(e1.getMessage()); return; }
        long before = System.nanoTime();
        Point nearest1 = graph.nearestPoint(city1);
        Point nearest2 = graph.nearestPoint(city2);
        List<Point> shortestRoute;
        double dist;
            shortestRoute = graph.route(nearest1, nearest2); 
            dist = graph.routeDistance(shortestRoute);
        long after = System.nanoTime();
        System.out.println("City 1's Closest Vertex in Road Network: " + nearest1);
        System.out.println("City 2's Closest Vertex in Road Network: " + nearest2);
        System.out.println("Shortest route between " + nearest1 + " and " + nearest2 + ": " + dist + " mi");
        System.out.println("Total time to get nearest points, route, and distance: " + ((after - before) / 1000000) + " ms");
        Visualize vis = new Visualize("data/usa.vis", "images/usa.png");
        vis.drawRoute(shortestRoute);
    }
}
