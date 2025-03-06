package View;

import Component_Admin.MenuA;
import Event.EventMenuSelected;
import Form.MainForm;
import FormDatBan.BookingTable;
import FormDatBan.QuanLyLich_Form;
import FormDatBan.TableMenuS_Form;
import Form_Staff.KhachHang_Form;
import Form_Staff.LichSuHoaDon;
import Form_Staff.MenuManagement_Form;
import Form_Staff.ReservationManagement_Form;
import Form_Staff.StaffManagement_Form;
import Form_Staff.ThongKeDoanhThu_Form;
import Form_Staff.Voucher_Form;
import Model.ModelNguoiDung;
import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.config.DBConnect;
import net.miginfocom.swing.MigLayout;

public class Admin extends javax.swing.JFrame {

    private MigLayout layout;
    private MenuA menu;
    private MainForm main;
    private ModelNguoiDung user;

    public Admin() throws HeadlessException {
        initComponents();
        init();
        setTitle("Meo Meo Restaurant");
    }

    public Admin(ModelNguoiDung user) {
        this.user = user;
        initComponents();
        init();
        setTitle("Meo Meo Restaurant");
    }

    public void init() {
        layout = new MigLayout("fill", "0[]0[100%, fill]0", "0[fill, top]0");
        bg.setLayout(layout);
        menu = new MenuA();
        main = new MainForm();

        menu.addEvent(new EventMenuSelected() {
            @Override
            public void menuSelected(int menuIndex, int subMenuIndex) {
                switch (menuIndex) {
                    case 0 -> main.showForm(new ReservationManagement_Form());
                    case 1 -> main.showForm(new QuanLyLich_Form() );
                    case 2 -> main.showForm(new BookingTable(user.getUserID()));
                    case 3 -> main.showForm(new TableMenuS_Form(main));
                    case 4 -> {
                        try {
                            main.showForm(new StaffManagement_Form());
                        } catch (SQLException ex) {
                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    case 5 -> main.showForm(new KhachHang_Form());
                    case 6 -> main.showForm(new MenuManagement_Form());
                    
                    case 7 -> {
                        try {
                            main.showForm(new Voucher_Form());
                        } catch (Exception ex) {
                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    case 8 -> {
                        try {
                            main.showForm(new LichSuHoaDon());
                        } catch (SQLException ex) {
                            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    case 9 -> main.showForm(new ThongKeDoanhThu_Form());
                    case 10 -> dispose();
                    default -> {
                    }
                }
            }
        });

        
        
        
        menu.initMenuItem();
        bg.add(menu, "w 265!, spany 2"); // Span Y 2 cells
        bg.add(main, "w 100%, h 100%");
        main.showForm(new ReservationManagement_Form()); // Default form
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JLayeredPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bg.setBackground(new java.awt.Color(153, 153, 153));
        bg.setPreferredSize(new java.awt.Dimension(1200, 730));

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1321, Short.MAX_VALUE)
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 724, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 1321, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }

    public static void main(ModelNguoiDung user) {

        try {
            DBConnect.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin(user).setVisible(true);
            }
        });
    }
     

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane bg;
    // End of variables declaration//GEN-END:variables
}
