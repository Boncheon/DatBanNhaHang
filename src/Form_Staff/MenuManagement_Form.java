package Form_Staff;

import Model.ModelLoaiMonAn;
import Model.ModelMonAn;
import Repository.LoaiMon_rp;
import Repository.monAn_rp1;
import static com.sun.tools.attach.VirtualMachine.list;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import raven.MonAn.TableActionCellEdittor;
import raven.MonAn.TableActionCellRender;
import raven.MonAn.TableActionEvent;
import response.MonAn_response;
import swing.EventSwitchSelected;

public class MenuManagement_Form extends javax.swing.JPanel {

    private DecimalFormat df;

    private DefaultTableModel mol;
    private monAn_rp1 rp;
    private LoaiMon_rp lmrp;
    private DefaultComboBoxModel dcbm;

    public MenuManagement_Form() {

        LoaiMon_rp loaiMon = new LoaiMon_rp();
        initComponents();
        init();
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                if (tbl_monAn.getSelectedRow() == -1) { // Kiểm tra nếu chưa chọn dòng nào
                    JOptionPane.showMessageDialog(null, "Bạn chưa chọn dòng");
                } else {
                    int response = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn sửa dữ liệu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        int index = tbl_monAn.getSelectedRow();
                        MonAn_response ma = rp.getAll().get(index);
                        rp.update(ma.getMaMonan(), convertResponseTomodel(getFormData()));
                        JOptionPane.showMessageDialog(null, "Bạn sửa thành công");
                        showDataTable(rp.getAll());
                    } else {
                        JOptionPane.showMessageDialog(null, "Đã hủy sửa dữ liệu");
                    }
                }
            }
            
            public void oninsert(int row) {
                if (tbl_monAn.getSelectedRow() == -1) { // Kiểm tra nếu chưa chọn dòng nào
                    JOptionPane.showMessageDialog(null, "Bạn chưa chọn dòng");
                } else {
                    int response = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn thêm dữ liệu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        rp.add(convertResponseTomodel(getFormData()));
                        showDataTable(rp.getAll());
                        JOptionPane.showMessageDialog(null, "Thêm thành công");
                    } else {
                        JOptionPane.showMessageDialog(null, "Đã hủy thêm dữ liệu");
                    }
                }
            
            }

            @Override
            public void onTrangthai(int row) {
                loadData();
                int selectedRow = tbl_monAn.getSelectedRow();
if (selectedRow != -1) {
    MonAn_response monAn = list.get(selectedRow); // Giả sử list chứa danh sách các món ăn
    boolean newTrangThai = !monAn.getTrangThai(); // Chuyển đổi trạng thái
    monAn.setTrangThai(newTrangThai);

    // Hộp thoại xác nhận
    int choice = JOptionPane.showConfirmDialog(null,
            "Bạn có chắc chắn muốn thay đổi trạng thái của món ăn này?",
            "Xác nhận thay đổi trạng thái",
            JOptionPane.YES_NO_OPTION);

    if (choice == JOptionPane.YES_OPTION) {
        rp.updateTrangThai(monAn.getMaMonan(), newTrangThai); // Giả sử rp.updateTrangThai tồn tại
        JOptionPane.showMessageDialog(null, "Đã cập nhật trạng thái");
        // Cập nhật giao diện người dùng
        showDataTable(rp.getAll()); // Làm mới bảng với dữ liệu đã cập nhật
        detailMonAn(selectedRow); // Hiển thị dữ liệu đã cập nhật trong các trường văn bản
    } else {
        // Đặt lại giao diện người dùng về trạng thái trước đó
        monAn.setTrangThai(!newTrangThai); // Đặt lại trạng thái trước đó
    }
} else {
    // Xử lý trường hợp không có hàng nào được chọn
    JOptionPane.showMessageDialog(null, "Chọn món ăn cần thay đổi trạng thái.");
            }
            }
     };
        tbl_monAn.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        
        tbl_monAn.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEdittor(event));
