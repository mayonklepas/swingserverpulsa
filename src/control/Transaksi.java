/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import helper.Dbhelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mulyadi
 */
public class Transaksi {

    view.Transaksi form;
    Dbhelper dbh = new Dbhelper();
    String ids;
    NumberFormat nf=NumberFormat.getInstance();

    public Transaksi(view.Transaksi form) {
        this.form = form;
        loaddata();
        caridata();
        hapus();
        refresh();
        konfirmasi();
    }

    public void loaddata() {
        try {
            dbh.connect();
            DefaultTableModel dtm = new DefaultTableModel();
            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();
            form.tbview.setModel(dtm);
            dtm.addColumn("ID");
            dtm.addColumn("Tanggal Masuk");
            dtm.addColumn("No HP");
            dtm.addColumn("ID Member");
            dtm.addColumn("Nama");
            dtm.addColumn("Layanan");
            dtm.addColumn("Harga Beli");
            dtm.addColumn("Harga Jual");
            dtm.addColumn("Status");
            dtm.addColumn("Tujuan");
            dtm.addColumn("No HP Tujuan");
            ResultSet res = dbh.read("SELECT data_transaksi.id AS idtransaksi,tanggal::date,"
                    + "data_transaksi.nohp,data_member.nama AS namamember,data_member.id AS idmember,"
                    + "data_tarif.nama AS namatarif, CASE WHEN tipe_member='normal' THEN harga ELSE harga_reseller END AS harga,harga_pulsa,"
                    + "data_transaksi.status,tujuan,nohptujuan"
                    + " FROM data_transaksi LEFT JOIN data_member ON data_transaksi.nohp = data_member.nohp"
                    + " LEFT JOIN data_tarif ON data_transaksi.id_tarif=data_tarif.id ORDER BY data_transaksi.id DESC").executeQuery();
            while (res.next()) {
                Object[] o = new Object[11];
                o[0] = res.getString("idtransaksi");
                o[1] = res.getString("tanggal");
                o[2] = res.getString("nohp");
                o[3] = res.getString("idmember");
                o[4] = res.getString("namamember");
                o[5] = res.getString("namatarif");
                o[6] = res.getString("harga");
                o[7] = res.getString("harga_pulsa");
                o[8] = res.getString("status");
                o[9] = res.getString("tujuan");
                o[10] = res.getString("nohptujuan");
                dtm.addRow(o);
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Layanan.class.getName()).log(Level.SEVERE, null, ex);
        }

        form.ltotal.setText("Jumlah Data = " + form.tbview.getRowCount());
        double totaldapat=0;
        for (int i = 0; i < form.tbview.getRowCount(); i++) {
            totaldapat=totaldapat+Double.parseDouble(form.tbview.getValueAt(i, 7).toString());
        }
        form.ltotaluang.setText("Pendapatan = "+ nf.format(totaldapat));
        ids = "";
    }

    public void caridata() {
        form.edcari.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                rawcari();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                rawcari();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                rawcari();
            }
        });

    }

    public void rawcari() {
        try {
            dbh.connect();
            DefaultTableModel dtm = new DefaultTableModel();
            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();
            form.tbview.setModel(dtm);
            dtm.addColumn("ID");
            dtm.addColumn("Tanggal Masuk");
            dtm.addColumn("No HP");
            dtm.addColumn("ID Member");
            dtm.addColumn("Nama");
            dtm.addColumn("Layanan");
            dtm.addColumn("Harga Beli");
            dtm.addColumn("Harga Jual");
            dtm.addColumn("Status");
            dtm.addColumn("Tujuan");
            dtm.addColumn("No HP Tujuan");
            Object[] ocari = new Object[2];
            ocari[0] = "%" + form.edcari.getText() + "%";
            ocari[1] = "%" + form.edcari.getText() + "%";
            ResultSet res = dbh.readdetail("SELECT data_transaksi.id AS idtransaksi,tanggal::date,"
                    + "data_transaksi.nohp,data_member.nama AS namamember,data_member.id AS idmember,"
                    + "data_tarif.nama AS namatarif, CASE WHEN tipe_member='normal' THEN harga ELSE harga_reseller END AS harga,harga_pulsa,"
                    + "data_transaksi.status,tujuan,nohptujuan"
                    + " FROM data_transaksi LEFT JOIN data_member ON data_transaksi.nohp = data_member.nohp"
                    + " LEFT JOIN data_tarif ON data_transaksi.id_tarif=data_tarif.id "
                    + "WHERE data_member.nama ILIKE ? OR  data_transaksi.nohp ILIKE ? ORDER BY data_transaksi.id DESC", 2, ocari).executeQuery();
            while (res.next()) {
                Object[] o = new Object[11];
                o[0] = res.getString("idtransaksi");
                o[1] = res.getString("tanggal");
                o[2] = res.getString("nohp");
                o[3] = res.getString("idmember");
                o[4] = res.getString("namamember");
                o[5] = res.getString("namatarif");
                o[6] = res.getString("harga");
                o[7] = res.getString("harga_pulsa");
                o[8] = res.getString("status");
                o[9] = res.getString("tujuan");
                o[10] = res.getString("nohptujuan");
                dtm.addRow(o);
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Layanan.class.getName()).log(Level.SEVERE, null, ex);
        }
        form.ltotal.setText("Jumlah Data = " + form.tbview.getRowCount());
        double totaldapat=0;
        for (int i = 0; i < form.tbview.getRowCount(); i++) {
            totaldapat=totaldapat+Double.parseDouble(form.tbview.getValueAt(i, 7).toString());
        }
        form.ltotaluang.setText("Pendapatan = "+ nf.format(totaldapat));
    }

    public void refresh() {
        form.brefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                loaddata();
                form.edcari.setText("");
            }
        });
    }

    public void hapus() {
        form.bhapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (JOptionPane.showConfirmDialog(form, "Yakin ingin Menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION, 0) == 0) {
                    try {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        dbh.connect();
                        int[] i = form.tbview.getSelectedRows();
                        for (int j = 0; j < i.length; j++) {
                            dbh.delete("DELETE FROM data_transaksi WHERE id::character varying=?", form.tbview.getValueAt(i[j], 0).toString());
                        }

                        dbh.disconnect();
                    } catch (SQLException ex) {
                        Logger.getLogger(Layanan.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JOptionPane.showMessageDialog(form, "Data Berhasi Dihapus");
                    loaddata();
                }
            }
        });
    }

    public void konfirmasi() {
        form.bkonfirmasi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
               if (JOptionPane.showConfirmDialog(form, "Yakin ingin Mengkonfirmasi data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION, 0) == 0) {
                    try {
                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        dbh.connect();
                        int[] i = form.tbview.getSelectedRows();
                        for (int j = 0; j < i.length; j++) {
                            dbh.delete("UPDATE data_transaksi SET status='Terkonfirmasi' WHERE id::character varying=?", form.tbview.getValueAt(i[j], 0).toString());
                        }

                        dbh.disconnect();
                    } catch (SQLException ex) {
                        Logger.getLogger(Layanan.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JOptionPane.showMessageDialog(form, "Data Berhasi Dikonfirmasi");
                    loaddata();
                }
            }
        });
    }

}
