/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingserverpulsa;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import helper.Modemhelper;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.BeautyEyeLookAndFeelCross;
import org.jb2011.lnf.beautyeye.BeautyEyeLookAndFeelWin;
import view.Mainform;

/**
 *
 * @author Mulyadi
 */
public class Swingserverpulsa {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            BeautyEyeLNFHelper.frameBorderStyle=BeautyEyeLNFHelper.frameBorderStyle.osLookAndFeelDecorated;
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Swingserverpulsa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Swingserverpulsa.class.getName()).log(Level.SEVERE, null, ex);
        }
        Mainform mf=new Mainform();
        mf.setTitle("Server Pulsa");
        mf.setLocationRelativeTo(null);
        mf.setVisible(true);
    }

}
