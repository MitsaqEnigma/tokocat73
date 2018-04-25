/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Java.Connect;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import static java.lang.Thread.sleep;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dii
 */
public class Toko_ReturPenjualan extends javax.swing.JFrame {

    private HashMap konversi, idReturn, jmlBarang;
    private DefaultTableModel tabelBarang, tabelRetur;
    private ResultSet hasil;
    public Statement stmt;
    private Connect connection;
    private String code_barang, code_konversi;
    private String noNota = "";
    
    public Toko_ReturPenjualan() {
        initComponents();
        this.setLocationRelativeTo(null);
        connection = new Connect();
        konversi = new HashMap(); idReturn = new HashMap(); jmlBarang = new HashMap();
        tabelBarang = new DefaultTableModel(new String[]{"No.","Kode","Nama Barang"},0);
        jTable2.setModel(tabelBarang);
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTable2.getColumnModel().getColumn(1).setPreferredWidth(5);
        tabelRetur = new DefaultTableModel(new String[]{"No.","Barang","Satuan","Jumlah","Harga","Saldo"},0);
        jTable3.setModel(tabelRetur);
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(1);
        jTable3.getColumnModel().getColumn(2).setPreferredWidth(5);
        jTable3.getColumnModel().getColumn(3).setPreferredWidth(5);
        jTable3.getColumnModel().getColumn(4).setPreferredWidth(5);
        jTable3.getColumnModel().getColumn(5).setPreferredWidth(5);
        showDate(); 
        isiTabelReturn("*");
    }
    
    public void setPlaceHolder(javax.swing.JTextField a, String b) {
        if(b == null) {
            b = "Search";
        }
        if(a.getText().equals(b)) {
            a.setText("");
        }
        else if(a.getText() == null || a.getText().equals("")) {
            a.setText(b);
        }
    }
    
