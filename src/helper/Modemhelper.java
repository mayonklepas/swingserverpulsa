/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import getset.Ussdgetset;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOutboundMessageNotification;
import org.smslib.IUSSDNotification;
import org.smslib.InboundMessage;
import org.smslib.Message;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.USSDResponse;
import org.smslib.modem.SerialModemGateway;


/**
 *
 * @author Minami
 */
public class Modemhelper implements Runnable{

    public static SerialModemGateway g1;
    Filehelper fh = new Filehelper();
    Dbhelper dbh = new Dbhelper();

    class inbound implements IInboundMessageNotification {

        @Override
        public void process(AGateway ag, Message.MessageTypes mt, InboundMessage im) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            if (mt == Message.MessageTypes.INBOUND) {
                try {
                    String fixnohp = "0" + im.getOriginator().substring(2);
                    String tanggal = new SimpleDateFormat("dd-MM-yyyy H:m:s").format(im.getDate());
                    insertpesan(tanggal, fixnohp, im.getText().toLowerCase());

                    String[] kode = im.getText().toLowerCase().split("\\.");

                    if (fh.systemservice().equals("nonaktif")) {

                        OutboundMessage om = new OutboundMessage(im.getOriginator(),
                                fh.pesannonaktif());
                        Service.getInstance().sendMessage(om);

                    } else if (fh.systemservice().equals("standing by")) {

                    } else if (im.getText().toLowerCase().contains(fh.hostkata3()) == true) {
                        String nohpdaripesan = getnohpdaripesan(im.getText());
                        OutboundMessage om = new OutboundMessage(getnohpterakhirtransaksi(nohpdaripesan), nohpdaripesan + " " + fh.pesanmenunggu());
                        Service.getInstance().sendMessage(om);
                        //updatestatustransaksi2("Menunggu",getnohpdaripesan(im.getText()));
                        /*Media sound = new Media(getClass().getResource("/media/TimeOut-Reformation.mp3").toString());
                        MediaPlayer mp = new MediaPlayer(sound);
                        mp.play();*/

                    } else if (im.getText().toLowerCase().contains(fh.hostkata1()) == true) {
                        String nohpdaripesan = getnohpdaripesan(im.getText());
                        OutboundMessage om = new OutboundMessage(getnohpterakhirtransaksi(nohpdaripesan), nohpdaripesan + " " + fh.pesansukses());
                        Service.getInstance().sendMessage(om);
                        //updatestatustransaksi("Terkonfirmasi");
                        updatestatustransaksi2("Terkonfirmasi", nohpdaripesan);
                        /*Media sound = new Media(getClass().getResource("/media/ExceedCharge.mp3").toString());
                        MediaPlayer mp = new MediaPlayer(sound);
                        mp.play();*/

                    } else if (im.getText().toLowerCase().contains(fh.hostkata2()) == true) {
                        String nohpdaripesan = getnohpdaripesan(im.getText());
                        OutboundMessage om = new OutboundMessage(getnohpterakhirtransaksi(nohpdaripesan), nohpdaripesan + " " + fh.pesangagal());
                        Service.getInstance().sendMessage(om);
                        //updatestatustransaksi("Gagal");
                        updatestatustransaksi2("Gagal", nohpdaripesan);

                        /*Media sound = new Media(getClass().getResource("/media/Error.mp3").toString());
                        MediaPlayer mp = new MediaPlayer(sound);
                        mp.play();*/

                    } else if (cekstatus(im.getOriginator()).equals("disable")) {

                    } else if (kode.length == 1) {

                        String kodetarif = cektarif(im.getText().toLowerCase());
                        String nohp = ceknohp(fixnohp);
                        if (nohp.equals("") || nohp == null) {
                            OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf nomor belum terdaftar. \n hubungi 083129345215 untuk mendaftar ");
                            Service.getInstance().sendMessage(om);
                        } else if (kodetarif.equals("") || kodetarif == null) {
                            OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf Layanan Tidak tersedia, gunakan minta.info untuk melihat info");
                            Service.getInstance().sendMessage(om);
                        } else if (cektransaksi(nohp, im.getText().toLowerCase()) >= 1) {
                            OutboundMessage om = new OutboundMessage(im.getOriginator(), "Transaksi sudah dilakukan, harap menunggu jika pulsa belum terisi");
                            Service.getInstance().sendMessage(om);

                            String kode_eksekusi = kodetarif + "." + nohp + "." + fh.hostpin();
                            OutboundMessage om2 = new OutboundMessage(fh.hostpusatpesan2(), kode_eksekusi);
                            Service.getInstance().sendMessage(om2);
                            /*Media sound = new Media(getClass().getResource("/media/StartUp.mp3").toString());
                            MediaPlayer mp = new MediaPlayer(sound);
                            mp.play();*/

                        } else if (ceknohpgetkategori(nohp).contains("Post Paid")) {
                            inserttransaksi(nohp, im.getText().toLowerCase(), "sendiri", nohp);
                            String kode_eksekusi = kodetarif + "." + nohp + "." + fh.hostpin();
                            OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                            Service.getInstance().sendMessage(om);
                            /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                            MediaPlayer mp = new MediaPlayer(sound);
                            mp.play();*/
                        } else {
                            Double deposit = ceknohpgetdeposit(nohp);
                            Double tagihan = cektagihan(nohp);
                            Double totalsaldo = deposit - tagihan;
                            Double harga = cektarifgetharga(im.getText().toLowerCase());
                            if (totalsaldo >= harga) {
                                inserttransaksi(nohp, im.getText().toLowerCase(), "sendiri", nohp);
                                String kode_eksekusi = kodetarif + "." + nohp + "." + fh.hostpin();
                                OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                                Service.getInstance().sendMessage(om);
                                /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                                MediaPlayer mp = new MediaPlayer(sound);
                                mp.play();*/
                            } else {
                                OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf Saldo Tidak mencukupi");
                                Service.getInstance().sendMessage(om);
                            }
                        }

                    } else if (kode.length == 2) {

                        if (kode[0].equals("minta")) {

                            if (kode[1].equals("info")) {
                                OutboundMessage om = new OutboundMessage(im.getOriginator(),
                                        "Maaf saat ini Request Info hanya melalui telegram"
                                );
                                Service.getInstance().sendMessage(om);
                            } else if (kode[1].equals("tagihan")) {
                                String nohp = ceknohp(fixnohp);
                                if (nohp.equals("") || nohp == null) {
                                    OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf nomor belum terdaftar. \n hubungi " + fh.nohp() + " untuk mendaftar");
                                    Service.getInstance().sendMessage(om);
                                } else {
                                    OutboundMessage om = new OutboundMessage(im.getOriginator(),
                                            "Total Tagihan Anda = " + mintatagihan(nohp));
                                    Service.getInstance().sendMessage(om);
                                }

                            } else if (kode[1].equals("saldo")) {
                                NumberFormat nf = NumberFormat.getInstance();
                                String nohp = ceknohp(fixnohp);
                                Double deposit = ceknohpgetdeposit(nohp);
                                Double tagihan = cektagihan(nohp);
                                Double saldo = deposit - tagihan;
                                if (nohp.equals("") || nohp == null) {
                                    OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf nomor belum terdaftar. \n hubungi " + fh.nohp() + " untuk mendaftar");
                                    Service.getInstance().sendMessage(om);
                                } else {
                                    OutboundMessage om = new OutboundMessage(im.getOriginator(),
                                            "Total Saldo Anda = " + nf.format(saldo));
                                    Service.getInstance().sendMessage(om);
                                }

                            } else {
                                OutboundMessage om = new OutboundMessage(im.getOriginator(), "Layanan tidak tersedia, gunakan minta.info untuk melihat info");
                                Service.getInstance().sendMessage(om);
                            }

                        } else {
                            String kodetarif = cektarif(kode[0]);
                            String nohp = ceknohp(fixnohp);
                            if (nohp.equals("") || nohp == null) {
                                OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf nomor belum terdaftar. \n hubungi " + fh.nohp() + " untuk mendaftar");
                                Service.getInstance().sendMessage(om);
                            } else if (kodetarif.equals("") || kodetarif == null) {
                                OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf Layanan Tidak tersedia, gunakan minta.info untuk melihat info");
                                Service.getInstance().sendMessage(om);
                            } else if (cektransaksi(kode[1], kode[0]) >= 1) {
                                OutboundMessage om = new OutboundMessage(im.getOriginator(), "Transaksi sudah dilakukan, harap menunggu jika pulsa belum terisi");
                                Service.getInstance().sendMessage(om);

                                String kode_eksekusi = kodetarif + "." + kode[1] + "." + fh.hostpin();
                                OutboundMessage om2 = new OutboundMessage(fh.hostpusatpesan2(), kode_eksekusi);
                                Service.getInstance().sendMessage(om2);

                                /*Media sound = new Media(getClass().getResource("/media/StartUp.mp3").toString());
                                MediaPlayer mp = new MediaPlayer(sound);
                                mp.play();*/

                            } else if (ceknohpgetkategori(nohp).contains("Post Paid")) {
                                inserttransaksi(nohp, kode[0], "lain", kode[1]);
                                String kode_eksekusi = kodetarif + "." + kode[1] + "." + fh.hostpin();
                                OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                                Service.getInstance().sendMessage(om);
                                /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                                MediaPlayer mp = new MediaPlayer(sound);
                                mp.play();*/
                            } else {
                                Double deposit = ceknohpgetdeposit(nohp);
                                Double tagihan = cektagihan(nohp);
                                Double totalsaldo = deposit - tagihan;
                                Double harga = cektarifgetharga(kode[0]);
                                if (totalsaldo >= harga) {
                                    inserttransaksi(nohp, kode[0], "lain", kode[1]);
                                    String kode_eksekusi = kodetarif + "." + kode[1] + "." + fh.hostpin();
                                    OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                                    Service.getInstance().sendMessage(om);
                                    /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                                    MediaPlayer mp = new MediaPlayer(sound);
                                    mp.play();*/
                                } else {
                                    OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf Saldo Tidak mencukupi");
                                    Service.getInstance().sendMessage(om);
                                }
                            }
                        }

                    } else if (kode.length == 3) {

                        if (kode[0].equals("darilain")) {
                            String kodetarif = cektarif(kode[1]);
                            String nohp = ceknohp(kode[2]);
                            if (kodetarif.equals("") || kodetarif == null) {
                                OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf Layanan Tidak tersedia, gunakan minta.info untuk melihat info");
                                Service.getInstance().sendMessage(om);

                            } else if (nohp.equals("") || nohp == null) {
                                OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf nomor belum terdaftar. \n hubungi " + fh.nohp() + " untuk mendaftar");
                                Service.getInstance().sendMessage(om);
                            } else if (cektransaksi(nohp, kode[1]) >= 1) {
                                OutboundMessage om = new OutboundMessage(im.getOriginator(), "Transaksi sudah dilakukan, harap menunggu jika pulsa belum terisi");
                                Service.getInstance().sendMessage(om);

                                String kode_eksekusi = kodetarif + "." + nohp + "." + fh.hostpin();
                                OutboundMessage om2 = new OutboundMessage(fh.hostpusatpesan2(), kode_eksekusi);
                                Service.getInstance().sendMessage(om2);
                                /*Media sound = new Media(getClass().getResource("/media/StartUp.mp3").toString());
                                MediaPlayer mp = new MediaPlayer(sound);
                                mp.play();*/

                            } else if (ceknohpgetkategori(nohp).contains("Post Paid")) {
                                inserttransaksi(nohp, kode[1], "sendiri", nohp);
                                String kode_eksekusi = kodetarif + "." + nohp + "." + fh.hostpin();
                                OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                                Service.getInstance().sendMessage(om);
                                /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                                MediaPlayer mp = new MediaPlayer(sound);
                                mp.play();*/
                            } else {
                                Double deposit = ceknohpgetdeposit(nohp);
                                Double tagihan = cektagihan(nohp);
                                Double totalsaldo = deposit - tagihan;
                                Double harga = cektarifgetharga(kode[1]);
                                if (totalsaldo >= harga) {
                                    inserttransaksi(nohp, kode[1], "sendiri", nohp);
                                    String kode_eksekusi = kodetarif + "." + nohp + "." + fh.hostpin();
                                    OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                                    Service.getInstance().sendMessage(om);
                                    /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                                    MediaPlayer mp = new MediaPlayer(sound);
                                    mp.play();*/
                                } else {
                                    OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf Saldo Tidak mencukupi");
                                    Service.getInstance().sendMessage(om);
                                }
                            }

                        } else if (kode[0].equals("pln")) {
                            String kodetarif = cektarif(kode[1]);
                            String nohp = ceknohp(fixnohp);
                            if (kodetarif.equals("") || kodetarif == null) {
                                OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf Layanan Tidak tersedia, gunakan minta.info untuk melihat info");
                                Service.getInstance().sendMessage(om);

                            } else if (nohp.equals("") || nohp == null) {
                                OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf nomor belum terdaftar. \n hubungi " + fh.nohp() + " untuk mendaftar");
                                Service.getInstance().sendMessage(om);
                            } else if (ceknohpgetkategori(nohp).contains("Post Paid")) {
                                inserttransaksi(nohp, kode[1], "sendiri", kode[2]);
                                String kode_eksekusi = kodetarif + "." + kode[2] + "#" + nohp + "." + fh.hostpin();
                                OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                                Service.getInstance().sendMessage(om);
                                /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                                MediaPlayer mp = new MediaPlayer(sound);
                                mp.play();*/
                            } else {
                                Double deposit = ceknohpgetdeposit(nohp);
                                Double tagihan = cektagihan(nohp);
                                Double totalsaldo = deposit - tagihan;
                                Double harga = cektarifgetharga(kode[1]);
                                if (totalsaldo >= harga) {
                                    inserttransaksi(nohp, kode[1], "sendiri", kode[2]);
                                    String kode_eksekusi = kodetarif + "." + kode[2] + "#" + nohp + "." + fh.hostpin();
                                    OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                                    Service.getInstance().sendMessage(om);
                                    /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                                    MediaPlayer mp = new MediaPlayer(sound);
                                    mp.play();*/
                                } else {
                                    OutboundMessage om = new OutboundMessage(im.getOriginator(), "Maaf Saldo Tidak mencukupi");
                                    Service.getInstance().sendMessage(om);
                                }
                            }
                        } else {
                            OutboundMessage om = new OutboundMessage(im.getOriginator(), "Format Salah, gunakan minta.info untuk melihat info");
                            Service.getInstance().sendMessage(om);
                        }

                    } else {
                        OutboundMessage om = new OutboundMessage(im.getOriginator(), "Format Salah, gunakan minta.info untuk melihat info");
                        Service.getInstance().sendMessage(om);
                    }

                    Service.getInstance().deleteMessage(im);
                } catch (TimeoutException ex) {
                    Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
                } catch (GatewayException ex) {
                    Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    class ussdnotif implements IUSSDNotification {

        @Override
        public void process(AGateway ag, USSDResponse ussdr) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            /*String datasend=null;
                byte[] bitesend=null;
                bitesend=PduUtils.pduToBytes(ussdr.getContent());
                datasend=PduUtils.decode7bitEncoding(bitesend); */
            Ussdgetset ugs = new Ussdgetset();
            ugs.setPesan(ussdr.getContent());
        }

    }

    class outbound implements IOutboundMessageNotification {

        @Override
        public void process(AGateway ag, OutboundMessage om) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    @Override
    public void run() {
        try {
            //super.run(); //To change body of generated methods, choose Tools | Templates.
            inbound in = new inbound();
            outbound out = new outbound();
            ussdnotif ussdnotif = new ussdnotif();
            g1 = new SerialModemGateway(fh.modem(), fh.commport(), Integer.parseInt(fh.bautrate()), fh.vendor(), fh.tipe());
            g1.setProtocol(AGateway.Protocols.PDU);
            g1.setInbound(true);
            g1.setOutbound(true);
            g1.setSimPin(fh.simpin());
            g1.setSmscNumber(fh.smscenter());
            Service.getInstance().setOutboundMessageNotification(out);
            Service.getInstance().setInboundMessageNotification(in);
            Service.getInstance().setUSSDNotification(ussdnotif);
            Service.getInstance().addGateway(g1);
            Service.getInstance().startService();
        } catch (GatewayException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SMSLibException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendussd(String kode) {
        try {
            g1.sendCustomATCommand("AT+CUSD=1,\"" + kode + "\",15\r");
        } catch (GatewayException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String cektarif(String tarif) {
        String kodeussd = "";
        try {
            dbh.connect();
            Object[] oussd = new Object[1];
            oussd[0] = tarif;
            ResultSet resussd = dbh.readdetail("SELECT kode FROM data_tarif WHERE kondisi='enable' AND id=?", 1, oussd).executeQuery();
            while (resussd.next()) {
                kodeussd = resussd.getString("kode");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kodeussd;
    }

    public String ceknohp(String nohp) {
        String notujuan = "";
        try {
            dbh.connect();
            Object[] onohp = new Object[1];
            onohp[0] = nohp;
            ResultSet resnohp = dbh.readdetail("SELECT nohp FROM data_member WHERE status='enable' AND nohp=?", 1, onohp).executeQuery();
            while (resnohp.next()) {
                notujuan = resnohp.getString("nohp");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return notujuan;
    }

    public String cekstatus(String nohp) {
        String status = "";
        try {
            dbh.connect();
            Object[] onohp = new Object[1];
            onohp[0] = nohp;
            ResultSet resnohp = dbh.readdetail("SELECT status FROM data_member WHERE nohp=?", 1, onohp).executeQuery();
            while (resnohp.next()) {
                status = resnohp.getString("status");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return status;
    }

    public void updatestatustransaksi(String status) {
        try {
            int id = 0;
            dbh.connect();
            Object[] o = new Object[2];
            o[0] = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            o[1] = "Belum Terkonfirmasi";
            ResultSet res = dbh.readdetail("SELECT id FROM data_transaksi WHERE tanggal::date = ?::date "
                    + "AND status=? ORDER BY ID ASC LIMIT 1", 2, o).executeQuery();
            while (res.next()) {
                id = res.getInt("id");
            }

            Object[] oup = new Object[2];
            oup[0] = status;
            oup[1] = id;
            dbh.update("UPDATE data_transaksi SET status=? WHERE id=? ", 2, oup);

            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updatestatustransaksi2(String status, String nomortujuan) {
        try {
            int id = 0;
            dbh.connect();
            Object[] o = new Object[3];
            o[0] = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            o[1] = "Belum Terkonfirmasi";
            o[2] = nomortujuan;
            ResultSet res = dbh.readdetail("SELECT id FROM data_transaksi WHERE tanggal::date = ?::date "
                    + "AND status=? AND nohptujuan=? ORDER BY ID ASC LIMIT 1", 3, o).executeQuery();
            while (res.next()) {
                id = res.getInt("id");
            }

            Object[] oup = new Object[2];
            oup[0] = status;
            oup[1] = id;
            dbh.update("UPDATE data_transaksi SET status=? WHERE id=? ", 2, oup);

            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getnohpterakhirtransaksi(String notujuan) {
        String nohp = "";
        try {
            dbh.connect();
            Object[] o = new Object[3];
            o[0] = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            o[1] = "Belum Terkonfirmasi";
            o[2] = notujuan;
            ResultSet res = dbh.readdetail("SELECT nohp FROM data_transaksi WHERE tanggal::date = ?::date "
                    + "AND status=? AND nohptujuan=? ORDER BY ID ASC LIMIT 1", 3, o).executeQuery();
            while (res.next()) {
                nohp = res.getString("nohp");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nohp;
    }

    public void insertpesan(String tanggal, String nohp, String pesan) {
        try {
            dbh.connect();
            Object[] op = new Object[4];
            op[0] = tanggal;
            op[1] = nohp;
            op[2] = pesan;
            op[3] = "SMS";
            dbh.insert("INSERT INTO data_pesan(tanggal_masuk,nohp,pesan,kategori) VALUES(?,?,?,?)", 4, op);
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void inserttransaksi(String nohp, String idtarif, String tujuan, String nohptujuan) {
        try {
            dbh.connect();
            String tipemember = "";
            Object[] otipe = new Object[1];
            otipe[0] = nohp;
            ResultSet restipemember = dbh.readdetail("SELECT tipe_member FROM data_member WHERE nohp=?", 1, otipe).executeQuery();
            while (restipemember.next()) {
                tipemember = restipemember.getString("tipe_member");
            }

            double harga_pulsa = 0;
            Object[] oc = new Object[1];
            oc[0] = idtarif;
            ResultSet restarif = dbh.readdetail("SELECT harga,harga_reseller FROM data_tarif WHERE id=?", 1, oc).executeQuery();
            while (restarif.next()) {
                if (tipemember.equals("normal")) {
                    harga_pulsa = restarif.getDouble("harga");
                } else if (tipemember.equals("reseller")) {
                    harga_pulsa = restarif.getDouble("harga_reseller");
                }

            }

            Object[] ot = new Object[6];
            ot[0] = nohp;
            ot[1] = idtarif;
            ot[2] = "Belum Terkonfirmasi";
            ot[3] = tujuan;
            ot[4] = nohptujuan;
            ot[5] = harga_pulsa;
            dbh.insert("INSERT INTO data_transaksi(nohp,id_tarif,status,tujuan,nohptujuan,harga_pulsa) VALUES(?,?,?,?,?,?)", 6, ot);

            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StringBuilder info() {
        StringBuilder sb = new StringBuilder();
        try {
            dbh.connect();
            ResultSet res = dbh.read("SELECT id,nama,harga FROM data_tarif").executeQuery();
            int no = 1;
            NumberFormat nf = NumberFormat.getInstance();
            while (res.next()) {
                sb.append(no + ". " + res.getString("id") + " = " + res.getString("nama") + "(" + nf.format(res.getDouble("harga")) + ")\n");
                no++;
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb;
    }

    public int cektransaksi(String nohptujuan, String id_tarif) {
        int cek = 0;
        try {
            dbh.connect();
            Object[] o = new Object[4];
            o[0] = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            o[1] = nohptujuan;
            o[2] = "Belum Terkonfirmasi";
            o[3] = id_tarif;
            ResultSet res = dbh.readdetail("SELECT COUNT(id) AS total FROM data_transaksi WHERE "
                    + "tanggal::date=?::date AND nohptujuan=? AND status=? AND id_tarif=?", 4, o).executeQuery();
            while (res.next()) {
                cek = res.getInt("total");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cek;
    }

    public String mintatagihan(String nohp) {
        String tagihan = "";
        NumberFormat nf = NumberFormat.getInstance();
        try {
            dbh.connect();
            Object[] o = new Object[1];
            o[0] = nohp;
            ResultSet res = dbh.readdetail("SELECT SUM(CASE WHEN tipe_member='normal' THEN harga ELSE harga_reseller END) AS total "
                    + "FROM data_transaksi LEFT JOIN data_tarif ON data_transaksi.id_tarif=data_tarif.id "
                    + "LEFT JOIN data_member ON data_transaksi.nohp = data_member.nohp WHERE data_transaksi.nohp=?", 1, o).executeQuery();
            while (res.next()) {
                tagihan = nf.format(res.getDouble("total"));
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tagihan;

    }

    public String ceknohpgetkategori(String nohp) {
        String kategori = "";
        try {
            dbh.connect();
            Object[] otelid = new Object[1];
            otelid[0] = nohp;
            ResultSet resnohp = dbh.readdetail("SELECT kategori FROM data_member WHERE status = 'enable' AND nohp=?", 1, otelid).executeQuery();
            while (resnohp.next()) {
                kategori = resnohp.getString("kategori");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return kategori;
    }

    public Double ceknohpgetdeposit(String nohp) {
        Double deposit = 0.0;
        try {
            dbh.connect();
            Object[] otelid = new Object[1];
            otelid[0] = nohp;
            ResultSet resnohp = dbh.readdetail("SELECT deposit FROM data_member WHERE status = 'enable' AND nohp=?", 1, otelid).executeQuery();
            while (resnohp.next()) {
                deposit = resnohp.getDouble("deposit");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return deposit;
    }

    public Double cektagihan(String nohp) {
        Double tagihan = 0.0;
        try {
            dbh.connect();
            Object[] o = new Object[1];
            o[0] = nohp;
            /*ResultSet res = dbh.readdetail("SELECT SUM(CASE WHEN tipe_member='normal' THEN harga ELSE harga_reseller END) AS total "
                    + "FROM data_transaksi LEFT JOIN data_tarif ON data_transaksi.id_tarif=data_tarif.id "
                    + "LEFT JOIN data_member ON data_transaksi.nohp = data_member.nohp "
                    + "WHERE data_transaksi.nohp=? AND data_transaksi.status='Terkonfirmasi'", 1, o).executeQuery();*/
            ResultSet res = dbh.readdetail("SELECT SUM(harga_pulsa) AS total FROM data_transaksi"
                    + " WHERE data_transaksi.nohp=? AND data_transaksi.status='Terkonfirmasi'", 1, o).executeQuery();
            while (res.next()) {
                tagihan = res.getDouble("total");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tagihan;

    }

    public Double cektarifgetharga(String id_tarif) {
        Double harga = 0.0;
        try {
            dbh.connect();
            Object[] okode = new Object[1];
            okode[0] = id_tarif;
            ResultSet restarif = dbh.readdetail("SELECT harga FROM data_tarif WHERE kondisi='enable' AND id=?", 1, okode).executeQuery();
            while (restarif.next()) {
                harga = restarif.getDouble("harga");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Modemhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return harga;
    }

    public String getnohpdaripesan(String pesan) {
        String[] pecah1 = pesan.split("\\.");
        String[] pecah2 = pecah1[1].split(" ");
        String nohp = pecah2[0];
        return nohp;
    }

}
