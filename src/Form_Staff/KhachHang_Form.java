package Form_Staff;

import Model.ModelHoaDon;
import Model.ModelKhachHang;
import Model.ModelKhachHang;
import Repo.HoaDonRepository;

import Repo.Repository;
import Swing_Customer_ScrollBar.ScrollBarCustom;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JTable;
import javax.swing.RowFilter;
import raven.khachhangs.TableActionCellEdittor;
import raven.khachhangs.TableActionCellRender;
import raven.khachhangs.TableActionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;





public class KhachHang_Form extends javax.swing.JPanel {
    private Repo.HoaDonRepository rphd = new HoaDonRepository();
    private Repo.Repository rp = new Repository();
    
    private DefaultTableModel mol = new DefaultTableModel();
    private DefaultTableModel mol1 = new DefaultTableModel();

    private DecimalFormat df;
    

    public KhachHang_Form() {

        initComponents();
        init();
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                 int selectedRow = tbl_KhachHang.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Bạn chưa chọn dòng");
        return;
    }
String tenKhachHang = txt_tenkhachhang.getText(); // Ví dụ: txtTenKhachHang.getText();
    String soDienThoai = txt_sodienthoai.getText(); 
    String email = txt_email.getText();
    String diachi = txt_diachi.getText();
    String ngaythamgia = txt_ngaythamgia.getText();
    
    // Kiểm tra các trường dữ liệu
     if (tenKhachHang.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Mời nhập Tên khách hàng");
        return;
    }
    if (soDienThoai.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Mời nhập Số điện thoại");
        return;
    }
    if(email.isEmpty()){
        JOptionPane.showMessageDialog(null, "xin moi nhap email");
        return ;
    }
    if(diachi.isEmpty()){
        JOptionPane.showMessageDialog(null, "xin moi nhap dia chi");
        return;
    }
    if(ngaythamgia.isBlank()){
        JOptionPane.showMessageDialog(null, "xin moi nhap ngay tham gia");
    }
    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn sửa không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    ModelKhachHang kh = rp.getAll().get(selectedRow);
    boolean updateResult = rp.update(getFormData(), kh.getMakhachhang());

    if (updateResult) {
        JOptionPane.showMessageDialog(null, "Bạn sửa thành công");
    } else {
        JOptionPane.showMessageDialog(null, "Sửa thất bại");
    }

    showDataTable(rp.getAll());

    showDataTable(rp.getAll());
              
            }

        
        };
        tbl_KhachHang.getColumnModel().getColumn(8).setCellRenderer (new TableActionCellRender());
        tbl_KhachHang.getColumnModel().getColumn(8).setCellEditor(new TableActionCellEdittor(event));
        

        rp = new Repository();
        
        mol = (DefaultTableModel) tbl_KhachHang.getModel();
        mol1 = (DefaultTableModel)tbl_HoaDon.getModel();
        
        showDataTable(rp.getAll());
        showDataTable1(rphd.getAll());
        txt_makhachhang.setEnabled(false);
        txt_ngaythamgia.setEnabled(false);
        
        

    }

    public void init() {

    }

    public void getNumberofF() {

    }

    public void initTable() {

    }

    public void searchTable(String txt) {
        
    }

    private void showDataTable(ArrayList<ModelKhachHang> lists) {
        mol.setRowCount(0);
        AtomicInteger index = new AtomicInteger(1); // Khoi tao 1 gia tri bat dau bang 1 de tu dong tang
        // for..each + lamda 
        lists.forEach(s -> mol.addRow(new Object[]{
            index.getAndIncrement(), s.getMakhachhang(), s.getTen(),
            s.getSodt(), s.getEmail(), s.isGioitinh() ? "nam" : "nu", s.getDiachi(), s.getNgaythamgia()
        }));

    }
 private void showDataTable1(ArrayList<ModelHoaDon> lists) {
        mol1.setRowCount(0);
        AtomicInteger index = new AtomicInteger(1); // Khoi tao 1 gia tri bat dau bang 1 de tu dong tang
        // for..each + lamda 
        lists.forEach(s -> mol1.addRow(new Object[]{
            index.getAndIncrement(), s.getID_HoaDon(), s.getMaHoaDon(),s.getID_datban(),
            s.getID_NhanVien(), s.getID_Voucher() , s.getNgayTao(), s.getTongTien(),
            s.getPhuPhi(),  s.getChietkhau(), s.getTongSauGiam(), s.isTrangThai() ?"chưa thanh toán":"đã thanh toán"
        }));

    }

   

    private void detaikhachhang(int index) {
        ModelKhachHang kh = rp.getAll().get(index);
        
        txt_makhachhang.setText(kh.getMakhachhang());
        txt_tenkhachhang.setText(kh.getTen());
        txt_sodienthoai.setText(kh.getSodt());
        txt_email.setText(kh.getEmail());

        rdo_nam.setSelected(kh.isGioitinh());
        rdo_nu.setSelected(!kh.isGioitinh());
        txt_diachi.setText(kh.getDiachi());
        txt_ngaythamgia.setText(kh.getNgaythamgia());

    }
    private void detaihoadon(int index) {
        ModelHoaDon kh = rphd.getAll().get(index);
      
        

    }
    
  