    public void showDate() {
        java.util.Timer t = new java.util.Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                java.util.Date d = new java.util.Date( );
                java.text.SimpleDateFormat dt = new java.text.SimpleDateFormat ("E dd-MM-yyyy 'at' hh:mm:ss a");
                vdatetime.setText(dt.format(d));
            }
        }, 500, 500);
    }
    
    private String tampilTabelDataBarang(String search){
        String data = "";
        try{
            data = "SELECT TPD.kode_barang, B.nama_barang, TPD.jumlah_barang "
                + "FROM barang B, toko_penjualan_detail TPD "
                + "WHERE no_faktur_toko_penjualan = '"+noNota+"' AND "
                + "TPD.kode_barang = B.proud_code "
                +(search.equalsIgnoreCase("*") ? "" : "AND B.nama_barang LIKE '%" +search+ "%' " )+" "
                + "ORDER BY TPD.kode_barang";
            hasil = connection.ambilData(data);
            setModelTabelBarang(hasil);
        } catch(Exception e){
            System.out.println("Error /Toko_ReturPenjualan/tampilTabelDataBarang -> "+e);
        }
        return data;
    }
    
    public void setModelTabelBarang(ResultSet hasil){
        try{
            int no = 1;
            while (hasil.next()){
                String kode = hasil.getString("kode_barang");
                String nama = hasil.getString("nama_barang");
                tabelBarang.addRow(new Object[]{no,kode,nama});
                jmlBarang.put(kode, hasil.getString("jumlah_barang"));
                no++;
            }
        } catch(Exception e){
            System.out.println("Error /Toko_ReturPenjualan/setModel -> "+e);
        }
    }
    
    private void deleteTabel(DefaultTableModel NamaTabel){
        int baris = NamaTabel.getRowCount();
        for (int i = 0; i < baris; i++) {
            NamaTabel.removeRow(0);
        }
    }
    
    private void isiPilihBarang(String kode){
        try{
            String data = "SELECT TPD.kode_barang, B.nama_barang, B.harga_jual_3_barang, TPD.kode_barang_konversi, K.nama_konversi "
                    + "FROM barang B, toko_penjualan_detail TPD, konversi K "
                    + "WHERE B.proud_code = TPD.kode_barang AND TPD.kode_barang_konversi = K.kode_konversi AND "
                    + "TPD.kode_barang = '"+kode+"' ";
            hasil = connection.ambilData(data);
            while (hasil.next()){
//                String kode_barang = hasil.getString("kode_barang");
                code_konversi = hasil.getString("kode_barang_konversi");
                vNamaBarang.setText(hasil.getString("nama_barang"));
                vHarga.setText(hasil.getString("harga_jual_3_barang"));
                jLabel8.setText("*Jumlah Maksimal Return : "+jmlBarang.get(kode));
                code_barang = hasil.getString("kode_barang");
                vSatuan.removeAllItems();
                vSatuan.addItem(hasil.getString("nama_konversi"));
            }
//            System.out.println("isi pilih barang sukses");
//            selectKonversi(kode_barang);
        } catch(Exception e){
            System.out.println("Toko_Retur/isiPilihBarang "+e);
        }
    }
    
    private String selectLastDataDetailReturn(){
        String lastNo = "";
        try{    
            String data = "SELECT no_faktur_toko_penjualan_return "
                    + "FROM toko_penjualan_detail_return "
                    + "ORDER BY id_toko_penjualan_return_detail DESC LIMIT 1";
            hasil = connection.ambilData(data);
                if(hasil.next()){
                    String nomor = hasil.getString("no_faktur_toko_penjualan_return");
                    int noLama = Integer.parseInt(nomor.substring(nomor.length() - 4));
                    noLama++;
                    String no = Integer.toString(noLama);
                    if(no.length() == 1){
                        no = "000"+no;
                    } else if(no.length() == 2){
                        no = "00"+no;
                    } else if(no.length() == 3){
                        no = "0"+no;
                    }
                    lastNo = no;
                } else{
                    lastNo = "0001";
                }
        }catch(Exception e){
            System.out.println("TokoRetur/selectLastData - "+e);
        }
        return lastNo;
    }
    
    private void selectKonversi(String kode_barang){
        vSatuan.removeAllItems();
        try{
            String data = "SELECT k.kode_konversi, k.nama_konversi "
                    + "FROM konversi k, barang_konversi bk "
                    + "WHERE bk.kode_konversi = k.kode_konversi AND bk.kode_barang = '"+kode_barang+"' ";
            hasil = connection.ambilData(data);
            while(hasil.next()){
                String kode = hasil.getString("kode_konversi");
                String nama = hasil.getString("nama_konversi");
                konversi.put(nama, kode);
                vSatuan.addItem(nama);
            }
        } catch(Exception e){
            System.out.println("Toko_Return/selectKonversi - "+e);
        }
    }
    
    private void isiTabelReturn(String search){
        ResultSet nama_barang, harga_barang;
        try{
            String data = "SELECT TKR.id_toko_keranjang, B.nama_barang, K.nama_konversi, TKR.jumlah_barang, B.harga_jual_3_barang, TKR.harga_barang "
                    + "FROM barang B, konversi K, toko_keranjang_return TKR "
                    + "WHERE TKR.kode_barang = B.proud_code AND TKR.kode_barang_konversi = K.kode_konversi "
                    + (search.equalsIgnoreCase("*") ? "" : "AND B.nama_barang LIKE '%"+search+"%' ")+" "
                    + "ORDER BY TKR.id_toko_keranjang";
//            String data = "SELECT DT.id_toko_penjualan_return_detail, B.nama_barang, K.nama_konversi, DT.jumlah_barang, B.harga_jual_3_barang, DT.harga_barang "
//                    + "FROM toko_penjualan_detail_return DT, barang B, konversi K "
//                    + "WHERE DT.kode_barang = proud_code AND DT.kode_barang_konversi = K.kode_konversi "
//                    + (search.equalsIgnoreCase("*") ? "" : "AND B.nama_barang LIKE '%"+search+"%' ")+" "
//                    + "ORDER BY id_toko_penjualan_return_detail";
            hasil = connection.ambilData(data);
            setModelTabelReturn(hasil);
        } catch(Exception e){
            System.out.println("Toko_Return/isiTabelBarang - "+e);
        }
    }
    
    private void setModelTabelReturn(ResultSet hasil){
        try{
            int no = 1;
            while(hasil.next()){
                String barang = hasil.getString("nama_barang");
                String satuan = hasil.getString("nama_konversi");
                String jumlah = hasil.getString("jumlah_barang");
                String harga = hasil.getString("harga_jual_3_barang");
                String total = hasil.getString("harga_barang");
                tabelRetur.addRow(new Object[]{no,barang,satuan,jumlah,harga,total});
                idReturn.put(no,hasil.getString("id_toko_keranjang"));
                no++;
            }
            setTotalReturn();
        } catch(Exception e){
            System.out.println("Toko_Return/setModelTabelReturn - "+e);
        }
    }
    
    private void setTotalReturn(){
        try{
            int total = 0;
            String data = "SELECT harga_barang "
                    + "FROM toko_keranjang_return";
            hasil = connection.ambilData(data);
            while(hasil.next()){
                total = total + hasil.getInt("harga_barang");
            }
            jTextField5.setText(Integer.toString(total));
        } catch(Exception e){
            System.out.println("Toko_Return/setTotalReturn - "+e);
        }
    }
    
    private void inputTokoKeranjangReturn(String codeBarang){
        int total = Integer.parseInt(vJumlah.getText()) * Integer.parseInt(vHarga.getText());
        try{
            String data = "INSERT INTO toko_keranjang_return(kode_barang, jumlah_barang, harga_barang, kode_barang_konversi) "
                    + "VALUES ('"+codeBarang+"', '"+vJumlah.getText()+"', '"+total+"', '"+code_konversi+"')";
            connection.simpanData(data);
            dPilihBarang.dispose();
        }catch(Exception e){
            System.out.println("Toko_Retur/inputTokoKeranjangReturn - "+e);
        }
    }
    
    private void simpanTokoDetailReturn(String codeBarang){
//        int total = Integer.parseInt(vJumlah.getText()) * Integer.parseInt(vHarga.getText());
        
//                vdatetime.setText(dt.format(d));
        
        try{
            String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
            String no = "RT"+year.substring(year.length()-2)+"-"+selectLastDataDetailReturn();
            String kode_pegawai = "1";
            java.util.Date d = new java.util.Date();
            java.text.SimpleDateFormat dt = new java.text.SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
            String date = dt.format(d);
            String data = "INSERT INTO toko_penjualan_return(no_faktur_toko_penjualan_return, tgl_toko_penjualan_return, kode_pegawai) "
                    + "VALUES ('"+no+"', '"+date+"', '"+kode_pegawai+"')";
            connection.simpanData(data);
            dPilihBarang.dispose();
        } catch(Exception e){
            System.out.println("Toko_Retur/inputTokoDetailReturn - "+e);
        }
    }
    
    private void hapusBarang(String kode){
        try{
            String data = "DELETE FROM toko_keranjang_return "
                    + "WHERE id_toko_keranjang = '"+kode+"' ";
            connection.simpanData(data);
            JOptionPane.showMessageDialog(null, "Barang berhasil dihapus");
            deleteTabel(tabelRetur);
            isiTabelReturn("*");
        } catch(Exception e){
            System.out.println("Toko_Return/hapusBarang - "+e);
        }
    }
    
    private void hitungTotalHarga(){
        int harga = Integer.parseInt(vHarga.getText().toString());
        int jumlah = Integer.parseInt(vJumlah.getText().toString());
        int total = harga*jumlah;
        vTotalHarga.setText(Integer.toString(total));
    }
    
    private String selectNoNota(String nomor){
        String noNota = "";
        try{
            String data = "SELECT no_faktur_toko_penjualan "
                    + "FROM toko_penjualan "
                    + "WHERE no_faktur_toko_penjualan = '"+nomor+"' ";
            hasil = connection.ambilData(data);
            if(hasil.next()){
                noNota = hasil.getString("no_faktur_toko_penjualan");
            } else{
                JOptionPane.showMessageDialog(null, "Nomor Nota Tidak ditemukan");
            }
        } catch(Exception e){
            System.out.println("Toko_Retur/selectNoNota "+e);
        }
        return noNota;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dPilihBarang = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        vNamaBarang = new javax.swing.JTextField();
        vSatuan = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        vHarga = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        vJumlah = new javax.swing.JTextField();
        vTotalStok = new javax.swing.JLabel();
        vTotalHarga = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnBatal = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        dPilihDataBarang = new javax.swing.JDialog();
        vSearch = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jReturPenjualan = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        vdatetime = new javax.swing.JLabel();
        btnTambahBarangRetur = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        btnSimpanPenjualan1 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        vSearchRetur = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        vNota = new javax.swing.JTextField();
        vOk = new javax.swing.JButton();

        dPilihBarang.setTitle("Pilih Barang");
        dPilihBarang.setBackground(new java.awt.Color(255, 255, 255));
        dPilihBarang.setSize(new java.awt.Dimension(400, 400));

        jLabel4.setText("Nama Barang");

        vNamaBarang.setEditable(false);
        vNamaBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vNamaBarangMouseClicked(evt);
            }
        });

        vSatuan.disable();

        jLabel5.setText("Satuan");

        vHarga.setEditable(false);

        jLabel6.setText("Harga");

        jLabel7.setText("Jumlah");

        vJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                vJumlahKeyReleased(evt);
            }

        });
        vJumlah.setText("0");

        vTotalStok.setText("");

        vTotalHarga.setEditable(false);
        vTotalHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vTotalHargaActionPerformed(evt);
            }
        });

        jLabel9.setText("Total Harga");

        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        btnSimpan.setBackground(new java.awt.Color(85, 222, 93));
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jLabel8.setText("*Jumlah Maksimal Return : ");

        javax.swing.GroupLayout dPilihBarangLayout = new javax.swing.GroupLayout(dPilihBarang.getContentPane());
        dPilihBarang.getContentPane().setLayout(dPilihBarangLayout);
        dPilihBarangLayout.setHorizontalGroup(
            dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dPilihBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dPilihBarangLayout.createSequentialGroup()
                        .addGroup(dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9))
                        .addGap(115, 115, 115))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dPilihBarangLayout.createSequentialGroup()
                        .addComponent(btnSimpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vSatuan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(vNamaBarang)
                    .addComponent(vHarga)
                    .addComponent(vJumlah)
                    .addComponent(vTotalHarga)
                    .addGroup(dPilihBarangLayout.createSequentialGroup()
                        .addGroup(dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(vTotalStok)
                            .addComponent(btnBatal))
                        .addGap(0, 72, Short.MAX_VALUE)))
                .addContainerGap())
        );
        dPilihBarangLayout.setVerticalGroup(
            dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dPilihBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(vNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(5, 5, 5)
                .addComponent(jLabel8)
                .addGap(5, 5, 5)
                .addComponent(vTotalStok)
                .addGap(11, 11, 11)
                .addGroup(dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(dPilihBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBatal)
                    .addComponent(btnSimpan))
                .addGap(11, 11, 11))
        );

        dPilihDataBarang.setTitle("Pilih Data Barang");
        dPilihDataBarang.setBackground(new java.awt.Color(255, 255, 255));
        dPilihDataBarang.setSize(new java.awt.Dimension(400, 227));

        vSearch.setText("Search");
        vSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                vSearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                vSearchFocusLost(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);

        vSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                vSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout dPilihDataBarangLayout = new javax.swing.GroupLayout(dPilihDataBarang.getContentPane());
        dPilihDataBarang.getContentPane().setLayout(dPilihDataBarangLayout);
        dPilihDataBarangLayout.setHorizontalGroup(
            dPilihDataBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dPilihDataBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dPilihDataBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dPilihDataBarangLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(vSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addContainerGap())
        );
        dPilihDataBarangLayout.setVerticalGroup(
            dPilihDataBarangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dPilihDataBarangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(vSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        jReturPenjualan.setBackground(new java.awt.Color(255, 255, 255));
        jReturPenjualan.setInheritsPopupMenu(true);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel14.setText("Return Penjualan Toko");

        vdatetime.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        vdatetime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        vdatetime.setText("T | W");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(vdatetime, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(vdatetime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        btnTambahBarangRetur.setText("Tambah Barang Penjualan");
        btnTambahBarangRetur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahBarangReturActionPerformed(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "No", "Barang", "Satuan", "Jumlah", "Harga", "Saldo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        btnSimpanPenjualan1.setText("Simpan Penjualan");
        btnSimpanPenjualan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanPenjualan1ActionPerformed(evt);
            }
        });

        jLabel16.setText("Total");

        jTextField5.setEditable(false);
        jTextField5.setText("0");

        vSearchRetur.setText("Search");
        vSearchRetur.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                vSearchReturFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                vSearchReturFocusLost(evt);
            }
        });
        vSearchRetur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                vSearchReturKeyReleased(evt);
            }
        });

        jButton1.setText("Hapus");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        vNota.setText("Nomor Nota");
        vNota.setCaretPosition(6);
        vNota.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                vNotaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                vNotaFocusLost(evt);
            }
        });

        vOk.setText("OK");
        vOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jReturPenjualanLayout = new javax.swing.GroupLayout(jReturPenjualan);
        jReturPenjualan.setLayout(jReturPenjualanLayout);
        jReturPenjualanLayout.setHorizontalGroup(
            jReturPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jReturPenjualanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jReturPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jReturPenjualanLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jReturPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jReturPenjualanLayout.createSequentialGroup()
                                .addComponent(btnSimpanPenjualan1)
                                .addGap(112, 112, 112))
                            .addGroup(jReturPenjualanLayout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField5))))
                    .addGroup(jReturPenjualanLayout.createSequentialGroup()
                        .addGroup(jReturPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(vNota, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTambahBarangRetur, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jReturPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(vOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(vSearchRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jReturPenjualanLayout.setVerticalGroup(
            jReturPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jReturPenjualanLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(jReturPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(vNota)
                    .addComponent(vOk, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jReturPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahBarangRetur)
                    .addComponent(vSearchRetur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jReturPenjualanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(btnSimpanPenjualan1)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jReturPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jReturPenjualan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void vNamaBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vNamaBarangMouseClicked
        deleteTabel(tabelBarang);
        tampilTabelDataBarang("*");
        dPilihDataBarang.show();
        dPilihDataBarang.setLocationRelativeTo(null);
    }//GEN-LAST:event_vNamaBarangMouseClicked

    private void vTotalHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vTotalHargaActionPerformed
        
    }//GEN-LAST:event_vTotalHargaActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        dPilihBarang.dispose();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void vSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vSearchFocusGained
        setPlaceHolder(vSearch, null);
    }//GEN-LAST:event_vSearchFocusGained

    private void vSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vSearchFocusLost
        setPlaceHolder(vSearch, null);
    }//GEN-LAST:event_vSearchFocusLost

    private void vSearchReturFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vSearchReturFocusLost
        setPlaceHolder(vSearchRetur, null);
    }//GEN-LAST:event_vSearchReturFocusLost

    private void vSearchReturFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vSearchReturFocusGained
        setPlaceHolder(vSearchRetur, null);
    }//GEN-LAST:event_vSearchReturFocusGained

    private void btnTambahBarangReturActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahBarangReturActionPerformed
        if(!vNota.getText().equals("Nomor Nota")){
            dPilihBarang.show();
            dPilihBarang.setLocationRelativeTo(null);
        } else{
            JOptionPane.showMessageDialog(null, "Silahkan masukan Nomor Nota");
        }
        
    }//GEN-LAST:event_btnTambahBarangReturActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        
    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int row = jTable3.getSelectedRow();
        if(row <= -1){
            JOptionPane.showMessageDialog(null, "Silahkan Pilih Barang yg akan dihapus");
        } else{
            row = row +1;
            hapusBarang(idReturn.get(row).toString());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnSimpanPenjualan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanPenjualan1ActionPerformed
        
    }//GEN-LAST:event_btnSimpanPenjualan1ActionPerformed

    private void vNotaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vNotaFocusGained
        if(vNota.getText().equals("Nomor Nota")){
            vNota.setText("");
        }
    }//GEN-LAST:event_vNotaFocusGained

    private void vNotaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_vNotaFocusLost
        if(vNota.getText().isEmpty()){
            vNota.setText("Nomor Nota");
        }
    }//GEN-LAST:event_vNotaFocusLost

    private void vOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vOkActionPerformed
        if(vOk.getText().equals("OK")){
            String nomor_nota = vNota.getText();
            if(nomor_nota.equals("Nomor Nota")){
                JOptionPane.showMessageDialog(null, "Silahkan isi Nomor Nota");
            } else{
                noNota = selectNoNota(nomor_nota);
                if(!noNota.equalsIgnoreCase("")){
                    vNota.setEditable(false);
                    vOk.setText("Edit");
                }
            }
        } else{
            vNota.setEditable(true);
            vOk.setText("OK");
            
        }
    }//GEN-LAST:event_vOkActionPerformed

    private void vSearchReturKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_vSearchReturKeyReleased
        deleteTabel(tabelRetur);
        isiTabelReturn(vSearchRetur.getText());
    }//GEN-LAST:event_vSearchReturKeyReleased

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt){
        if(code_barang != null && (!vJumlah.getText().equalsIgnoreCase("")) && 
                !vTotalHarga.getText().equalsIgnoreCase("")){
            int jumlahStok = Integer.parseInt(jmlBarang.get(code_barang).toString());
            int jml = Integer.parseInt(vJumlah.getText());
            if(jml <= jumlahStok && jml > 0){
                inputTokoKeranjangReturn(code_barang);
                vNamaBarang.setText(""); vSatuan.removeAllItems(); vJumlah.setText("0"); 
                vHarga.setText(""); vTotalHarga.setText(""); code_barang = null; 
                deleteTabel(tabelRetur);
                isiTabelReturn("*");
//                dPilihBarang.dispose();
            } else{
                JOptionPane.showMessageDialog(null, "Jumlah harus Kurang dari atau sama dengan Jumlah maksimal return");
            }
        } else{
            JOptionPane.showMessageDialog(null, "Silahkan Pilih Barang, Jumlah Barang & Total Harga");
        }
    }
    
    private void vSearchKeyReleased(java.awt.event.KeyEvent evt){
//        aa;
        deleteTabel(tabelBarang);
        tampilTabelDataBarang(vSearch.getText().toString());
    }
    private void vJumlahKeyReleased(java.awt.event.KeyEvent evt){
//        aa
        //set text to input only int
        char vchar = evt.getKeyChar();
        if(!(Character.isDigit(vchar)) || (vchar == KeyEvent.VK_BACK_SPACE) || (vchar == KeyEvent.VK_DELETE)){
            evt.consume();
        }
        
        if(!vJumlah.getText().equalsIgnoreCase("") && !vHarga.getText().equalsIgnoreCase("")){
            hitungTotalHarga();
        }
    }
    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {                                     
        int row = jTable2.getSelectedRow();
        if(row < 0){
            JOptionPane.showMessageDialog(null, "Silahkan Pilih Barang");
        } else{
//            System.out.println("row yg di dipilih "+row);
            String kode = jTable2.getModel().getValueAt(row, 1).toString();
            isiPilihBarang(kode);
            dPilihDataBarang.dispose();
            hitungTotalHarga();
        }
    }
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
            java.util.logging.Logger.getLogger(Toko_ReturPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Toko_ReturPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Toko_ReturPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Toko_ReturPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new Toko_ReturPenjualan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnSimpanPenjualan1;
    private javax.swing.JButton btnTambahBarangRetur;
    private javax.swing.JDialog dPilihBarang;
    private javax.swing.JDialog dPilihDataBarang;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jReturPenjualan;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField vHarga;
    private javax.swing.JTextField vJumlah;
    private javax.swing.JTextField vNamaBarang;
    private javax.swing.JTextField vNota;
    private javax.swing.JButton vOk;
    private javax.swing.JComboBox vSatuan;
    private javax.swing.JTextField vSearch;
    private javax.swing.JTextField vSearchRetur;
    private javax.swing.JTextField vTotalHarga;
    private javax.swing.JLabel vTotalStok;
    private javax.swing.JLabel vdatetime;
    // End of variables declaration//GEN-END:variables
}
