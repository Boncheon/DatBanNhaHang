package Form_Staff;

import Model.ModelDatMon;
import Model.ModelLichSuHoaDon;
import Model.ModelThongTinDatBan;
import Repository.RepoDatMon;
import Repository.RepoLichSuHoaDon;
import Repository.RepoThongTinDatBan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

public class LichSuHoaDon extends javax.swing.JPanel {

    private DefaultTableModel tableModelHoaDon;
    private RepoLichSuHoaDon repo = new RepoLichSuHoaDon();

    public LichSuHoaDon() throws SQLException {
        initComponents();
        fillHoaDonTable();


    }

    private List<ModelLichSuHoaDon> getAllLichSuHoaDon() throws SQLException {
        return repo.getAll();
    }


    private void fillHoaDonTable() {
        try {
            List<ModelLichSuHoaDon> hoaDonList = repo.getAll();
            DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
            model.setRowCount(0); // Clear existing rows

            for (ModelLichSuHoaDon hd : hoaDonList) {
                Object[] rowData = new Object[]{
                    hd.getID_HoaDon(),
                    hd.getMaHoaDon(),
                    hd.getId_DatBan(),
                    hd.getId_KhachHang(),
                    hd.getId_NhanVien(),
                    hd.getNgayTao(),
                    hd.getChietKhau(),
                    hd.getPhuPhi(),
                    hd.getTongSauGiam(),
                    hd.isTrangThai() ? "Đã thanh toán" : "Chưa thanh toán"
                };
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void filterData() throws SQLException, ParseException {
        String trangThai = (String) CbbTrangThai.getSelectedItem();
        Date fromDate = DateChooserTuNgay.getDate();
        Date toDate = DateChooserDenNgay.getDate();
        String fromPriceText = txtKhoangTu.getText();
        String toPriceText = txtDen.getText();
        double fromPrice = fromPriceText.isEmpty() ? 0 : Double.parseDouble(fromPriceText);
        double toPrice = toPriceText.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(toPriceText);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<ModelLichSuHoaDon> filteredList = new ArrayList<>();

        for (ModelLichSuHoaDon hd : repo.getAll()) {
            boolean matchesTrangThai = "Tất cả".equals(trangThai)
                    || ("Đã thanh toán".equals(trangThai) && hd.isTrangThai())
                    || ("Chưa thanh toán".equals(trangThai) && !hd.isTrangThai());

            boolean matchesDateRange = true;
            try {
                Date ngayTaoDate = dateFormat.parse(hd.getNgayTao());
                matchesDateRange = (fromDate == null || !ngayTaoDate.before(fromDate))
                        && (toDate == null || !ngayTaoDate.after(toDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            boolean matchesPriceRange = hd.getTongSauGiam() >= fromPrice && hd.getTongSauGiam() <= toPrice;

            if (matchesTrangThai && matchesDateRange && matchesPriceRange) {
                filteredList.add(hd);
            }
        }

        DefaultTableModel model = (DefaultTableModel) tblHoaDon.getModel();
        model.setRowCount(0); // Clear existing rows

        for (ModelLichSuHoaDon hd : filteredList) {
            Object[] rowData = new Object[]{
                hd.getID_HoaDon(),
                hd.getMaHoaDon(),
                hd.getId_DatBan(),
                hd.getId_KhachHang(),
                hd.getId_NhanVien(),
                hd.getNgayTao(),
                hd.getChietKhau(),
                hd.getPhuPhi(),
                hd.getTongSauGiam(),
                hd.isTrangThai() ? "Đã thanh toán" : "Chưa thanh toán"
            };
            model.addRow(rowData);
        }
    }



    private void loadDatBanData(int idDatBan) throws SQLException {
        RepoThongTinDatBan repoDatBan = new RepoThongTinDatBan(); // Ensure you have this repository implemented
        List<ModelThongTinDatBan> datBanList = repoDatBan.getDatBanById(idDatBan);
        
        DefaultTableModel model = (DefaultTableModel) tblDatBan.getModel();
        model.setRowCount(0); // Clear existing rows

        for (ModelThongTinDatBan db : datBanList) {
            Object[] rowData = new Object[]{
                db.getMaDatBan(),
                db.getTenKhachHang(),
                db.getNgayDat(),
                db.getGioDatBan(),
                db.getSoNguoi(),
                db.getTenBan()
            };
            model.addRow(rowData);
        }
    }

    private void loadDatMonData(int idDatBan) throws SQLException {
        RepoDatMon repoDatMon = new RepoDatMon(); // Ensure you have this repository implemented
        List<ModelDatMon> datMonList = repoDatMon.getDatMonByIdDatBan(idDatBan);
        
        DefaultTableModel model = (DefaultTableModel) tblDatMon.getModel();
        model.setRowCount(0); // Clear existing rows

        for (ModelDatMon dm : datMonList) {
            Object[] rowData = new Object[]{
                dm.getId_DatMon(),
                dm.getTenMonAn(),
                dm.getGia(),
                dm.getSoLuong(),
                dm.getThanhTien()
            };
            model.addRow(rowData);
        }
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        DateChooserDenNgay = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        DateChooserTuNgay = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtDen = new javax.swing.JTextField();
        txtKhoangTu = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        CbbTrangThai = new javax.swing.JComboBox<>();
        btnLoc = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDatBan = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDatMon = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Tìm kiếm hóa đơn");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 0));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/invoice.png"))); // NOI18N
        jLabel2.setText("Lịch sử hóa đơn");

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/icons8-iris-scan-24.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Trạng thái hóa đơn");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        DateChooserDenNgay.setToolTipText("");

        jLabel5.setText("Đến ngày");

        DateChooserTuNgay.setToolTipText("");

        jLabel4.setText("Từ ngày");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(DateChooserDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(DateChooserTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(DateChooserTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(DateChooserDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Tìm theo ngày");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setText("Đến");

        jLabel8.setText("Khoảng từ");

        txtDen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(12, 12, 12))
                    .addComponent(jLabel8))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDen, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtKhoangTu, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtKhoangTu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtDen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Tìm theo khoảng giá");

        CbbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đã thanh toán", "Chưa thanh toán" }));
        CbbTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CbbTrangThaiActionPerformed(evt);
            }
        });

        btnLoc.setBackground(new java.awt.Color(255, 102, 102));
        btnLoc.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnLoc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/invoice.png"))); // NOI18N
        btnLoc.setText("Lọc");
        btnLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocActionPerformed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(153, 153, 255));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/invoice.png"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(CbbTrangThai, 0, 348, Short.MAX_VALUE)
                        .addGap(82, 82, 82)
                        .addComponent(jLabel9)
                        .addGap(473, 473, 473))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(btnLoc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addComponent(CbbTrangThai))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addGap(112, 112, 112))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã hóa đơn", "Mã đặt bàn", "Mã khách hàng", "Mã nhân viên", "Ngày tạo", "Chiết khấu", "Phụ phí", "Tổng sau giảm", "Trạng thái"
            }
        ));
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        tblDatBan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã đặt bàn", "Tên khách hàng", "Ngày đặt", "Giờ đặt", "Số người", "Bàn"
            }
        ));
        tblDatBan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDatBanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblDatBan);

        tblDatMon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Món ăn", "Giá", "Số lượng", "Thành tiền"
            }
        ));
        jScrollPane3.setViewportView(tblDatMon);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/invoice.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(401, 401, 401))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel2)
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSearch)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtDenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDenActionPerformed

    private void btnLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocActionPerformed
        try {
            filterData();
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnLocActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        int selectedRow = tblHoaDon.getSelectedRow();
        if (selectedRow != -1) {
            Object idDatBanObj = tblHoaDon.getValueAt(selectedRow, 2); // Assuming ID_DatBan is the third column
            int idDatBan = Integer.parseInt(idDatBanObj.toString());
            try {
                loadDatBanData(idDatBan);
                loadDatMonData(idDatBan); // Load DatMon data
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void CbbTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CbbTrangThaiActionPerformed
    }//GEN-LAST:event_CbbTrangThaiActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed

    }//GEN-LAST:event_btnSearchActionPerformed

    private void tblDatBanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDatBanMouseClicked
        int selectedRow = tblDatBan.getSelectedRow();
        if (selectedRow != -1) {
            Object idDatBanObj = tblDatBan.getValueAt(selectedRow, 0); // Assuming ID_DatBan is the first column
            int idDatBan = Integer.parseInt(idDatBanObj.toString());
            try {
                loadDatMonData(idDatBan); // Load DatMon data
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_tblDatBanMouseClicked

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        CbbTrangThai.setSelectedIndex(0); 
        DateChooserTuNgay.setDate(null); 
        DateChooserDenNgay.setDate(null); 
        txtKhoangTu.setText("");
        txtDen.setText(""); 

        fillHoaDonTable(); 
    }//GEN-LAST:event_btnClearActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CbbTrangThai;
    private com.toedter.calendar.JDateChooser DateChooserDenNgay;
    private com.toedter.calendar.JDateChooser DateChooserTuNgay;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnLoc;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblDatBan;
    private javax.swing.JTable tblDatMon;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextField txtDen;
    private javax.swing.JTextField txtKhoangTu;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
