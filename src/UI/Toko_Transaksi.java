/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Java.Connect;
import Java.ListSalesman;
import Java.ListTokoTransaksi;
import com.sun.glass.events.KeyEvent;
import java.awt.Component;
import static java.awt.event.KeyEvent.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.CellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
//import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Toko_Transaksi extends javax.swing.JDialog {

    private ArrayList<ListTokoTransaksi> list;
    private ListTokoTransaksi listTokoTransaksi;
    private DefaultTableModel tabel;
    Connect connection, connection1;
    ResultSet rs, rs1, rs2 = null;
    private PreparedStatement PS;
    private TableModel model;
    private ResultSet hasil, hasil1;
    private boolean merahAktif = false;

    public Toko_Transaksi() {
        initComponents();
    }

    public Toko_Transaksi(java.awt.Frame parent, boolean modal, Connect connection) {
        super(parent, modal);
        initComponents();
        panelMerah.setVisible(false);
        this.connection = connection;
//        AutoCompleteDecorator.decorate(comCustomer);
//        AutoCompleteDecorator.decorate(comBarang);
        setTanggal();
        loadNumberTable();
        FillComboCustomer();
        loadComTableBarang();
    }

    private void FillComboCustomer() {
        String sql = "";
        try {
            sql = "SELECT nama_customer from customer order by nama_customer asc";
            PS = connection.Connect().prepareStatement(sql);
            rs = PS.executeQuery();
            while (rs.next()) {
                String name = rs.getString("nama_customer");
                comCustomer.addItem(name);
            }
        } catch (Exception e) {
            System.out.println("ERROR -> " + e.getMessage());
        } finally {

        }
    }

    private void loadNumberTable() {
        int baris = jTableTransaksi.getRowCount();
        for (int a = 0; a < baris; a++) {
            String nomor = String.valueOf(a + 1);
            jTableTransaksi.setValueAt(nomor + ".", a, 0);
        }
    }

    private void loadComTableBarang() {
        try {
            String sql = "select * from barang order by nama_barang asc";
            PS = connection.Connect().prepareStatement(sql);
            rs = PS.executeQuery();
            while (rs.next()) {
                String name = rs.getString(4);
                comBarang.addItem(name);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Eror" + e);
        }

    }
    
    private void setTanggal() {
        try {
            Calendar ca = new GregorianCalendar();
            String day = ca.get(Calendar.DAY_OF_MONTH) + "";
            String month = ca.get(Calendar.MONTH) + 1 + "";
            String year = ca.get(Calendar.YEAR) + "";

            if (day.length() == 1) {
                day = "0" + day;
            }
            if (month.length() == 1) {
                month = "0" + month;
            }

            String dd = year + "-" + month + "-" + day;

            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dd);
            jDateChooser1.setDate(date);
        } catch (ParseException ex) {
            Logger.getLogger(Toko_Transaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//    private void setModel(ResultSet hasil) {
//        try {
//            list = new ArrayList<>();
//            while (hasil.next()) {
//                this.listBarang = new ListBarang();
//                this.listBarang(hasil.getInt("kode_pegawai"));
//                this.listPegawai.setKode_unik(hasil.getInt("kode_unik"));
//                this.listPegawai.setNama_pegawai(hasil.getString("nama_pegawai"));
//                this.listPegawai.setKode_lokasi(hasil.getString("nama_lokasi"));
//                this.listPegawai.setAlamat_pegawai(hasil.getString("alamat_pegawai"));
//                this.listPegawai.setKota_pegawai(hasil.getString("kota_pegawai"));
//                this.listPegawai.setTelepon_pegawai(hasil.getString("telepon_pegawai"));
//                this.listPegawai.setContact_pegawai(hasil.getString("contact_pegawai"));
//                this.listPegawai.setStatus_pegawai(hasil.getInt("status_pegawai"));
//                list.add(listPegawai);
//                listPegawai = null;
//            }
//            model = new modelTabelPegawai(list);
//            jTable6.setModel(model);
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, e);
//        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dPembayaran = new javax.swing.JDialog();
        jLabel10 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        bBatal = new javax.swing.JButton();
        bSimpan = new javax.swing.JButton();
        comBarang = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jTotal = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JSeparator();
        jTextField11 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableTransaksi = new javax.swing.JTable();
        bPrintTransaksi = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        jComboBox4 = new javax.swing.JComboBox();
        txtNama = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        bExitTransaksi = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jTextField17 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        bClearTransaksi = new javax.swing.JButton();
        bSaveTransaksi = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txtAlamat = new javax.swing.JTextField();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel26 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        panelMerah = new javax.swing.JPanel();
        jTextField93 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        jTextField94 = new javax.swing.JTextField();
        jTextField95 = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jTextField96 = new javax.swing.JTextField();
        jTextField97 = new javax.swing.JTextField();
        jLabel99 = new javax.swing.JLabel();
        jTextField98 = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        jTextField99 = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        comCustomer = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        buttonMerah = new javax.swing.JButton();

        dPembayaran.setTitle("Pembayaran");
        dPembayaran.setBackground(new java.awt.Color(255, 255, 255));
        dPembayaran.setResizable(false);
        dPembayaran.setSize(new java.awt.Dimension(383, 270));
        dPembayaran.setType(java.awt.Window.Type.POPUP);

        jLabel10.setText("Total Pembelian");

        jTextField2.setEditable(false);
        jTextField2.setText("0");

        jLabel11.setText("Pembayaran");

        jLabel12.setText("Jenis Pembayaran");

        jTextField4.setEditable(false);
        jTextField4.setText("0");

        jLabel14.setText("Kembalian");

        bBatal.setText("Batal");
        bBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBatalActionPerformed(evt);
            }
        });

        bSimpan.setText("Simpan");

        javax.swing.GroupLayout dPembayaranLayout = new javax.swing.GroupLayout(dPembayaran.getContentPane());
        dPembayaran.getContentPane().setLayout(dPembayaranLayout);
        dPembayaranLayout.setHorizontalGroup(
            dPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dPembayaranLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addGroup(dPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField4)
                    .addComponent(jComboBox1, 0, 199, Short.MAX_VALUE)
                    .addComponent(jTextField2)
                    .addComponent(jTextField3))
                .addContainerGap())
            .addGroup(dPembayaranLayout.createSequentialGroup()
                .addGap(113, 113, 113)
                .addComponent(bSimpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bBatal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dPembayaranLayout.setVerticalGroup(
            dPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dPembayaranLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(dPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bBatal)
                    .addComponent(bSimpan))
                .addContainerGap())
        );

        comBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Pilih Barang-" }));
        comBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comBarangActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(255, 153, 0));

        jPanel5.setBackground(new java.awt.Color(255, 204, 51));

        jPanel6.setBackground(new java.awt.Color(153, 51, 0));
        jPanel6.setDoubleBuffered(false);

        jTotal.setBackground(new java.awt.Color(153, 51, 0));
        jTotal.setFont(new java.awt.Font("Impact", 1, 48)); // NOI18N
        jTotal.setForeground(new java.awt.Color(255, 204, 51));
        jTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jTotal.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTotal)
        );

        jPanel7.setBackground(new java.awt.Color(255, 204, 51));
        jPanel7.setDoubleBuffered(false);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(153, 51, 0));
        jLabel15.setText("Total Bayar");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jLabel22.setText("No. Order");

        jTextField16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField16.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField16.setText("Jumlah Qty : 0");

        jTextField13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel28.setText("Grand Total");

        jTableTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Kode", "Nama", "Satuan (1/2/3)", "Qty", "J.Harga (1/2/3)", "Harga", "Sub Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableTransaksiKeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(jTableTransaksi);
        if (jTableTransaksi.getColumnModel().getColumnCount() > 0) {
            jTableTransaksi.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comBarang));
        }

        bPrintTransaksi.setText("Print");

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("Potongan");

        jTextField15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel30.setText("Sub Total");

        jTextField14.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel21.setText("Tanggal");

        jLabel20.setText("Alamat");

        bExitTransaksi.setMnemonic(VK_ESCAPE);
        bExitTransaksi.setText("Esc - Exit");
        bExitTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExitTransaksiActionPerformed(evt);
            }
        });

        jCheckBox1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBox1.setText("Verifikasi Administrator");

        jTextField17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField17.setText("Jumlah Item : 0");

        jLabel19.setText("Nama");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        jLabel23.setText("No. Faktur");

        jLabel25.setText("T.O.P");

        jLabel18.setText("Customer");

        bClearTransaksi.setText("F9 - Clear");
        bClearTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bClearTransaksiActionPerformed(evt);
            }
        });

        bSaveTransaksi.setMnemonic(VK_F12);
        bSaveTransaksi.setText("F12 - Save");
        bSaveTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSaveTransaksiActionPerformed(evt);
            }
        });

        jLabel27.setText("Keterangan");

        jCheckBox2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBox2.setText("Non Denda");

        jLabel26.setText("Staf");

        jLabel31.setText("Kasir");

        panelMerah.setBackground(new java.awt.Color(153, 0, 0));

        jTextField93.setBackground(new java.awt.Color(255, 204, 153));
        jTextField93.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Harga Jual 1");

        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 255, 255));
        jLabel96.setText("Harga Jual 2");

        jTextField94.setBackground(new java.awt.Color(255, 204, 153));
        jTextField94.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField95.setBackground(new java.awt.Color(255, 204, 153));
        jTextField95.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel97.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(255, 255, 255));
        jLabel97.setText("No Jual");

        jLabel98.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(255, 255, 255));
        jLabel98.setText("Tanggal");

        jTextField96.setBackground(new java.awt.Color(255, 204, 153));
        jTextField96.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTextField97.setBackground(new java.awt.Color(255, 204, 153));
        jTextField97.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel99.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(255, 255, 255));
        jLabel99.setText("Hrg. Jual F.");

        jTextField98.setBackground(new java.awt.Color(255, 204, 153));
        jTextField98.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel100.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(255, 255, 255));
        jLabel100.setText("Qty.");

        jTextField99.setBackground(new java.awt.Color(255, 204, 153));
        jTextField99.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel101.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(255, 255, 255));
        jLabel101.setText("Harga Jual 3");

        javax.swing.GroupLayout panelMerahLayout = new javax.swing.GroupLayout(panelMerah);
        panelMerah.setLayout(panelMerahLayout);
        panelMerahLayout.setHorizontalGroup(
            panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMerahLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMerahLayout.createSequentialGroup()
                        .addComponent(jLabel101)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField99, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMerahLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField93))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMerahLayout.createSequentialGroup()
                        .addComponent(jLabel96)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField94))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMerahLayout.createSequentialGroup()
                        .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel97)
                            .addComponent(jLabel99))
                        .addGap(27, 27, 27)
                        .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField95, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addComponent(jTextField97))))
                .addGap(18, 18, 18)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel98)
                    .addComponent(jLabel100))
                .addGap(13, 13, 13)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField98, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                    .addComponent(jTextField96))
                .addGap(5, 5, 5))
        );
        panelMerahLayout.setVerticalGroup(
            panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMerahLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField98, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(jTextField93, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel100)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel96)
                    .addComponent(jTextField94, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel98)
                    .addComponent(jTextField96, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(jTextField99, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel97)
                    .addComponent(jTextField95, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel99)
                    .addComponent(jTextField97, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jTextField1.setText("TOKO");
        jTextField1.setEnabled(false);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Lokasi");

        jTextField5.setText("TUNAI");
        jTextField5.setEnabled(false);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        comCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comCustomerActionPerformed(evt);
            }
        });

        buttonMerah.setText(">");
        buttonMerah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMerahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator19, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 406, Short.MAX_VALUE)
                        .addComponent(jLabel30)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jLabel29)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel28)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox2)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(bSaveTransaksi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bClearTransaksi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bPrintTransaksi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bExitTransaksi))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel18)
                                            .addComponent(jLabel19)
                                            .addComponent(jLabel20))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtNama)
                                            .addComponent(txtAlamat)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(comCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(buttonMerah))))
                                    .addComponent(jCheckBox1))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField11))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel22)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel21)
                                                .addGap(31, 31, 31)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jComboBox4, 0, 151, Short.MAX_VALUE)
                                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel1))
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField12)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                                    .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)))
                            .addComponent(jSeparator18))
                        .addGap(10, 10, 10)
                        .addComponent(panelMerah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bSaveTransaksi)
                            .addComponent(bClearTransaksi)
                            .addComponent(bPrintTransaksi)
                            .addComponent(bExitTransaksi))
                        .addGap(24, 24, 24)
                        .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel25)
                                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(buttonMerah))
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel26)
                                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel20)
                                    .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23)
                                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1))
                                .addGap(5, 5, 5)
                                .addComponent(jCheckBox1))
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(panelMerah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bExitTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExitTransaksiActionPerformed
        this.dispose();
    }//GEN-LAST:event_bExitTransaksiActionPerformed

    private void bClearTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bClearTransaksiActionPerformed

    }//GEN-LAST:event_bClearTransaksiActionPerformed

    private void bSaveTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveTransaksiActionPerformed
        dPembayaran.show();
        dPembayaran.setLocationRelativeTo(null);
    }//GEN-LAST:event_bSaveTransaksiActionPerformed

    private void bBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalActionPerformed
        dPembayaran.dispose();
    }//GEN-LAST:event_bBatalActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void comBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comBarangActionPerformed
        try {
            String sql = "select * from barang where nama_barang = '" + comBarang.getSelectedItem() + "'";
            PS = connection.Connect().prepareStatement(sql);
            rs = PS.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String id_lama = rs.getString(2);
                String harga = rs.getString("harga_rata_rata_barang");

                int selectedRow = jTableTransaksi.getSelectedRow();
                if (selectedRow != -1) {
                    jTableTransaksi.setValueAt(id, selectedRow, 1);
                    jTableTransaksi.setValueAt(id_lama, selectedRow, 3);
                    jTableTransaksi.setValueAt(0, selectedRow, 4);
                    jTableTransaksi.setValueAt(harga, selectedRow, 6);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Eror" + e);
        }

    }//GEN-LAST:event_comBarangActionPerformed

    private void comCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comCustomerActionPerformed
        try {
            String sql = sql = "SELECT c.nama_customer, c.alamat_customer from customer c where c.nama_customer='" + comCustomer.getSelectedItem().toString() + "'";
            PS = connection.Connect().prepareStatement(sql);
            rs1 = PS.executeQuery();
            while (rs1.next()) {
                String nama = rs1.getString(1);
                String alamat = rs1.getString(2);
                txtNama.setText(nama);
                txtAlamat.setText(alamat);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Eror" + e);
        }
    }//GEN-LAST:event_comCustomerActionPerformed

    private void jTableTransaksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableTransaksiKeyPressed
        DefaultTableModel model = (DefaultTableModel) jTableTransaksi.getModel();
        int selectedRow = jTableTransaksi.getSelectedRow();
        int baris = jTableTransaksi.getRowCount();
        int totalBiaya = 0;
        int jumlah = 0, harga = 0;
        int qty = 0;
        TableModel tabelModel;

        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_DOWN) {    // Membuat Perintah Saat Menekan Enter

            tabelModel = jTableTransaksi.getModel();
            for (int i = 0; i < baris; i++) {
                jumlah = Integer.parseInt(tabelModel.getValueAt(i, 4).toString());
                harga = Integer.parseInt(tabelModel.getValueAt(i, 6).toString());
                int subtotal = jumlah*harga;
                tabelModel.setValueAt(subtotal, i, 7);
               
                totalBiaya = totalBiaya + (jumlah * harga);
                qty += jumlah;
            }
            if (harga == 0) {
                JOptionPane.showMessageDialog(null, "Data Terakhir Tidak Boleh kosong");
            } else {
                jTotal.setText("" + totalBiaya);
                model.addRow(new Object[]{"", "", "", "", "0", "", "0"});
            }
        }
        loadNumberTable();
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableTransaksiKeyPressed

    private void buttonMerahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMerahActionPerformed
        if (merahAktif == false) {
            buttonMerah.setText("<");
            merahAktif = true;
            panelMerah.setVisible(merahAktif);
        } else {
            buttonMerah.setText(">");
            merahAktif = false;
            panelMerah.setVisible(merahAktif);
        }
    }//GEN-LAST:event_buttonMerahActionPerformed

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
            java.util.logging.Logger.getLogger(Toko_Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Toko_Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Toko_Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Toko_Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new Toko_Transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBatal;
    private javax.swing.JButton bClearTransaksi;
    private javax.swing.JButton bExitTransaksi;
    private javax.swing.JButton bPrintTransaksi;
    private javax.swing.JButton bSaveTransaksi;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton buttonMerah;
    private javax.swing.JComboBox<String> comBarang;
    private javax.swing.JComboBox<String> comCustomer;
    private javax.swing.JDialog dPembayaran;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JTable jTableTransaksi;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField93;
    private javax.swing.JTextField jTextField94;
    private javax.swing.JTextField jTextField95;
    private javax.swing.JTextField jTextField96;
    private javax.swing.JTextField jTextField97;
    private javax.swing.JTextField jTextField98;
    private javax.swing.JTextField jTextField99;
    private javax.swing.JLabel jTotal;
    private javax.swing.JPanel panelMerah;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}

class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {

    public MyComboBoxRenderer(String[] items) {
        super(items);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setSelectedItem(value);
        return this;
    }
}

class MyComboBoxEditor extends DefaultCellEditor {

    public MyComboBoxEditor(String[] items) {
        super(new JComboBox(items));
    }
}
