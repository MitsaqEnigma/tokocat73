/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Java;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Miracle
 */
public class modelTabelBarang extends AbstractTableModel{
    
    private List<ListBarang> list;

    public modelTabelBarang(List<ListBarang> list) {
        this.list=list;
    }
   

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 10;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return 0;
            case 1:
                return list.get(rowIndex).getKode_barang();
            case 2:
                return list.get(rowIndex).getNama_barang();
            case 3:
                return 0;
            case 4:
                return 0;
            case 5:
                return 0;
            case 6:
                return list.get(rowIndex).getHarga_jual_1_barang();
            case 7:
                return list.get(rowIndex).getHarga_jual_1_barang()*2;
            default:
                return null;
        }
    }
    
     @Override
    public String getColumnName(int column) {
        switch (column){
            case 0:
                return "No";
            case 1:
                return "Kode";
            case 2:
                return "Nama";
            case 3:
                return "Satuan";
            case 4:
                return "Qty";
            case 5:
                return "J.Harga (1/2/3)";
            case 6:
                return "Harga";
            case 7:
                return "Sub Total";
            default:
                return null;
        }
    }

    
}
