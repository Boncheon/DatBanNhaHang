package Form_Staff;

import Form_Staff.ReadQRCode.QRCodeObserver;
import Model.ModelNhanVien;
import Repository.RepoStaff;
import Swing_Customer_ScrollBar.ScrollBarCustom;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import raven.cell.TableActionCellEditor;
import raven.cell.TableActionCellRender;
import raven.cell.TableActionEvent;

public class StaffManagement_Form extends javax.swing.JPanel implements QRCodeObserver {

    private ArrayList<ModelNhanVien> list;
    private RepoStaff repo = new RepoStaff();
    DefaultTableModel model;
    String MaUpdate;
    private DecimalFormat df;
    private String soCCCD;

    public StaffManagement_Form() throws SQLException {

        initComponents();
        init();
   addFilterListeners();
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                if (validateForm()) {
                    int confirmed = JOptionPane.showConfirmDialog(null,
                            "Bạn có chắc chắn muốn sửa?",
                            "Xác nhận sửa",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmed == JOptionPane.YES_OPTION) {

                        ModelNhanVien nv = readDataForm();

                        if (nv != null) {
                            // Kiểm tra trùng lặp số điện thoại và email
                            if (isPhoneNumberDuplicate(nv)) {
                                JOptionPane.showMessageDialog(null, "Số điện thoại đã tồn tại");
                                return;
                            }

                            if (isEmailDuplicate(nv)) {
                                JOptionPane.showMessageDialog(null, "Email đã tồn tại");
                                return;
                            }

                            try {
                                repo.update(nv, MaUpdate);
                                loadData();
                                filltoTable();
                                JOptionPane.showMessageDialog(null, "Sửa thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            } catch (SQLException ex) {
                                Logger.getLogger(StaffManagement_Form.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(null, "Sửa thất bại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }

            }

            @Override
            public void onChange(int row) {
                int selectedRow = tblNhanVien.getSelectedRow();
                if (selectedRow != -1) {
                    ModelNhanVien nv = list.get(selectedRow);
                    boolean newTrangThai = !nv.isTrangThai(); // Toggle the status
                    nv.setTrangThai(newTrangThai);

                    // Confirmation dialog
                    int choice = JOptionPane.showConfirmDialog(null,
                            "Bạn có chắc chắn muốn thay đổi trạng thái của nhân viên này?",
                            "Xác nhận thay đổi trạng thái",
                            JOptionPane.YES_NO_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        // Update the database
                        try {
                            repo.updateTrangThai(nv.getId_NV(), newTrangThai); // Assuming repo.updateTrangThai exists
                            JOptionPane.showMessageDialog(null, "Đã cập nhật trạng thái");
                        } catch (SQLException ex) {
                            // Handle exception (e.g., show error message)
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Lỗi cập nhật trạng thái vào cơ sở dữ liệu.");
                            return;
                        }

                        // Update the UI
                        filltoTable(); // Refresh the table with updated data
                        displayTextField(selectedRow); // Display updated data in text fields
                    } else {
                        // Reset the UI to previous state (optional)
                        nv.setTrangThai(!newTrangThai); // Reset to previous status
                    }
                } else {
                    // Handle case when no row is selected
                    JOptionPane.showMessageDialog(null, "Chọn nhân viên cần thay đổi trạng thái.");

                }
            }

        };
        tblNhanVien.getColumnModel().getColumn(11).setCellRenderer(new TableActionCellRender());
        tblNhanVien.getColumnModel().getColumn(11).setCellEditor(new TableActionCellEditor(event));
        tblNhanVien.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                return super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
            }
        });

        this.loadData();
        model = (DefaultTableModel) tblNhanVien.getModel();
        this.filltoTable();

        rdoTatCaFilter.setSelected(true);
     
    }

    private void addFilterListeners() {
        // Add listener for search field
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                applyFilters();
            }
        });