ModelKhachHang readForm(){
      
                String makhachhang;
                String ten;
                String sodt;
                String email;
                boolean gioitinh = false;
                String diachi;
                String ngaythamgia;
                
        makhachhang = txt_makhachhang.getText().trim();
        if(makhachhang.isEmpty()){
            JOptionPane.showMessageDialog(this, "Ma k the de trong");
            txt_makhachhang.requestFocus();
            return null;
        }
        ten = txt_tenkhachhang.getText();
        if(ten.isEmpty()){
            JOptionPane.showMessageDialog(this, "Ten k the de trong");
            txt_tenkhachhang.requestFocus();
            return null;
        }
        try {
            sodt = txt_sodienthoai.getText();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "sodt khong the de de trong");
            txt_sodienthoai.requestFocus();
            return null;
        }
        try {
        email = txt_email.getText();
      
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "email khong the de trong");
        txt_email.requestFocus();
        return null;
    }
        try {
            diachi = txt_diachi.getText();
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "diachi khong the de trong");
        txt_diachi.requestFocus();
        return null;
    }
        try {
            ngaythamgia = txt_ngaythamgia.getText();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ngay tham gia k the thieu");
            txt_ngaythamgia.requestFocus();
            return null;
        }
        return new ModelKhachHang( makhachhang, ten, sodt, email, gioitinh, diachi, ngaythamgia);
    }

    
    public void exportToExcel(String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách khach hanng");

        // Lấy số lượng dòng và cột từ model của table
        int rowCount = mol.getRowCount();
        int columnCount = mol.getColumnCount();

        // Tạo một dòng đầu tiên làm tiêu đề
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < columnCount; col++) {
            headerRow.createCell(col).setCellValue(mol.getColumnName(col));
        }

        // Thêm dữ liệu từ table vào sheet
        for (int row = 0; row < rowCount; row++) {
            Row excelRow = sheet.createRow(row + 1);
            for (int col = 0; col < columnCount; col++) {
                Object value = mol.getValueAt(row, col);
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
        }

        // Các phương thức khác của lớp StaffManagement_Form
        // ...
    }
    private ModelKhachHang getFormData() {
        // builder => lombox
        ModelKhachHang kh = ModelKhachHang.builder()
                
                .gioitinh(rdo_nam.isSelected())
                .makhachhang(txt_makhachhang.getText())
                .ten(txt_tenkhachhang.getText())
                .ngaythamgia(txt_ngaythamgia.getText())
                .sodt(txt_sodienthoai.getText())
                .email(txt_email.getText())
                .diachi(txt_diachi.getText())
                .build();
        // tuong ung : contructor khong tham so 
//        KhachHang kh1 = new KhachHang();
        return kh;
    }
    
      private ModelHoaDon getFormData1() {
        // builder => lombox
        ModelHoaDon hd = ModelHoaDon.builder()
               
              
                
                .build();
        // tuong ung : contructor khong tham so 
//        KhachHang kh1 = new KhachHang();
        return hd;
    }

   
