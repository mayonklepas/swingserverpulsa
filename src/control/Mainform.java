/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import helper.Modemhelper;
import helper.Telegramhelper;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JOptionPane;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import view.Ussd;

/**
 *
 * @author Mulyadi
 */
public class Mainform {

    view.Mainform form;

    public Mainform(view.Mainform form) {
        this.form = form;
        Thread th = new Thread(new Modemhelper());
                TelegramBotsApi tba = new TelegramBotsApi();
        try {
            tba.registerBot(new Telegramhelper());
        } catch (TelegramApiException ex) {
            Logger.getLogger(Mainform.class.getName()).log(Level.SEVERE, null, ex);
        }
        th.start();
        layanan();
        pesan();
        member();
        transaksi();
        home();
        laporan();
        ussd();
    }

    public void layanan() {
        form.blayanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                form.container.removeAll();
                view.Layanan panel = new view.Layanan();
                form.container.setLayout(new GridLayout());
                form.container.add(panel);
                panel.repaint();
                panel.revalidate();
            }
        });
    }

    public void member() {
        form.bmember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                form.container.removeAll();
                view.Member panel = new view.Member();
                form.container.setLayout(new GridLayout());
                form.container.add(panel);
                panel.repaint();
                panel.revalidate();
            }
        });
    }

    public void transaksi() {
        form.btransaksi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                form.container.removeAll();
                view.Transaksi panel = new view.Transaksi();
                form.container.setLayout(new GridLayout());
                form.container.add(panel);
                panel.repaint();
                panel.revalidate();
            }
        });
    }

    public void pesan() {
        form.bpesan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                form.container.removeAll();
                view.Pesan panel = new view.Pesan();
                form.container.setLayout(new GridLayout());
                form.container.add(panel);
                panel.repaint();
                panel.revalidate();
            }
        });
    }

    public void home() {
        form.bhome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                form.container.removeAll();
                view.Home panel = new view.Home();
                form.container.setLayout(new GridLayout());
                form.container.add(panel);
                panel.repaint();
                panel.revalidate();
            }
        });
    }

    public void laporan() {
        form.blaporan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                form.container.removeAll();
                view.Laporan panel = new view.Laporan();
                form.container.setLayout(new GridLayout());
                form.container.add(panel);
                panel.repaint();
                panel.revalidate();
            }
        });
    }

    public void ussd() {
        form.bussd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                view.Ussd ussd = new view.Ussd();
                ussd.setTitle("USSD");
                ussd.setLocationRelativeTo(null);
                ussd.setVisible(true);
            }
        });
    }

}
