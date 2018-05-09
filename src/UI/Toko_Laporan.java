package UI;

import Java.Connect;
import Java.ListTokoLaporanToko;
import com.toedter.calendar.JDateChooser;
import java.beans.PropertyChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class arraysort implements Comparator<ListTokoLaporanToko> {

    @Override
    public int compare(ListTokoLaporanToko a, ListTokoLaporanToko b) {
        return b.getTglsort() - a.getTglsort();
    }
}

public class Toko_Laporan extends javax.swing.JDialog {

    private SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dt2 = new SimpleDateFormat("dd-MM-yyyy");
    private DefaultTableModel laporanPenjualan, laporanReturn;
    private ResultSet hasil;
    private Connect connection;
    private PreparedStatement PS;
    private String tgl0, tgl1, tgl2;
//    
    ArrayList<ListTokoLaporanToko> list;
    ListTokoLaporanToko laporanToko;

    public Toko_Laporan() {
//        super(parent, modal);
        initComponents();
//        this.setLocationRelativeTo(null);
//        laporanPenjualan = new DefaultTableModel(new String[]{"No.", "Kode", "Nama", "Qty", "Satuan", "Harga Jual",
//            "HPP", "Total Jual", "Total HPP", "LabaRugi", "%"}, 0);
//        jTable1.setModel(laporanPenjualan);
//        jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
//
////        
//        list = new ArrayList<>();
//        getData();

    }