//    ModelKhachHang readForm() {
//        String id;
//        String makhachhang, ten, sodt, email, diachi, ngaythamgia;
//
//        boolean gioiTinh;
//        id = txt_id.getText().trim();
//        if (id.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "id khong the de trong");
//        }
//        makhachhang = txt_makhachhang.getText().trim();
//        if (makhachhang.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Ma khach hang k the de trong");
//            txt_makhachhang.requestFocus();
//            return null;
//        }
//        ten = txt_tenkhachhang.getText();
//        if (ten.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Ten khach hang k the de trong");
//            txt_tenkhachhang.requestFocus();
//            return null;
//        }
//        sodt = txt_sodienthoai.getText();
//        if (sodt.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "so dien thoai khong the de trong");
//            txt_sodienthoai.requestFocus();
//            return null;
//        }
//        email = txt_email.getText().trim();
//        if (email.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "email khong the de trong");
//            txt_email.requestFocus();
//            return null;
//        }
//
//        diachi = txt_diachi.getText().trim();
//        if (diachi.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "dia chi khong the de trong");
//            txt_diachi.requestFocus();
//            return null;
//
//        }
//        ngaythamgia = txt_ngaythamgia.getText().trim();
//        if (ngaythamgia.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "ngay tham gia khong the de trong");
//            txt_ngaythamgia.requestFocus();
//            return null;
//        }
//        if (rdo_nam.isSelected()) {
//            gioiTinh = true;
//        } else {
//            gioiTinh = false;
//
//        }
//        return new ModelKhachHang(i, makhachhang, ten, sodt, email, gioiTinh, diachi, ngaythamgia);
//
//    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_KhachHang = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        txt_makhachhang = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_tenkhachhang = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_sodienthoai = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        rdo_nam = new javax.swing.JRadioButton();
        rdo_nu = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        txt_ngaythamgia = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txt_diachi = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbl_HoaDon = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btn_them = new javax.swing.JButton();
        btn_sua = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        btn_xuat = new javax.swing.JButton();
        txt_search2 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txt_search = new javax.swing.JTextField();

        setBackground(new java.awt.Color(247, 247, 247));

        tbl_KhachHang.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tbl_KhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID_KhachHang", "Mã khách hàng", "Tên", "Số điện thoại", "Email", "Giới tính", "Hoạt động", "Địa chỉ", "Ngày tham gia"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_KhachHang.setRowHeight(35);
        tbl_KhachHang.setSelectionBackground(java.awt.Color.black);
        tbl_KhachHang.setShowGrid(false);
        tbl_KhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_KhachHangMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_KhachHang);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txt_makhachhang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_makhachhangActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Tên khách hàng");

        txt_tenkhachhang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_tenkhachhangActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Số điện thoại");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Giới tính");

        buttonGroup1.add(rdo_nam);
        rdo_nam.setText("nam");

        buttonGroup1.add(rdo_nu);
        rdo_nu.setText("nu");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Ngày tham gia");

        txt_ngaythamgia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ngaythamgiaActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Địa chỉ");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Email");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Mã khách hàng ");

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 51, 51));
        jLabel15.setText("Thiết lập thông tin khách hàng");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel15)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel15)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9))
                        .addGap(57, 57, 57)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txt_email, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                            .addComponent(txt_diachi, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_sodienthoai, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_makhachhang)
                            .addComponent(txt_tenkhachhang, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_ngaythamgia, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(rdo_nam)
                                .addGap(18, 18, 18)
                                .addComponent(rdo_nu))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txt_makhachhang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txt_tenkhachhang, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txt_sodienthoai, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(54, 54, 54)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(rdo_nam)
                    .addComponent(rdo_nu))
                .addGap(55, 55, 55)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txt_ngaythamgia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_diachi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txt_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/staff (1).png"))); // NOI18N
        jLabel1.setText("QUẢN LÝ KHÁCH HÀNG");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 51, 51));
        jLabel21.setText("Thông tin khách hàng");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel21)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        tbl_HoaDon.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tbl_HoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Stt", "ID_HoaDon", "MaHoaDon", "ID_Datban", "ID_NhanVien", "ID_VouCher", "NgayTao", "TongTien", "PhuPhi", "TienCoc", "TongSauGiam", "Trang thai"
            }
        ));
        tbl_HoaDon.setRowHeight(25);
        tbl_HoaDon.setSelectionBackground(java.awt.Color.black);
        tbl_HoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_HoaDonMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbl_HoaDon);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btn_them.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        btn_them.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/1343436_add_circle_download_plus_icon.png"))); // NOI18N
        btn_them.setText("Thêm");
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_themActionPerformed(evt);
            }
        });

        btn_sua.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        btn_sua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/change.png"))); // NOI18N
        btn_sua.setText("Sửa");
        btn_sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_suaActionPerformed(evt);
            }
        });

        btn_reset.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        btn_reset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/astrology.png"))); // NOI18N
        btn_reset.setText("Làm mới");
        btn_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resetActionPerformed(evt);
            }
        });

        btn_xuat.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        btn_xuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/invoice.png"))); // NOI18N
        btn_xuat.setText("Xuất");
        btn_xuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_xuatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_them, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_sua, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(btn_reset, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(btn_xuat, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_sua, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_them, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_reset, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_xuat, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        txt_search2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_search2MouseClicked(evt);
            }
        });
        txt_search2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_search2KeyReleased(evt);
            }
        });

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 51));
        jLabel2.setText("Lịch sử hóa đơn");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        txt_search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txt_searchMouseClicked(evt);
            }
        });
        txt_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchActionPerformed(evt);
            }
        });
        txt_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_searchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addComponent(jScrollPane3)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(215, 215, 215)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txt_search2, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(378, 378, 378))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_search, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_search2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 82, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed

    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtTongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongActionPerformed

    private void btn_themActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_themActionPerformed
         if (tbl_KhachHang.getSelectedRow() == -1) { // Kiểm tra nếu chưa chọn dòng nào
        JOptionPane.showMessageDialog(null, "Bạn chưa chọn dòng");
        return;
    }
         String makhachhang = txt_makhachhang.getText();
    String tenKhachHang = txt_tenkhachhang.getText();
    String soDienThoai = txt_sodienthoai.getText(); 
    String email = txt_email.getText();
    String diachi = txt_diachi.getText();
    String ngaythamgia = txt_ngaythamgia.getText();
    
    // Kiểm tra các trường dữ liệu
    if (tenKhachHang.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Mời nhập Tên khách hàng");
        return;
    }
    if (soDienThoai.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Mời nhập Số điện thoại");
        return;
    }
    if (email.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Xin mời nhập email");
        return;
    }
    if (diachi.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Xin mời nhập địa chỉ");
        return;
    }
    if (ngaythamgia.isBlank()) {
        JOptionPane.showMessageDialog(null, "Xin mời nhập ngày tham gia");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn thêm không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    // Giả sử bạn có một phương thức để thêm dữ liệu từ các trường vào repository
    rp.add(new ModelKhachHang( makhachhang, tenKhachHang, soDienThoai, email, true, diachi, ngaythamgia));
    showDataTable(rp.getAll());
    JOptionPane.showMessageDialog(null, "Thêm thành công");
    }//GEN-LAST:event_btn_themActionPerformed

    private void btn_suaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_suaActionPerformed
       int selectedRow = tbl_KhachHang.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null, "Bạn chưa chọn dòng");
        return;
    }
