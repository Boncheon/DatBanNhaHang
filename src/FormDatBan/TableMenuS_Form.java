
package FormDatBan;

import Service.ServiceStaff;
import Model.ModelBan;
import Model.ModelNhanVien;
import Component_Staff.CardBanS;
import Form.MainForm;
import Swing_Customer_ScrollBar.ScrollBarCustom;
import Swing.WrapLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;


public class TableMenuS_Form extends javax.swing.JPanel {
    private ServiceStaff serviceS;
    private ArrayList<ModelBan> list;
    private ModelNhanVien staff;
    private final MainForm main;

    public TableMenuS_Form(MainForm main) {
        this.main=main;
        serviceS=new ServiceStaff();
        initComponents();
        init();
    }

    public void init(){
        panel.setLayout(new WrapLayout(WrapLayout.LEADING,20,20));
        txtSearch.setHint("Tìm kiếm bàn . . .");
        jScrollPane1.setVerticalScrollBar(new ScrollBarCustom());
        //Thêm data cho Menu
        initMenuTable();

                lbTitle.setText("Quản lý Bàn");
                lbTitle.setIcon(new ImageIcon(getClass().getResource("/Icons/one.png")));

          
 
    }
    public void initMenuTable(){
        try {
            list = serviceS.MenuTable();
        } catch (SQLException ex) {
            Logger.getLogger(TableMenuS_Form.class.getName()).log(Level.SEVERE, null, ex);
        }
            for(ModelBan data:list){
            panel.add(new CardBanS(staff,data,main));

            }
    }
    public void searchTable(String txt){
        panel.removeAll();
        for(ModelBan data:list){
            if(data.getName().toLowerCase().contains(txt.toLowerCase())){
                panel.add(new CardBanS(staff,data,main));
            }
        }
        panel.repaint();
        panel.revalidate();
    }
    public void initMenuTableState(String txt) throws SQLException{
            list=serviceS.MenuTableState(txt);
            panel.removeAll();
            for(ModelBan data:list){   
            panel.add(new CardBanS(staff,data,main));
            }
            

        panel.repaint();
        panel.revalidate();
    }
    
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        txtSearch = new Swing.MyTextField();
        jLabel2 = new javax.swing.JLabel();
        state1 = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(247, 247, 247));

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.setBackground(new java.awt.Color(215, 221, 232));

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 905, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 536, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panel);

        lbTitle.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(108, 91, 123));
        lbTitle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/one.png"))); // NOI18N
        lbTitle.setText("Quản lý Bàn");
        lbTitle.setIconTextGap(10);

        txtSearch.setSuffixIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Search.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(108, 91, 123));
        jLabel2.setText("Hiển thị theo");

        state1.setEditable(true);
        state1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        state1.setForeground(new java.awt.Color(108, 91, 123));
        state1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Còn trống", "Đang dùng bữa", "Đã đặt trước" }));
        state1.setToolTipText("Sắp xếp");
        state1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(164, 145, 145), 2));
        state1.setFocusable(false);
        state1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                state1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbTitle)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(state1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(state1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        searchTable(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtSearchMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtSearchMouseEntered
        searchTable(txtSearch.getText().trim());
    }//GEN-LAST:event_txtSearchMouseEntered

    private void state1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_state1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_state1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel panel;
    private javax.swing.JComboBox<String> state1;
    private Swing.MyTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
