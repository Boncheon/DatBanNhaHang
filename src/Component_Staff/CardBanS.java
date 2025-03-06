package Component_Staff;

import Model.ModelBan;
import Model.ModelNhanVien;
import Dialog.MS_CancelReserve;
import Dialog.MS_ConfirmReserve;
import Form.MainForm;
import FormDatBan.BookingTable;
import View.Staff;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardBanS extends javax.swing.JPanel {
    
    private final ModelBan table;
    private ModelNhanVien staff;
    private MainForm main;
    private MS_ConfirmReserve book;
    private MS_CancelReserve cancel;
    
    public CardBanS(ModelNhanVien staff,ModelBan table,MainForm main) {
        this.staff=staff;
        this.table = table;
        this.main=main;
        initComponents();
        init();
    }
    
    public void init(){
        book = new MS_ConfirmReserve(Staff.getFrames()[0], true);
        cancel = new MS_CancelReserve(Staff.getFrames()[0], true);
        setPreferredSize(new Dimension(300, 325));
        lbTitle.setText(table.getName());

         switch (table.getStatus()) {
            case "Con trong" -> {
                setBackground(Color.GREEN); // Bàn trống có màu xanh lá
                cmdAdj.setBackground(Color.decode("#355C7D"));
                cmdAdj.setText("ĐẶT TRƯỚC");
                // Thêm sự kiện xử lý đặt bàn
                cmdAdj.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        openBookingTable();
                    }
                });
            }
            case "Dang dung bua" -> {
                setBackground(Color.ORANGE); // Đang dùng bữa có màu cam
                cmdAdj.setText("GỌI MÓN");
                cmdAdj.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Xử lý gọi món
                    }
                });
            }
            case "Da dat truoc" -> {
                setBackground(Color.RED); // Đã đặt trước có màu đỏ
                cmdAdj.setText("HỦY ĐẶT TRƯỚC");
                cmdAdj.setBackground(Color.decode("#c94b4b"));
                cmdAdj.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                       cancel.CancelReserve(table);
                    }
                });
            }
            default -> {
                setBackground(Color.LIGHT_GRAY); // Trạng thái khác có màu xám nhạt
            }
        }
    }
    private void openBookingTable() {
        BookingTable bookingTable = new BookingTable(PROPERTIES);
        main.showForm(bookingTable);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new Swing.PanelRound();
        lbTitle = new javax.swing.JLabel();
        img = new javax.swing.JLabel();
        cmdAdj = new Swing.Button();

        panelRound1.setPreferredSize(new java.awt.Dimension(300, 300));

        lbTitle.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(108, 91, 123));
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setText("Title");

        img.setBackground(new java.awt.Color(233, 228, 240));
        img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/table2.png"))); // NOI18N
        img.setOpaque(true);

        cmdAdj.setBackground(new java.awt.Color(108, 91, 123));
        cmdAdj.setForeground(new java.awt.Color(255, 255, 255));
        cmdAdj.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/custom.png"))); // NOI18N
        cmdAdj.setText("TÙY CHỈNH");
        cmdAdj.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        cmdAdj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAdjActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(img, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelRound1Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(lbTitle))
                    .addGroup(panelRound1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(cmdAdj, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addComponent(img, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdAdj, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAdjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAdjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdAdjActionPerformed

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        super.paintComponent(g);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Swing.Button cmdAdj;
    private javax.swing.JLabel img;
    private javax.swing.JLabel lbTitle;
    private Swing.PanelRound panelRound1;
    // End of variables declaration//GEN-END:variables
}
