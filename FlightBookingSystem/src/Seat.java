public class Seat {
    int id;
    String seatNumber;
    public Seat(int id, String seatNumber) {
        this.id = id;
        this.seatNumber = seatNumber;
    }
    @Override
    public String toString() {
        return seatNumber;
    }
}