        // Add listeners for combo boxes
        cboTrangThaiFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                applyFilters();
            }
        });

        cboChucVuFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                applyFilters();
            }
        });

        // Add listeners for radio buttons
        rdoNamFilter.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                applyFilters();
            }
        });

        rdoNuFilter.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                applyFilters();
            }
        });

        rdoTatCaFilter.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                applyFilters();
            }
        });
    }

    public void applyFilters() {
        String trangThaiFilter = cboTrangThaiFilter.getSelectedItem().toString();
        String chucVuFilter = cboChucVuFilter.getSelectedItem().toString();
        String searchST = txtSearch.getText().trim();

        // Lấy giá trị giới tính từ radiobutton
        String gioiTinhFilter = "";
        if (rdoNamFilter.isSelected()) {
            gioiTinhFilter = "Nam";
        } else if (rdoNuFilter.isSelected()) {
            gioiTinhFilter = "Nữ";
        } else {
            gioiTinhFilter = ""; // Khi không chọn radiobutton nào (bao gồm "Tất cả")
        }

        // Tạo đối tượng TableRowSorter cho model của bảng
        model = (DefaultTableModel) tblNhanVien.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tblNhanVien.setRowSorter(sorter);

        // Tạo mảng các bộ lọc RowFilter để áp dụng
        ArrayList<RowFilter<Object, Object>> filters = new ArrayList<>();

        // Bộ lọc cho trạng thái (cột 10 trong bảng)
        if (!trangThaiFilter.equals("Tất cả")) {
            filters.add(RowFilter.regexFilter(trangThaiFilter, 10));
        }

        // Bộ lọc cho chức vụ (cột 9 trong bảng)
        if (!chucVuFilter.equals("Tất cả")) {
            filters.add(RowFilter.regexFilter(chucVuFilter, 9));
        }

        // Bộ lọc cho giới tính (cột 8 trong bảng)
        if (!gioiTinhFilter.equals("")) {
            filters.add(RowFilter.regexFilter(gioiTinhFilter, 8));
        }

        // Bộ lọc cho tìm kiếm (áp dụng trên tất cả các cột)
        if (!searchST.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + searchST));
        }

        // Kết hợp các bộ lọc lại với nhau để áp dụng đồng thời
        RowFilter<Object, Object> compoundRowFilter = RowFilter.andFilter(filters);

        // Đặt bộ lọc vào TableRowSorter của bảng
        sorter.setRowFilter(compoundRowFilter);
    }

    public void onQRCodeRead(String qrText) {
        // Process QR code text
        String[] data = qrText.split("\\|\\|");
        if (data.length == 2) {
            String newSoCCCD = data[0].trim(); // Read the new CCCD
            String[] fields = data[1].split("\\|");
            if (fields.length == 5) {
                boolean found = false;
                for (ModelNhanVien nv : list) {
                    String existingCCCD = nv.getCccd();
                    if (existingCCCD != null && existingCCCD.equals(newSoCCCD)) {
                        // Confirm updating the existing record
                        int result = JOptionPane.showConfirmDialog(
                                null,
                                "Thông tin nhân viên đã tồn tại. Bạn có muốn cập nhật thông tin không?",
                                "Xác nhận cập nhật",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );

                        if (result == JOptionPane.YES_OPTION) {
                            // Update text fields with existing employee data
                            lblMaNhanVien.setText(nv.getId_NV());
                            txtTenNhanVien.setText(nv.getTenNV());
                            txtSoDienThoai.setText(nv.getSdt());
                            txtDiaChi.setText(nv.getDiachi());
                            try {
                                // Assuming nv.getNgayVL() returns a String formatted as "dd/MM/yyyy"
                                Date ngaySinh = new SimpleDateFormat("yyyy-MM-dd").parse(nv.getNgaySinh());
                                dcNgaySinh.setDate(ngaySinh); // Set the date in JCalendarChooser
                            } catch (ParseException e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Định dạng ngày không hợp lệ: " + nv.getNgaySinh());
                            }
                            try {
                                // Assuming nv.getNgayVL() returns a String formatted as "dd/MM/yyyy"
                                Date ngayVaoLamDate = new SimpleDateFormat("yyyy-MM-dd").parse(nv.getNgayVL());
                                dcNgayVaoLam.setDate(ngayVaoLamDate); // Set the date in JCalendarChooser
                            } catch (ParseException e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Định dạng ngày không hợp lệ: " + nv.getNgayVL());
                            }
                            txtEmail.setText(nv.getEmail());
               
                            if (nv.isGioiTinh()) {
                                rdoNam.setSelected(true);
                            } else {
                                rdoNu.setSelected(true);
                            }
                            cboChucVu.setSelectedItem(nv.getChucvu());
                            // Store the current CCCD
                            soCCCD = nv.getCccd();
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    // If not found, ask user if they want to add the new entry
                    int result = JOptionPane.showConfirmDialog(
                            null,
                            "Quét thành công. Bạn có muốn thêm nhân viên?",
                            "Xác nhận thêm",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (result == JOptionPane.YES_OPTION) {
                        // Update text fields with QR code data
                        soCCCD = newSoCCCD;
                        txtTenNhanVien.setText(fields[0].trim());
                        String ngaySinh = fields[1].trim();
                        try {
                            // Format the date from "dd/MM/yyyy" to "yyyy-MM-dd" and then parse it
                            String formattedDate = formatDate(ngaySinh);
                            Date ngaySinhDate = new SimpleDateFormat("yyyy-MM-dd").parse(formattedDate);
                            dcNgaySinh.setDate(ngaySinhDate); // Set the date in JCalendarChooser
                        } catch (ParseException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Định dạng ngày sinh không hợp lệ: " + ngaySinh);
                        }
                        String gioiTinh = fields[2].trim();
                        if ("Nam".equalsIgnoreCase(gioiTinh)) {
                            rdoNam.setSelected(true);
                            rdoNu.setSelected(false);
                        } else if ("Nữ".equalsIgnoreCase(gioiTinh)) {
                            rdoNam.setSelected(false);
                            rdoNu.setSelected(true);
                        }
                        txtDiaChi.setText(fields[3].trim());

                    }
                }
            }
        }
    }

    public void loadData() throws SQLException {
        list = repo.getAll();

    }

    public void filltoTable() {
        model.setRowCount(0);
        for (ModelNhanVien nv : list) {
            Object[] row = {nv.getStt(), nv.getId_NV(), nv.getTenNV(), nv.getSdt(), nv.getDiachi(), nv.getNgaySinh(), nv.getNgayVL(), nv.getEmail(),
                nv.isGioiTinh() ? "Nam" : "Nữ", nv.getChucvu(), nv.isTrangThai() ? "Đang làm" : "Nghỉ làm"};
            model.addRow(row);
        }

    }

    public void displayTextField(int index) {
        ModelNhanVien nv = list.get(index);
        lblMaNhanVien.setText(nv.getId_NV());
        txtTenNhanVien.setText(nv.getTenNV());
        txtSoDienThoai.setText(nv.getSdt());
        txtDiaChi.setText(nv.getDiachi());
        try {
            // Assuming nv.getNgaySinh() returns a String formatted as "yyyy-MM-dd"
            Date ngaySinh = new SimpleDateFormat("yyyy-MM-dd").parse(nv.getNgaySinh());
            dcNgaySinh.setDate(ngaySinh); // Set the date in JCalendarChooser
        } catch (ParseException e) {
            e.printStackTrace(); // Log the exception
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        try {
            // Assuming nv.getNgayVL() returns a String formatted as "yyyy/MM/dd"
            Date ngayVL = new SimpleDateFormat("yyyy-MM-dd").parse(nv.getNgayVL());
            dcNgayVaoLam.setDate(ngayVL); // Set the date in JCalendarChooser
        } catch (ParseException e) {
            e.printStackTrace(); // Log the exception
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày vào làm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        txtEmail.setText(nv.getEmail());
 
        rdoNam.setSelected(nv.isGioiTinh());
        rdoNu.setSelected(!nv.isGioiTinh());
        cboChucVu.setSelectedItem(nv.getChucvu());
        soCCCD = nv.getCccd(); // Store the CCCD for further processing
    }

    public ModelNhanVien getDataForm() {
        String ma = lblMaNhanVien.getText();
        String ten = txtTenNhanVien.getText();
        String sdt = txtSoDienThoai.getText();
        String diaChi = txtDiaChi.getText();
        Date ngaySinh = dcNgaySinh.getDate(); // Get the selected date
        String ngaySinhFormatted = new SimpleDateFormat("yyyy-MM-dd").format(ngaySinh); // Format the date
        Date ngayVL = dcNgayVaoLam.getDate(); // Get the selected date
        String ngayVaoLamFormatted = new SimpleDateFormat("yyyy-MM-dd").format(ngayVL); // Format the date
        String email = txtEmail.getText();
        boolean gioiTinh = rdoNam.isSelected();
        String chucvu = cboChucVu.getSelectedItem().toString();
        boolean trangThai = true; // Assuming this is always true for new entries

        // Create a new ModelNhanVien object with the data from the form
        return new ModelNhanVien(sdt, sdt, ten, sdt, diaChi, email, email, email, gioiTinh, chucvu, trangThai, soCCCD, ma);
    }

    private ModelNhanVien readDataForm() {
        ModelNhanVien nv = new ModelNhanVien();
        nv.setId_NV(lblMaNhanVien.getText());
        nv.setTenNV(txtTenNhanVien.getText());
        nv.setSdt(txtSoDienThoai.getText());
        nv.setDiachi(txtDiaChi.getText());
        Date ngaySinh = dcNgaySinh.getDate(); // Get the date from the calendar chooser
        String ngaySinhFormatted = new SimpleDateFormat("yyyy-MM-dd").format(ngaySinh); // Format the date
        nv.setNgaySinh(ngaySinhFormatted); // Set the formatted date

        Date ngayVL = dcNgayVaoLam.getDate(); // Get the date from the calendar chooser
        String ngayVaoLamFormatted = new SimpleDateFormat("yyyy-MM-dd").format(ngayVL); // Format the date
        nv.setNgayVL(ngayVaoLamFormatted); // Set the formatted date
   
        nv.setEmail(txtEmail.getText());
        nv.setCccd(soCCCD);
        nv.setGioiTinh(rdoNam.isSelected());
        nv.setChucvu(cboChucVu.getSelectedItem().toString());
        nv.setTrangThai(true); // Assuming this is always true for updates
        return nv; // Return the populated ModelNhanVien object
    }
//search
//    public void search(String str) {
//
//        model = (DefaultTableModel) tblNhanVien.getModel();
//        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(model);
//        tblNhanVien.setRowSorter(trs);
//        trs.setRowFilter(RowFilter.regexFilter(str));
//
//    }
//Xuat excel

    public void exportToExcel(String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách nhân viên");

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
        }

        // Các phương thức khác của lớp StaffManagement_Form
        // ...
    }

    public void clearForm() {
        lblMaNhanVien.setText("");
        txtTenNhanVien.setText("");
        txtSoDienThoai.setText("");
        txtDiaChi.setText("");
        dcNgaySinh.setDate(null);

        dcNgayVaoLam.setDate(null);
        txtEmail.setText("");
        buttonGroup1.clearSelection();
        cboChucVu.setSelectedItem("Tất cả");
        soCCCD = "";
    }

    public boolean validateForm() {
        if (txtTenNhanVien.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập tên nhân viên");
            txtTenNhanVien.requestFocus();
            return false;
        }

        if (txtSoDienThoai.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập số điện thoại");
            txtSoDienThoai.requestFocus();
            return false;
        } else {
            String SDT = txtSoDienThoai.getText().trim();
            // Kiểm tra ngày vào làm theo định dạng hợp lệ, ví dụ dd/MM/yyyy
            if (!isValidPhoneNumber(SDT)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ");
                txtSoDienThoai.requestFocus();
                return false;
            }
        }
        if (txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập địa chỉ");
            txtDiaChi.requestFocus();
            return false;
        }
        Date ngaySinh = dcNgaySinh.getDate();
        if (ngaySinh == null) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập ngày sinh");
            dcNgaySinh.requestFocus();
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập email");
            txtEmail.requestFocus();
            return false;
        } else {
            String email = txtEmail.getText().trim();
            // Kiểm tra định dạng email hợp lệ
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ");
                txtEmail.requestFocus();
                return false;
            }
        }

        if (!rdoNam.isSelected() && !rdoNu.isSelected()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn giới tính");
            return false;
        }

        if (cboChucVu.getSelectedItem() == null || cboChucVu.getSelectedItem().toString().equalsIgnoreCase("Tất cả")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn chức vụ");
            cboChucVu.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isPhoneNumberDuplicate(ModelNhanVien nv) {
        // Kiểm tra số điện thoại đã tồn tại trong danh sách nhân viên, trừ bản ghi hiện tại
        for (ModelNhanVien existing : list) {
            if (existing.getId_NV() != nv.getId_NV() && existing.getSdt().equals(nv.getSdt())) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmailDuplicate(ModelNhanVien nv) {
        // Kiểm tra email đã tồn tại trong danh sách nhân viên, trừ bản ghi hiện tại
        for (ModelNhanVien existing : list) {
            if (existing.getId_NV() != nv.getId_NV() && existing.getEmail().equalsIgnoreCase(nv.getEmail())) {
                return true;
            }
        }
        return false;
    }

    private boolean isPhoneNumberExists(String phoneNumber) {
        // Kiểm tra số điện thoại đã tồn tại trong danh sách nhân viên
        for (ModelNhanVien existing : list) {
            if (existing.getSdt().equals(phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    private boolean isEmailExists(String email) {
        // Kiểm tra email đã tồn tại trong danh sách nhân viên
        for (ModelNhanVien existing : list) {
            if (existing.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

// Hàm kiểm tra định dạng ngày tháng
// Hàm kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "0\\d{9}"; // Bắt đầu bằng số 0 và theo sau là 9 chữ số
        return phoneNumber.matches(phoneNumberRegex);
    }

    private String formatDate(String dateStr) {
        // Giả sử định dạng ngày sinh từ QR code là dd/MM/yyyy
        SimpleDateFormat inputFormat = new SimpleDateFormat("ddMMyyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = "";
        try {
            Date date = inputFormat.parse(dateStr);
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu không thể phân tích ngày từ chuỗi
            formattedDate = dateStr; // Hoặc trả về chuỗi trống
        }
        return formattedDate;
    }

    private boolean isCCCDExists(String cccd) {
        for (ModelNhanVien nv : list) {
            if (nv.getCccd().equals(cccd)) {
                return true;
            }
        }
        return false;
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().substring(0, 8); // Example: Generate an 8-character password
    }

    public void init() {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        lbTitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtDiaChi = new javax.swing.JTextField();
        txtTenNhanVien = new javax.swing.JTextField();
        rdoNu = new javax.swing.JRadioButton();
        rdoNam = new javax.swing.JRadioButton();
        cboChucVu = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtSoDienThoai = new javax.swing.JTextField();
        lblMaNhanVien = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        dcNgayVaoLam = new com.toedter.calendar.JDateChooser();
        dcNgaySinh = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnXuatExcel = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnQuetCCCD = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cboChucVuFilter = new javax.swing.JComboBox<>();
        cboTrangThaiFilter = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        rdoNamFilter = new javax.swing.JRadioButton();
        rdoNuFilter = new javax.swing.JRadioButton();
        rdoTatCaFilter = new javax.swing.JRadioButton();
        btnMacDinh = new javax.swing.JButton();

        setBackground(new java.awt.Color(247, 247, 247));

        lbTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(108, 91, 123));
        lbTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/steward.png"))); // NOI18N
        lbTitle.setText("QUẢN LÝ NHÂN VIÊN");
        lbTitle.setIconTextGap(10);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        buttonGroup1.add(rdoNu);
        rdoNu.setText("Nữ");

        buttonGroup1.add(rdoNam);
        rdoNam.setText("Nam");

        cboChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Quản lý", "Lễ tân", "Nhân viên" }));

        jLabel1.setText("Mã nhân viên");

        jLabel2.setText("Tên nhân viên");

        jLabel3.setText("Số điện thoại");

        jLabel4.setText("Giới tính");

        jLabel5.setText("Chức vụ");

        jLabel7.setText("Địa chỉ");

        jLabel8.setText("Ngày vào làm");

        lblMaNhanVien.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblMaNhanVien.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel11.setText("Email");

        jLabel13.setText("Ngày sinh");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtTenNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                        .addComponent(lblMaNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(jLabel13))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(29, 29, 29)))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtEmail)
                    .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dcNgayVaoLam, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                    .addComponent(dcNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rdoNam)
                        .addGap(18, 18, 18)
                        .addComponent(rdoNu)))
                .addGap(0, 17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMaNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 1, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcNgaySinh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dcNgayVaoLam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTenNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(rdoNam)
                            .addComponent(rdoNu))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(40, 40, 40))))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/1343436_add_circle_download_plus_icon.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnXuatExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/invoice.png"))); // NOI18N
        btnXuatExcel.setText("Xuất danh sách");
        btnXuatExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatExcelActionPerformed(evt);
            }
        });

        btnReset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/icons8-reset-24.png"))); // NOI18N
        btnReset.setText("Làm mới");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnQuetCCCD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/icons8-iris-scan-24.png"))); // NOI18N
        btnQuetCCCD.setText("Quét CCCD");
        btnQuetCCCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuetCCCDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnQuetCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXuatExcel)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addComponent(btnThem)
                .addGap(36, 36, 36)
                .addComponent(btnReset)
                .addGap(35, 35, 35)
                .addComponent(btnXuatExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(btnQuetCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã nhân viên", "Tên nhân viên", "Số điện thoại", "Địa chỉ", "Ngày sinh", "Ngày vào làm", "Email", "Giới tính", "Chức vụ", "Trạng thái", "Hành động"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhanVien.setRowHeight(40);
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblNhanVien);
        if (tblNhanVien.getColumnModel().getColumnCount() > 0) {
            tblNhanVien.getColumnModel().getColumn(0).setPreferredWidth(25);
            tblNhanVien.getColumnModel().getColumn(1).setPreferredWidth(70);
            tblNhanVien.getColumnModel().getColumn(5).setPreferredWidth(60);
            tblNhanVien.getColumnModel().getColumn(7).setPreferredWidth(40);
            tblNhanVien.getColumnModel().getColumn(8).setPreferredWidth(50);
            tblNhanVien.getColumnModel().getColumn(9).setPreferredWidth(40);
            tblNhanVien.getColumnModel().getColumn(10).setPreferredWidth(50);
            tblNhanVien.getColumnModel().getColumn(11).setPreferredWidth(60);
        }

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        txtSearch.setToolTipText("");
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKeyReleased(evt);
            }
        });

        jLabel6.setText("Tìm kiếm");

        jLabel9.setText("Chức vụ");

        cboChucVuFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Quản lý", "Lễ tân", "Nhân viên" }));

        cboTrangThaiFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Đang làm", "Nghỉ làm" }));

        jLabel10.setText("Trạng thái");

        jLabel12.setText("Giới tính");

        buttonGroup2.add(rdoNamFilter);
        rdoNamFilter.setText("Nam");

        buttonGroup2.add(rdoNuFilter);
        rdoNuFilter.setText("Nữ");

        buttonGroup2.add(rdoTatCaFilter);
        rdoTatCaFilter.setText("Tất cả");

        btnMacDinh.setText("Mặc định");
        btnMacDinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMacDinhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(cboChucVuFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69)
                        .addComponent(jLabel10)
                        .addGap(31, 31, 31)
                        .addComponent(cboTrangThaiFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addComponent(rdoNamFilter)
                        .addGap(42, 42, 42)
                        .addComponent(rdoNuFilter)
                        .addGap(32, 32, 32)
                        .addComponent(rdoTatCaFilter)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(btnMacDinh)
                        .addGap(22, 22, 22))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(btnMacDinh))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cboChucVuFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(cboTrangThaiFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(rdoNamFilter)
                    .addComponent(rdoNuFilter)
                    .addComponent(rdoTatCaFilter))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(282, 282, 282)
                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtTongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongActionPerformed

    private void tblNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseClicked
        // TODO add your handling code here:
        int index = tblNhanVien.getSelectedRow();
        this.displayTextField(index);
    }//GEN-LAST:event_tblNhanVienMouseClicked


    private void txtSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKeyReleased
        // TODO add your handling code here:
