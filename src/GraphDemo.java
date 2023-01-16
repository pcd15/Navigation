import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidAlgorithmParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Demonstrates the calculation of shortest paths in the US Highway
 * network between two given cities, showcasing the functionality of GraphProcessor and using
 * Visualize to illustrate the route on an image of the US
 * 
 * @author David Ntim
 * @author Paul Dilly
 */

public class GraphDemo {

    public static Map<String, Point> cities;
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the name of a city followed by its state's abbreviation, separated by a space (e.g., Durham NC): ");
        String name1 = input.nextLine();
        System.out.print("Enter the name of another city followed by its state's abbreviation, separated by a space (e.g., Durham NC): ");
        String name2 = input.nextLine();
        input.close();
        createCityMap("data/uscities.csv");
        GraphProcessor graph = new GraphProcessor();
        FileInputStream usa = new FileInputStream("data/usa.graph");
        graph.initialize(usa); 
        Point city1 = cities.get(name1);
        Point city2 = cities.get(name2);
        long before = System.nanoTime();
        Point nearest1 = graph.nearestPoint(city1);
        Point nearest2 = graph.nearestPoint(city2);
        List<Point> shortestRoute = graph.route(nearest1, nearest2); 
        double dist = graph.routeDistance(shortestRoute);
        long after = System.nanoTime();
        System.out.println("Nearest point to " + name1 + ": " + nearest1);
        System.out.println("Nearest point to " + name2 + ": " + nearest2);
        System.out.println("Shortest route from " + nearest1 + " to " + nearest2 + ": " + dist + " mi");
        System.out.println("Total time to get nearest points, route, and distance: " + ((after - before) / 1000000) + " ms");
        Visualize vis = new Visualize("data/usa.vis", "data/usa.png");
        vis.drawRoute(shortestRoute);
    }

    public static void createCityMap(String file) throws FileNotFoundException {
        cities = new HashMap<>();
        Scanner city = new Scanner(new FileInputStream(new File(file)));
        while (city.hasNextLine()) {
            String[] split = city.nextLine().split(",");
            if (split.length == 4) {
                cities.put(split[0] + " " + split[1], new Point(Double.parseDouble(split[2]), Double.parseDouble(split[3])));
            }
            else {
                cities.put(split[0].substring(1) + "," + split[1].substring(0, split[1].length() - 1) + " " + split[2], new Point(Double.parseDouble(split[3]), Double.parseDouble(split[4])));
            }
        }
    }
}
