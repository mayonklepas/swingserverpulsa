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
import view.SMS;

/**
 *
 * @author Mulyadi
 */
public class Member {

    view.Member form;
    Dbhelper dbh = new Dbhelper();
    String ids;

    public Member(view.Member form) {
        this.form = form;
        loaddata();
        selectdata();
        caridata();
        hapus();
        refresh();
        simpan();
        re();
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
            dtm.addColumn("Nama");
            dtm.addColumn("Nohp");
            dtm.addColumn("Telegram");
            dtm.addColumn("Kategori");
            dtm.addColumn("Deposit");
            dtm.addColumn("Alamat");
            dtm.addColumn("Tipe");
            dtm.addColumn("Status");
            ResultSet res = dbh.read("SELECT id,nama,nohp,telegram_id,kategori,deposit,alamat,tipe_member,status"
                    + " FROM data_member ORDER BY nama ASC").executeQuery();
            while (res.next()) {
                Object[] o = new Object[9];
                o[0] = res.getString("id");
                o[1] = res.getString("nama");
                o[2] = res.getString("nohp");
                o[3] = res.getString("telegram_id");
                o[4] = res.getString("kategori");
                o[5] = res.getString("deposit");
                o[6] = res.getString("alamat");
                o[7] = res.getString("tipe_member");
                o[8] = res.getString("status");
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
            dtm.addColumn("Nohp");
            dtm.addColumn("Telegram");
            dtm.addColumn("Kategori");
            dtm.addColumn("Deposit");
            dtm.addColumn("Alamat");
            dtm.addColumn("Tipe");
            dtm.addColumn("Status");
            Object[] ocari = new Object[5];
            ocari[0] = "%" + form.edcari.getText() + "%";
            ocari[1] = "%" + form.edcari.getText() + "%";
            ocari[2] = "%" + form.edcari.getText() + "%";
            ocari[3] = "%" + form.edcari.getText() + "%";
            ocari[4] = "%" + form.edcari.getText() + "%";
            ResultSet res = dbh.readdetail("SELECT id,nama,nohp,telegram_id,kategori,deposit,alamat,tipe_member,status"
                    + " FROM data_member WHERE "
                    + "nama ILIKE ? OR "
                    + "nohp ILIKE ? OR "
                    + "kategori ILIKE ? OR "
                    + "tipe_member ILIKE ? OR "
                    + "status ILIKE ? ORDER BY nama ASC ", 5, ocari).executeQuery();
            while (res.next()) {
                Object[] o = new Object[9];
                o[0] = res.getString("id");
                o[1] = res.getString("nama");
                o[2] = res.getString("nohp");
                o[3] = res.getString("telegram_id");
                o[4] = res.getString("kategori");
                o[5] = res.getString("deposit");
                o[6] = res.getString("alamat");
                o[7] = res.getString("tipe_member");
                o[8] = res.getString("status");
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
                form.ednama.setText(form.tbview.getValueAt(i, 1).toString());
                form.ednohp.setText(form.tbview.getValueAt(i, 2).toString());
                form.edtelid.setText(form.tbview.getValueAt(i, 3).toString());
                if (form.tbview.getValueAt(i, 4).toString().equals("Post Paid")) {
                    form.ckategori.setSelectedIndex(0);
                } else {
                    form.ckategori.setSelectedIndex(1);
                }
                form.eddeposit.setText(form.tbview.getValueAt(i, 5).toString());
                form.edalamat.setText(form.tbview.getValueAt(i, 6).toString());
                if (form.tbview.getValueAt(i, 7).toString().equals("normal")) {
                    form.ctipe.setSelectedIndex(0);
                } else {
                    form.ctipe.setSelectedIndex(1);
                }

                if (form.tbview.getValueAt(i, 8).toString().equals("disable")) {
                    form.cstatus.setSelectedIndex(0);
                } else {
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
                            dbh.delete("DELETE FROM data_member WHERE id=?", form.tbview.getValueAt(i[j], 0).toString());
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
                        Object[] o = new Object[9];
                        o[0] = new Setnumberhelper().nourut("MB", "id", "data_member");
                        o[1] = form.ednama.getText();
                        o[2] = form.ednohp.getText();
                        o[3] = form.edtelid.getText();
                        o[4] = form.ckategori.getSelectedItem().toString();
                        o[5] = Double.parseDouble(form.eddeposit.getText());
                        o[6] = form.edalamat.getText();
                        o[7] = form.ctipe.getSelectedItem().toString();
                        o[8] = form.cstatus.getSelectedItem().toString();
                        dbh.insert("INSERT INTO data_member(id,nama,nohp,telegram_id,kategori,deposit,"
                                + "alamat,tipe_member,status)"
                                + " VALUES(?,?,?,?,?,?,?,?,?)", 9, o);
                        JOptionPane.showMessageDialog(form, "Data Berhasil Disimpan");
                        loaddata();
                    } else {
                        Object[] o = new Object[9];
                        o[0] = form.ednama.getText();
                        o[1] = form.ednohp.getText();
                        o[2] = form.edtelid.getText();
                        o[3] = form.ckategori.getSelectedItem().toString();
                        o[4] = Double.parseDouble(form.eddeposit.getText());
                        o[5] = form.edalamat.getText();
                        o[6] = form.ctipe.getSelectedItem().toString();
                        o[7] = form.cstatus.getSelectedItem().toString();
                        o[8] = ids;
                        dbh.insert("UPDATE data_member SET nama=?,nohp=?,telegram_id=?,kategori=?,deposit=?,"
                                + "alamat=?,tipe_member=?,status=? WHERE id=?", 9, o);
                        JOptionPane.showMessageDialog(form, "Data Berhasil Diperbaharui");
                    }
                    dbh.disconnect();
                } catch (SQLException ex) {
                    Logger.getLogger(Layanan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );

    }

    public void re() {
        form.bclear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.;
                form.ednama.setText("");
                form.ednohp.setText("");
                form.edtelid.setText("");
                form.ckategori.setSelectedIndex(0);
                form.eddeposit.setText("");
                form.edalamat.setText("");
                form.ctipe.setSelectedIndex(0);
                form.cstatus.setSelectedIndex(0);
                form.edcari.setText("");
                ids = "";
            }
        });
    }

    public void kirimpesan() {
        form.bkirimpesan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                int i = form.tbview.getSelectedRow();
                Pesangetset pgs = new Pesangetset();
                pgs.setNohp(form.tbview.getValueAt(i, 2).toString());
                pgs.setPesandia("");
                view.SMS sms = new view.SMS();
                sms.setVisible(true);
            }
        });
    }
}