//        cbo_loai1.removeAllItems();
//        cbo_loai1.addItem("Món chính");
//        cbo_loai1.addItem("Tráng miệng");
//        cbo_loai1.addItem("Khai vị");
//        cbo_loai1.addItem("Nước ngọt");

    }

    public void init() {
        txt_maMonan.setEnabled(false);
        rp = new monAn_rp1();
        lmrp = new LoaiMon_rp();
        mol = (DefaultTableModel) tbl_monAn.getModel();
        dcbm = (DefaultComboBoxModel) cbo_loai.getModel();
        showDataTable(rp.getAll());
        showComboboxModel();

//        loadLoaiMon();
    }

    private void showComboboxModel() {
        dcbm.removeAllElements();
        lmrp.getAll().forEach(tlm -> dcbm.addElement(tlm.getTenLoaimon()));
    }

    private void showDataTable(ArrayList<MonAn_response> lists) {
        mol.setRowCount(0);
        AtomicInteger index = new AtomicInteger(1);// tu tang 1 gia tri vao ban dau 
        lists.forEach(s -> mol.addRow(new Object[]{
            index.getAndIncrement(), s.getMaMonan(), s.getTenMonan(), s.getDonGia(),
            s.getTenLoaiMon(), s.getTrangThai() ? "Hoạt Động" : "Tạm Dừng"
        }));
    }
//    private void loadLoaiMon (ArrayList<LoaiMon_rp>list){
//        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel)cbo_loai .getModel();
//        for (LoaiMon_rp lm : list) {
//            comboBoxModel.addElement(lm);
//        }
//    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        lbTitle = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_monAn = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txt_maMonan = new javax.swing.JTextField();
        txt_TenmonAn = new javax.swing.JTextField();
        txt_gia = new javax.swing.JTextField();
        rdo_td = new javax.swing.JRadioButton();
        rdo_hd = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        cbo_loai = new javax.swing.JComboBox<>();
        actionButton1 = new raven.cell.ActionButton();
        jPanel2 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_sreah = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_giaMax = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_giaMin = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        cbo_loai1 = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(247, 247, 247));

        lbTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(108, 91, 123));
        lbTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/menu.png"))); // NOI18N
        lbTitle.setText("QUẢN LÝ THỰC ĐƠN");
        lbTitle.setIconTextGap(10);

        jSeparator2.setBackground(new java.awt.Color(76, 76, 76));

        tbl_monAn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã món ăn", "Tên món ăn", "Giá", "Loại", "Trạng thái", "Hoạt động"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_monAn.setGridColor(new java.awt.Color(153, 153, 153));
        tbl_monAn.setRowHeight(35);
        tbl_monAn.setSelectionBackground(new java.awt.Color(204, 204, 204));
        tbl_monAn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_monAnMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_monAn);
        if (tbl_monAn.getColumnModel().getColumnCount() > 0) {
            tbl_monAn.getColumnModel().getColumn(0).setPreferredWidth(30);
        }

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        buttonGroup1.add(rdo_td);
        rdo_td.setText("Tạm Dừng");
        rdo_td.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_tdActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdo_hd);
        rdo_hd.setText("Hoạt Động");

        jLabel4.setText("Mã món ăn:");

        jLabel5.setText("Tên món ăn:");

        jLabel6.setText("Giá:");

        jLabel7.setText("Trạng thái:");

        jLabel8.setText("Loại món:");

        jLabel9.setText("jLabel9");
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton6.setText("chọn ảnh");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        cbo_loai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_loaiActionPerformed(evt);
            }
        });

        actionButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/1343436_add_circle_download_plus_icon.png"))); // NOI18N
        actionButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txt_TenmonAn, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                        .addComponent(txt_maMonan)
                        .addComponent(txt_gia))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rdo_hd)
                        .addGap(18, 18, 18)
                        .addComponent(rdo_td)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jButton6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cbo_loai, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(actionButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_maMonan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(87, 87, 87)
                        .addComponent(txt_gia, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_TenmonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdo_hd)
                            .addComponent(rdo_td, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(45, 45, 45))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbo_loai, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(actionButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/change.png"))); // NOI18N
        jButton4.setText("Làm mới");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/invoice.png"))); // NOI18N
        jButton5.setText("Xuất danh sách");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jToggleButton1.setText("Trạng thái");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/1343436_add_circle_download_plus_icon.png"))); // NOI18N
        jButton1.setText("Thêm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(36, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(30, 30, 30))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jButton4)
                .addGap(31, 31, 31)
                .addComponent(jButton5)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );

        jLabel1.setText("TÌm kiếm: ");

        txt_sreah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_sreahActionPerformed(evt);
            }
        });
        txt_sreah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_sreahKeyReleased(evt);
            }
        });

        jLabel2.setText("Value (from):");

        txt_giaMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_giaMaxActionPerformed(evt);
            }
        });

        jLabel3.setText("Value (to):");

        txt_giaMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_giaMinActionPerformed(evt);
            }
        });

        jButton3.setText("Tìm kiếm");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Tất cả");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButton2);
        jRadioButton2.setText("Hoạt Động");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        buttonGroup3.add(jRadioButton3);
        jRadioButton3.setText("Tạm Dừng");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        cbo_loai1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_loai1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSeparator2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(267, 267, 267))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txt_giaMax, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_giaMin, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_sreah))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbo_loai1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton3)
                .addGap(45, 45, 45)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txt_sreah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txt_giaMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txt_giaMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbo_loai1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton2)
                            .addComponent(jRadioButton3)
                            .addComponent(jButton3))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed

    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtTongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongActionPerformed

    private void tbl_monAnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_monAnMouseClicked
        // TODO add your handling code here:
        detailMonAn(tbl_monAn.getSelectedRow());
