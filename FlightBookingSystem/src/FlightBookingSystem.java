import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class FlightBookingSystem extends JFrame {
    private JComboBox<Flight> flightComboBox;
    private JComboBox<Seat> seatComboBox;
    private JTextField nameField, cardField;
    private JButton bookButton;
    private List<Flight> flights;
    private List<Seat> seats;
    private Flight selectedFlight;
    private Seat selectedSeat;
    public FlightBookingSystem() {
        setTitle("Flight Booking System");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));
        flightComboBox = new JComboBox<>();
        seatComboBox = new JComboBox<>();
        nameField = new JTextField();
        cardField = new JTextField();
        bookButton = new JButton("Book Seat");
        flights = getFlights();
        for (Flight f : flights) {
            flightComboBox.addItem(f);
        }
        add(new JLabel("Select Flight:"));
        add(flightComboBox);
        add(new JLabel("Select Seat:"));
        add(seatComboBox);
        add(new JLabel("Your Name:"));
        add(nameField);
        add(new JLabel("Card Number (16 digits):"));
        add(cardField);
        add(new JLabel(""));
        add(bookButton);
        flightComboBox.addActionListener(e -> {
            selectedFlight = (Flight) flightComboBox.getSelectedItem();
            seatComboBox.removeAllItems();
            if (selectedFlight != null) {
                seats = getAvailableSeats(selectedFlight.id);
                for (Seat s : seats) {
                    seatComboBox.addItem(s);
                }
            }
        });
        bookButton.addActionListener(e -> {
            selectedSeat = (Seat) seatComboBox.getSelectedItem();
            String name = nameField.getText().trim();
            String card = cardField.getText().trim();

            if (selectedFlight == null || selectedSeat == null || name.isEmpty() || !card.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(this, "Please fill all fields correctly.");
                return;
            }
            if (processPayment(card, 500)) {
                if (bookSeat(name, selectedFlight.id, selectedSeat.id)) {
                    JOptionPane.showMessageDialog(this, "Booking Successful!");
                    logBookingToJSON(name, selectedFlight, selectedSeat);
                } else {
                    JOptionPane.showMessageDialog(this, "Booking Failed. Seat might already be booked.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Payment Failed.");
            }
        });
        if (!flights.isEmpty()) {
            flightComboBox.setSelectedIndex(0);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightBookingSystem().setVisible(true));
    }
    public Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/AirlineManagement", "root", "Qwert@12345");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Connection Error: " + e.getMessage());
            return null;
        }
    }
    public List<Flight> getFlights() {
        List<Flight> flights = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM flights")) {
            while (rs.next()) {
                flights.add(new Flight(rs.getInt("id"), rs.getString("flight_number"), rs.getString("origin"), rs.getString("destination")));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching flights: " + e.getMessage());
        }
        return flights;
    }
    public List<Seat> getAvailableSeats(int flightId) {
        List<Seat> seats = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM seats WHERE flight_id = ? AND is_booked = 0")) {
            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                seats.add(new Seat(rs.getInt("id"), rs.getString("seat_number")));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching seats: " + e.getMessage());
        }
        return seats;
    }
    public boolean bookSeat(String customerName, int flightId, int seatId) {
        try (Connection conn = connect()) {
            if (conn == null) return false;
            conn.setAutoCommit(false);
            PreparedStatement update = conn.prepareStatement("UPDATE seats SET is_booked = 1 WHERE id = ? AND is_booked = 0");
            update.setInt(1, seatId);
            int rows = update.executeUpdate();
            if (rows == 0) {
                conn.rollback();
                return false;
            }
            PreparedStatement insert = conn.prepareStatement("INSERT INTO bookings (customer_name, seat_id, flight_id) VALUES (?, ?, ?)");
            insert.setString(1, customerName);
            insert.setInt(2, seatId);
            insert.setInt(3, flightId);
            insert.executeUpdate();

            conn.commit();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Booking error: " + e.getMessage());
            return false;
        }
    }
    public boolean processPayment(String card, double amount) {
        return card.matches("\\d{16}");
    }
    public void logBookingToJSON(String customerName, Flight flight, Seat seat) {
        Map<String, Object> bookingData = new LinkedHashMap<>();
        bookingData.put("customer", customerName);
        bookingData.put("flight", flight.flightNumber);
        bookingData.put("seat", seat.seatNumber);
        bookingData.put("timestamp", new java.util.Date().toString());
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("booking_log.json", true)) {
            writer.write(gson.toJson(bookingData) + System.lineSeparator());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "JSON logging error: " + e.getMessage());
        }
    }
}