    public Toko_Laporan(java.awt.Frame parent, boolean modal, Connect connection) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
//        laporanPenjualan = new DefaultTableModel(new String[]{"No.", "Kode", "Nama", "Qty", "Satuan", "Harga Jual", "HPP", "Total Jual", "Total HPP", "LabaRugi", "%"}, 0);
//        jTable1.setModel(laporanPenjualan);
//        jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
        
//        
        this.connection = connection;
//        list = new ArrayList<>();
        tgl0 = getCurrentDate();
        getData(true, tgl0, tgl0);
        jLabel3.setText(getCurrentDate2());
        
        
        
    }
    //tanggal dengan format yyyy-MM-dd (untuk database)
    private String getCurrentDate() {
        String date = "";
        java.util.Date d = new java.util.Date();
        date = dt.format(d);
        return date;
    }
    //tanggal dengan format dd-MM-yyyy
    private String getCurrentDate2(){
        String date2 = "";
        java.util.Date d = new java.util.Date();
        date2 = dt2.format(d);
        return date2;
    }

    private void getData(boolean currentDate, String tgl1, String tgl2) {
        list = new ArrayList<>();
        try {
            laporanPenjualan = new DefaultTableModel(new String[]{"No.", "Kode", "Nama", "Qty", "Satuan", "Harga Jual", "HPP", "Total Jual", "Total HPP", "LabaRugi", "%"}, 0);
//            penjualan
            String sql = "SELECT PD.no_faktur_toko_penjualan, B.nama_barang, P.tgl_toko_penjualan, PD.jumlah_barang, K.nama_konversi, B.harga_jual_2_barang, PD.harga_barang "
                    + "FROM toko_penjualan_detail PD, barang B, konversi K, toko_penjualan P "
                    + "WHERE PD.kode_barang = B.proud_code AND PD.kode_barang_konversi = K.kode_konversi AND "
                    + "P.no_faktur_toko_penjualan = PD.no_faktur_toko_penjualan AND "
                    + (currentDate == true ? "CAST(P.tgl_toko_penjualan as date) = '"+tgl1+"' " : 
                    "P.tgl_toko_penjualan BETWEEN '"+tgl1+"' AND '"+tgl2+"' ")+" ";
//            System.out.println(sql);
            hasil = connection.ambilData(sql);
            setData(hasil);
//            return
            sql = "SELECT PDR.no_faktur_toko_penjualan_return, B.nama_barang, PR.tgl_toko_penjualan_return, "
                    + "PDR.jumlah_barang, K.nama_konversi, B.harga_jual_2_barang, PDR.harga_barang "
                    + "FROM toko_penjualan_detail_return PDR, barang B, konversi K, toko_penjualan_return PR "
                    + "WHERE PDR.kode_barang = B.proud_code AND PDR.kode_barang_konversi = K.kode_konversi "
                    + "AND PR.no_faktur_toko_penjualan_return = PDR.no_faktur_toko_penjualan_return AND "
                    + (currentDate == true ? "CAST(PR.tgl_toko_penjualan_return as date) = '"+tgl1+"' " :
                    "PR.tgl_toko_penjualan_return BETWEEN '" + tgl1 + "' AND '" + tgl2 + "' ")+" ";
//            System.out.println(sql);
            hasil = connection.ambilData(sql);
            setData(hasil);
        } catch (Exception e) {
            System.out.println(e.toString() + "_line_62");
        } finally {
            showData();
        }
    }

    private void setData(ResultSet hasil) {
        try {
            while (hasil.next()) {
                laporanToko = new ListTokoLaporanToko();
                laporanToko.setNo_faktur(hasil.getString(1));
                laporanToko.setNamabarang(hasil.getString(2));
                laporanToko.setTanggal(hasil.getString(3));
                laporanToko.setTgl(hasil.getDate(3));
                laporanToko.setJumlah(hasil.getInt(4));
                laporanToko.setNamakonversi(hasil.getString(5));
                laporanToko.setHargajual(hasil.getInt(6));
                laporanToko.setTotaljual(hasil.getInt(7));
                list.add(laporanToko);
                laporanToko = null;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    private void showData() {
        int no = 0;
        
//        for (int i = 0; i < list.size(); i++) {
//            laporanToko = list.get(i);
//            laporanPenjualan.addRow(new Object[]{
//                laporanToko.getNo_faktur(),
//                laporanToko.getNamabarang(),
//                laporanToko.getTanggal()
//            });
//        }
//        sort here
        Collections.sort(list, new arraysort());
        for (int i = 0; i < list.size(); i++) {
            laporanToko = list.get(i);
            laporanPenjualan.addRow(new Object[]{
                ++no,
                laporanToko.getNo_faktur(),
                laporanToko.getNamabarang(),
                laporanToko.getJumlah(),
                laporanToko.getNamakonversi(),
                laporanToko.getHargajual(),
                0,
                laporanToko.getTotaljual(),
                0,
                0,
                0
//                laporanToko.getTanggal()
            });
        }
        jTable1.setModel(laporanPenjualan);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
    }
    
    private void deleteTabel(){
        int baris = laporanPenjualan.getRowCount();
        for (int i = 0; i < baris; i++) {
            laporanPenjualan.removeRow(0);
        }
    }

    private Boolean compareDate() {
        boolean kembali = false;
        try {
//            String sDate1 = jDateChooser1.getDateFormatString();
//            Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(sDate1);
        } catch (Exception e) {
            System.out.println("Toko_Laporan/btnTampilkanLapJual - " + e);
        }
        return kembali;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        jCheckBox1.setText("Tanggal");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel1.setText("s.d");

        jButton1.setText("Print");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel2.setText("Detail Laba Rugi");

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel3.setText("Tanggal s/d Tanggal");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Kode", "Nama", "Qty", "Satuan", "Harga Jual", "HPP", "Total Jual", "Total HPP", "LabaRugi", "%"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 761, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jDateChooser1.setDateFormatString("dd-MM-yyyy");
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jDateChooser2.setDateFormatString("dd-MM-yyyy");
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jLabel4.setText("Kriteria");

        jButton2.setText("Print Faktur");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextField1)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1))
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox1)
                        .addComponent(jLabel1)
                        .addComponent(jButton1))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        if (jCheckBox1.isSelected()) {
            if (jDateChooser1.getDate() != null && jDateChooser2.getDate() != null) {
                String tanggal1 = dt.format(jDateChooser1.getDate());
                String tanggal2 = dt.format(jDateChooser2.getDate());
                getData(false, tanggal1, tanggal2);
                jLabel3.setText(dt2.format(jDateChooser1.getDate()) + " s/d " + dt2.format(jDateChooser2.getDate()));
            }
        }
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        if (jCheckBox1.isSelected()) {
            if (jDateChooser1.getDate() != null && jDateChooser2.getDate() != null) {
                String tanggal1 = dt.format(jDateChooser1.getDate());
                String tanggal2 = dt.format(jDateChooser2.getDate());
                getData(false, tanggal1, tanggal2);
                jLabel3.setText(dt2.format(jDateChooser1.getDate()) + " s/d " + dt2.format(jDateChooser2.getDate()));
            }
        }
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if(!jCheckBox1.isSelected()){
            tgl0 = getCurrentDate();
            getData(true, tgl0, tgl0);
            jLabel3.setText(getCurrentDate2());
        } else{
            if (jDateChooser1.getDate() != null && jDateChooser2.getDate() != null) {
                tgl1 = dt.format(jDateChooser1.getDate());
                tgl2 = dt.format(jDateChooser2.getDate());
                getData(false, tgl1, tgl2);
                jLabel3.setText(dt2.format(jDateChooser1.getDate()) + " s/d " + dt2.format(jDateChooser2.getDate()));
            }
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

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
            java.util.logging.Logger.getLogger(Toko_Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Toko_Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Toko_Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Toko_Laporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Toko_Laporan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
