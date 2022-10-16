package cn.chenyunlong.natcross;

import person.pluto.natcross2.CommonConstants;
import person.pluto.natcross2.clientside.ClientControlThread;
import person.pluto.natcross2.clientside.config.SecretInteractiveClientConfig;

import javax.swing.*;

/**
 * @author 陈云龙
 * @date 2020/11/20
 **/
public class Main {
    private JPanel mainPanel;
    private JButton button1;
    private JTextArea thisMessagesToShowTextArea;
    private JScrollPane schoolPanel;
    private JRadioButton httpRadioButton;
    private JRadioButton httpsRadioButton;
    private JPanel typeTadioGroup;
    private JTextField port;
    private JTextField controlCenter;
    private JTextField localhostTextField;
    private JLabel destIp;
    private JTextField destPort;
    private JButton start;
    private JPanel imagePanel;
    private final JScrollBar verticalScrollBar;
    private ClientControlThread controlThread;

    public Main() {
        verticalScrollBar = schoolPanel.getVerticalScrollBar();
        verticalScrollBar.setVisible(true);

        SecretInteractiveClientConfig config = new SecretInteractiveClientConfig();

        button1.addActionListener(e -> {
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());

            String centerText = controlCenter.getText();
            String listenPort = port.getText();
            String destIp = this.destIp.getText();
            String destPortStr = this.destPort.getText();

            // 设置服务端IP和端口
            config.setClientServiceIp("localhost");
            config.setClientServicePort(10110);
            // 设置对外暴露的端口，该端口的启动在服务端，这里只是表明要跟服务端的那个监听服务对接
            config.setListenServerPort(Integer.parseInt(listenPort));
            // 设置要暴露的目标IP和端口
            config.setDestIp(destIp);
            config.setDestPort(Integer.parseInt(destPortStr));

            // 设置交互密钥和签名key
            config.setBaseAesKey(CommonConstants.AES_KEY);
            config.setTokenKey(CommonConstants.TOKEN_KEY);
            controlThread = new ClientControlThread(config);
        });

        start.addActionListener((e) -> {
            try {
                boolean control = controlThread.createControl();
                if (control) {
                    thisMessagesToShowTextArea.append("创建隧道成功\n");
                } else {
                    thisMessagesToShowTextArea.append("创建隧道失败\n");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Window Form");
        frame.setBounds(100, 100, 640, 380);
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