//        String searchST = txtSearch.getText();
//        search(searchST);
    }//GEN-LAST:event_txtSearchKeyReleased

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        int confirmed = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn làm mới?",
                "Xác nhận đặt lại",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            clearForm();

        }
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnXuatExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatExcelActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            exportToExcel(selectedFolder.getAbsolutePath() + File.separator + "DanhSachNhanVien.xlsx");
        }
    }//GEN-LAST:event_btnXuatExcelActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
      
        if (validateForm()) {
        int confirmed = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn thêm?",
                "Xác nhận thêm",
                JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            dcNgayVaoLam.setDate(new Date());
            ModelNhanVien nv = this.readDataForm();

            if (nv != null) {
                // Kiểm tra trùng lặp số điện thoại và email
                if (isPhoneNumberExists(nv.getSdt())) {
                    JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại");
                    return;
                }

                if (isEmailExists(nv.getEmail())) {
                    JOptionPane.showMessageDialog(this, "Email đã tồn tại");
                    return;
                }

                try {
                    String generatedPassword = null;
                    if (!nv.getChucvu().equalsIgnoreCase("nhân viên")) {
                        generatedPassword = generatePassword();
                        nv.setMatKhau(generatedPassword);
                    } else {
                        nv.setMatKhau(null);
                    }

                    repo.add(nv);
                    loadData();
                    filltoTable();
                    JOptionPane.showMessageDialog(this, "Thêm mới thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                    // Check if chucvu is not "nhân viên" before sending the email
                    if (generatedPassword != null) {
                        String emailBody = "Dear " + nv.getTenNV() + ",\n\nTài khoản của bạn đã được tạo. Mật khẩu là: " + generatedPassword + "\n\nTrân trọng,\nRestaurant MeoMeo";
                        EmailUtil.sendEmail(nv.getEmail(), "Mật khẩu đăng nhập", emailBody);
                        JOptionPane.showMessageDialog(this, "Mật khẩu đã được gửi về email", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                    // Clear form fields

                } catch (SQLException ex) {
                    Logger.getLogger(StaffManagement_Form.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Thêm mới thất bại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnQuetCCCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuetCCCDActionPerformed
        // TODO add your handling code here:
        ReadQRCode qr = new ReadQRCode();
        qr.addObserver(this); // Register as an observer
        qr.setVisible(true);
    }//GEN-LAST:event_btnQuetCCCDActionPerformed

    private void btnMacDinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMacDinhActionPerformed
        // TODO add your handling code here:
        txtSearch.setText("");

        // Reset combo boxes
        cboTrangThaiFilter.setSelectedIndex(0);
        cboChucVuFilter.setSelectedIndex(0);

        // Reset radio buttons
        rdoTatCaFilter.setSelected(true);
    }//GEN-LAST:event_btnMacDinhActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMacDinh;
    private javax.swing.JButton btnQuetCCCD;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXuatExcel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JComboBox<String> cboChucVu;
    private javax.swing.JComboBox<String> cboChucVuFilter;
    private javax.swing.JComboBox<String> cboTrangThaiFilter;
    private com.toedter.calendar.JDateChooser dcNgaySinh;
    private com.toedter.calendar.JDateChooser dcNgayVaoLam;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lblMaNhanVien;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNamFilter;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JRadioButton rdoNuFilter;
    private javax.swing.JRadioButton rdoTatCaFilter;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSoDienThoai;
    private javax.swing.JTextField txtTenNhanVien;
    // End of variables declaration//GEN-END:variables
}
