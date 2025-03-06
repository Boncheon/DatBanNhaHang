package FormDatBan;

import Form.MainForm;
import Model.HoaDon;
import Model.ModelKH;
import Repository.DatMonRepository;
import Repository.HoaDonRepository;
import Repository.LoaiMon_rp;
import Repository.ReservationRepository;
import Repository.ThucDonRepository;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import response.MonAn_response;

/**
 * Creates new form BookingTable
 */
public class BookingTable extends javax.swing.JPanel {
    private ThucDonRepository thucDonRepository = new ThucDonRepository();
    private ReservationRepository reservationRepository = new ReservationRepository();
    private DatMonRepository datMonRepository = new DatMonRepository();
    private HoaDonRepository hoaDonRepository = new HoaDonRepository();
    private LoaiMon_rp loaiMonRepository = new LoaiMon_rp();
    private int idDatBanHienTai = -1;
    private MainForm main;
    private int currentUser;

    public BookingTable(int currentUser) {
        this.currentUser = currentUser; 
        initComponents();
        loadThucDonData();
        loadLoaiMonData();
        loadBanNames();
        Date today = new Date();
        jDateChooser1.setDate(today);
        addTblDatMonKeyListener();
    }
    private String getCustomerID() {
        // Logic to retrieve customer ID from the repository based on phone number
        String soDienThoai = txtSoDienThoai.getText();
        return reservationRepository.getCustomerIDByPhoneNumber(soDienThoai);
    }
    private void loadBanNames() {
        List<String> banNames = reservationRepository.getAllBanNames();
        for (String ban : banNames) {
            cbbBan1.addItem(ban);
        }
    }

    private void loadLoaiMonData() {
        cbbLoaiMon.addItem("Tất cả"); // Thêm mục "Tất cả"
        List<String> loaiMonList = loaiMonRepository.getAllLoaiMon();
        for (String loaiMon : loaiMonList) {
            cbbLoaiMon.addItem(loaiMon);
        }
    }

    public void setCustomerInfo(String tenKhachHang, String soDienThoai) {
        txtTenKhachHang.setText(tenKhachHang);
        txtSoDienThoai.setText(soDienThoai);
    }
    public void clearBookingForm() {
        txtTenKhachHang.setText("");
        txtSoDienThoai.setText("");
        txtTamTinh.setText("");
        cbbBan1.setSelectedIndex(0);
        cbbLoaiMon.setSelectedIndex(0);
        timePicker1.setTime(null);
        jDateChooser1.setDate(null);

        DefaultTableModel model = (DefaultTableModel) tblDatMon.getModel();
        model.setRowCount(0);
    }
    private void loadThucDonDataByLoaiMon(String loaiMon) {
        List<MonAn_response> monAnList = thucDonRepository.getMonAnByLoaiMon(loaiMon);
        String[] columnNames = {"Mã món", "Tên món", "Đơn giá", "Loại món"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa bất kỳ ô nào
            }
        };

        for (MonAn_response monAn : monAnList) {
            Object[] row = {
                monAn.getMaMonan(),
                monAn.getTenMonan(),
                monAn.getDonGia(),
                monAn.getTenLoaiMon()
            };
            tableModel.addRow(row);
        }

