package FormDatBan;

import model.Reservation;
import Repository.ReservationRepository;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class GranttChartPanel extends JPanel {

    private List<Reservation> reservations;
    private Date reservationDate;
    private int cellWidth = 85; // Width of each column (hour)
    private int cellHeight = 50; // Height of each row (table)
    private int startHour = 10; // Start hour to display
    private int endHour = 22; // End hour to display
    private Map<String, Integer> tableRowMap; // Map to store row position of each table

    public GranttChartPanel() {
        initComponents();
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
        if (reservationDate != null) {
            ReservationRepository repo = new ReservationRepository();
            reservations = repo.getReservationsByDate(new java.sql.Date(reservationDate.getTime()));
            sortReservationsByTableName();
            mapTablesToRows();
        }
        repaint();
    }

    private void sortReservationsByTableName() {
        Collections.sort(reservations, new Comparator<Reservation>() {
            @Override
            public int compare(Reservation r1, Reservation r2) {
                return r1.getTenBan().compareTo(r2.getTenBan());
            }
        });
    }

    private void mapTablesToRows() {
        tableRowMap = new HashMap<>();
        int rowIndex = 1;
        for (Reservation reservation : reservations) {
            if (!tableRowMap.containsKey(reservation.getTenBan())) {
                tableRowMap.put(reservation.getTenBan(), rowIndex++);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (reservationDate == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw grid lines for hours and tables
        drawGrid(g2d);

        // Draw reservations
        drawReservations(g2d);
    }

    private void drawGrid(Graphics2D g2d) {
        if (reservationDate == null) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        // Draw column headers (hours)
        for (int i = startHour; i <= endHour; i++) {
            int x = (i - startHour) * cellWidth + cellWidth; // Set the first column for row headers (tables)
            g2d.drawLine(x, 0, x, height);
            g2d.drawString(i + ":00", x + 10, 20); // Display hour at the top of the column
        }

        // Draw row headers (tables)
        if (reservations != null && !reservations.isEmpty()) {
            int y = cellHeight;
            Set<String> drawnTables = new HashSet<>();
            for (Reservation reservation : reservations) {
                if (!drawnTables.contains(reservation.getTenBan())) {
                    g2d.drawLine(0, y, width, y);
                    g2d.drawString(reservation.getTenBan(), 5, y + cellHeight / 2); // Display table name on the left of the row
                    y += cellHeight;
                    drawnTables.add(reservation.getTenBan());
                }
            }
        }
    }

    private void drawReservations(Graphics2D g2d) {
        if (reservations == null || reservations.isEmpty()) {
            return;
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        for (Reservation reservation : reservations) {
            int startHourIndex = reservation.getGioDat().getHours() - startHour;
            int startMinute = reservation.getGioDat().getMinutes();
            int duration = reservation.getDuration(); // Assume you have a method to get the duration in minutes

            if (startHourIndex >= 0 && startHourIndex <= (endHour - startHour)) {
                int x = startHourIndex * cellWidth + (startMinute * cellWidth / 60) + cellWidth;
                int rowIndex = tableRowMap.get(reservation.getTenBan());
                int y = rowIndex * cellHeight;
                int width = (duration * cellWidth / 60); // Duration in minutes to width in pixels
                int height = cellHeight - 2; // Height of the reservation bar

                switch (reservation.getTrangThai()) {
                    case "Đã check-in":
                        g2d.setColor(Color.GREEN);
                        break;
                    case "Chưa check-in":
                        g2d.setColor(Color.BLUE);
                        break;
                    case "Hủy":
                        g2d.setColor(Color.RED);
                        break;
                    default:
                        g2d.setColor(Color.GRAY);
                        break;
                }

                g2d.fillRect(x + 1, y + 1, width - 2, height); // Draw the reservation rectangle
                g2d.setColor(Color.BLACK);

                // Display customer information: name, phone number, number of people, notes
                String customerInfo = String.format("%s, %s, %d người, %s",
                        reservation.getTenKhachHang(),
                        reservation.getSoDienThoai(),
                        reservation.getSoNguoi(),
                        reservation.getGhiChu());

                g2d.drawString(customerInfo, x + 10, y + height / 2); // Display customer information
            }
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
