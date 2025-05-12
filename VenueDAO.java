import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueDAO {

    public List<Venue> getAllVenues() {
        List<Venue> venues = new ArrayList<>();
        String sql = "SELECT * FROM venues";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venue venue = new Venue();
                venue.setVenueId(rs.getInt("venue_id"));
                venue.setVenueName(rs.getString("venue_name"));
                venue.setVenueType(rs.getString("venue_type"));
                venue.setCapacity(rs.getInt("capacity"));
                venues.add(venue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return venues;
    }

    public boolean isVenueAvailable(int venueId, Date eventDate, Time eventTime) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE venue_id = ? AND event_date = ? AND event_time = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, venueId);
            ps.setDate(2, eventDate);
            ps.setTime(3, eventTime);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Venue> getAvailableVenuesByType(String type, String date) {
        List<Venue> venues = new ArrayList<>();
        String sql = """
                SELECT * FROM venues 
                WHERE venue_type = ? 
                  AND venue_id NOT IN (
                      SELECT venue_id FROM bookings 
                      WHERE event_date = ?
                  )
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, type);
            ps.setDate(2, Date.valueOf(date));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Venue venue = new Venue();
                venue.setVenueId(rs.getInt("venue_id"));
                venue.setVenueName(rs.getString("venue_name"));
                venue.setVenueType(rs.getString("venue_type"));
                venue.setCapacity(rs.getInt("capacity"));
                venues.add(venue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return venues;
    }
}
