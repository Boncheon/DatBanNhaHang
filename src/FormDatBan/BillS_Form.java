package FormDatBan;

import Dialog.MS_PaymentSuccess;
import Model.HoaDon;
import Model.ModelNguoiDung;
import Repository.HoaDonRepository;
import Service.ServiceStaff;
import java.io.File;
import java.io.IOException;
import static java.lang.Double.parseDouble;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class BillS_Form extends javax.swing.JPanel {
    private int user;
    private MS_PaymentSuccess conf;

    public BillS_Form(int user) {
        this.user = user;
        initComponents();
        addDocumentListeners();
        setTodayDate();
    }

    private void addDocumentListeners() {
        txtPhuPhi.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTienCanTra();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTienCanTra();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTienCanTra();
            }
        });

        txtChietKhau.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTienCanTra();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTienCanTra();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTienCanTra();
            }
        });

        txtTienKhachDua.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTienTraLai();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTienTraLai();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTienTraLai();
            }
        });
    }

    private void setTodayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        lbNgHD.setText(formatter.format(date));
    }

    private void updateTienCanTra() {
        try {
            double tongTien = Double.parseDouble(txtTongTien.getText());
            double phuPhi = Double.parseDouble(txtPhuPhi.getText());
            double chietKhau = Double.parseDouble(txtChietKhau.getText());

            double tienCanTra = tongTien + phuPhi - chietKhau;
            txtTienCanTra.setText(String.valueOf(tienCanTra));
            updateTienTraLai();
        } catch (NumberFormatException e) {
            txtTienCanTra.setText("0");
        }
    }

    private void updateTienTraLai() {
        try {
            double tienKhachDua = Double.parseDouble(txtTienKhachDua.getText());
            double tienCanTra = Double.parseDouble(txtTienCanTra.getText());

            double tienTraLai = tienKhachDua - tienCanTra;
            txtTienTraLai.setText(String.valueOf(tienTraLai));
        } catch (NumberFormatException e) {
            txtTienTraLai.setText("0");
        }
    }

    public void setFormData(String maDatBan, String maKhachHang, String tongTien, String phuPhi, String chietKhau, String tienCanTra, String tienKhachDua, String tienTraLai, List<Object[]> orderDetails) {
        txtIdDatBan.setText(maDatBan);
        txtidKhachHang.setText(maKhachHang);
        txtTongTien.setText(tongTien);
        txtPhuPhi.setText(phuPhi);
        txtChietKhau.setText(chietKhau);
        txtTienCanTra.setText(tienCanTra);
        txtTienKhachDua.setText(tienKhachDua);
        txtTienTraLai.setText(tienTraLai);

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        for (Object[] row : orderDetails) {
            model.addRow(row);
        }
    }
    private void generateBillPDF(String fileName, String maHoaDon, String ngay, String khachHang, List<Object[]> orderDetails,
                                 double tienMonAn, double tienGiamGia, double tongTien, double tienKhachDua, double tienTraLai) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.newLineAtOffset(220, 750);
            contentStream.showText("Royal TheDreamers Restaurant");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.newLineAtOffset(250, 720);
            contentStream.showText("HOA DON THANH TOAN");
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 680);
            contentStream.showText("Ma Hoa Don: " + maHoaDon);
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Ngay: " + ngay);
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Khach Hang: " + khachHang);
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(50, 620);
            contentStream.showText("Mon An");
            contentStream.newLineAtOffset(200, 0);
            contentStream.showText("So luong");
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText("Thanh tien");
            contentStream.endText();

            int yPosition = 600;
            for (Object[] item : orderDetails) {
                String monAn = (String) item[0];
                String soLuong = item[1].toString();
                String thanhTien = item[2].toString();

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText(monAn);
                contentStream.newLineAtOffset(200, 0);
                contentStream.showText(soLuong);
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(thanhTien);
                contentStream.endText();
                yPosition -= 15;
            }

            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition - 30);
            contentStream.showText("Tien mon an: " + String.format("%,.0f", tienMonAn) + "d");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Tien giam gia: " + String.format("%,.0f", tienGiamGia) + "d");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Tong tien: " + String.format("%,.0f", tongTien) + "d");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Tien khach dua: " + String.format("%,.0f", tienKhachDua) + "d");
            contentStream.newLineAtOffset(0, -15);
            contentStream.showText("Tien tra lai: " + String.format("%,.0f", tienTraLai) + "d");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.newLineAtOffset(200, yPosition - 90);
            contentStream.showText("THANK YOU FOR YOUR PURCHASE");
            contentStream.endText();
        }

        document.save(fileName);
        document.close();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new Swing.PanelRound();
        lbTongtien = new javax.swing.JLabel();
        lbTitle = new javax.swing.JLabel();
        lbNgHD = new javax.swing.JLabel();
        lbidKH = new javax.swing.JLabel();
        lbidHD = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lbTongtien1 = new javax.swing.JLabel();
        lbTongtien2 = new javax.swing.JLabel();
        lbTongtien3 = new javax.swing.JLabel();
        lbTongtien4 = new javax.swing.JLabel();
        lbTongtien5 = new javax.swing.JLabel();
        cmdOk = new Swing.ButtonOutLine();
        cmdHuy = new Swing.ButtonOutLine();
        cmdExportBill = new Swing.Button();
        txtTienCanTra = new javax.swing.JTextField();
        txtTienTraLai = new javax.swing.JTextField();
        txtTienKhachDua = new javax.swing.JTextField();
        txtChietKhau = new javax.swing.JTextField();
        txtPhuPhi = new javax.swing.JTextField();
        txtTongTien = new javax.swing.JTextField();
        txtIdDatBan = new javax.swing.JTextField();
        txtidKhachHang = new javax.swing.JTextField();

        lbTongtien.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTongtien.setForeground(new java.awt.Color(89, 89, 89));
        lbTongtien.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTongtien.setText("Tổng tiền");

        lbTitle.setBackground(new java.awt.Color(255, 255, 255));
        lbTitle.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(108, 91, 123));
        lbTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/search.png"))); // NOI18N
        lbTitle.setText("Thông tin Hóa Đơn");
        lbTitle.setIconTextGap(10);

        lbNgHD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbNgHD.setForeground(new java.awt.Color(89, 89, 89));
        lbNgHD.setText("Ngày ");

        lbidKH.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbidKH.setForeground(new java.awt.Color(89, 89, 89));
        lbidKH.setText("Mã Khách Hàng");

        lbidHD.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbidHD.setForeground(new java.awt.Color(89, 89, 89));
        lbidHD.setText("Mã đặt bàn");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Món ăn", "Số lượng", "Thành tiền"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        lbTongtien1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTongtien1.setForeground(new java.awt.Color(89, 89, 89));
        lbTongtien1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTongtien1.setText("Phụ phí");

        lbTongtien2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTongtien2.setForeground(new java.awt.Color(89, 89, 89));
        lbTongtien2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTongtien2.setText("Tiền cần trả");

        lbTongtien3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTongtien3.setForeground(new java.awt.Color(89, 89, 89));
        lbTongtien3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTongtien3.setText("Chiết khấu");

        lbTongtien4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTongtien4.setForeground(new java.awt.Color(89, 89, 89));
        lbTongtien4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTongtien4.setText("Tiền trả lại");

        lbTongtien5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTongtien5.setForeground(new java.awt.Color(89, 89, 89));
        lbTongtien5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbTongtien5.setText("Tiền khách đưa");

        cmdOk.setBackground(new java.awt.Color(0, 204, 0));
        cmdOk.setText("Xác nhận thanh toán");
        cmdOk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmdOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOkActionPerformed(evt);
            }
        });

        cmdHuy.setBackground(new java.awt.Color(255, 0, 0));
        cmdHuy.setText("Hủy");
        cmdHuy.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        cmdExportBill.setBackground(new java.awt.Color(255, 255, 102));
        cmdExportBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/printer.png"))); // NOI18N
        cmdExportBill.setText("Xuất hóa đơn");
        cmdExportBill.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cmdExportBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExportBillActionPerformed(evt);
            }
        });

        txtTienTraLai.setBackground(new java.awt.Color(255, 255, 153));
        txtTienTraLai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTienTraLai.setForeground(new java.awt.Color(255, 51, 51));
        txtTienTraLai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienTraLaiActionPerformed(evt);
            }
        });

        txtTienKhachDua.setBackground(new java.awt.Color(255, 255, 153));
        txtTienKhachDua.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTienKhachDua.setForeground(new java.awt.Color(255, 0, 51));
        txtTienKhachDua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTienKhachDuaActionPerformed(evt);
            }
        });

        txtIdDatBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdDatBanActionPerformed(evt);
            }
        });

        txtidKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidKhachHangActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelRound1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbidHD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdDatBan, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbidKH)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtidKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(cmdExportBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelRound1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelRound1Layout.createSequentialGroup()
                            .addGap(43, 43, 43)
                            .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(cmdOk, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelRound1Layout.createSequentialGroup()
                                    .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbTongtien1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbTongtien, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbTongtien3, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtChietKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPhuPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(panelRound1Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbTongtien5)
                                        .addComponent(lbTongtien4, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbTongtien2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtTienCanTra, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTienTraLai, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(panelRound1Layout.createSequentialGroup()
                                    .addGap(89, 89, 89)
                                    .addComponent(cmdHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(30, Short.MAX_VALUE))
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbNgHD)
                .addGap(74, 74, 74))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound1Layout.createSequentialGroup()
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRound1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(lbTitle)
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbNgHD, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdExportBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbidHD, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbidKH, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtIdDatBan, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtidKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTongtien, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTongtien2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTienCanTra, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTongtien1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTongtien5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhuPhi, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTongtien3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbTongtien4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTienTraLai, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtChietKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdOk, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdDatBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdDatBanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdDatBanActionPerformed

    private void txtidKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidKhachHangActionPerformed

    private void txtTienKhachDuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienKhachDuaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTienKhachDuaActionPerformed

    private void txtTienTraLaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTienTraLaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTienTraLaiActionPerformed

    private void cmdOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOkActionPerformed
                try {
            // Lấy dữ liệu từ các text fields
            String idDatBan = txtIdDatBan.getText();
            String idKhachHang = txtidKhachHang.getText();
            double tongTien = Double.parseDouble(txtTongTien.getText());
            double phuPhi = Double.parseDouble(txtPhuPhi.getText());
            double chietKhau = Double.parseDouble(txtChietKhau.getText());
            double tienCanTra = Double.parseDouble(txtTienCanTra.getText());
            double tienKhachDua = Double.parseDouble(txtTienKhachDua.getText());
            double tienTraLai = Double.parseDouble(txtTienTraLai.getText());

            int idNhanVien = user;

            // Tạo đối tượng HoaDon
            HoaDon hoaDon = new HoaDon();
            hoaDon.setIdDatBan(Integer.parseInt(idDatBan));
            hoaDon.setIdKhachHang(Integer.parseInt(idKhachHang));
            hoaDon.setIdNhanVien(idNhanVien);
            hoaDon.setTongTien(tongTien);
            hoaDon.setPhuPhi(phuPhi);
            hoaDon.setChietKhau(chietKhau);
            hoaDon.setTongSauGiam(tienCanTra);
            hoaDon.setTrangThai(true); // Đã thanh toán

            // Gọi phương thức lưu thông tin vào cơ sở dữ liệu
            HoaDonRepository hoaDonRepository = new HoaDonRepository();
            hoaDonRepository.saveHoaDon(hoaDon);

            // Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");


        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi lưu hóa đơn!");
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmdOkActionPerformed
    private String removeVietnameseAccents(String text) {
        String normalizedText = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalizedText.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    private void cmdExportBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExportBillActionPerformed
    PDDocument document = new PDDocument();
    PDPage page = new PDPage();
    document.addPage(page);

    try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
        contentStream.newLineAtOffset(220, 750);
        contentStream.showText("Meo Meo Restaurant");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 18);
        contentStream.newLineAtOffset(250, 720);
        contentStream.showText("HOA DON THANH TOAN");
        contentStream.endText();

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 680);
        contentStream.showText("Ma Hoa Don: " + txtIdDatBan.getText());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Ngay: " + lbNgHD.getText());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Khach Hang: " + txtidKhachHang.getText());
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
        contentStream.newLineAtOffset(50, 620);
        contentStream.showText("Mon An");
        contentStream.newLineAtOffset(200, 0);
        contentStream.showText("So luong");
        contentStream.newLineAtOffset(100, 0);
        contentStream.showText("Thanh tien");
        contentStream.endText();

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int yPosition = 600;
        for (int i = 0; i < model.getRowCount(); i++) {
            String monAn = (String) model.getValueAt(i, 0);
            String soLuong = model.getValueAt(i, 1).toString();
            String thanhTien = model.getValueAt(i, 2).toString();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText(monAn);
            contentStream.newLineAtOffset(200, 0);
            contentStream.showText(soLuong);
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(thanhTien);
            contentStream.endText();
            yPosition -= 15;
        }

        contentStream.beginText();
        contentStream.newLineAtOffset(50, yPosition - 30);
        contentStream.showText("Tien mon an: " + txtTongTien.getText() + "d");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Phu phi: " + txtPhuPhi.getText() + "d");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Chiet khau: " + txtChietKhau.getText() + "d");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Tong tien: " + txtTienCanTra.getText() + "d");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Tien khach dua: " + txtTienKhachDua.getText() + "d");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Tien tra lai: " + txtTienTraLai.getText() + "d");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN, 16);
        contentStream.newLineAtOffset(200, yPosition - 90);
        contentStream.showText("THANK YOU FOR YOUR PURCHASE");
        contentStream.endText();

    } catch (IOException e) {
        Logger.getLogger(BillS_Form.class.getName()).log(Level.SEVERE, null, e);
    }

    // Ensure the ExportBill directory exists
    File exportDir = new File("ExportBill");
    if (!exportDir.exists()) {
        exportDir.mkdir();
    }

    // Save the PDF file in the ExportBill directory
    try {
        String filePath = "ExportBill/Bill_" + txtIdDatBan.getText() + ".pdf";
        document.save(filePath);
        document.close();
        JOptionPane.showMessageDialog(this, "Hóa đơn đã được xuất ra file PDF thành công!\nFile path: " + filePath);
    } catch (IOException e) {
        Logger.getLogger(BillS_Form.class.getName()).log(Level.SEVERE, null, e);
        JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xuất file PDF!");
    }
    }//GEN-LAST:event_cmdExportBillActionPerformed
    private double parseDouble(String text) {
        return text.isEmpty() ? 0 : Double.parseDouble(text);
    }
    private void resetForm() {
        txtIdDatBan.setText("");
        txtidKhachHang.setText("");
        txtTongTien.setText("");
        txtPhuPhi.setText("");
        txtChietKhau.setText("");
        txtTienCanTra.setText("");
        txtTienKhachDua.setText("");
        txtTienTraLai.setText("");
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Swing.Button cmdExportBill;
    private Swing.ButtonOutLine cmdHuy;
    private Swing.ButtonOutLine cmdOk;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lbNgHD;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbTongtien;
    private javax.swing.JLabel lbTongtien1;
    private javax.swing.JLabel lbTongtien2;
    private javax.swing.JLabel lbTongtien3;
    private javax.swing.JLabel lbTongtien4;
    private javax.swing.JLabel lbTongtien5;
    private javax.swing.JLabel lbidHD;
    private javax.swing.JLabel lbidKH;
    private Swing.PanelRound panelRound1;
    private javax.swing.JTextField txtChietKhau;
    private javax.swing.JTextField txtIdDatBan;
    private javax.swing.JTextField txtPhuPhi;
    private javax.swing.JTextField txtTienCanTra;
    private javax.swing.JTextField txtTienKhachDua;
    private javax.swing.JTextField txtTienTraLai;
    private javax.swing.JTextField txtTongTien;
    private javax.swing.JTextField txtidKhachHang;
    // End of variables declaration//GEN-END:variables
}
