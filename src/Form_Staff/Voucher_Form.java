package Form_Staff;


import EasyXLS.a.V;
import Model.Voucher;
import Repository.VoucherRepo;
import Swing_Customer_ScrollBar.ScrollBarCustom;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;






public class Voucher_Form extends javax.swing.JPanel {
    
    private ArrayList<Voucher> list;
    private VoucherRepo repo = new VoucherRepo();
    DefaultTableModel model;
    String MaUpdate;
    private DecimalFormat df;

    public Voucher_Form() throws SQLException, ParseException {
        
       
        
        initComponents();
        
        this.cboTrangThai.setEnabled(false);
        this.loadData();
        this.fillToTable();
        setAutoStatus();
        model = (DefaultTableModel) tblList.getModel();

        
        rdoGiamGiaCoDinh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rdoGiamGiaCoDinh.isSelected()) {
                    txtPhanTramGiam.setEnabled(false);
                    txtTienGiamToiDa.setEnabled(false);
                    txtTienGiamToiThieu.setEnabled(false);
                    txtGioiHanLuotDung.setEditable(true);
                    txtSoTienGiamNhatDinh.setEnabled(true);
                } else { 
                    setDefaultFieldState(); 
                }
            }
        });
        
        rdoGiamGiaPhanTram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rdoGiamGiaPhanTram.isSelected()) {
                    txtPhanTramGiam.setEnabled(true);
                    txtTienGiamToiDa.setEnabled(true);
                    txtTienGiamToiThieu.setEnabled(true);
                    txtGioiHanLuotDung.setEditable(true);
                    txtSoTienGiamNhatDinh.setEnabled(false);
                } else {
                    setDefaultFieldState();
                }
            }
        });
        
        
    }
    
     private void setDefaultFieldState() {
        
        txtPhanTramGiam.setEnabled(false);
        txtTienGiamToiDa.setEnabled(false);
        txtTienGiamToiThieu.setEnabled(false);
        txtGioiHanLuotDung.setEditable(false);
        txtSoTienGiamNhatDinh.setEnabled(false);
    }
    
    
    public void loadData() throws SQLException{
        
        list = repo.getAll();
         
    }
    
    
    public void fillToTable(){
        
         DefaultTableModel model = (DefaultTableModel) tblList.getModel();
        model.setRowCount(0);
        
        for (Voucher vc : list) {
            
            Object[] row = {vc.getID_Voucher(),vc.getMaVoucher(),vc.getLoaiVoucher(),vc.getNgayBatDau(),vc.getNgayKetThuc(),vc.getPhanTram(),vc.getSoTienGiam(),vc.getSoLanDaDung(),vc.getGioiHanLuotDung(),vc.getTienGiamToiDa(),vc.getTienGiamToiThieu(),vc.getMoTa(),vc.getTrangThai()};
            
            model.addRow(row);
            
        }
    }
    
    
    public void displaytextField(int index){
        
        Voucher vc= list.get(index);
        
        lblMaVoucher.setText(vc.getMaVoucher());
        
        rdoGiamGiaCoDinh.setSelected(vc.getLoaiVoucher().equals("Giảm giá cố định"));
        rdoGiamGiaPhanTram.setSelected(!vc.getLoaiVoucher().equals("Giảm giá cố định"));
        txtNgayBatDau.setText(vc.getNgayBatDau());
        txtNgayKetThuc.setText(vc.getNgayKetThuc());
        txtPhanTramGiam.setText(String.valueOf(vc.getPhanTram()));
        txtSoTienGiamNhatDinh.setText(String.valueOf(vc.getSoTienGiam()));
        txtGioiHanLuotDung.setText(String.valueOf(vc.getGioiHanLuotDung()));
        txtTienGiamToiDa.setText(String.valueOf(vc.getTienGiamToiDa()));
        txtTienGiamToiThieu.setText(String.valueOf(vc.getTienGiamToiThieu()));
        txaMoTa.setText(vc.getMoTa());
        cboTrangThai.setSelectedItem(vc.getTrangThai());
    }
    
    
        private void setAutoStatus() {
        Thread t = new Thread(() -> {
        while (true) {
            try {
            Thread.sleep(1000);
          } catch (Exception e) {
            break;
          }
            
          try {
            List<Voucher> listPGG = repo.getAll();
            for (Voucher vc : listPGG) {
              Date now = new Date();
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format based on your date format

              try {
                Date dateHetHan = sdf.parse(vc.getNgayKetThuc());
                Date dateBatDau = sdf.parse(vc.getNgayBatDau());

                if (dateHetHan.before(now)) {
                  repo.setStatusEXP(vc.getMaVoucher(), "Không khả dụng");
                } else if (dateBatDau.before(now)) {
                  repo.setStatusEXP(vc.getMaVoucher(), "Đang diễn ra");
                } else if (dateBatDau.after(now)) {
                  repo.setStatusEXP(vc.getMaVoucher(), "Sắp diễn ra");
                }
              } catch (ParseException e) {
                
              }
            }
          } catch (SQLException ex) {
            Logger.getLogger(Voucher_Form.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
            });
        t.start();
      }
    
        
    
    
    
   
    
    public void exportToExcel(String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Phiếu giảm giá");
        
        // Lấy số lượng dòng và cột từ model của table
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();

        // Tạo một dòng đầu tiên làm tiêu đề
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < columnCount; col++) {
            headerRow.createCell(col).setCellValue(model.getColumnName(col));
        }

        // Thêm dữ liệu từ table vào sheet
        for (int row = 0; row < rowCount; row++) {
            Row excelRow = sheet.createRow(row + 1);
            for (int col = 0; col < columnCount; col++) {
                Object value = model.getValueAt(row, col);
                if (value != null) {
                    excelRow.createCell(col).setCellValue(value.toString());
                } else {
                    excelRow.createCell(col).setCellValue("");
                }
            }
        }

        // Lưu workbook vào file Excel
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
            JOptionPane.showMessageDialog(this, "Xuất Excel thành công.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi xuất Excel: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}
    
    
    
    
    
    
    
     public Voucher getDataForm(){
        
        
        String ma = lblMaVoucher.getText();
        String loai = rdoGiamGiaCoDinh.isSelected()?"Giảm giá cố định":"Giảm giá phần trăm";
        String ngayBatDau = txtNgayBatDau.getText();
        String ngayKetThuc = txtNgayKetThuc.getText();
        float phanTramGiam = Float.parseFloat(txtPhanTramGiam.getText());
        float soTienGiam = Float.parseFloat(txtSoTienGiamNhatDinh.getText());
        int gioiHanLuotDung = Integer.parseInt(txtGioiHanLuotDung.getText());
        float tienGiamToiDa = Float.parseFloat(txtTienGiamToiDa.getText());
        float tienGiamToiThieu = Float.parseFloat(txtTienGiamToiThieu.getText());
        String moTa = txaMoTa.getText();
        String trangThai = cboTrangThai.getSelectedItem().toString();
       
       Voucher vc = new Voucher(moTa, moTa, loai, trangThai, ngayBatDau, ngayKetThuc, phanTramGiam, soTienGiam, soTienGiam, gioiHanLuotDung, tienGiamToiDa, tienGiamToiThieu, moTa);
       return vc;
    }
    
    

    public Voucher readDataForm(){
        
        Voucher vc = new Voucher();
        
        vc.setID_Voucher(lblMaVoucher.getText());
        vc.setLoaiVoucher(rdoGiamGiaCoDinh.isSelected()? "Giảm giá cố định" : "Giảm giá phần trăm");
        vc.setNgayBatDau(txtNgayBatDau.getText());
        vc.setNgayKetThuc(txtNgayKetThuc.getText());
        vc.setPhanTram(Float.parseFloat(txtPhanTramGiam.getText()));
        vc.setSoTienGiam(Float.parseFloat(txtSoTienGiamNhatDinh.getText()));
        vc.setGioiHanLuotDung(Integer.parseInt(txtGioiHanLuotDung.getText()));
        vc.setTienGiamToiDa(Float.parseFloat(txtTienGiamToiDa.getText()));
        vc.setTienGiamToiThieu(Float.parseFloat(txtTienGiamToiThieu.getText()));
        vc.setMoTa(txaMoTa.getText());
        vc.setTrangThai(cboTrangThai.getSelectedItem().toString());
        
        return vc;
        
    }
    
    
    public void search(String str){
        
        model = (DefaultTableModel) tblList.getModel();
        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
        tblList.setRowSorter(trs);
        trs.setRowFilter(RowFilter.regexFilter(str));
        
    }
    
    public boolean validateForm() {
//        if (txtLoaiVoucher.getText().equals("")) {
//            JOptionPane.showMessageDialog(this, "Bạn chưa nhập loại voucher");
//            txtLoaiVoucher.requestFocus();
//            return false;
//        }
        if (txaMoTa.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập mô tả");
            txaMoTa.requestFocus();
            return false;
        }
        if (txtNgayBatDau.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập ngày bắt đầu");
            txtNgayBatDau.requestFocus();
            return false;
        }
        if (txtNgayKetThuc.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập ngày kết thúc");
            txtNgayKetThuc.requestFocus();
            return false;
        }
        if (txtPhanTramGiam.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập phần trăm giảm");
            txtPhanTramGiam.requestFocus();
            return false;
       
        }
        
        if (txtGioiHanLuotDung.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập giới hạn lượt dùng");
            txtGioiHanLuotDung.requestFocus();
            return false;
       
        }
        
        if (txtTienGiamToiThieu.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập tiền giảm tối thiểu");
            txtTienGiamToiThieu.requestFocus();
            return false;
       
        }
        
        if (txtTienGiamToiDa.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập tiền giảm tối đa");
            txtTienGiamToiDa.requestFocus();
            return false;
       
        }
        
        return true;
    }
    
    
    
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        lbTitle = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        txaMoTa = new javax.swing.JTextField();
        txtNgayBatDau = new javax.swing.JTextField();
        txtNgayKetThuc = new javax.swing.JTextField();
        txtPhanTramGiam = new javax.swing.JTextField();
        txtGioiHanLuotDung = new javax.swing.JTextField();
        txtTienGiamToiThieu = new javax.swing.JTextField();
        txtTienGiamToiDa = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblMaVoucher = new javax.swing.JLabel();
        cboTrangThai = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        txtSoTienGiamNhatDinh = new javax.swing.JTextField();
        rdoGiamGiaPhanTram = new javax.swing.JRadioButton();
        rdoGiamGiaCoDinh = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnExcel = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblList = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSearchMa = new javax.swing.JTextField();

        setBackground(new java.awt.Color(247, 247, 247));

        lbTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(108, 91, 123));
        lbTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/menu (3).png"))); // NOI18N
        lbTitle.setText("Quản Lý Phiếu Giảm Giá");
        lbTitle.setIconTextGap(10);

        jSeparator2.setBackground(new java.awt.Color(76, 76, 76));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Trạng thái");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Mô tả");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Loại Voucher");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Mã Voucher");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Ngày Kết Thúc");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Ngày Bắt Đầu");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Phần trăm giảm");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Giới hạn lượt dùng");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Tiền giảm tối thiểu");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Tiền giảm tối đa");

        cboTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sắp diễn ra", "Đang diễn ra", "Không khả dụng", " " }));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Số tiền giảm nhất định");

        buttonGroup1.add(rdoGiamGiaPhanTram);
        rdoGiamGiaPhanTram.setSelected(true);
        rdoGiamGiaPhanTram.setText("giảm giá phần trăm");

        buttonGroup1.add(rdoGiamGiaCoDinh);
        rdoGiamGiaCoDinh.setText("giảm giá cố định");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txaMoTa, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(jLabel5)
                    .addComponent(lblMaVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtTienGiamToiThieu, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhanTramGiam, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rdoGiamGiaCoDinh)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGioiHanLuotDung, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTienGiamToiDa, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7)
                            .addComponent(rdoGiamGiaPhanTram))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(txtNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtSoTienGiamNhatDinh, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(cboTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(32, 32, 32))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(rdoGiamGiaPhanTram))
                    .addComponent(lblMaVoucher))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(rdoGiamGiaCoDinh))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txaMoTa, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtNgayBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtNgayKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtPhanTramGiam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtGioiHanLuotDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(txtSoTienGiamNhatDinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(51, 51, 51)
                        .addComponent(txtTienGiamToiThieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addComponent(txtTienGiamToiDa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/1343436_add_circle_download_plus_icon.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/change.png"))); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/invoice.png"))); // NOI18N
        btnExcel.setText("Xuất danh sách");
        btnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelActionPerformed(evt);
            }
        });

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/change.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExcel)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1))
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(btnThem)
                .addGap(18, 18, 18)
                .addComponent(btnSua)
                .addGap(18, 18, 18)
                .addComponent(btnExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLamMoi)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        tblList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã Voucher", "Loại Voucher", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Phần Trăm Giảm", "Số Tiền Giảm Nhất Định", "Số Lần Đã Dùng", "Giới Hạn Lượt Dùng", "Tiền Giảm Tối Đa", "Tiền Giảm Tối Thiểu", "Mô Tả", "Trạng Thái"
            }
        ));
        tblList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblList);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Tìm Kiếm");

        txtSearchMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchMaActionPerformed(evt);
            }
        });
        txtSearchMa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchMaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtSearchMa, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSearchMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addGap(19, 19, 19))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(247, 247, 247)
                        .addComponent(lbTitle))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(221, 221, 221)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(340, 340, 340)
                .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lbTitle)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed

    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtTongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongActionPerformed

    private void txtSearchMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchMaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchMaActionPerformed

    private void tblListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListMouseClicked
        
        int index = tblList.getSelectedRow();
        this.displaytextField(index);
        
        
    }//GEN-LAST:event_tblListMouseClicked
    
    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        
          Voucher vc = this.getDataForm();

            if (validateForm()) {
            if (this.getDataForm() != null) {
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn thêm không",
                "Thông Báo", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                repo.add(vc);
                loadData();
                fillToTable();
                JOptionPane.showMessageDialog(this, "Thêm thành công");
            } catch (SQLException ex) {
                Logger.getLogger(StaffManagement_Form.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage());
            }
        } else {
            // User canceled or closed the dialog
            JOptionPane.showMessageDialog(this, "Đã hủy.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Không có dữ liệu để thêm.");
    }
}

         
        
        
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        
        lblMaVoucher.setText("");
        buttonGroup1.clearSelection();
        cboTrangThai.setSelectedItem("Không khả dụng");
        txtNgayBatDau.setText("");
        txtNgayKetThuc.setText("");
        txtPhanTramGiam.setText("0.0");
        txtSoTienGiamNhatDinh.setText("0.0");
        txtGioiHanLuotDung.setText("0");
        txtTienGiamToiDa.setText("0.0");
        txtTienGiamToiThieu.setText("0.0");
        txaMoTa.setText("");

          
        
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void txtSearchMaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchMaKeyReleased
        
        String searchST = txtSearchMa.getText();
        search(searchST);
        
    }//GEN-LAST:event_txtSearchMaKeyReleased

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        
        Voucher vc = this.readDataForm();
        
        if (validateForm()) {
    if (this.readDataForm() != null) {
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn sửa không ?",
                "Thông Báo", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                repo.update(vc, MaUpdate);
                loadData();
                fillToTable();
                JOptionPane.showMessageDialog(this, "Sửa thành công");
            } catch (SQLException ex) {
                Logger.getLogger(StaffManagement_Form.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Lỗi khi sửa: " + ex.getMessage());
            }
        } else {
            // User canceled or closed the dialog
            JOptionPane.showMessageDialog(this, "Đã hủy.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Không có dữ liệu để sửa.");
    }
}
        
        
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelActionPerformed
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            exportToExcel(selectedFolder.getAbsolutePath() + File.separator + "PhieuGiamGia.xlsx");
        
        }
    }//GEN-LAST:event_btnExcelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExcel;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lblMaVoucher;
    private javax.swing.JRadioButton rdoGiamGiaCoDinh;
    private javax.swing.JRadioButton rdoGiamGiaPhanTram;
    private javax.swing.JTable tblList;
    private javax.swing.JTextField txaMoTa;
    private javax.swing.JTextField txtGioiHanLuotDung;
    private javax.swing.JTextField txtNgayBatDau;
    private javax.swing.JTextField txtNgayKetThuc;
    private javax.swing.JTextField txtPhanTramGiam;
    private javax.swing.JTextField txtSearchMa;
    private javax.swing.JTextField txtSoTienGiamNhatDinh;
    private javax.swing.JTextField txtTienGiamToiDa;
    private javax.swing.JTextField txtTienGiamToiThieu;
    // End of variables declaration//GEN-END:variables
}
