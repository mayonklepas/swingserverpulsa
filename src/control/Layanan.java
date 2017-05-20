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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jb2011.lnf.beautyeye.utils.RawCache;

/**
 *
 * @author Mulyadi
 */
public class Layanan {

    view.Layanan form;
    Dbhelper dbh = new Dbhelper();
    String ids;

    public Layanan(view.Layanan form) {
        this.form = form;
        loaddata();
        selectdata();
        caridata();
        hapus();
        refresh();
        simpan();
        re();
    }

    public void loaddata() {
        try {
            dbh.connect();
            DefaultTableModel dtm = new DefaultTableModel();
            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();
            form.tbview.setModel(dtm);
            dtm.addColumn("ID");
            dtm.addColumn("Nama");
            dtm.addColumn("Nominal");
            dtm.addColumn("Harga Normal");
            dtm.addColumn("Harga Reseller");
            dtm.addColumn("Kode Eksekusi");
            dtm.addColumn("Status");
            ResultSet res = dbh.read("SELECT id,nama,jumlah,harga,harga_reseller,kode,kondisi FROM data_tarif ORDER BY nama ASC").executeQuery();
            while (res.next()) {
                Object[] o = new Object[7];
                o[0] = res.getString("id");
                o[1] = res.getString("nama");
                o[2] = res.getString("jumlah");
                o[3] = res.getString("harga");
                o[4] = res.getString("harga_reseller");
                o[5] = res.getString("kode");
                o[6] = res.getString("kondisi");
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
            dtm.addColumn("Nama");
            dtm.addColumn("Nominal");
            dtm.addColumn("Harga Normal");
            dtm.addColumn("Harga Reseller");
            dtm.addColumn("Kode Eksekusi");
            dtm.addColumn("Status");
            Object[] ocari = new Object[7];
            ocari[0] = "%" + form.edcari.getText() + "%";
            ocari[1] = "%" + form.edcari.getText() + "%";
            ocari[2] = "%" + form.edcari.getText() + "%";
            ocari[3] = "%" + form.edcari.getText() + "%";
            ocari[4] = "%" + form.edcari.getText() + "%";
            ocari[5] = "%" + form.edcari.getText() + "%";
            ocari[6] = "%" + form.edcari.getText() + "%";
            ResultSet res = dbh.readdetail("SELECT id,nama,jumlah,harga,harga_reseller,kode,kondisi FROM data_tarif "
                    + "WHERE id::character varying ILIKE ? OR "
                    + "nama ILIKE ? OR "
                    + "jumlah::character varying ILIKE ? OR "
                    + "harga::character varying ILIKE ? OR "
                    + "harga_reseller::character varying ILIKE ? OR "
                    + "kode ILIKE ? OR "
                    + "kondisi ILIKE ? ", 7, ocari).executeQuery();
            while (res.next()) {
                Object[] o = new Object[7];
                o[0] = res.getString("id");
                o[1] = res.getString("nama");
                o[2] = res.getString("jumlah");
                o[3] = res.getString("harga");
                o[4] = res.getString("harga_reseller");
                o[5] = res.getString("kode");
                o[6] = res.getString("kondisi");
                dtm.addRow(o);
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Layanan.class.getName()).log(Level.SEVERE, null, ex);
        }
        form.ltotal.setText("Jumlah Data = " + form.tbview.getRowCount());
    }

    public void selectdata() {
        form.tbview.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                int i = form.tbview.getSelectedRow();
                ids = form.tbview.getValueAt(i, 0).toString();
                form.edid.setText(form.tbview.getValueAt(i, 0).toString());
                form.ednama.setText(form.tbview.getValueAt(i, 1).toString());
                form.ednominal.setText(form.tbview.getValueAt(i, 2).toString());
                form.edharganormal.setText(form.tbview.getValueAt(i, 3).toString());
                form.edhargareseller.setText(form.tbview.getValueAt(i, 4).toString());
                form.edkodeeksekusi.setText(form.tbview.getValueAt(i, 5).toString());
                if (form.tbview.getValueAt(i, 6).toString().equals("disable")){
                    form.cstatus.setSelectedIndex(0);
                }else{
                    form.cstatus.setSelectedIndex(1);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
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
                            dbh.delete("DELETE FROM data_tarif WHERE id=?", form.tbview.getValueAt(i[j], 0).toString());
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

    public void simpan() {

        form.bsimpan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    dbh.connect();
                    if (ids.equals("")) {
                        Object[] o = new Object[7];
                        o[0] = form.edid.getText();
                        o[1] = form.ednama.getText();
                        o[2] = Double.parseDouble(form.ednominal.getText());
                        o[3] = Double.parseDouble(form.edharganormal.getText());
                        o[4] = Double.parseDouble(form.edhargareseller.getText());
                        o[5] = form.edkodeeksekusi.getText();
                        o[6] = form.cstatus.getSelectedItem().toString();
                        dbh.insert("INSERT INTO data_tarif(id,nama,jumlah,harga,harga_reseller,kode,kondisi)"
                                + " VALUES(?,?,?,?,?,?,?)", 7, o);
                        JOptionPane.showMessageDialog(form, "Data Berhasil Disimpan");
                        loaddata();
                    } else {
                        Object[] o = new Object[8];
                        o[0] = form.edid.getText();
                        o[1] = form.ednama.getText();
                        o[2] = Double.parseDouble(form.ednominal.getText());
                        o[3] = Double.parseDouble(form.edharganormal.getText());
                        o[4] = Double.parseDouble(form.edhargareseller.getText());
                        o[5] = form.edkodeeksekusi.getText();
                        o[6] = form.cstatus.getSelectedItem().toString();
                        o[7] = ids;
                        dbh.insert("UPDATE data_tarif SET id=?,nama=?,jumlah=?,harga=?,harga_reseller=?,kode=?,"
                                + "kondisi=? WHERE id=?", 8, o);
                        JOptionPane.showMessageDialog(form, "Data Berhasil Diperbaharui");
                    }
                    dbh.disconnect();
                } catch (SQLException ex) {
                    Logger.getLogger(Layanan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public void re() {
        form.bclear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                form.edid.setText("");
                form.ednama.setText("");
                form.ednominal.setText("");
                form.edharganormal.setText("");
                form.edhargareseller.setText("");
                form.edkodeeksekusi.setText("");
                form.cstatus.setSelectedIndex(0);
                form.edcari.setText("");
                ids="";
            }
        });
    }

}
