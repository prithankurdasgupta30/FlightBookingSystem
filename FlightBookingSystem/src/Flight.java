public class Flight {
    int id;
    String flightNumber;
    String origin;
    String destination;
    public Flight(int id, String flightNumber, String origin, String destination) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
    }
    @Override
    public String toString() {
        return flightNumber + " (" + origin + " → " + destination + ")";
    }
}