//                showmonAn.setVisible(true);
    }//GEN-LAST:event_tbl_monAnMouseClicked

    private void rdo_tdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_tdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdo_tdActionPerformed

    private void cbo_loaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_loaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_loaiActionPerformed
// hàm tìm kiếm
    //satrt
    private void txt_sreahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_sreahActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txt_sreahActionPerformed


    private void txt_sreahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_sreahKeyReleased
        // TODO add your handling code here:   
//     String searchST = txt_sreah.getText();
//        search(searchST);
    }//GEN-LAST:event_txt_sreahKeyReleased

    private void txt_giaMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_giaMaxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_giaMaxActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if (tbl_monAn.getSelectedRow() == -1) { // Kiểm tra nếu chưa chọn dòng nào
            JOptionPane.showMessageDialog(null, "Bạn chưa chọn dòng");
        } else {
            int response = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa dữ liệu này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                int index = tbl_monAn.getSelectedRow();
                txt_TenmonAn.setText("");
                txt_gia.setText("");
                txt_maMonan.setText("");
                JOptionPane.showMessageDialog(null, "Xóa thành công");
            } else {
                JOptionPane.showMessageDialog(null, "Đã hủy xóa dữ liệu");
            }
        }
        showComboboxModel();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        showDataTable(rp.searh(txt_sreah.getText()));