        tblThucDon.setModel(tableModel);
    }

    private void loadThucDonData() {
        List<MonAn_response> monAnList = thucDonRepository.getAll();
        String[] columnNames = {"Mã món", "Tên món", "Đơn giá", "Loại món"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa bất kỳ ô nào
            }
        };

        for (MonAn_response monAn : monAnList) {
            Object[] row = {
                monAn.getMaMonan(),
                monAn.getTenMonan(),
                monAn.getDonGia(),
                monAn.getTenLoaiMon()
            };
            tableModel.addRow(row);
        }

        tblThucDon.setModel(tableModel);
        tblThucDon.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    int selectedRow = tblThucDon.getSelectedRow();
                    if (selectedRow != -1) {
                        addSelectedMonAnToTblDatMon(selectedRow);
                    }
                }
            }
        });
    }

    private void addSelectedMonAnToTblDatMon(int selectedRow) {
        DefaultTableModel thucDonTableModel = (DefaultTableModel) tblThucDon.getModel();
        DefaultTableModel datMonTableModel = (DefaultTableModel) tblDatMon.getModel();

        Object[] row = new Object[6];
        row[0] = datMonTableModel.getRowCount() + 1; // STT
        row[1] = thucDonTableModel.getValueAt(selectedRow, 1); // Tên món
        row[2] = thucDonTableModel.getValueAt(selectedRow, 2); // Đơn giá
        row[3] = 1; // Số lượng mặc định là 1
        row[4] = (double) thucDonTableModel.getValueAt(selectedRow, 2); // Thành tiền = Đơn giá * Số lượng (1)
        row[5] = ""; // Ghi chú mặc định là rỗng

        datMonTableModel.addRow(row);
        updateTamTinh();
    }

    private void addTblDatMonKeyListener() {
        tblDatMon.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int selectedRow = tblDatMon.getSelectedRow();
                    int selectedColumn = tblDatMon.getSelectedColumn();
                    if (selectedColumn == 3) { // Chỉ khi cột "Số lượng" được chỉnh sửa
                        updateThanhTienColumn(selectedRow);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_F2) {
                    int selectedRow = tblDatMon.getSelectedRow();
                    if (selectedRow != -1) {
                        removeSelectedRowFromTblDatMon(selectedRow);
                    }
                }
            }
        });
    }

    private void addTblDatMonTableModelListener() {
        tblDatMon.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
                    int row = e.getFirstRow();
                    updateThanhTienColumn(row);
                }
            }
        });
    }

    private void updateThanhTienColumn(int row) {
        DefaultTableModel datMonTableModel = (DefaultTableModel) tblDatMon.getModel();
        try {
            int soLuong = Integer.parseInt(datMonTableModel.getValueAt(row, 3).toString());
            double donGia = Double.parseDouble(datMonTableModel.getValueAt(row, 2).toString());
            double thanhTien = soLuong * donGia;
            datMonTableModel.setValueAt(thanhTien, row, 4);
            updateTamTinh();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    private void removeSelectedRowFromTblDatMon(int selectedRow) {
        DefaultTableModel datMonTableModel = (DefaultTableModel) tblDatMon.getModel();
        datMonTableModel.removeRow(selectedRow);
        updateTamTinh();
    }

    private void updateTamTinh() {
        DefaultTableModel datMonTableModel = (DefaultTableModel) tblDatMon.getModel();
        double total = 0;
        for (int i = 0; i < datMonTableModel.getRowCount(); i++) {
            total += Double.parseDouble(datMonTableModel.getValueAt(i, 4).toString());
        }
        txtTamTinh.setText(String.valueOf(total));
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSoDienThoai = new javax.swing.JTextField();
        btnChonKhachHang = new javax.swing.JButton();
        btnThemKhachHang = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtTenKhachHang = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        SpinnerSoNguoi = new javax.swing.JSpinner();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        timePicker1 = new com.github.lgooddatepicker.components.TimePicker();
        txtGhiChu = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cbbBan1 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDatMon = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblThucDon = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        btnBoMon = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        cbbLoaiMon = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtTamTinh = new javax.swing.JTextField();
        lbTitle = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        btnThanhToan = new javax.swing.JButton();
        btnTamTinh = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtPhuPhi = new javax.swing.JTextField();
        txtChietKhau = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("Thông tin đặt bàn");
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));

        jLabel2.setText("Khách hàng");

        btnChonKhachHang.setText("...");
        btnChonKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonKhachHangActionPerformed(evt);
            }
        });

        btnThemKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/1343436_add_circle_download_plus_icon.png"))); // NOI18N
        btnThemKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemKhachHangActionPerformed(evt);
            }
        });

        jLabel3.setText("Số điện thoại");

        jLabel4.setText("Ngày đặt");

        jLabel5.setText("Giờ đặt");

        jLabel7.setText("Ghi chú");

        jDateChooser1.setDateFormatString("MMM dd, yy");

        jLabel8.setText("Số người");

        cbbBan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbBan1ActionPerformed(evt);
            }
        });

        jLabel10.setText("Bàn");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGhiChu)
                            .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(SpinnerSoNguoi, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbBan1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel1))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(16, 16, 16)
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnChonKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnThemKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnThemKhachHang, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnChonKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(timePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SpinnerSoNguoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbBan1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(100, 100, 100))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtGhiChu)
                        .addGap(40, 40, 40))))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblDatMon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên món", "Đơn giá", "Số lượng", "Thành tiền", "Ghi chú"
            }
        ));
        jScrollPane2.setViewportView(tblDatMon);

        tblThucDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "STT", "Tên món", "Đơn giá", "Loại món"
            }
        ));
        tblThucDon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblThucDonKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblThucDon);

        jLabel9.setText("Đặt món ăn");
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 204));

        jButton2.setText("Thêm món (Enter)");
        jButton2.setBackground(new java.awt.Color(51, 153, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnBoMon.setText("Bỏ món (F2)");
        btnBoMon.setBackground(new java.awt.Color(255, 102, 102));
        btnBoMon.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBoMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBoMonActionPerformed(evt);
            }
        });

        cbbLoaiMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbLoaiMonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(btnBoMon)
                        .addGap(37, 37, 37)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbbLoaiMon, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBoMon, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbbLoaiMon, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/booking.png"))); // NOI18N
        jButton1.setText("Đặt món");
        jButton1.setBackground(new java.awt.Color(204, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Tạm tính");
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 204));

        txtTamTinh.setEditable(false);
        txtTamTinh.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTamTinh.setForeground(new java.awt.Color(255, 0, 0));

        lbTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/meeting.png"))); // NOI18N
        lbTitle.setText("Tạo đặt bàn");
        lbTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(108, 91, 123));
        lbTitle.setIconTextGap(10);

        jPanel3.setBackground(new java.awt.Color(255, 255, 204));
        jPanel3.setForeground(new java.awt.Color(255, 255, 204));

        jLabel11.setText("Thời gian phục vụ");
        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 0));

        jLabel12.setText("Sáng : 10:00 - 15:00");
        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 204));

        jLabel13.setText("Chiều : 14:30 - 22:00");
        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 204));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel11))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13))
        );

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/booking.png"))); // NOI18N
        jButton3.setText("Đặt bàn");
        jButton3.setBackground(new java.awt.Color(204, 255, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/booking.png"))); // NOI18N
        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.setBackground(new java.awt.Color(204, 255, 255));
        btnThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnTamTinh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/booking.png"))); // NOI18N
        btnTamTinh.setText("Tạm tính");
        btnTamTinh.setBackground(new java.awt.Color(204, 255, 255));
        btnTamTinh.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTamTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTamTinhActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 204));
        jLabel14.setText("Phụ phí");

        txtPhuPhi.setEditable(false);
        txtPhuPhi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtPhuPhi.setText("0");
        txtPhuPhi.setForeground(new java.awt.Color(255, 0, 0));

        txtChietKhau.setEditable(false);
        txtChietKhau.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtChietKhau.setText("0");
        txtChietKhau.setForeground(new java.awt.Color(255, 0, 0));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 204));
        jLabel15.setText("Chiết khấu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(243, 243, 243)
                        .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTamTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtChietKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTamTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btnThanhToan))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(671, 671, 671)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPhuPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(180, 180, 180))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtTamTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(txtPhuPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtChietKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTamTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblThucDonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblThucDonKeyPressed

    }//GEN-LAST:event_tblThucDonKeyPressed

    private void btnChonKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonKhachHangActionPerformed
        MS_ConfirmCustomer dialog = new MS_ConfirmCustomer((java.awt.Frame) SwingUtilities.getWindowAncestor(this), true, this);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_btnChonKhachHangActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    if (this.idDatBanHienTai == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng lưu đặt bàn trước khi lưu đặt món");
        return;
    }

    // Thêm thông tin các món đã đặt vào cơ sở dữ liệu
    DefaultTableModel datMonTableModel = (DefaultTableModel) tblDatMon.getModel();
    for (int i = 0; i < datMonTableModel.getRowCount(); i++) {
        int idMonAn = datMonRepository.getIdMonAnByName(datMonTableModel.getValueAt(i, 1).toString());
        int soLuong = Integer.parseInt(datMonTableModel.getValueAt(i, 3).toString());
        String ghiChuMon = (String) datMonTableModel.getValueAt(i, 5);
        double thanhTien = Double.parseDouble(datMonTableModel.getValueAt(i, 4).toString());

        datMonRepository.addDatMon(this.idDatBanHienTai, idMonAn, soLuong, ghiChuMon, thanhTien);
    }

    JOptionPane.showMessageDialog(this, "Lưu đặt món thành công");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbbBan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbBan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbBan1ActionPerformed

    private void btnThemKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemKhachHangActionPerformed
        MS_AddC dialog = new MS_AddC((java.awt.Frame) SwingUtilities.getWindowAncestor(this), true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        ModelKH newCustomer = dialog.getCustomer();
        if (newCustomer != null) {
            setCustomerInfo(newCustomer.getTen(), newCustomer.getSodt());
        }      // TODO add your handling code here:
    }//GEN-LAST:event_btnThemKhachHangActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
                int selectedRow = tblThucDon.getSelectedRow();
                if (selectedRow != -1) {
                    addSelectedMonAnToTblDatMon(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(BookingTable.this, "Vui lòng chọn một món ăn từ thực đơn.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cbbLoaiMonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLoaiMonActionPerformed
                String selectedLoaiMon = (String) cbbLoaiMon.getSelectedItem();
                if ("Tất cả".equals(selectedLoaiMon)) {
                    loadThucDonData();
                } else {
                    loadThucDonDataByLoaiMon(selectedLoaiMon);
                }        // TODO add your handling code here:
    }//GEN-LAST:event_cbbLoaiMonActionPerformed

    private void btnBoMonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBoMonActionPerformed
        int selectedRow = tblDatMon.getSelectedRow();
        if (selectedRow != -1) {
            removeSelectedRowFromTblDatMon(selectedRow);
        } else {
            JOptionPane.showMessageDialog(BookingTable.this, "Vui lòng chọn một món ăn để bỏ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnBoMonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    String tenKH = txtTenKhachHang.getText();
    String soDienThoai = txtSoDienThoai.getText();
    java.util.Date ngayDat = jDateChooser1.getDate();
    java.sql.Time gioDat = java.sql.Time.valueOf(timePicker1.getTime().toString() + ":00");
    int soNguoi = (Integer) SpinnerSoNguoi.getValue();
    String tenBan = (String) cbbBan1.getSelectedItem();
    String ghiChu = txtGhiChu.getText();
    int idNhanVien = currentUser; 
    String trangThai = "Chưa check-in";

    // Thêm thông tin đặt bàn vào cơ sở dữ liệu và lấy ID_DatBan vừa được tạo
    int idDatBan = reservationRepository.addReservations(tenKH, soDienThoai, new java.sql.Date(ngayDat.getTime()), gioDat, soNguoi, tenBan, ghiChu, idNhanVien, trangThai);

    // Kiểm tra nếu lưu đặt bàn thành công
    if (idDatBan != -1) {
        JOptionPane.showMessageDialog(this, "Lưu đặt bàn thành công");
        // Lưu ID đặt bàn vào biến toàn cục để sử dụng khi lưu đặt món
        this.idDatBanHienTai = idDatBan;
    } else {
        JOptionPane.showMessageDialog(this, "Lưu đặt bàn thất bại");
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // Check if a booking has been made
        if (idDatBanHienTai == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng đặt bàn trước khi thanh toán.");
            return;
        }

        String maKhachHang = getCustomerID();
        String maDatBan = String.valueOf(idDatBanHienTai);

        double tongTien = Double.parseDouble(txtTamTinh.getText());
        double phuPhi = Double.parseDouble(txtPhuPhi.getText());
        double chietKhau = Double.parseDouble(txtChietKhau.getText());
        double tienCanTra = tongTien + phuPhi - chietKhau;
        double tienKhachDua = 0; // Assume this value will be entered later
        double tienTraLai = tienKhachDua - tienCanTra;

        // Get order details from the table
        DefaultTableModel model = (DefaultTableModel) tblDatMon.getModel();
        List<Object[]> orderDetails = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            String tenMonAn = (String) model.getValueAt(i, 1);
            int soLuong = Integer.parseInt(model.getValueAt(i, 3).toString());
            double thanhTien = Double.parseDouble(model.getValueAt(i, 4).toString());
            orderDetails.add(new Object[]{tenMonAn, soLuong, thanhTien});
        }

        // Open BillS_Form and pass the data
        JFrame frame = new JFrame();
        BillS_Form billForm = new BillS_Form(currentUser);
        billForm.setFormData(maDatBan, maKhachHang, String.valueOf(tongTien), String.valueOf(phuPhi), String.valueOf(chietKhau), String.valueOf(tienCanTra), String.valueOf(tienKhachDua), String.valueOf(tienTraLai), orderDetails);
        frame.add(billForm);
        frame.pack();
        frame.setVisible(true);
    

       
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void btnTamTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTamTinhActionPerformed
        if (this.idDatBanHienTai == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng lưu đặt bàn trước khi tạm tính");
            return;
        }

        // Lấy thông tin từ các trường trong form
        int idNhanVien = currentUser;
        Date ngayTao = new Date();
        double tongTien = Double.parseDouble(txtTamTinh.getText());
        boolean trangThai = false; // Trạng thái là false

        // Gọi phương thức để lưu thông tin tạm tính vào cơ sở dữ liệu
        boolean success = hoaDonRepository.saveTamTinh(idDatBanHienTai, idNhanVien, new java.sql.Date(ngayTao.getTime()), tongTien, trangThai);

        if (success) {
            JOptionPane.showMessageDialog(this, "Lưu tạm tính thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Lưu tạm tính thất bại");
        }
    }//GEN-LAST:event_btnTamTinhActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner SpinnerSoNguoi;
    private javax.swing.JButton btnBoMon;
    private javax.swing.JButton btnChonKhachHang;
    private javax.swing.JButton btnTamTinh;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThemKhachHang;
    private javax.swing.JComboBox<String> cbbBan1;
    private javax.swing.JComboBox<String> cbbLoaiMon;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JLabel lbTitle;
    private javax.swing.JTable tblDatMon;
    private javax.swing.JTable tblThucDon;
    private com.github.lgooddatepicker.components.TimePicker timePicker1;
    private javax.swing.JTextField txtChietKhau;
    private javax.swing.JTextField txtGhiChu;
    private javax.swing.JTextField txtPhuPhi;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSoDienThoai;
    private javax.swing.JTextField txtTamTinh;
    private javax.swing.JTextField txtTenKhachHang;
    // End of variables declaration//GEN-END:variables
}
