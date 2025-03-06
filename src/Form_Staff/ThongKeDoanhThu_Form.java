/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Form_Staff;

import Model.Model_Card;
import Repository.ThongkeReponsitory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import response.ThongKeReponse;


/**
 *
 * @author LENOVO
 */
public class ThongKeDoanhThu_Form extends javax.swing.JPanel {

    /**
     * Creates new form ThongKeHoaDon_Form
     */
    private Repository.ThongkeReponsitory TKR = new ThongkeReponsitory();
    private DefaultTableModel mol;
    public ThongKeDoanhThu_Form() {
        initComponents();   
        mol = (DefaultTableModel)tbl_monAntop.getModel();
        showDataTable(TKR.getAll());
//        setData();
        thongkengay();
        thongkeThang();
        tongdoanhThu();
        SoHoaDon();
             jDateChooser2.setDate(day);
        
        // Đăng ký sự kiện thay đổi ngày trên jDateChooser2
        jDateChooser2.addPropertyChangeListener("date", evt -> {
            Date selectedDate = jDateChooser2.getDate();
            thongkengay(selectedDate);
            thongkeThang(selectedDate);
            tongdoanhthu(selectedDate);
            SoHoaDon(selectedDate);
        });
    }

    Date day = new Date();
    SimpleDateFormat fomaterDay = new SimpleDateFormat("dd-MM-yyyy");
    String dayString = fomaterDay.format(day);

//       Year year = new Year();
//        SimpleDateFormat fomaterYear = new SimpleDateFormat("yyyy");
//        String yearString = fomaterMonth.format(year);
  public void SoHoaDon() {
        Date today = Calendar.getInstance().getTime();
        SoHoaDon(today);
    }

    // Phương thức tính số hóa đơn với ngày được cung cấp
    public void SoHoaDon(Date date) {
        // Định dạng tháng để hiển thị
        SimpleDateFormat fomaterMonth = new SimpleDateFormat("MM-yyyy");
        String monthYearString = fomaterMonth.format(date);

        // Gọi phương thức SoHoaDon của đối tượng TKR với ngày được cung cấp
        Integer soHoaDon = TKR.SoHoaDon(date);

        // Cập nhật card1 với tháng định dạng và giá trị trả về
        card1.setData(new Model_Card("Số Hóa Đơn Bán Tháng: " + monthYearString, soHoaDon != null ? soHoaDon.toString() : "0"));
    }
    // ngày deaflit
   public void thongkengay() {
        Date today = Calendar.getInstance().getTime();
        thongkengay(today);
    }
    public void thongkengay(Date date) {
        // Định dạng ngày để hiển thị
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dayString = sdf.format(date);

        // Gọi phương thức doanhthungay với ngày được cung cấp
        Double doanhThuNgay = TKR.doanhthungay(date);

        // Cập nhật card2 với ngày định dạng và giá trị trả về
        card2.setData(new Model_Card("Doanh Thu Ngày: " + dayString, doanhThuNgay != null ? doanhThuNgay.toString() : "0"));
    }
//
// tháng deafault
 public void thongkeThang() {
        Date today = Calendar.getInstance().getTime();
        thongkeThang(today);
    }