//        double giaMax = Double.parseDouble(txt_giaMax.getText());
//        double giaMin = Double.parseDouble(txt_giaMin.getText());
//        showDataTable(rp.searhKhoang(giaMax, giaMin));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // TODO add your handling code here:
        showDataTable(rp.getAllTamDung());
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        showDataTable(rp.getAll());
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        showDataTable(rp.getAllhoatDong());
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void txt_giaMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_giaMinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_giaMinActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            exportToExcel(selectedFolder.getAbsolutePath() + File.separator + "MonAn.xlsx");
    }//GEN-LAST:event_jButton5ActionPerformed
    }

    public class SharedData {

        private static String fillname;
        private static byte[] personImage;

        public static String getFillname() {
            return fillname;
        }

        public static void setFillname(String fillname) {
            SharedData.fillname = fillname;
        }

        public static byte[] getPersonImage() {
            return personImage;
        }

        public static void setPersonImage(byte[] personImage) {
            SharedData.personImage = personImage;
        }
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        JFileChooser filechooser = new JFileChooser();
        filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = filechooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = filechooser.getSelectedFile();
            String fillname = file.getAbsolutePath();
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(fillname).getImage().getScaledInstance(jLabel9.getWidth(), jLabel9.getHeight(), java.awt.Image.SCALE_SMOOTH));
            jLabel9.setIcon(imageIcon);

            try {
                File image = new File(fillname);
                FileInputStream fis = new FileInputStream(image);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    bos.write(buf, 0, readNum);
                }
                SharedData.setFillname(fillname);
                SharedData.setPersonImage(bos.toByteArray()); // Lưu trữ dữ liệu ảnh
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
        int index = tbl_monAn.getSelectedRow();
        MonAn_response ma = rp.getAll().get(index);
        rp.updateAnh(ma.getMaMonan(), convertResponseTomodel(getFormData()));
        showDataTable(rp.getAll());
    }//GEN-LAST:event_jButton6ActionPerformed

    private void cbo_loai1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_loai1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_loai1ActionPerformed

    private void actionButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionButton1ActionPerformed
        // TODO add your handling code here:
        LoaiMon_From lm = new LoaiMon_From();
        lm.setVisible(true);
        lm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                showComboboxModel(); // Cập nhật JComboBox trong lớp chính
            }
        });

    }//GEN-LAST:event_actionButton1ActionPerformed
    private ArrayList<MonAn_response> list = new ArrayList<>();

    private void loadData() {
        list = rp.getAll();
    }
    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // Lấy hàng được chọn từ bảng
        loadData();
        int selectedRow = tbl_monAn.getSelectedRow(); // Đảm bảo tên bảng là tbl_monAn
        if (selectedRow != -1) {
            MonAn_response monAn = list.get(selectedRow); // Giả sử list chứa danh sách các món ăn
            boolean newTrangThai = !monAn.getTrangThai(); // Chuyển đổi trạng thái
            monAn.setTrangThai(newTrangThai);

            // Hộp thoại xác nhận
            int choice = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn thay đổi trạng thái của món ăn này?",
                    "Xác nhận thay đổi trạng thái",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                // Cập nhật cơ sở dữ liệu
                boolean updateSuccess = rp.updateTrangThai(monAn.getMaMonan(), newTrangThai); // Giả sử rp.updateTrangThai tồn tại
                if (updateSuccess) {
                    JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái");

                    // Cập nhật giao diện người dùng
                    showDataTable(rp.getAll()); // Làm mới bảng với dữ liệu đã cập nhật
                    detailMonAn(selectedRow); // Hiển thị dữ liệu đã cập nhật trong các trường văn bản
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi cập nhật trạng thái.");
                }
            } else {
                // Đặt lại giao diện người dùng về trạng thái trước đó (tuỳ chọn)
                monAn.setTrangThai(!newTrangThai); // Đặt lại trạng thái trước đó
            }
        } else {
            // Xử lý trường hợp không có hàng nào được chọn
            JOptionPane.showMessageDialog(this, "Chọn món ăn cần thay đổi trạng thái.");
        }

    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
           if (tbl_monAn.getSelectedRow() == -1) { // Kiểm tra nếu chưa chọn dòng nào           
            JOptionPane.showMessageDialog(null, "Bạn chưa chọn dòng");
        } else {
            rp.add(convertResponseTomodel(getFormData()));
            showDataTable(rp.getAll());
            JOptionPane.showMessageDialog(null, "Thêm thành công");
        }
