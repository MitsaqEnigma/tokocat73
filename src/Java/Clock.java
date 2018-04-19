package Java;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Clock implements ActionListener {

    private JLabel jLabel;
    private SimpleDateFormat sdf;
    
    public Clock(JLabel jLabel, int type) {
        this.jLabel = jLabel;
        sdf = new SimpleDateFormat(type == 1 ? "hh:mm:ss a" : "E, d MMMM yyyy");
        Timer t = new Timer(1000, this);
        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        jLabel.setText(sdf.format(new Date()));
    }

}
