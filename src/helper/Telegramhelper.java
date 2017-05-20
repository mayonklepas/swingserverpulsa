/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.smslib.GatewayException;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 *
 * @author Minami
 */
public class Telegramhelper extends TelegramLongPollingBot {

    Dbhelper dbh = new Dbhelper();
    Modemhelper mh = new Modemhelper();
    Filehelper fh = new Filehelper();

    @Override
    public String getBotToken() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //return "229173580:AAEL0wXUn7_HbLt57uCM5h6_V3YHCfechvs";
        //return "234678772:AAER4wSFBbxGwyO0xa7s7VvYAd4nJ5gWY-A";
        return fh.bottoken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                //create a object that contains the information to send back the message

                Date date = new Date(message.getDate().longValue() * 1000);
                String tanggal = new SimpleDateFormat("dd-MM-yyyy H:m:s").format(date);
                String[] kodepesan = message.getText().toLowerCase().split("\\.");

                if (fh.systemservice().equals("nonaktif")) {

                    SendMessage sendMessageRequest = new SendMessage();
                    sendMessageRequest.setChatId(message.getChatId().toString());
                    sendMessageRequest.setText(fh.pesannonaktif());
                    try {
                        sendMessage(sendMessageRequest);
                    } catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }

                } else if (fh.systemservice().equals("standing by")) {
                    SendMessage sendMessageRequest = new SendMessage();
                    sendMessageRequest.setChatId(message.getChatId().toString());
                    sendMessageRequest.setText("Sedang menyiapkan server, tunggulah beberapa saat lagi");
                    try {
                        sendMessage(sendMessageRequest);
                    } catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                } else if (kodepesan.length == 1) {

                    if (message.getText().equals("/start")) {
                        SendMessage sendMessageRequest = new SendMessage();
                        sendMessageRequest.setChatId(message.getChatId().toString());
                        sendMessageRequest.setText("Telegram ID anda : " + message.getChatId().toString());
                        try {
                            sendMessage(sendMessageRequest);
                        } catch (TelegramApiException ex) {
                            ex.printStackTrace();
                        }
                    } else {

                        String nohpdaritelid = cektelidgetnohp(message.getChatId().toString());
                        String kodetarif = cektarif(message.getText().toLowerCase());
                        if (nohpdaritelid.equals("") || nohpdaritelid == null) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString());
                            sendMessageRequest.setText("Maaf ID belum terdaftar. \nhubungi " + fh.nohp() + " untuk mendaftar");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }
                        } else if (kodetarif.equals("") || kodetarif == null) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString());
                            sendMessageRequest.setText("Maaf layanan tidak tersedia. \ngunakan minta.info untuk melihat info");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }
                        } else if (cektransaksi(nohpdaritelid, message.getText().toLowerCase()) >= 1) {

                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString());
                            sendMessageRequest.setText("Transaksi sudah dilakukan, harap menunggu jika pulsa belum terisi");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }

                            String kode_eksekusi = kodetarif + "." + nohpdaritelid + "." + fh.hostpin();
                            OutboundMessage om = new OutboundMessage(fh.hostpusatpesan2(), kode_eksekusi);
                            try {
                                Service.getInstance().sendMessage(om);
                            } catch (TimeoutException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (GatewayException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            /*Media sound = new Media(getClass().getResource("/media/StartUp.mp3").toString());
                            MediaPlayer mp = new MediaPlayer(sound);
                            mp.play();*/

                        } else if (cektelidgetkategori(message.getChatId().toString()).equals("Post Paid")) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText("Permintaan sedang diproses, mohon tunggu");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }
                            insertpesan(tanggal, nohpdaritelid, message.getText().toLowerCase());
                            inserttransaksi(nohpdaritelid, kodepesan[0], "sendiri", nohpdaritelid);
                            String kode_eksekusi = kodetarif + "." + nohpdaritelid + "." + fh.hostpin();
                            OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                            try {
                                Service.getInstance().sendMessage(om);
                            } catch (TimeoutException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (GatewayException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                            MediaPlayer mp = new MediaPlayer(sound);
                            mp.play();*/
                        } else {
                            Double deposit = cektelidgetdeposit(message.getChatId().toString());
                            Double tagihan = cektagihan(nohpdaritelid);
                            Double totalsaldo = deposit - tagihan;
                            Double harga = cektarifgetharga(message.getText().toLowerCase());
                            if (totalsaldo >= harga) {
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText("Permintaan sedang diproses, mohon tunggu");
                                try {
                                    sendMessage(sendMessageRequest);
                                } catch (TelegramApiException ex) {
                                    ex.printStackTrace();
                                }
                                insertpesan(tanggal, nohpdaritelid, message.getText().toLowerCase());
                                inserttransaksi(nohpdaritelid, kodepesan[0], "sendiri", nohpdaritelid);
                                String kode_eksekusi = kodetarif + "." + nohpdaritelid + "." + fh.hostpin();
                                OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                                try {
                                    Service.getInstance().sendMessage(om);
                                } catch (TimeoutException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (GatewayException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                                MediaPlayer mp = new MediaPlayer(sound);
                                mp.play();*/
                            } else {
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText("Maaf Saldo Tidak Mencukupi");
                                try {
                                    sendMessage(sendMessageRequest);
                                } catch (TelegramApiException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                    }

                    //dua kata
                } else if (kodepesan.length == 2) {
                    if (kodepesan[0].equals("minta")) {
                        if (kodepesan[1].equals("info")) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText(
                                    "Kode Layanan \n" + info().toString() + "\n\n"
                                    + "Cara dan format pengiriman\n"
                                    + "SMS\n"
                                    + "Ke nomor sendiri :ketik kode (contoh : x5)\n\n"
                                    + "Ke nomor lain:ketik kode.nomor_tujuan (contoh : x5.08123456789)\n\n"
                                    + "Dari nomor lain ke nomor sendiri:ketik darilain.kode.nomor_sendiri (contoh : darilain.x5.083129345215)\n\n"
                                    + "TELEGRAM\n"
                                    + "Ke nomor sendiri :ketik kode (contoh : x5)\n\n"
                                    + "Ke nomor lain :ketik kode.nomor (contoh : x5.083129345215)\n\n"
                                    + "Dari nomor lain ke nomor sendiri :ketik darilain.kode.nomor_tujuan (contoh : darilain.x5.08123456789)\n\n"
                                    + "KHUSUS Token Listrik melalui SMS atau Telegram\n\n"
                                    + "ketik pln.kode_layanan.nomor_meteran (contoh : pln.l20.12345678910)\n\n"
                                    + "PUSAT PESAN\n"
                                    + "SMS : 081907123456\n"
                                    + "Telegram : @meteorpulsa_bot\n\n\n"
                                    + "Untuk Info lebih lanjut silahkan hubungi kami di : " + fh.nohp() + " \n\n"
                                    + "--Terima kasih-- \n\n"
                                    + fh.namaperusahaan() + " \n\n"
                            );
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }
                        } else if (kodepesan[1].equals("tagihan")) {
                            String nohp = cektelidgetnohp(message.getChatId().toString());

                            if (nohp.equals("") || nohp == null) {
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText("Maaf ID belum terdaftar. \nhubungi " + fh.nohp() + " untuk mendaftar");
                                try {
                                    sendMessage(sendMessageRequest);
                                } catch (TelegramApiException ex) {
                                    ex.printStackTrace();
                                }
                            } else {
                                String totaltagihan = mintatagihan(nohp);
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText("Total Tagihan Anda = " + totaltagihan + "\n"
                                        + "Rincian :\n" + gettagihan(nohp).toString());
                                try {
                                    sendMessage(sendMessageRequest);
                                } catch (TelegramApiException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        } else if (kodepesan[1].equals("saldo")) {
                            String nohp = cektelidgetnohp(message.getChatId().toString());

                            if (nohp.equals("") || nohp == null) {
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText("Maaf ID belum terdaftar. \nhubungi " + fh.nohp() + " untuk mendaftar");
                                try {
                                    sendMessage(sendMessageRequest);
                                } catch (TelegramApiException ex) {
                                    ex.printStackTrace();
                                }
                            } else {

                                NumberFormat nf = NumberFormat.getInstance();
                                Double deposit = cektelidgetdeposit(message.getChatId().toString());
                                Double tagihan = cektagihan(nohp);
                                Double saldo = deposit - tagihan;
                                String totalsaldo = nf.format(saldo);
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText("Total Saldo Anda = " + totalsaldo);
                                try {
                                    sendMessage(sendMessageRequest);
                                } catch (TelegramApiException ex) {
                                    ex.printStackTrace();
                                }
                            }

                        } else {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText("Maaf format salah. \ngunakan minta.info untuk melihat info");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }

                        }
                    } else if (kodepesan[0].equals("555")) {

                        if (kodepesan[1].equals("pesan")) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText(pesansekarang().toString());
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }
                        } else if (kodepesan[1].equals("tagihan")) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText(semuatagihan().toString());
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }
                        } else {

                            for (int i = 0; i < getalltellid().size(); i++) {
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(String.valueOf(getalltellid().get(i))); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText(kodepesan[1]);
                                try {
                                    sendMessage(sendMessageRequest);
                                } catch (TelegramApiException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                    } else {
                        String kodetarif = cektarif(kodepesan[0]);
                        String nohpdaritelid = cektelidgetnohp(message.getChatId().toString());

                        if (nohpdaritelid.equals("") || nohpdaritelid == null) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText("Maaf nomor belum terdaftar. \nhubungi " + fh.nohp() + " untuk mendaftar");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }
                        } else if (kodetarif.equals("") || kodetarif == null) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText("Maaf layanan tidak tersedia. \ngunakan minta.info untuk melihat info");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }
                        } else if (cektransaksi(kodepesan[1], kodepesan[0]) >= 1) {

                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText("Transaksi sudah dilakukan, harap menunggu jika pulsa belum terisi");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }

                            String kode_eksekusi = kodetarif + "." + kodepesan[1] + "." + fh.hostpin();
                            OutboundMessage om = new OutboundMessage(fh.hostpusatpesan2(), kode_eksekusi);
                            try {
                                Service.getInstance().sendMessage(om);
                            } catch (TimeoutException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (GatewayException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            /*Media sound = new Media(getClass().getResource("/media/StartUp.mp3").toString());
                            MediaPlayer mp = new MediaPlayer(sound);
                            mp.play();*/

                        } else if (cektelidgetkategori(message.getChatId().toString()).equals("Post Paid")) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText("Permintaan sedang diproses, mohon tunggu");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }
                            insertpesan(tanggal, nohpdaritelid, message.getText().toLowerCase());
                            inserttransaksi(nohpdaritelid, kodepesan[0], "lain", kodepesan[1]);
                            String kode_eksekusi = kodetarif + "." + kodepesan[1] + "." + fh.hostpin();
                            OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                            try {
                                Service.getInstance().sendMessage(om);
                            } catch (TimeoutException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (GatewayException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                            MediaPlayer mp = new MediaPlayer(sound);
                            mp.play();*/
                        } else {
                            Double deposit = cektelidgetdeposit(message.getChatId().toString());
                            Double tagihan = cektagihan(nohpdaritelid);
                            Double totalsaldo = deposit - tagihan;
                            Double harga = cektarifgetharga(message.getText().toLowerCase());
                            if (totalsaldo >= harga) {
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText("Permintaan sedang diproses, mohon tunggu");
                                try {
                                    sendMessage(sendMessageRequest);
                                } catch (TelegramApiException ex) {
                                    ex.printStackTrace();
                                }
                                insertpesan(tanggal, nohpdaritelid, message.getText().toLowerCase());
                                inserttransaksi(nohpdaritelid, kodepesan[0], "lain", kodepesan[1]);
                                String kode_eksekusi = kodetarif + "." + kodepesan[1] + "." + fh.hostpin();
                                OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                                try {
                                    Service.getInstance().sendMessage(om);
                                } catch (TimeoutException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (GatewayException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                                MediaPlayer mp = new MediaPlayer(sound);
                                mp.play();*/
                            } else {
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText("Maaf Saldo Tidak Mencukupi");
                                try {
                                    sendMessage(sendMessageRequest);
                                } catch (TelegramApiException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                    }

                    // tiga kata
                } else if (kodepesan.length == 3) {
                    if (kodepesan[0].equals("pln")) {
                        String kodetarif = cektarif(kodepesan[1]);
                        String nohpdaritelid = cektelidgetnohp(message.getChatId().toString());
                        if (kodetarif.equals("") || kodetarif == null) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText("Maaf layanan tidak tersedia. \ngunakan minta.info untuk melihat info");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }

                        } else if (nohpdaritelid.equals("") || nohpdaritelid == null) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText("Maaf ID belum terdaftar. \nhubungi " + fh.nohp() + " untuk mendaftar");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }

                        } else if (cektelidgetkategori(message.getChatId().toString()).equals("Post Paid")) {
                            SendMessage sendMessageRequest = new SendMessage();
                            sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                            sendMessageRequest.setText("Permintaan sedang diproses, mohon tunggu");
                            try {
                                sendMessage(sendMessageRequest);
                            } catch (TelegramApiException ex) {
                                ex.printStackTrace();
                            }
                            insertpesan(tanggal, nohpdaritelid, message.getText().toLowerCase());
                            inserttransaksi(nohpdaritelid, kodepesan[1], "sendiri", kodepesan[2]);
                            String kode_eksekusi = kodetarif + "." + kodepesan[2] + "#" + nohpdaritelid + "." + fh.hostpin();
                            OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                            try {
                                Service.getInstance().sendMessage(om);
                            } catch (TimeoutException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (GatewayException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                            MediaPlayer mp = new MediaPlayer(sound);
                            mp.play();*/
                        } else {
                            Double deposit = cektelidgetdeposit(message.getChatId().toString());
                            Double tagihan = cektagihan(nohpdaritelid);
                            Double totalsaldo = deposit - tagihan;
                            Double harga = cektarifgetharga(message.getText().toLowerCase());
                            if (totalsaldo >= harga) {
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText("Permintaan sedang diproses, mohon tunggu");
                                try {
                                    sendMessage(sendMessageRequest);
                                } catch (TelegramApiException ex) {
                                    ex.printStackTrace();
                                }
                                insertpesan(tanggal, nohpdaritelid, message.getText().toLowerCase());
                                inserttransaksi(nohpdaritelid, kodepesan[1], "sendiri", kodepesan[2]);
                                String kode_eksekusi = kodetarif + "." + kodepesan[2] + "." + nohpdaritelid + "." + fh.hostpin();
                                OutboundMessage om = new OutboundMessage(fh.hostpusatpesan(), kode_eksekusi);
                                try {
                                    Service.getInstance().sendMessage(om);
                                } catch (TimeoutException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (GatewayException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                                }

                                /*Media sound = new Media(getClass().getResource("/media/333-Ver2.mp3").toString());
                                MediaPlayer mp = new MediaPlayer(sound);
                                mp.play();*/
                            } else {
                                SendMessage sendMessageRequest = new SendMessage();
                                sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                                sendMessageRequest.setText("Maaf Saldo Tidak Mencukupi");
                                try {
                                    sendMessage(sendMessageRequest);
                                } catch (TelegramApiException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                    } else if (kodepesan[0].equals("555")) {
                        SendMessage sendMessageRequest = new SendMessage();
                        sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                        sendMessageRequest.setText("Pesan ke -> "+kodepesan[1]+" sedang dikirim");
                        try {
                            sendMessage(sendMessageRequest);
                        } catch (TelegramApiException ex) {
                            ex.printStackTrace();
                        }
                        OutboundMessage obmsg = new OutboundMessage(kodepesan[1], kodepesan[2] + "\n Balas ke 083129345215");
                        try {
                            Service.getInstance().sendMessage(obmsg);
                        } catch (TimeoutException ex) {
                            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (GatewayException ex) {
                            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        SendMessage sendMessageRequest = new SendMessage();
                        sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                        sendMessageRequest.setText("Maaf Format Salah. \ngunakan minta.info untuk melihat info");
                        try {
                            sendMessage(sendMessageRequest);
                        } catch (TelegramApiException ex) {
                            ex.printStackTrace();
                        }
                    }

                } else {
                    SendMessage sendMessageRequest = new SendMessage();
                    sendMessageRequest.setChatId(message.getChatId().toString()); //who should get the message? the sender from which we got the message...
                    sendMessageRequest.setText("Maaf Format Salah. \ngunakan minta.info untuk melihat info");
                    try {
                        sendMessage(sendMessageRequest);
                    } catch (TelegramApiException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        }
    }

    @Override
    public String getBotUsername() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //return "mulyadi_bot";
        //return "meteorpulsa_bot";
        return fh.botname();
    }

    public String cektarif(String tarif) {
        String kodetarif = "";
        try {
            dbh.connect();
            Object[] okode = new Object[1];
            okode[0] = tarif;
            ResultSet restarif = dbh.readdetail("SELECT kode FROM data_tarif WHERE kondisi='enable' AND id=?", 1, okode).executeQuery();
            while (restarif.next()) {
                kodetarif = restarif.getString("kode");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kodetarif;
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
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return notujuan;
    }

    public String cektelid(String telid) {
        String telegram_id = "";
        try {
            dbh.connect();
            Object[] otelid = new Object[1];
            otelid[0] = telid;
            ResultSet resnohp = dbh.readdetail("SELECT telegram_id FROM data_member WHERE telegram_id=?", 1, otelid).executeQuery();
            while (resnohp.next()) {
                telegram_id = resnohp.getString("telegram_id");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return telegram_id;
    }

    public String cektelidgetnohp(String telid) {
        String nohp = "";
        try {
            dbh.connect();
            Object[] otelid = new Object[1];
            otelid[0] = telid;
            ResultSet resnohp = dbh.readdetail("SELECT nohp FROM data_member WHERE status = 'enable' AND telegram_id=?", 1, otelid).executeQuery();
            while (resnohp.next()) {
                nohp = resnohp.getString("nohp");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nohp;
    }

    public String cektelidgetkategori(String telid) {
        String kategori = "";
        try {
            dbh.connect();
            Object[] otelid = new Object[1];
            otelid[0] = telid;
            ResultSet resnohp = dbh.readdetail("SELECT kategori FROM data_member WHERE status = 'enable' AND telegram_id=?", 1, otelid).executeQuery();
            while (resnohp.next()) {
                kategori = resnohp.getString("kategori");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return kategori;
    }

    public Double cektelidgetdeposit(String telid) {
        Double deposit = 0.0;
        try {
            dbh.connect();
            Object[] otelid = new Object[1];
            otelid[0] = telid;
            ResultSet resnohp = dbh.readdetail("SELECT deposit FROM data_member WHERE status = 'enable' AND telegram_id=?", 1, otelid).executeQuery();
            while (resnohp.next()) {
                deposit = resnohp.getDouble("deposit");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return deposit;
    }

    public Double cektagihan(String nohp) {
        Double tagihan = 0.0;
        NumberFormat nf = NumberFormat.getInstance();
        try {
            dbh.connect();
            Object[] o = new Object[1];
            o[0] = nohp;
            ResultSet res = dbh.readdetail("SELECT SUM(CASE WHEN tipe_member='normal' THEN harga ELSE harga_reseller END) AS total "
                    + "FROM data_transaksi LEFT JOIN data_tarif ON data_transaksi.id_tarif=data_tarif.id "
                    + "LEFT JOIN data_member ON data_transaksi.nohp = data_member.nohp "
                    + "WHERE data_transaksi.nohp=? AND data_transaksi.status='Terkonfirmasi'", 1, o).executeQuery();
            while (res.next()) {
                tagihan = res.getDouble("total");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return harga;
    }

    public void insertpesan(String tanggal, String nohp, String pesan) {
        try {
            dbh.connect();
            Object[] op = new Object[4];
            op[0] = tanggal;
            op[1] = nohp;
            op[2] = pesan;
            op[3] = "TELEGRAM";
            dbh.insert("INSERT INTO data_pesan(tanggal_masuk,nohp,pesan,kategori) VALUES(?,?,?,?)", 4, op);
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
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
            ResultSet res = dbh.read("SELECT id,nama,harga FROM data_tarif WHERE kondisi='enable' ORDER BY nama").executeQuery();
            int no = 1;
            NumberFormat nf = NumberFormat.getInstance();
            while (res.next()) {
                sb.append(no + ". " + res.getString("id") + " = " + res.getString("nama") + "(" + nf.format(res.getDouble("harga")) + ")\n");
                no++;
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb;
    }

    public StringBuilder gettagihan(String nohp) {
        StringBuilder sb = new StringBuilder();
        try {
            dbh.connect();
            Object[] o = new Object[1];
            o[0] = nohp;
            /*ResultSet res = dbh.readdetail("SELECT tanggal::date,id_tarif,CASE WHEN tipe_member='normal' "
                    + "THEN harga ELSE harga_reseller END AS harga "
                    + "FROM data_transaksi LEFT JOIN data_tarif ON data_transaksi.id_tarif=data_tarif.id "
                    + "LEFT JOIN data_member ON data_transaksi.nohp = data_member.nohp "
                    + "WHERE data_transaksi.nohp=? AND data_transaksi.status='Terkonfirmasi' ORDER BY tanggal", 1, o).executeQuery();*/
            ResultSet res = dbh.readdetail("SELECT tanggal::date,id_tarif,harga_pulsa "
                    + "FROM data_transaksi INNER JOIN data_member ON data_transaksi.nohp = data_member.nohp "
                    + "WHERE data_transaksi.nohp=? AND data_transaksi.status='Terkonfirmasi' ORDER BY tanggal", 1, o).executeQuery();
            int no = 1;
            NumberFormat nf = NumberFormat.getInstance();
            while (res.next()) {
                sb.append(no + ". " + res.getString("tanggal") + " | " + res.getString("id_tarif") + " | " + nf.format(res.getDouble("harga_pulsa")) + "\n");
                no++;
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb;
    }

    public List getalltellid() {
        List ls = new ArrayList();
        try {
            dbh.connect();
            ResultSet res = dbh.read("SELECT telegram_id FROM data_member WHERE telegram_id != 'null'").executeQuery();
            while (res.next()) {
                ls.add(res.getString("telegram_id"));
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }

    public int cektransaksi(String nohptujuan, String id_tarif) {
        int cek = 0;
        try {
            dbh.connect();
            Object[] o = new Object[3];
            o[0] = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            o[1] = nohptujuan;
            o[2] = id_tarif;
            ResultSet res = dbh.readdetail("SELECT COUNT(id) AS total FROM data_transaksi WHERE "
                    + "tanggal::date=?::date AND nohptujuan=? AND id_tarif=?", 3, o).executeQuery();
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
            /*ResultSet res = dbh.readdetail("SELECT SUM(CASE WHEN tipe_member='normal' THEN harga ELSE harga_reseller END) AS total "
                    + "FROM data_transaksi LEFT JOIN data_tarif ON data_transaksi.id_tarif=data_tarif.id "
                    + "LEFT JOIN data_member ON data_transaksi.nohp = data_member.nohp "
                    + "WHERE data_transaksi.nohp=? AND data_transaksi.status='Terkonfirmasi'", 1, o).executeQuery();*/
            ResultSet res = dbh.readdetail("SELECT SUM(harga_pulsa) AS total FROM data_transaksi"
                    + " WHERE data_transaksi.nohp=? AND data_transaksi.status='Terkonfirmasi'", 1, o).executeQuery();
            while (res.next()) {
                tagihan = nf.format(res.getDouble("total"));
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tagihan;

    }

    public StringBuilder pesansekarang() {
        StringBuilder pesan = new StringBuilder();
        try {
            dbh.connect();
            Object[] o = new Object[1];
            o[0] = java.sql.Date.valueOf(LocalDate.now());
            ResultSet res = dbh.readdetail("SELECT data_pesan.nohp,nama,pesan "
                    + "FROM data_pesan LEFT JOIN data_member "
                    + "ON data_pesan.nohp=data_member.nohp WHERE tanggal_simpan::date=? ORDER BY data_pesan.id DESC", 1, o).executeQuery();
            while (res.next()) {
                pesan.append(res.getString("nohp") + "|" + res.getString("nama") + ": \n" + res.getString("pesan") + "\n\n");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pesan;
    }

    public StringBuilder semuatagihan() {
        StringBuilder pesan = new StringBuilder();
        NumberFormat nf = NumberFormat.getInstance();
        try {
            dbh.connect();
            /*ResultSet res = dbh.read("SELECT data_member.nama,SUM(CASE WHEN tipe_member='normal' THEN harga ELSE harga_reseller END) AS total "
                    + "FROM data_transaksi LEFT JOIN data_tarif ON data_transaksi.id_tarif=data_tarif.id "
                    + "LEFT JOIN data_member ON data_transaksi.nohp = data_member.nohp "
                    + "WHERE data_transaksi.status='Terkonfirmasi' GROUP BY data_member.nama").executeQuery();*/
            ResultSet res = dbh.read("SELECT data_member.nama,SUM(harga_pulsa) AS total "
                    + "FROM data_transaksi LEFT JOIN data_tarif ON data_transaksi.id_tarif=data_tarif.id "
                    + "LEFT JOIN data_member ON data_transaksi.nohp = data_member.nohp "
                    + "WHERE data_transaksi.status='Terkonfirmasi' GROUP BY data_member.nama").executeQuery();
            while (res.next()) {
                pesan.append(res.getString("nama") + " : " + nf.format(res.getDouble("total")).toString() + "\n");
            }
            dbh.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(Telegramhelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pesan;
    }

}