    // Phương thức chấp nhận tham số ngày cho thongkeThang
    public void thongkeThang(Date date) {
        // Định dạng tháng để hiển thị
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
        String monthString = sdf.format(date);

        // Gọi phương thức doanhThuThang với ngày được cung cấp
        Double doanhThuThang = TKR.doanhThuThang(date);

        // Cập nhật card3 với tháng định dạng và giá trị trả về
        card3.setData(new Model_Card("Doanh Thu Tháng: " + monthString, doanhThuThang != null ? doanhThuThang.toString() : "0"));
    }
     public void tongdoanhThu() {
        Date today = Calendar.getInstance().getTime();
        tongdoanhthu(today);
    }
    public void tongdoanhthu(Date date) {
        // Định dạng năm để hiển thị
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String yearString = sdf.format(date);

        // Gọi phương thức tongDoanhThuTheoNam với năm được cung cấp
        Double tongDoanhThu = TKR.tongDoanhThuTheoNam(Integer.parseInt(yearString));

        // Cập nhật card4 với năm định dạng và giá trị trả về
        card4.setData(new Model_Card("Tổng Doanh Thu Năm: " + yearString, tongDoanhThu != null ? tongDoanhThu.toString() : "0"));
    }

    
       private void showDataTable(ArrayList<ThongKeReponse> lists) {
        mol.setRowCount(0);
       mol = (DefaultTableModel) tbl_monAntop.getModel();
        AtomicInteger index = new AtomicInteger(1);// tu tang 1 gia tri vao ban dau 
        lists.forEach(s -> mol.addRow(new Object[]{
            index.getAndIncrement(), s.getMaMonAn(),s.getTenMonAn(),s.getGia(),s.getSoLuong(),s.getThanhTien()
        }));
    } 
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        rdo_bdc = new javax.swing.JRadioButton();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        rdo_bdd = new javax.swing.JRadioButton();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        card1 = new com.component.Card();
        card2 = new com.component.Card();
        card3 = new com.component.Card();
        card4 = new com.component.Card();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_monAntop = new javax.swing.JTable();

        jButton1.setText("Biểu Đồ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("THỐNG KÊ  DOANH THU");

        buttonGroup1.add(rdo_bdc);
        rdo_bdc.setText("Biểu đồ cột");
        rdo_bdc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_bdcActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdo_bdd);
        rdo_bdd.setText("biểu đồ đường");
        rdo_bdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdo_bddActionPerformed(evt);
            }
        });

        jLayeredPane1.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        card1.setColor1(new java.awt.Color(255, 51, 0));
        card1.setColor2(new java.awt.Color(255, 255, 153));
        jLayeredPane1.add(card1);

        card2.setColor1(new java.awt.Color(255, 255, 153));
        card2.setColor2(new java.awt.Color(102, 204, 0));
        jLayeredPane1.add(card2);

        card3.setColor1(new java.awt.Color(0, 51, 153));
        card3.setColor2(new java.awt.Color(153, 255, 153));
        jLayeredPane1.add(card3);
        jLayeredPane1.add(card4);

        tbl_monAntop.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Số thứ tự", "Mã món ăn", "Tên món ăn", "Giá", "Số Lượng món ăn", "Thành tiền"
            }
        ));
        jScrollPane1.setViewportView(tbl_monAntop);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 963, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 32, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45)
                                .addComponent(rdo_bdc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdo_bdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rdo_bdc)
                        .addComponent(rdo_bdd)
                        .addComponent(jButton1)))
                .addGap(34, 34, 34)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rdo_bdcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_bdcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdo_bdcActionPerformed


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (rdo_bdc.isSelected()) {
            // Tạo một JFrame mới và thêm BieuDoCotPanel vào đó
            JFrame frame = new JFrame("Biểu Đồ Cột");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new BieuDoCot_from());
            frame.pack(); // Điều chỉnh kích thước frame để phù hợp với nội dung
            frame.setLocationRelativeTo(null); // Hiển thị frame ở giữa màn hình
            frame.setVisible(true);
        } else {
            // Xử lý khi rdo_bdc không được chọn, ví dụ: hiển thị thông báo hoặc làm gì đó khác
            JFrame frame = new JFrame("Biểu Đồ Đường");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(new BieuDoDuong_from());
            frame.pack(); // Điều chỉnh kích thước frame để phù hợp với nội dung
            frame.setLocationRelativeTo(null); // Hiển thị frame ở giữa màn hình
            frame.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed
    }
    private void rdo_bddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdo_bddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdo_bddActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private com.component.Card card1;
    private com.component.Card card2;
    private com.component.Card card3;
    private com.component.Card card4;
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdo_bdc;
    private javax.swing.JRadioButton rdo_bdd;
    private javax.swing.JTable tbl_monAntop;
    // End of variables declaration//GEN-END:variables

}
