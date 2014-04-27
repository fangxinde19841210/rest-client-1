package org.wiztools.restclient.ui.reqssl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.wiztools.restclient.bean.SSLKeyStore;
import org.wiztools.restclient.ui.RCFileView;
import org.wiztools.restclient.ui.UIUtil;

/**
 *
 * @author subwiz
 */
public class KeyStorePanel extends JPanel implements KeyStoreListener {
    
    @Inject private KeyStoreDialog jd;
    
    private final JLabel jl = new JLabel();
    private final JTextField jtf = new JTextField(23);
    private final JButton jb_addEdit = new JButton(UIUtil.getIconFromClasspath(RCFileView.iconBasePath + "pencil_add.png"));
    private final JButton jb_clear = new JButton(UIUtil.getIconFromClasspath(RCFileView.iconBasePath + "delete.png"));
    
    private SSLKeyStore keyStore = null;
    
    private final String tmpl = "type={0}; fileName={1}; password={2}";
    
    @PostConstruct
    public void init() {
        jtf.setEditable(false);
        
        jd.addKeyStoreListener(this);
        
        jb_addEdit.setToolTipText("Add / Edit");
        jb_clear.setToolTipText("Clear");
        
        jb_addEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jd.setVisible(true);
            }
        });
        
        jb_clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jd.clear();
                jtf.setText("");
            }
        });
        
        JPanel jp_jtf = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        jp_jtf.add(jtf);
        
        setLayout(new BorderLayout());
        
        add(jl, BorderLayout.WEST);
        add(jp_jtf, BorderLayout.CENTER);
        add(UIUtil.getFlowLayoutLeftAlignedMulti(jb_addEdit, jb_clear), BorderLayout.EAST);
    }
    
    public void setLabel(String label) {
        jl.setText(label);
    }
    
    public void setTitle(String title) {
        jd.setTitle(title);
    }

    @Override
    public void onOk(SSLKeyStore store) {
        if(store != null) {
            String pwdAvailable = (store.getPassword()!=null && store.getPassword().length > 0)? "Yes": "No";
            String txt = MessageFormat.format(tmpl,
                    store.getType(), store.getFile().getName(), pwdAvailable);
            jtf.setText(txt);
            jtf.setCaretPosition(0);
            
            keyStore = store;
        }
    }
    
    @Override
    public void onCancel() {
        jd.setKeyStore(keyStore);
    }
    
    public SSLKeyStore getKeyStore() {
        return keyStore;
    }
    
    public void setKeyStore(SSLKeyStore store) {
        jd.setKeyStore(store);
        onOk(store);
    }
    
    public void clear() {
        jd.clear();
        keyStore = null;
    }
}
