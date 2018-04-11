package UI;

import Java.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Master_Salesman extends javax.swing.JDialog {

    private ArrayList<ListSalesman> list;
    private ListSalesman listSalesman;
    private DefaultTableModel tabel;
    private ResultSet hasil;
    private Connect connection;
    private int dd;

    public Master_Salesman() {
        initComponents();
//        connection = new Connect();
//        tabel = new DefaultTableModel(new String[]{"No", "Nama", "Contact", "Telepon", "Alamat", "Kota"}, 0);
//        jTable9.setModel(tabel);
//        jTable9.getColumnModel().getColumn(0).setPreferredWidth(5);
//        dd = jComboBox7.getSelectedIndex();
    }

    public Master_Salesman(java.awt.Frame parent, boolean modal, Connect connection) {
        super(parent, modal);
        initComponents();
        this.connection = connection;
//        code here
        jTable9.setModel(new DefaultTableModel(new String[]{"No", "Nama", "Contact", "Telepon", "Alamat", "Kota"}, 0));
        tampilTabel(1);
    }

    private void deleteTabel() {
        for (int i = 0; i < tabel.getRowCount(); i++) {
            tabel.removeRow(0);
        }
    }

    public String tampilTabel(int aktif) {
        String data = "SELECT kode_salesman, nama_salesman, contact_salesman, telepon_salesman, alamat, kota_salesman "
                + "FROM salesman "
                + (aktif != -1 ? "WHERE aktif_sales = " + aktif + "" : "") + "";
        try {
            hasil = connection.ambilData(data);
            setModel(hasil);
        } catch (Exception e) {
            System.out.println("Master_Salesman_Line_43_" + e.toString());
            System.out.println(data);
        }
        return data;
    }

    public void setModel(ResultSet hasil) {
        try {
            list = new ArrayList<>();
            tabel = new DefaultTableModel(new String[]{"No", "Nama", "Contact", "Telepon", "Alamat", "Kota"}, 0);
//            int no = 1;
            while (hasil.next()) {
                listSalesman = new ListSalesman();
                listSalesman.setNo(hasil.getInt("kode_salesman"));
                listSalesman.setNama_salesman(hasil.getString("nama_salesman"));
                listSalesman.setContact_salesman(hasil.getString("contact_salesman"));
                listSalesman.setTelepon_salesman(hasil.getString("telepon_salesman"));
                listSalesman.setAlamat_salesman(hasil.getString("alamat"));
                listSalesman.setKota_salesman(hasil.getString("kota_salesman"));
                list.add(listSalesman);
                tabel.addRow(new Object[]{
                    listSalesman.getNo(),
                    listSalesman.getContact_salesman(),
                    listSalesman.getTelepon_salesman(),
                    listSalesman.getAlamat_salesman(),
                    listSalesman.getKota_salesman()
                });
//                no++;
                listSalesman = null;
            }
        } catch (SQLException e) {
            System.out.println("Master_Salesman_Line_56_" + e.toString());
        } finally {
            jTable9.setModel(tabel);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jButton33 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jComboBox7 = new javax.swing.JComboBox<>();
        jButton29 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jTextField90 = new javax.swing.JTextField();
        jLabel184 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton33.setBackground(new java.awt.Color(71, 166, 227));
        jButton33.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton33.setText("Edit");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama", "Contact", "Telepon", "Alamat", "Kota"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(jTable9);

        jComboBox7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aktif ", "Tidak Aktif ", "Semua" }));
        jComboBox7.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBox7PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jButton29.setBackground(new java.awt.Color(71, 166, 227));
        jButton29.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton29.setText("Tambah Salesman");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton34.setBackground(new java.awt.Color(71, 166, 227));
        jButton34.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jButton34.setText("Login");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jTextField90.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cari", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));
        jTextField90.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField90KeyTyped(evt);
            }
        });

        jLabel184.setText("Refresh");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField90, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel184, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel184))
                            .addComponent(jTextField90, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton34)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jLabel45.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(49, 112, 143));
        jLabel45.setText("Salesman Form");

        jLabel47.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(49, 112, 143));
        jLabel47.setText("waktu");

        jLabel46.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(49, 112, 143));
        jLabel46.setText("tanggal");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel46)
                .addGap(57, 57, 57)
                .addComponent(jLabel47)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46)
                        .addComponent(jLabel47)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(832, 592));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        Master_Salesman_TambahSalesman ts = new Master_Salesman_TambahSalesman();
        ts.setVisible(true);
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        Master_Salesman_TambahSalesman ts = new Master_Salesman_TambahSalesman();
        ts.setVisible(true);
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        Master_Login log = new Master_Login();
        log.setVisible(true);
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jComboBox7PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox7PopupMenuWillBecomeInvisible
        dd = jComboBox7.getSelectedIndex();
        deleteTabel();
        switch (dd) {
            case 0:
                tampilTabel(1);
                break;
            case 1:
                tampilTabel(0);
                break;
            case 2:
                tampilTabel(-1);
                break;
            default:
                break;
        }
    }//GEN-LAST:event_jComboBox7PopupMenuWillBecomeInvisible

    private void jTextField90KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField90KeyTyped

    }//GEN-LAST:event_jTextField90KeyTyped

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
            java.util.logging.Logger.getLogger(Master_Salesman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Master_Salesman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Master_Salesman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Master_Salesman.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Master_Salesman().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JLabel jLabel184;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextField jTextField90;
    // End of variables declaration//GEN-END:variables
}
