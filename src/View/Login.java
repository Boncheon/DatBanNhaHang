package View;

import Model.ModelLogin;
import Model.ModelNguoiDung;
import Service.ServiceUser;
import Swing.Button;
import Swing.MyPasswordField;
import Swing.MyTextField;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;

/**
 * Login form for the application.
 */
public class Login extends javax.swing.JFrame {
    private ModelNguoiDung user; // Model Tài khoản người dùng
    private String name; // Tên Khách Hàng
    private ModelLogin dataLogin; // Model thông tin đăng nhập
    private Icon hide;
    private Icon show;
    private char def;

    private ServiceUser serviceUser; // Service class for user operations

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        hide = new ImageIcon(getClass().getResource("/Icons/hide.png"));
        show = new ImageIcon(getClass().getResource("/Icons/view.png"));
        initLogin();
        serviceUser = new ServiceUser(); // Initialize the service class
    }

    private void initLogin() {
        txtPassword.setSuffixIcon(show);
        def = txtPassword.getEchoChar();
        txtPassword.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (txtPassword.getSuffixIcon().equals(hide)) {
                    txtPassword.setSuffixIcon(show);
                    txtPassword.setEchoChar((char) 0);
                } else {
                    txtPassword.setSuffixIcon(hide);
                    txtPassword.setEchoChar(def);
                }
            }
        });

        // Button "Quên mật khẩu"
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new Swing.PanelRound();
        jLabel1 = new javax.swing.JLabel();
        txtEmail = new Swing.MyTextField();
        txtPassword = new Swing.MyPasswordField();
        cmdForget = new javax.swing.JButton();
        cmd = new Swing.Button();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 0, 153));
        jLabel1.setText("ĐĂNG NHẬP");

        txtEmail.setHint("Email");
        txtEmail.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/mail.png"))); // NOI18N

        txtPassword.setHint("Mật khẩu");
        txtPassword.setPrefixIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/pass.png"))); // NOI18N

        cmdForget.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmdForget.setText("Quên mật khẩu của bạn? ");

        cmd.setBackground(new java.awt.Color(153, 0, 153));
        cmd.setForeground(new java.awt.Color(255, 255, 255));
        cmd.setText("ĐĂNG NHẬP");
        cmd.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cmd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon_Staff/Không tiêu đề(1).jpg"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 0, 153));
        jLabel3.setText("Nhà hàng Meo Meo - 235 Văn Tiến Dũng, Bắc Từ Liêm, Hà Nội");

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRound1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRound1Layout.createSequentialGroup()
                                .addGap(0, 63, Short.MAX_VALUE)
                                .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound1Layout.createSequentialGroup()
                                        .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(121, 121, 121))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound1Layout.createSequentialGroup()
                                        .addGroup(panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmdForget)
                                            .addComponent(cmd, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(152, 152, 152))))
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(panelRound1Layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmdForget)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmd, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdActionPerformed
    String email = txtEmail.getText().trim();
    String password = String.valueOf(txtPassword.getPassword());
    dataLogin = new ModelLogin(email, password);

    try {
        user = serviceUser.login(dataLogin);
        if (user != null) {
            if ("Quản lý".equals(user.getRole())) {
                // Login successful, and user is a manager, open the Staff form
                JOptionPane.showMessageDialog(this, "Welcome " + user.getEmail(), "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                Admin.main(user);
                this.dispose(); // Close the login window
            }else if("Lễ tân".equals(user.getRole())){
                JOptionPane.showMessageDialog(this, "Welcome " + user.getEmail(), "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                Staff.main(user);
                this.dispose();
            }
            else {
                // User is not a manager, show an error message
                JOptionPane.showMessageDialog(this, "Bạn không có quyền cần thiết để truy cập ứng dụng này.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Login failed, show an error message
            JOptionPane.showMessageDialog(this, "Email hoặc mật khẩu không hợp lệ", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        // Handle SQL exceptions
        JOptionPane.showMessageDialog(this, "An error occurred while trying to log in: " + e.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
    }
    
    }//GEN-LAST:event_cmdActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private Swing.Button cmd;
    private javax.swing.JButton cmdForget;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private Swing.PanelRound panelRound1;
    private Swing.MyTextField txtEmail;
    private Swing.MyPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}
