package UI;

import Java.Clock;
//import Java.TrBarang;
import Java.Connect;
import Java.ListSalesman;
import Java.ListTokoTransaksi;
import com.sun.glass.events.KeyEvent;
import java.awt.Component;
import static java.awt.event.KeyEvent.*;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.CellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Toko_Transaksi2 extends javax.swing.JDialog implements KeyListener {

    private ArrayList<ListTokoTransaksi> list;
//    private ArrayList<TrBarang> listbarang;
//    private ArrayList<TrBarang> trlist;
    private ListTokoTransaksi listTokoTransaksi;
    private DefaultTableModel tabel;
    Connect connection, connection1;
    ResultSet rs, rs1, rs2, rsx = null;
    private PreparedStatement PS, PSx;
    private TableModel model;
    private ResultSet hasil, hasil1;
    private boolean merahAktif = false;
    private String id;
    private int harga, jumlah;
    private int totalBiaya = 0;
    private int subtotal = 0;
    private String dd;

    public Toko_Transaksi2() {
        initComponents();
        panelMerah.setVisible(false);
        connection = new Connect();
//        trlist = new ArrayList<>();
        AutoCompleteDecorator.decorate(comBarang);
        AutoCompleteDecorator.decorate(comSatuan);
        AturlebarKolom();
        setTanggal();
        loadNumberTable();
        loadComTableBarang();
        String nofak = noFakturPenjualan();
        noFak.setText(nofak);
        int nocus = noCustomerHariIni();
        noCus.setText(String.valueOf(nocus));
        addKeyListener(this);

    }

    public Toko_Transaksi2(java.awt.Frame parent, boolean modal, Connect connection) {
        super(parent, modal);
        initComponents();
    }

    private void hapussemuatabel() {
        int Hapus = JOptionPane.showConfirmDialog(null, "Apakah anda yakin mau menghapus semua data di tabel", "konfirmasi", JOptionPane.YES_NO_OPTION);
        if (Hapus == 0) {
            DefaultTableModel model = (DefaultTableModel) jTableTransaksi.getModel();
            for (int i = jTableTransaksi.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
            model.addRow(new Object[]{"", "", "", "", "0", "", "0", "0", "0"});

        }
        HitungSemua();
    }

    private void HitungSemua() {
        int subtotalfix = 0, grandtotal = 0, discount = 0, jumlahItem = 0, jumlahQty = 0;
        if (!textDiscount.getText().equals("")) {
            discount = Integer.parseInt(textDiscount.getText().toString());
        }
        if (jTableTransaksi.getRowCount() >= 1) {
            for (int i = jTableTransaksi.getRowCount() - 1; i > -1; i--) {
                int x = Integer.parseInt(jTableTransaksi.getValueAt(i, 7).toString());
                subtotalfix += x;
            }
        }
        subTotalFix.setText(String.valueOf(subtotalfix));

        jumlahItem = jTableTransaksi.getRowCount() - 1;
        textJumlahItem.setText(String.valueOf(jumlahItem));

        int i = jTableTransaksi.getRowCount();

        for (int j = 0; j < i; j++) {
            jumlahQty += Integer.parseInt(jTableTransaksi.getValueAt(j, 4).toString());
        }
        textJumlahQty.setText(String.valueOf(jumlahQty));

        if (discount > subtotalfix) {
            JOptionPane.showMessageDialog(null, "Discount tidak boleh lebih besar daripada subtotal.");
            discount = 0;
            textDiscount.setText(String.valueOf(discount));
        } else {
            grandtotal = subtotalfix - discount;
            textGrandTotal.setText(String.valueOf(grandtotal));
            jTotal.setText(String.valueOf(grandtotal));
        }
    }

    private void AturlebarKolom() {
        TableColumn column;
        jTableTransaksi.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        column = jTableTransaksi.getColumnModel().getColumn(0);
        column.setPreferredWidth(70);
        column = jTableTransaksi.getColumnModel().getColumn(1);
        column.setPreferredWidth(90);
        column = jTableTransaksi.getColumnModel().getColumn(2);
        column.setPreferredWidth(240);
        column = jTableTransaksi.getColumnModel().getColumn(3);
        column.setPreferredWidth(110);
        column = jTableTransaksi.getColumnModel().getColumn(4);
        column.setPreferredWidth(100);
        column = jTableTransaksi.getColumnModel().getColumn(5);
        column.setPreferredWidth(140);
        column = jTableTransaksi.getColumnModel().getColumn(6);
        column.setPreferredWidth(170);
        column = jTableTransaksi.getColumnModel().getColumn(7);
        column.setPreferredWidth(200);
        column = jTableTransaksi.getColumnModel().getColumn(8);
        column.setPreferredWidth(0);
        column.setMaxWidth(0);
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

    private void loadComSatuanBarang(String kodeBarang) {
        String kali = "";
        try {
            String sql = "select k.nama_konversi,b.harga_jual_2_barang*bk.jumlah_konversi from konversi k, barang_konversi bk, barang b where b.kode_barang "
                    + "= bk.kode_barang and bk.kode_konversi = k.kode_konversi and b.kode_barang = '" + kodeBarang + "'";
            PS = connection.Connect().prepareStatement(sql);
            rs = PS.executeQuery();
            comSatuan.removeAllItems();
            while (rs.next()) {
                String name = rs.getString(1);
                comSatuan.addItem(name);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Eror" + e);
        }
    }

    private void loadHargaSatuanBarang() {
        String satuan = comSatuan.getSelectedItem().toString();
        String namaBarang = comBarang.getSelectedItem().toString();
        String kali = "";
        try {
            String sql = "select k.nama_konversi,b.harga_jual_2_barang*bk.jumlah_konversi from konversi k, barang_konversi bk, barang b where b.kode_barang "
                    + "= bk.kode_barang and bk.kode_konversi = k.kode_konversi and b.nama_barang = '" + namaBarang + "' and k.nama_konversi='" + satuan + "'";
            PS = connection.Connect().prepareStatement(sql);
            rs = PS.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                kali = rs.getString(2);
            }
            jTableTransaksi.setValueAt(kali, jTableTransaksi.getSelectedRow(), 5);
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
            String hours = ca.get(Calendar.HOUR_OF_DAY) + "";
            String minutes = ca.get(Calendar.MINUTE) + "";
            String seconds = ca.get(Calendar.SECOND) + "";

            if (day.length() == 1) {
                day = "0" + day;
            }
            if (month.length() == 1) {
                month = "0" + month;
            }
            dd = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
            Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dd);
            jDateChooser1.setDate(date);

        } catch (ParseException ex) {
            Logger.getLogger(Toko_Transaksi2.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
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
        comSatuan = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jTotal = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JSeparator();
        noFak = new javax.swing.JTextField();
        textJumlahQty = new javax.swing.JTextField();
        textGrandTotal = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableTransaksi = new javax.swing.JTable();
        bPrintTransaksi = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        subTotalFix = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        jComboBox4 = new javax.swing.JComboBox();
        txtNama = new javax.swing.JTextField();
        textDiscount = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        bExitTransaksi = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        textJumlahItem = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        textStaff = new javax.swing.JTextField();
        bClearTransaksi = new javax.swing.JButton();
        bSaveTransaksi = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txtAlamat = new javax.swing.JTextField();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLabel26 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        panelMerah = new javax.swing.JPanel();
        textHargaJual1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        textHargaJual2 = new javax.swing.JTextField();
        textNoJual = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        textTanggal = new javax.swing.JTextField();
        textHargaJualF = new javax.swing.JTextField();
        jLabel99 = new javax.swing.JLabel();
        textQty = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        textHargaJual3 = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        textToko = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        textTunai = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        buttonMerah = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        noCus = new javax.swing.JTextField();

        dPembayaran.setTitle("Pembayaran");
        dPembayaran.setBackground(new java.awt.Color(255, 255, 255));
        dPembayaran.setResizable(false);
        dPembayaran.setSize(new java.awt.Dimension(383, 270));
        dPembayaran.setType(java.awt.Window.Type.POPUP);

        jLabel10.setText("Total Pembelian");

        jTextField2.setEditable(false);
        jTextField2.setText("0");

        jLabel11.setText("Pembayaran");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

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
        bSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanActionPerformed(evt);
            }
        });

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

        comBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-" }));
        comBarang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comBarangItemStateChanged(evt);
            }
        });
        comBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comBarangActionPerformed(evt);
            }
        });

        comSatuan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));
        comSatuan.setSelectedIndex(0);
        comSatuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comSatuanActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

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

        textJumlahQty.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        textJumlahQty.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        textJumlahQty.setText("0");

        textGrandTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textGrandTotal.setText("0");

        jLabel28.setText("Grand Total");

        jTableTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Kode", "Nama", "Satuan", "Qty", "J.Harga (1/2/3)", "Harga", "Sub Total", "Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, false, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableTransaksiKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableTransaksiKeyReleased(evt);
            }
        });
        jScrollPane5.setViewportView(jTableTransaksi);
        if (jTableTransaksi.getColumnModel().getColumnCount() > 0) {
            jTableTransaksi.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comBarang));
            jTableTransaksi.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comSatuan));
            jTableTransaksi.getColumnModel().getColumn(8).setMinWidth(0);
            jTableTransaksi.getColumnModel().getColumn(8).setPreferredWidth(0);
            jTableTransaksi.getColumnModel().getColumn(8).setMaxWidth(0);
        }

        bPrintTransaksi.setText("Print");

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("Potongan");

        subTotalFix.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel30.setText("Sub Total");

        txtNama.setEditable(false);
        txtNama.setText("Cash Toko");

        textDiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        textDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textDiscountKeyReleased(evt);
            }
        });

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

        textJumlahItem.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        textJumlahItem.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        textJumlahItem.setText(" 0");

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

        txtAlamat.setText("-");

        jCheckBox2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheckBox2.setText("Non Denda");

        jLabel26.setText("Staf");

        jLabel31.setText("Kasir");

        panelMerah.setBackground(new java.awt.Color(153, 0, 0));

        textHargaJual1.setBackground(new java.awt.Color(255, 204, 153));
        textHargaJual1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Harga Jual 1");

        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 255, 255));
        jLabel96.setText("Harga Jual 2");

        textHargaJual2.setBackground(new java.awt.Color(255, 204, 153));
        textHargaJual2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        textNoJual.setBackground(new java.awt.Color(255, 204, 153));
        textNoJual.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel97.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(255, 255, 255));
        jLabel97.setText("No Jual");

        jLabel98.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(255, 255, 255));
        jLabel98.setText("Tanggal");

        textTanggal.setBackground(new java.awt.Color(255, 204, 153));
        textTanggal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        textHargaJualF.setBackground(new java.awt.Color(255, 204, 153));
        textHargaJualF.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel99.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(255, 255, 255));
        jLabel99.setText("Hrg. Jual F.");

        textQty.setBackground(new java.awt.Color(255, 204, 153));
        textQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel100.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(255, 255, 255));
        jLabel100.setText("Qty.");

        textHargaJual3.setBackground(new java.awt.Color(255, 204, 153));
        textHargaJual3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

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
                        .addComponent(textHargaJual3, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMerahLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(textHargaJual1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMerahLayout.createSequentialGroup()
                        .addComponent(jLabel96)
                        .addGap(18, 18, 18)
                        .addComponent(textHargaJual2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMerahLayout.createSequentialGroup()
                        .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel97)
                            .addComponent(jLabel99))
                        .addGap(27, 27, 27)
                        .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(textNoJual, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                            .addComponent(textHargaJualF))))
                .addGap(18, 18, 18)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel98)
                    .addComponent(jLabel100))
                .addGap(13, 13, 13)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textQty, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                    .addComponent(textTanggal))
                .addGap(5, 5, 5))
        );
        panelMerahLayout.setVerticalGroup(
            panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMerahLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(textHargaJual1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel100)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel96)
                    .addComponent(textHargaJual2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel98)
                    .addComponent(textTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel101)
                    .addComponent(textHargaJual3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel97)
                    .addComponent(textNoJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMerahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel99)
                    .addComponent(textHargaJualF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        textToko.setText("TOKO");
        textToko.setEnabled(false);
        textToko.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textTokoActionPerformed(evt);
            }
        });

        jLabel1.setText("Lokasi");

        textTunai.setText("TUNAI");
        textTunai.setEnabled(false);
        textTunai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textTunaiActionPerformed(evt);
            }
        });

        jDateChooser1.setDateFormatString("yyyy-MM-dd");

        buttonMerah.setText(">");
        buttonMerah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMerahActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Jumlah Qty :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Jumlah Item :");

        noCus.setEditable(false);

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
                        .addComponent(subTotalFix, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jLabel29)
                        .addGap(18, 18, 18)
                        .addComponent(textDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel28)
                        .addGap(18, 18, 18)
                        .addComponent(textGrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textJumlahItem, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(textJumlahQty, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                            .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                                            .addComponent(txtAlamat)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(noCus)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(buttonMerah))))
                                    .addComponent(jCheckBox1))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addGap(18, 18, 18)
                                        .addComponent(noFak))
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
                                        .addComponent(textStaff)
                                        .addComponent(textToko, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                                    .addComponent(textTunai, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)))
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
                                    .addComponent(textTunai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(buttonMerah)
                                    .addComponent(noCus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel26)
                                    .addComponent(textStaff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel20)
                                    .addComponent(txtAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel23)
                                    .addComponent(noFak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textToko, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textGrandTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(textDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(subTotalFix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(textJumlahQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textJumlahItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bExitTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bExitTransaksiActionPerformed
        this.dispose();
    }//GEN-LAST:event_bExitTransaksiActionPerformed

    private void bClearTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bClearTransaksiActionPerformed
        hapussemuatabel();
    }//GEN-LAST:event_bClearTransaksiActionPerformed

    private void bSaveTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveTransaksiActionPerformed
        if (Integer.parseInt(textGrandTotal.getText().toString()) < 0) {
            JOptionPane.showMessageDialog(null, "Potongan tidak boleh lebih besar dari jumlah bayar.");
        } else {
            jTextField3.setText("0");
            if (!textGrandTotal.getText().toString().equals("0")) {
                SimpanTokoPenjualan();
                jTextField2.setText(textGrandTotal.getText().toString());
                int kembalian = 0;
                int grandtotal = Integer.parseInt(jTextField2.getText().toString());

                Integer.parseInt(jTextField2.getText().toString());
                jTextField4.setText(String.valueOf(kembalian));
                jComboBox1.addItem("Cash");
                dPembayaran.show();
                dPembayaran.setLocationRelativeTo(null);
            } else {
                JOptionPane.showMessageDialog(null, "Silahkan Masukan Item terlebih dahulu!");
            }
        }
    }//GEN-LAST:event_bSaveTransaksiActionPerformed

    private void bBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBatalActionPerformed
        dPembayaran.dispose();
    }//GEN-LAST:event_bBatalActionPerformed

    private void textTokoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textTokoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textTokoActionPerformed

    private void textTunaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textTunaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textTunaiActionPerformed

    private void comBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comBarangActionPerformed
        int selectedRow = jTableTransaksi.getSelectedRow();
        try {
            String sql = "select b.kode_barang, b.nama_barang, b.harga_jual_1_barang, "
                    + "b.harga_jual_2_barang, b.harga_jual_3_barang, b.harga_rata_rata_barang, "
                    + "bl.jumlah from barang b, barang_lokasi bl, lokasi l where b.kode_barang = bl.kode_barang and "
                    + "bl.kode_lokasi = l.kode_lokasi and l.nama_lokasi='toko' and b.nama_barang = '" + comBarang.getSelectedItem() + "'";
            PS = connection.Connect().prepareStatement(sql);
            rs = PS.executeQuery();
            while (rs.next()) {
                id = rs.getString(1);
                jumlah = rs.getInt(7);
                harga = rs.getInt(4);
                if (selectedRow != -1) {
                    jTableTransaksi.setValueAt(id, selectedRow, 1);
                    jTableTransaksi.setValueAt(0, selectedRow, 4);
                    jTableTransaksi.setValueAt(harga, selectedRow, 5);
                    jTableTransaksi.setValueAt(jumlah, selectedRow, 8);
                }
                loadComSatuanBarang(jTableTransaksi.getValueAt(selectedRow, 1).toString());
                jTableTransaksi.setValueAt(comSatuan.getSelectedItem(), selectedRow, 3);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Eror" + e);
        } finally {
        }
    }//GEN-LAST:event_comBarangActionPerformed

    private void SimpanTokoPenjualan() {
        String nofak = noFakturPenjualan();
        int total = Integer.parseInt(jTotal.getText());
        PS = null;
        try {
            String sql = "insert into toko_penjualan(no_faktur_toko_penjualan,tgl_toko_penjualan,kode_pegawai,total) values (?,?,?,?)";
            PS = connection.Connect().prepareStatement(sql);
            PS.setString(1, nofak);
            PS.setString(2, dd);
            PS.setInt(3, 1);
            PS.setInt(4, total);

            PS.executeUpdate();
            simpanTokoPenjualanDetail(nofak);
        } catch (SQLException e) {
            System.out.println("Toko Transaksi : " + e.toString());
        }
    }

    private void jTableTransaksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableTransaksiKeyPressed
        DefaultTableModel model = (DefaultTableModel) jTableTransaksi.getModel();
        int selectedRow = jTableTransaksi.getSelectedRow();
        int baris = jTableTransaksi.getRowCount();
        int jumlah = 0, harga = 0;
        int qty = 0;

        TableModel tabelModel;
        tabelModel = jTableTransaksi.getModel();
        jumlah = Integer.parseInt(tabelModel.getValueAt(jTableTransaksi.getSelectedRow(), 4).toString());
        harga = Integer.parseInt(tabelModel.getValueAt(jTableTransaksi.getSelectedRow(), 6).toString());
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {    // Membuat Perintah Saat Menekan Enter
            if (Integer.parseInt(tabelModel.getValueAt(jTableTransaksi.getSelectedRow(), 4).toString()) > Integer.parseInt(tabelModel.getValueAt(jTableTransaksi.getSelectedRow(), 8).toString())) {
                JOptionPane.showMessageDialog(null, "Qty tidak boleh lebih dari stok. Stock tersedia =" + Integer.parseInt(tabelModel.getValueAt(jTableTransaksi.getSelectedRow(), 8).toString()), "", 2);
                jTableTransaksi.setValueAt(Integer.parseInt(tabelModel.getValueAt(jTableTransaksi.getSelectedRow(), 8).toString()), selectedRow, 4);
            } else {
                jumlah = Integer.parseInt(tabelModel.getValueAt(jTableTransaksi.getSelectedRow(), 4).toString());
                harga = Integer.parseInt(tabelModel.getValueAt(jTableTransaksi.getSelectedRow(), 6).toString());
                subtotal = jumlah * harga;
                tabelModel.setValueAt(subtotal, jTableTransaksi.getSelectedRow(), 7);
                HitungSemua();
                if (tabelModel.getValueAt(jTableTransaksi.getSelectedRow(), 7).toString().equals("0")) {
                    JOptionPane.showMessageDialog(null, "Data Terakhir Tidak Boleh kosong", "", 2);
                } else {
                    if (Integer.parseInt(tabelModel.getValueAt(jTableTransaksi.getRowCount() - 1, 7).toString()) != 0) {
                        model.addRow(new Object[]{"", "", "", "", "0", "", "0", "0", "0"});
                    }
                }

            }
        } else if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            if (jTableTransaksi.getRowCount() - 1 == -1) {
                JOptionPane.showMessageDialog(null, "Data didalam tabel telah tiada.", "", 2);
            } else {
                removeSelectedRows(jTableTransaksi);
                HitungSemua();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_INSERT) {
            if (jTableTransaksi.getRowCount() - 1 == -1) {
                model.addRow(new Object[]{"", "", "", "", "0", "", "0", "0", "0"});

            }
        }
        loadNumberTable();

    }//GEN-LAST:event_jTableTransaksiKeyPressed

    private static String rptabel(String b) {
        b = b.replace(",", "");
        b = NumberFormat.getNumberInstance(Locale.getDefault()).format(Double.parseDouble(b));
        return b;
    }

    private void removeSelectedRows(JTable table) {
        int Hapus = 1;
        Hapus = JOptionPane.showConfirmDialog(null, "Apakah anda yakin mau menghapus baris ?", "konfirmasi", JOptionPane.YES_NO_OPTION);
        if (Hapus == 0) {
            DefaultTableModel model = (DefaultTableModel) this.jTableTransaksi.getModel();
            int[] rows = table.getSelectedRows();

            for (int i = 0; i < rows.length; i++) {
                model.removeRow(rows[i] - i);
            }
        }
    }

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

    private int DapatKodeKonversi(String kodeBarang) {
        int kodeBarangKonversi = 0;
        try {
            String sql = "select kode_barang_konversi from barang_konversi where kode_barang ='" + kodeBarang + "'";
            PS = connection.Connect().prepareStatement(sql);
            rs = PS.executeQuery();
            if (rs.next()) {
                kodeBarangKonversi = rs.getInt(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Eror DapatKodeKonversi" + e);
        }
        return kodeBarangKonversi;
    }

    private void jTableTransaksiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableTransaksiKeyReleased

    }//GEN-LAST:event_jTableTransaksiKeyReleased
    private void simpanTokoPenjualanDetail(String nofak) {
        PS = null;
        int i = jTableTransaksi.getRowCount() - 1;
        try {
            for (int j = 0; j < i; j++) {
                String kodeBarang = jTableTransaksi.getValueAt(j, 1).toString();
                int jlh = Integer.parseInt(jTableTransaksi.getValueAt(j, 4).toString());
                int hrgaFix = Integer.parseInt(jTableTransaksi.getValueAt(j, 6).toString());
                int hrga2 = Integer.parseInt(jTableTransaksi.getValueAt(j, 5).toString());
                int kodeBarangKonversi = DapatKodeKonversi(kodeBarang);
                String data2 = "insert into toko_penjualan_detail(no_faktur_toko_penjualan,kode_barang,jumlah_barang,harga_barang,kode_barang_konversi) values (?,?,?,?,?)";

                PS = connection.Connect().prepareStatement(data2);
                PS.setString(1, nofak);
                PS.setString(2, kodeBarang);
                PS.setInt(3, jlh);
                PS.setInt(4, hrga2);
                PS.setInt(5, kodeBarangKonversi);

                PS.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Toko_Retur/simpanTokoPenjualanDetail- " + e);
        }
    }

    private String selectLastData(String year) {
        String lastNo = "";
        try {
            String data = "SELECT no_faktur_toko_penjualan FROM toko_penjualan ORDER BY id_toko_penjualan DESC LIMIT 1";
            hasil = connection.ambilData(data);
            if (hasil.next()) {                
                String nomor = hasil.getString("no_faktur_toko_penjualan");
                if(nomor.substring(2, 4).equalsIgnoreCase(year)){
                    int noLama = Integer.parseInt(nomor.substring(nomor.length() - 4));
                    noLama++;
                    String no = Integer.toString(noLama);
                    if (no.length() == 1) {
                        no = "000" + no;
                    } else if (no.length() == 2) {
                        no = "00" + no;
                    } else if (no.length() == 3) {
                        no = "0" + no;
                    }
                    lastNo = no;
                } else{
                    lastNo = "0001";
                }
                
            } else {
                lastNo = "0001";
            }
        } catch (Exception e) {
            System.out.println("TokoPenjualan/selectLastData - " + e);
        }
        return lastNo;
    }

    private int selectLastCustomer(String tanggal) {
        int lastNo = 0;
        try {
            String data = "select count(id_toko_penjualan) from toko_penjualan"
                    + " where tgl_toko_penjualan like '%" + tanggal + "%' ";
            hasil = connection.ambilData(data);
            if (hasil.next()) {
                int nomor = hasil.getInt(1);
                nomor = nomor + 1;
                lastNo = nomor;
            } else {
                lastNo = 1;
            }
        } catch (Exception e) {
            System.out.println("TokoPenjualan/selectLastCustomer - " + e);
        }
        return lastNo;
    }

    private String noFakturPenjualan() {
        String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
        String lastY = year.substring(year.length() - 2);
        String no = "PJ" + lastY + "-" + selectLastData(lastY);
        return no;
    }

    private int noCustomerHariIni() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = dt.format(jDateChooser1.getDate());

        int no = selectLastCustomer(tanggal);
        return no;
    }

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {
        if (jTextField3.getText().toString().equals("0")) {
            JOptionPane.showMessageDialog(null, "Bayar tidak boleh nol!");
        } else if (jTextField3.getText() == null) {
            JOptionPane.showMessageDialog(null, "Bayar tidak boleh kosong!");
        } else if (Integer.parseInt(jTextField3.getText().toString()) < Integer.parseInt(jTextField2.getText().toString())) {
            JOptionPane.showMessageDialog(null, "Uang yang dibayar kurang dari yang harus dibayar!");
        } else {
            dPembayaran.dispose();
            this.dispose();
            Toko_Transaksi2 x = new Toko_Transaksi2();
            x.setLocationRelativeTo(this);
            x.setVisible(true);
        }
    }

    private void comSatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comSatuanActionPerformed
        String sat = "";
        String bar = "";
        String kali = "";

        if (comSatuan.getSelectedItem() == null) {
        } else {
            if (comBarang.getSelectedItem() == null) {
            } else {
                sat = comSatuan.getSelectedItem().toString();
                bar = comBarang.getSelectedItem().toString();
                try {
                    String sql = "select k.nama_konversi,b.harga_jual_2_barang*bk.jumlah_konversi from konversi k, barang_konversi bk, barang b where b.kode_barang "
                            + "= bk.kode_barang and bk.kode_konversi = k.kode_konversi and b.nama_barang = '" + bar + "' and k.nama_konversi='" + sat + "'";
                    PSx = connection.Connect().prepareStatement(sql);
                    rsx = PSx.executeQuery();
                    while (rsx.next()) {
                        String name = rsx.getString(1);
                        kali = rsx.getString(2);
                    }
                    jTableTransaksi.setValueAt(kali, jTableTransaksi.getSelectedRow(), 6);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Eror" + e);
                }
            }
        }

    }//GEN-LAST:event_comSatuanActionPerformed

    private void textDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textDiscountKeyReleased
        textGrandTotal.setText(String.valueOf(Integer.parseInt(subTotalFix.getText().toString()) - Integer.parseInt(textDiscount.getText().toString())));
    }//GEN-LAST:event_textDiscountKeyReleased

    private void comBarangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comBarangItemStateChanged
        if (comBarang.getSelectedIndex() >= 0) {
        }
    }//GEN-LAST:event_comBarangItemStateChanged

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F12) {
            System.out.println("F12");

        }
    }//GEN-LAST:event_formKeyPressed
    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {
        jTextField4.setText(String.valueOf(Integer.parseInt(jTextField3.getText().toString()) - Integer.parseInt(jTextField2.getText().toString())));

    }

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
            java.util.logging.Logger.getLogger(Toko_Transaksi2.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Toko_Transaksi2.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Toko_Transaksi2.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Toko_Transaksi2.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new Toko_Transaksi2().setVisible(true);
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
    private javax.swing.JComboBox<String> comSatuan;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JLabel jTotal;
    private javax.swing.JTextField noCus;
    private javax.swing.JTextField noFak;
    private javax.swing.JPanel panelMerah;
    private javax.swing.JTextField subTotalFix;
    private javax.swing.JTextField textDiscount;
    private javax.swing.JTextField textGrandTotal;
    private javax.swing.JTextField textHargaJual1;
    private javax.swing.JTextField textHargaJual2;
    private javax.swing.JTextField textHargaJual3;
    private javax.swing.JTextField textHargaJualF;
    private javax.swing.JTextField textJumlahItem;
    private javax.swing.JTextField textJumlahQty;
    private javax.swing.JTextField textNoJual;
    private javax.swing.JTextField textQty;
    private javax.swing.JTextField textStaff;
    private javax.swing.JTextField textTanggal;
    private javax.swing.JTextField textToko;
    private javax.swing.JTextField textTunai;
    private javax.swing.JTextField txtAlamat;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
        System.out.println("x");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent evt) {
        int keyCode = evt.getKeyCode();
        int d;

        if (keyCode == KeyEvent.VK_LEFT) {
            System.out.println("x");
        }
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        System.out.println("z");
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
