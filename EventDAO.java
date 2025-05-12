import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

   
    public boolean addEvent(Event event) {
        String checkSql = "SELECT COUNT(*) FROM events WHERE venue_id = ? AND event_date = ? AND event_time = ?";
        String insertSql = "INSERT INTO events (event_name, event_type, event_date, event_time, venue_id, user_id, delete_requested) " +
                           "VALUES (?, ?, ?, ?, ?, ?, false)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement checkPs = con.prepareStatement(checkSql)) {

            checkPs.setInt(1, event.getVenueId());
            checkPs.setDate(2, Date.valueOf(event.getEventDate()));
            checkPs.setTime(3, Time.valueOf(event.getEventTime()));

            ResultSet rs = checkPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Error: This venue is already booked at the selected date and time.");
                return false;
            }

            try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                insertPs.setString(1, event.getEventName());
                insertPs.setString(2, event.getEventType());
                insertPs.setDate(3, Date.valueOf(event.getEventDate()));
                insertPs.setTime(4, Time.valueOf(event.getEventTime()));
                insertPs.setInt(5, event.getVenueId());
                insertPs.setInt(6, event.getUserId());

                return insertPs.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    
    public List<Event> getEventsByUserId(int userId) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

   
    public boolean requestEventDeletion(int eventId) {
        String sql = "UPDATE events SET delete_requested = true WHERE event_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

   
    public List<Event> getEventsRequestedForDeletion() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE delete_requested = true";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

   
    public boolean deleteEvent(int eventId) {
        String sql = "DELETE FROM events WHERE event_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("event_id"));
        event.setEventName(rs.getString("event_name"));
        event.setEventType(rs.getString("event_type"));
        event.setEventDate(rs.getDate("event_date").toString());
        event.setEventTime(rs.getTime("event_time").toString());
        event.setVenueId(rs.getInt("venue_id"));
        event.setUserId(rs.getInt("user_id"));
        event.setDeleteRequested(rs.getBoolean("delete_requested"));
        return event;
    }
}
