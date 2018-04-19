package UI;

import Java.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Master_Customer extends javax.swing.JDialog {

//    Object
    private Connect connection;
//    private ArrayList<ListCustomer> list;
    private ArrayList<myArrlist> arrlist;
    private myArrlist myarrlist;
    private ListCustomer listCustomer;
    private ListSalesman listSalesman;
    private ResultSet hasil;
    private PreparedStatement PS;
    private modelTabelCustomer model;
//    Var

    public Master_Customer() {
        initComponents();
    }

    public Master_Customer(java.awt.Frame parent, boolean modal, Connect connection) {
        super(parent, modal);
        initComponents();
//        code here
        this.connection = connection;
        tampilTabel("1");
    }

    private void tampilTabel(String param) {
        int i = 0;
        try {
            String datax = "SELECT * "
                    + "from customer c, salesman s "
                    + "where " + (param.equals("1") || param.equals("0") ? "c.status_customer =" + Integer.parseInt(param) : "c.nama_customer like '%" + param + "%'") + " and c.kode_salesman = s.kode_salesman";
            hasil = connection.ambilData(datax);
            setModel(hasil);
        } catch (NumberFormatException e) {
            System.out.println("Error tampil tabel");
        }
    }

    private int getNewId() {
        String sql = "SELECT kode_customer FROM customer ORDER BY kode_customer DESC LIMIT 1";
        int id = 0;
        try {
            hasil = connection.ambilData(sql);
            while (hasil.next()) {
                id = hasil.getInt("kode_customer") + 1;
            }
        } catch (SQLException e) {
        }
        return id;
    }

    private void setModel(ResultSet hasil) {
        try {
            myarrlist = new myArrlist();
//            list = new ArrayList<>();
            while (hasil.next()) {
                listCustomer = new ListCustomer();
                this.listCustomer.setKode_customer(hasil.getInt("kode_customer"));
                this.listCustomer.setNama_customer(hasil.getString("nama_customer"));
                this.listCustomer.setKota_customer(hasil.getString("kota_customer"));
                this.listCustomer.setAlamat_customer(hasil.getString("alamat_customer"));
                this.listCustomer.setProvinsi_customer(hasil.getString("provinsi_customer"));
                this.listCustomer.setTelepon_customer(hasil.getString("telepon_customer"));
                this.listCustomer.setContact_customer(hasil.getString("contact_customer"));
                this.listCustomer.setHari_tagihan(hasil.getString("hari_tagihan"));
                this.listCustomer.setStatus_customer(hasil.getInt("status_customer"));
                this.listCustomer.setKode_salesman(hasil.getInt("kode_salesman"));
                this.myarrlist.getLc().add(listCustomer);
                listCustomer = null;

                listSalesman = new ListSalesman();
                listSalesman.setNama_salesman(hasil.getString("nama_salesman"));
                this.myarrlist.getLs().add(listSalesman);
                listSalesman = null;
            }
            model = new modelTabelCustomer(myarrlist.getLc());
            jTable7.setModel(model);
        } catch (SQLException e) {
            System.out.println("Master_Customer_Line_54_" + e.toString());
        }
    }

    private void insertData(ListCustomer listCustomer) {
        PS = null;
        try {
            String sql = "insert into customer values(?,?,?,?,?,?,?,?,?,?)";
            PS = connection.Connect().prepareStatement(sql);
            PS.setInt(1, listCustomer.getKode_customer());
            PS.setString(2, listCustomer.getNama_customer());
            PS.setString(3, listCustomer.getKota_customer());
            PS.setString(4, listCustomer.getAlamat_customer());
            PS.setString(5, listCustomer.getProvinsi_customer());
            PS.setString(6, listCustomer.getTelepon_customer());
            PS.setString(7, listCustomer.getContact_customer());
            PS.setString(8, listCustomer.getHari_tagihan());
            PS.setInt(9, listCustomer.getStatus_customer());
            PS.setInt(10, listCustomer.getKode_salesman());
            PS.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Master_Customer_Line_84_" + e.toString());
        }
    }

    private void updateData(ListCustomer listCustomer) {
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel14 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Kriteria");

        jTextField5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.gray, java.awt.Color.lightGray, java.awt.Color.lightGray));
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.gray, java.awt.Color.gray, java.awt.Color.lightGray));

        jLabel16.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("DAFTAR CUSTOMER");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel16)
                .addGap(3, 3, 3))
        );

        jSeparator2.setForeground(new java.awt.Color(153, 153, 153));

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode", "Nama", "Contact", "Telepon", "Wilayah", "Alamat"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable7MouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTable7);

        jCheckBox1.setText("Tampilkan Customer Deactive");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
        });

        jSeparator3.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator4.setForeground(new java.awt.Color(153, 153, 153));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/if_manilla-folder-new_23456.png"))); // NOI18N
        jLabel18.setText("F2-New");
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/if_gtk-edit_20500.png"))); // NOI18N
        jLabel19.setText("F3-Edit");
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Gambar/cancel (3).png"))); // NOI18N
        jLabel20.setText("F5-Delete");
        jLabel20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel20MouseClicked(evt);
            }
        });

        jSeparator5.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator6.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 864, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jLabel18)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20)))
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(2, 2, 2)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jCheckBox1)
                .addGap(4, 4, 4)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        Master_Customer_NewEdit mcne = new Master_Customer_NewEdit(new Awal(rootPaneCheckingEnabled), rootPaneCheckingEnabled, listCustomer, getNewId());
        mcne.setLocationRelativeTo(new Awal(rootPaneCheckingEnabled));
        mcne.setVisible(true);
    }//GEN-LAST:event_jLabel18MouseClicked

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
//        listCustomer = list.get(jTable7.getSelectedRow());
        listCustomer = (ListCustomer) myarrlist.getLc().get(jTable7.getSelectedRow());
        listSalesman = (ListSalesman) myarrlist.getLs().get(jTable7.getSelectedRow());
        Object[] temp = {listCustomer, listSalesman};
        Master_Customer_NewEdit mcne = new Master_Customer_NewEdit(new Awal(rootPaneCheckingEnabled), rootPaneCheckingEnabled, temp, true);
        mcne.setLocationRelativeTo(new Awal(rootPaneCheckingEnabled));
        mcne.setVisible(true);
    }//GEN-LAST:event_jLabel19MouseClicked

    private void jTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable7MouseClicked
//        Master_Customer_KartuPiutang mckp = new Master_Customer_KartuPiutang();
//        mckp.setVisible(true);
    }//GEN-LAST:event_jTable7MouseClicked

    private void jLabel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseClicked
        int hapus = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin Menghapus data ini ?", "Konfimasi Tolak Hapus Data", JOptionPane.OK_CANCEL_OPTION);
        if (hapus == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(null, "Data sudah dihapus.");
        }
    }//GEN-LAST:event_jLabel20MouseClicked

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
        if (jCheckBox1.isSelected()) {
            tampilTabel("0");
        } else {
            tampilTabel("1");
        }
    }//GEN-LAST:event_jCheckBox1MouseClicked

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        tampilTabel(jTextField5.getText());
    }//GEN-LAST:event_jTextField5KeyReleased

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Master_Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Master_Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Master_Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Master_Customer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Master_Customer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