//                showDataTable(rp.getAll());
    }//GEN-LAST:event_jButton1ActionPerformed

    //end
    //onclick
    //start
    private void detailMonAn(int index) {

        MonAn_response ma = rp.getAll().get(index);
        txt_maMonan.setText(ma.getMaMonan());
        txt_TenmonAn.setText(ma.getTenMonan());
        txt_gia.setText(ma.getDonGia() + "");
        cbo_loai.setSelectedItem(ma.getTenLoaiMon());
        rdo_hd.setSelected(ma.getTrangThai());
        rdo_td.setSelected(!ma.getTrangThai());

// Giả sử ma.getAnh() trả về mảng byte chứa dữ liệu hình ảnh
        byte[] imgData = ma.getAnh(); // Giả sử ma.getAnh() trả về mảng byte chứa dữ liệu hình ảnh

        // Hiển thị hình ảnh lên jLabel9
        if (imgData != null) {
            // Chuyển đổi mảng byte thành ImageIcon
            ImageIcon imageIcon = new ImageIcon(imgData);
            // Tinh chỉnh kích thước hình ảnh để phù hợp với JLabel
            Image image = imageIcon.getImage().getScaledInstance(jLabel9.getWidth(), jLabel9.getHeight(), Image.SCALE_SMOOTH);
            jLabel9.setIcon(new ImageIcon(image));
        } else {
            // Nếu không có dữ liệu hình ảnh, xóa ảnh trên JLabel
            jLabel9.setIcon(null);
        }
    }
    //end

    private MonAn_response getFormData() {
        String maMonan = txt_maMonan.getText().trim();
        String tenMonan = txt_TenmonAn.getText().trim();
        String donGiaStr = txt_gia.getText().trim();
        String tenLoaiMon = cbo_loai.getSelectedItem().toString();
        boolean trangThai = rdo_hd.isSelected();

        if (maMonan.isEmpty() || tenMonan.isEmpty() || donGiaStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin.");
            return null;
        }

        double donGia;
        try {
            donGia = Double.parseDouble(donGiaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Giá tiền phải là một số hợp lệ.");
            return null;
        }

        return MonAn_response.builder()
                .maMonan(maMonan)
                .tenMonan(tenMonan)
                .donGia(donGia)
                .TenLoaiMon(tenLoaiMon)
                .TrangThai(trangThai)
                .build();
    }

//      public void search(String str){
//        
//        mol = (DefaultTableModel) tbl_monAn.getModel();
//        TableRowSorter<DefaultTableModel> trs = new TableRowSorter<>(mol);
//        tbl_monAn.setRowSorter(trs);
//        trs.setRowFilter(RowFilter.regexFilter(str));
//        
//    }
    private ModelMonAn convertResponseTomodel(MonAn_response response) {
        ModelLoaiMonAn lma = lmrp.getLoaiMaByMa(response.getTenLoaiMon());
        return ModelMonAn.builder()
                .maMonan(response.getMaMonan())
                .tenMonan(response.getTenMonan())
                .donGia(response.getDonGia())
                .Id_loaiMon(lma.getId_Loaimon())
                .TrangThai(response.getTrangThai())
                .Anh(response.getAnh())
                .build();
    }

    public void exportToExcel(String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Mon An");

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private raven.cell.ActionButton actionButton1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JComboBox<String> cbo_loai;
    private javax.swing.JComboBox<String> cbo_loai1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JRadioButton rdo_hd;
    private javax.swing.JRadioButton rdo_td;
    private javax.swing.JTable tbl_monAn;
    private javax.swing.JTextField txt_TenmonAn;
    private javax.swing.JTextField txt_gia;
    private javax.swing.JTextField txt_giaMax;
    private javax.swing.JTextField txt_giaMin;
    private javax.swing.JTextField txt_maMonan;
    private javax.swing.JTextField txt_sreah;
    // End of variables declaration//GEN-END:variables
}
