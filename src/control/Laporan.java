/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import helper.Dbhelper;
import helper.Filehelper;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Mulyadi
 */
public class Laporan {

    view.Laporan form;
    Filehelper fh = new Filehelper();
    Dbhelper dbh = new Dbhelper();

    public Laporan(view.Laporan form) {
        this.form = form;
        generatelaporan();
    }

    public void generatelaporan() {
        form.bcetak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                //form.laporankonten.removeAll();
                HashMap hash = null;
                String path = "";
                int i = form.ckategori.getSelectedIndex();
                if (i == 0) {
                    Date dari = form.ctanggaldari.getDate();
                    Date ke = form.ctanggalhingga.getDate();
                    hash = new HashMap(4);
                    hash.put("header", fh.namaperusahaan());
                    hash.put("detail", "Laporan Transaksi");
                    hash.put("dari", dari);
                    hash.put("ke", ke);
                    path = "laporan/laporan_transaksi.jasper";
                } else if (i == 1) {
                    Date dari = form.ctanggaldari.getDate();
                    Date ke = form.ctanggalhingga.getDate();
                    hash = new HashMap(4);
                    hash.put("header", fh.namaperusahaan());
                    hash.put("detail", "Laporan Pesan");
                    hash.put("dari", dari);
                    hash.put("ke", ke);
                    path = "laporan/laporan_pesan.jasper";
                } else if (i == 2) {
                    hash = new HashMap(2);
                    hash.put("header", fh.namaperusahaan());
                    hash.put("detail", "List Tarif dan Layanan");
                    path = "laporan/laporan_tarif.jasper";
                }
                
                Thread th=new Thread(new loadreport(hash, path));
                th.setDaemon(true);
                th.start();

            }
        });
    }

    public Connection conn() throws SQLException {
        dbh.conn = DriverManager.getConnection(dbh.host, dbh.user, dbh.password);
        return dbh.conn;
    }

    public class loadreport implements Runnable {

        HashMap hash;
        String path;

        public loadreport(HashMap hash, String path) {
            this.hash = hash;
            this.path = path;
        }

        @Override
        public void run() {
            try {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                JasperReport jr = (JasperReport) JRLoader.loadObject(new File(path));
                JasperPrint jp = JasperFillManager.fillReport(jr, hash, conn());
                JRViewer jvw=new JRViewer(jp);
                form.laporankonten.removeAll();
                form.laporankonten.setLayout(new GridLayout());
                form.laporankonten.add(jvw);
                jvw.repaint();
                jvw.revalidate();
            } catch (JRException ex) {
                Logger.getLogger(Laporan.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Laporan.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
