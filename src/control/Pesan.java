/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import getset.Pesangetset;
import helper.Dbhelper;
import helper.Setnumberhelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mulyadi
 */
public class Pesan {

    view.Pesan form;
    Dbhelper dbh = new Dbhelper();
    String ids;

    public Pesan(view.Pesan form) {
        this.form = form;
        loaddata();
        caridata();
        hapus();
        refresh();
        kirimpesan();
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
            dtm.addColumn("Nama");
            dtm.addColumn("ID Member");
            dtm.addColumn("Kategori");
            dtm.addColumn("Pesan");
            ResultSet res = dbh.read("SELECT data_pesan.id AS idpesan,tanggal_masuk,data_pesan.nohp,nama,data_member.id AS idmember,data_pesan.kategori AS katpesan,pesan"
                    + " FROM data_pesan LEFT JOIN data_member ON data_pesan.nohp = data_member.nohp ORDER BY data_pesan.id DESC").executeQuery();
            while (res.next()) {
                Object[] o = new Object[7];
                o[0] = res.getString("idpesan");
                o[1] = res.getString("tanggal_masuk");
                o[2] = res.getString("nohp");
                o[3] = res.getString("nama");
                o[4] = res.getString("idmember");
                o[5] = res.getString("katpesan");
                o[6] = res.getString("pesan");
                dtm.addRow(o);
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Layanan.class.getName()).log(Level.SEVERE, null, ex);
        }

        form.ltotal.setText("Jumlah Data = " + form.tbview.getRowCount());
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
            dtm.addColumn("Nama");
            dtm.addColumn("ID Member");
            dtm.addColumn("Kategori");
            Object[] ocari = new Object[5];
            ocari[0] = "%" + form.edcari.getText() + "%";
            ocari[1] = "%" + form.edcari.getText() + "%";
            ocari[2] = "%" + form.edcari.getText() + "%";
            ocari[3] = "%" + form.edcari.getText() + "%";
            ocari[4] = "%" + form.edcari.getText() + "%";
            ResultSet res = dbh.readdetail("SSELECT data_pesan.id AS idpesan,tanggal_masuk,data_pesan.nohp,nama,data_member.id AS idmember,data_pesan.kategori AS katpesan,pesan"
                    + " FROM data_pesan LEFT JOIN data_member ON data_pesan.nohp = data_member.nohp WHERE "
                    + "nama ILIKE ? OR "
                    + "nohp ILIKE ? OR "
                    + "katpesan ILIKE ? "
                    + "status ILIKE ? ORDER BY data_pesan.id DESC ", 5, ocari).executeQuery();
            while (res.next()) {
                Object[] o = new Object[7];
                o[0] = res.getString("idpesan");
                o[1] = res.getString("tanggal_masuk");
                o[2] = res.getString("nohp");
                o[3] = res.getString("nama");
                o[4] = res.getString("idmember");
                o[5] = res.getString("katpesan");
                o[6] = res.getString("pesan");
                dtm.addRow(o);
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Layanan.class.getName()).log(Level.SEVERE, null, ex);
        }
        form.ltotal.setText("Jumlah Data = " + form.tbview.getRowCount());
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
                            dbh.delete("DELETE FROM data_pesan WHERE id::character varying=?", form.tbview.getValueAt(i[j], 0).toString());
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


    public void kirimpesan() {
        form.bbalas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                int i=form.tbview.getSelectedRow();
                Pesangetset pgs=new Pesangetset();
                pgs.setNohp(form.tbview.getValueAt(i, 2).toString());
                pgs.setPesandia(form.tbview.getValueAt(i, 6).toString());
                view.SMS sms = new view.SMS();
                sms.setLocationRelativeTo(null);
                sms.setVisible(true);
            }
        });
    }

}
