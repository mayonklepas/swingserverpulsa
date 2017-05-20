/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author minami
 */
public class Filehelper {

    File dbsetting = new File("dbsetting");
    File modemsetting = new File("modemsetting");
    File modemsetting2 = new File("modemsetting2");
    File info = new File("info");
    File settingtambahan = new File("settingtambahan");
    File error = new File("error.txt");
    File utiliti = new File("util");

    public Filehelper() {
        if (!dbsetting.exists()) {
            try {
                dbsetting.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!modemsetting.exists()) {
            try {
                modemsetting.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!modemsetting2.exists()) {
            try {
                modemsetting2.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);

            }
        }


        if (!info.exists()) {
            try {
                info.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!settingtambahan.exists()) {
            try {
                settingtambahan.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!utiliti.exists()) {
            try {
                utiliti.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        }

    }
    }

    //database
    public String host() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(dbsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 1) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

        return dataline;
    }

    public String port() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(dbsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 2) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

        return dataline;
    }

    public String username() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(dbsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 3) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String password() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(dbsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 4) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String database() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(dbsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 5) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

        return dataline;
    }

    //modem1
    public String modem() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 1) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String commport() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 2) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);

        }

        return dataline;
    }

    public String bautrate() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 3) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String vendor() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 4) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

        return dataline;
    }

    public String tipe() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 5) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String smscenter() {

        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 6) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String simpin() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 7) {
                    dataline = data;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

        return dataline;
    }

    public String status() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 8) {
                    dataline = data;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    //modem2
    public String modem2() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting2));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 1) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String commport2() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting2));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 2) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String bautrate2() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting2));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 3) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String vendor2() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting2));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 4) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String tipe2() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting2));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 5) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String smscenter2() {

        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting2));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 6) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String simpin2() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting2));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 7) {
                    dataline = data;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String status2() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(modemsetting2));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 8) {
                    dataline = data;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

   

    //info
    public String namaperusahaan() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(info));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 1) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String nohp() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(info));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 2) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

        return dataline;
    }

    public String email() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(info));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 3) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

        return dataline;
    }

    public String alamat() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(info));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 4) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String keterangan() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(info));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 5) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    //utiliti
    public String botname() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(utiliti));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 1) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String bottoken() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(utiliti));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 2) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String hostpin() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(utiliti));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 3) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

        return dataline;
    }

    public String hostpusatpesan() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(utiliti));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 4) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String hostkata1() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(utiliti));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 5) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String hostkata2() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(utiliti));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 6) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

        return dataline;
    }

    public String hostkata3() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(utiliti));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 7) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }

    public String hostkata4() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(utiliti));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 8) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }
    public String hostkata5() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(utiliti));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 9) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }
    public String systemservice() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(utiliti));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 10) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }
    public String hostpusatpesan2() {
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(utiliti));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 11) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }
    
    public String pesannonaktif(){
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(settingtambahan));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 1) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }
    public String pesansukses(){
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(settingtambahan));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 2) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }
    public String pesangagal(){
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(settingtambahan));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 3) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }
    public String pesanmenunggu(){
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(settingtambahan));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 4) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }

        return dataline;
    }
    public String pesansalah(){
        String data = "";
        int line = 0;
        String dataline = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(settingtambahan));

            while ((data = br.readLine()) != null) {
                line++;
                if (line == 5) {
                    dataline = data;
                }
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

        return dataline;
    }

    //simpan
    public void simpanpengaturandb(String host, String port, String user, String password, String db) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(dbsetting));
            bw.write(host);
            bw.newLine();
            bw.write(port);
            bw.newLine();
            bw.write(user);
            bw.newLine();
            bw.write(password);
            bw.newLine();
            bw.write(db);
            bw.newLine();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }
    }

    public void simpanpengaturanmodem(String modem, String comport, String bautrate, String vendor, String tipe,
            String smscenter, String simpin, String status) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(modemsetting));
            bw.write(modem);
            bw.newLine();
            bw.write(comport);
            bw.newLine();
            bw.write(bautrate);
            bw.newLine();
            bw.write(vendor);
            bw.newLine();
            bw.write(tipe);
            bw.newLine();
            bw.write(smscenter);
            bw.newLine();
            bw.write(simpin);
            bw.newLine();
            bw.write(status);
            bw.newLine();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    public void simpanpengaturanmodem2(String modem, String comport, String bautrate, String vendor, String tipe,
            String smscenter, String simpin, String status) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(modemsetting2));
            bw.write(modem);
            bw.newLine();
            bw.write(comport);
            bw.newLine();
            bw.write(bautrate);
            bw.newLine();
            bw.write(vendor);
            bw.newLine();
            bw.write(tipe);
            bw.newLine();
            bw.write(smscenter);
            bw.newLine();
            bw.write(simpin);
            bw.newLine();
            bw.write(status);
            bw.newLine();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }
    }


    public void simpaninfo(String namaperusahaan, String nohp, String email, String alamat, String keterangan) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(info));
            bw.write(namaperusahaan);
            bw.newLine();
            bw.write(nohp);
            bw.newLine();
            bw.write(email);
            bw.newLine();
            bw.write(alamat);
            bw.newLine();
            bw.write(keterangan);
            bw.newLine();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }
    }

    public void simpanutil(String botname, String bottoken, String hostpin, String hostpusatpesan,
            String hostkata1, String hostkata2, String hostkata3, String hostkata4,
            String hostkata5,String systemservice,String hostpusatpesan2) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(utiliti));
            bw.write(botname);
            bw.newLine();
            bw.write(bottoken);
            bw.newLine();
            bw.write(hostpin);
            bw.newLine();
            bw.write(hostpusatpesan);
            bw.newLine();
            bw.write(hostkata1);
            bw.newLine();
            bw.write(hostkata2);
            bw.newLine();
            bw.write(hostkata3);
            bw.newLine();
            bw.write(hostkata4);
            bw.newLine();
            bw.write(hostkata5);
            bw.newLine();
            bw.write(systemservice);
            bw.newLine();
            bw.write(hostpusatpesan2);
            bw.newLine();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    public void logs(String settingtambahan) {
        try {
            BufferedWriter bwlog = new BufferedWriter(new FileWriter(settingtambahan));
            bwlog.write(settingtambahan);
            bwlog.close();
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

    }

    public void errs(String err) {
        try {
            BufferedWriter bwerr = new BufferedWriter(new FileWriter(error));
            bwerr.write(err);
            //bwlog.close();
        } catch (IOException ex) {
            Logger.getLogger(Filehelper.class.getName()).log(Level.SEVERE, null, ex);
           
        }

    }

}