String tenKhachHang = txt_tenkhachhang.getText(); // Ví dụ: txtTenKhachHang.getText();
    String soDienThoai = txt_sodienthoai.getText(); 
    String email = txt_email.getText();
    String diachi = txt_diachi.getText();
    String ngaythamgia = txt_ngaythamgia.getText();
    
    // Kiểm tra các trường dữ liệu
     if (tenKhachHang.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Mời nhập Tên khách hàng");
        return;
    }
    if (soDienThoai.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Mời nhập Số điện thoại");
        return;
    }
    if(email.isEmpty()){
        JOptionPane.showMessageDialog(null, "xin moi nhap email");
        return ;
    }
    
    if(diachi.isEmpty()){
        JOptionPane.showMessageDialog(null, "xin moi nhap dia chi");
        return;
    }
    if(ngaythamgia.isBlank()){
        JOptionPane.showMessageDialog(null, "xin moi nhap ngay tham gia");
    }
    int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn sửa không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    ModelKhachHang kh = rp.getAll().get(selectedRow);
    boolean updateResult = rp.update(getFormData(), kh.getMakhachhang());

    if (updateResult) {
        JOptionPane.showMessageDialog(null, "Bạn sửa thành công");
    } else {
        JOptionPane.showMessageDialog(null, "Sửa thất bại");
    }

    showDataTable(rp.getAll());

    }//GEN-LAST:event_btn_suaActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
      int index = tbl_KhachHang.getSelectedRow();

        txt_tenkhachhang.setText("");
        txt_sodienthoai.setText("");
        txt_email.setText("");
        if (tbl_KhachHang.getValueAt(index, 5).toString().equalsIgnoreCase("")) {
            rdo_nam.setSelected(true);
        } else {
            rdo_nu.setSelected(true);
        }
        txt_diachi.setText("");

        txt_makhachhang.setEnabled(false);
        txt_ngaythamgia.setEnabled(false);
         
        
       
      
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_resetActionPerformed
    
    private void tbl_KhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_KhachHangMouseClicked
       detaikhachhang(tbl_KhachHang.getSelectedRow());
         
        
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_KhachHangMouseClicked

    private void btn_xuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_xuatActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            exportToExcel(selectedFolder.getAbsolutePath() + File.separator + "DanhSachKhachhang.xlsx");
        // TODO add your handling code here:
  
        }  // TODO add your handling code here:
    }//GEN-LAST:event_btn_xuatActionPerformed

    private void txt_tenkhachhangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_tenkhachhangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_tenkhachhangActionPerformed

    private void txt_makhachhangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_makhachhangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_makhachhangActionPerformed

    private void txt_ngaythamgiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ngaythamgiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_ngaythamgiaActionPerformed

    private void tbl_HoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_HoaDonMouseClicked
      detaihoadon(tbl_HoaDon.getSelectedRow());   
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl_HoaDonMouseClicked

    private void txt_search2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_search2KeyReleased
     // Lấy DefaultTableModel từ JTable
    DefaultTableModel model = (DefaultTableModel) tbl_HoaDon.getModel();
    
    // Tạo TableRowSorter mới cho DefaultTableModel
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    tbl_HoaDon.setRowSorter(sorter);
    
    // Lấy nội dung tìm kiếm từ txt_search2
    String searchText = txt_search2.getText().trim();
    
    // Kiểm tra nếu nội dung tìm kiếm rỗng thì xóa bộ lọc
    if (searchText.isEmpty()) {
        sorter.setRowFilter(null);
    } else {
        try {
            // Sử dụng RowFilter để lọc các hàng dựa trên nội dung tìm kiếm
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        } catch (java.util.regex.PatternSyntaxException e) {
            // Nếu có lỗi cú pháp trong biểu thức chính quy, không áp dụng bộ lọc
            sorter.setRowFilter(null);
        }
    }
    }//GEN-LAST:event_txt_search2KeyReleased

    private void txt_search2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_search2MouseClicked
       
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_search2MouseClicked

    private void txt_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchKeyReleased
        DefaultTableModel model = (DefaultTableModel)tbl_KhachHang.getModel();
        TableRowSorter<DefaultTableModel> models = new TableRowSorter<>(model);
        tbl_KhachHang.setRowSorter(models);
        models.setRowFilter(RowFilter.regexFilter(txt_search.getText()));
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchKeyReleased

    private void txt_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchActionPerformed

    private void txt_searchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_searchMouseClicked
        DefaultTableModel model = (DefaultTableModel)tbl_KhachHang.getModel();
        TableRowSorter<DefaultTableModel> models = new TableRowSorter<>(model);
        tbl_KhachHang.setRowSorter(models);
        models.setRowFilter(RowFilter.regexFilter(txt_search.getText()));
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_sua;
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_xuat;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JRadioButton rdo_nam;
    private javax.swing.JRadioButton rdo_nu;
    private javax.swing.JTable tbl_HoaDon;
    private javax.swing.JTable tbl_KhachHang;
    private javax.swing.JTextField txt_diachi;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_makhachhang;
    private javax.swing.JTextField txt_ngaythamgia;
    private javax.swing.JTextField txt_search;
    private javax.swing.JTextField txt_search2;
    private javax.swing.JTextField txt_sodienthoai;
    private javax.swing.JTextField txt_tenkhachhang;
    // End of variables declaration//GEN-END:variables

  

   

    

//        txt_id.setText(tbl_KhachHang.getValueAt(i, -1).toString());
//        txt_makhachhang.setText(tbl_KhachHang.getValueAt(i, 0).toString());
//        txt_tenkhachhang.setText(tbl_KhachHang.getValueAt(i, 1).toString());
//
//        txt_sodienthoai.setText(tbl_KhachHang.getValueAt(i, 2).toString());
//        txt_email.setText(tbl_KhachHang.getValueAt(i, 3).toString());
//
//        txt_diachi.setText(tbl_KhachHang.getValueAt(i, 4).toString());
//        txt_ngaythamgia.setText(tbl_KhachHang.getValueAt(i, 5).toString());
//        if (tbl_KhachHang.getValueAt(i, 6).toString().equalsIgnoreCase("nam")) {
//            rdo_nam.setSelected(true);
//        } else {
//            rdo_nu.setSelected(true);
//        }
}
