/*
 * Copyright (c) 2019-2022 YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.nat.tools;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * 远程执行linux的shell script
 *
 * @author Ickes
 * @since V0.1
 */
@Slf4j
public class RemoteExecuteCommand {
    // 字符编码默认是utf-8
    private static String DEFAULT_CHARSET = "UTF-8";
    private Connection conn;
    private String ip;
    private String userName;
    private String userPwd;

    public RemoteExecuteCommand(String ip, String userName, String userPwd) {
        this.ip = ip;
        this.userName = userName;
        this.userPwd = userPwd;
    }


    /**
     * 远程登录linux的主机
     *
     * @return 登录成功返回true，否则返回false
     * @author Ickes
     * @since V0.1
     */
    public Boolean login() {
        boolean flg = false;
        try {
            conn = new Connection(ip);
            //连接
            conn.connect();
            //认证
            flg = conn.authenticateWithPassword(userName, userPwd);
        } catch (IOException e) {
            log.error("登录远程机器失败了{}", e.getLocalizedMessage());
            e.printStackTrace();
        }
        return flg;
    }

    public static void main(String[] args) {
        RemoteExecuteCommand rec = new RemoteExecuteCommand("121.36.163.95", "root", "4745701816Long");
        //执行命令
//        System.out.println(rec.execute("chmod 777 /usr/ksybak/myshell/tomcat-fw.sh"));
        System.out.println(rec.execute("ls /var"));
        //System.out.println(rec.execute("/usr/ksybak/myshell/tomcat-fw.sh"));
        //执行脚本
        //rec.execute("sh /usr/local/tomcat/bin/statup.sh");
        //这个方法与上面最大的区别就是，上面的方法，不管执行成功与否都返回，
        //这个方法呢，如果命令或者脚本执行错误将返回空字符串
        //System.out.println(rec.executeSuccess("ifconfig"));
    }

    /**
     * 远程执行shell脚本或者命令
     *
     * @param cmd 即将执行的命令
     * @return 命令执行完后返回的结果值
     */
    public String execute(String cmd) {
        String result = "";
        try {
            if (login()) {
                //打开一个会话
                Session session = conn.openSession();
                //执行命令
                session.execCommand(cmd);
                result = processStdout(session.getStdout(), DEFAULT_CHARSET);
                //如果为得到标准输出为空，说明脚本执行出错了
                if (!StringUtils.hasLength(result)) {
                    result = processStdout(session.getStderr(), DEFAULT_CHARSET);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析脚本执行返回的结果集
     *
     * @param in      输入流对象
     * @param charset 编码
     * @return 以纯文本的格式返回
     * @author Ickes
     * @since V0.1
     */
    private String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuilder buffer = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.error("不支持的输出：{}\n", e.getLocalizedMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static void setCharset(String charset) {
        DEFAULT_CHARSET = charset;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    /**
     * 远程执行shell脚本或者命令
     *
     * @param cmd 即将执行的命令
     * @return 命令执行成功后返回的结果值，如果命令执行失败，返回空字符串，不是null
     */
    @SuppressWarnings("unused")
    public String executeSuccess(String cmd) {
        String result = "";
        try {
            if (login()) {
                //打开一个会话
                Session session = conn.openSession();
                //执行命令
                session.execCommand(cmd);
                result = processStdout(session.getStdout(), DEFAULT_CHARSET);
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
