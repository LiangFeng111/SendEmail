/**
 * Created by 86157 on 2022/10/8.
 */



import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.util.List;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;


/**
 * @author 86157
 * @version 1.0
 * @description: TODO
 * @date 2022/10/8 22:31
 */
public class SendTest {
    public void sendEmail(String name,String body,String to) throws Exception {
        String userName = "2629337569@qq.com"; // 发件人QQ邮箱
        String password ="zqhoajcwccafdiaf";//激活POP3/SMTP服务的授权码
        String smtpHost = "smtp.qq.com"; // 邮件服务器，这个不用改
        String fromName="阿里";//发件人名称
        // 收件人的邮箱账号，多个收件人以半角逗号分隔
        String cc = ""; //抄送，多个抄送以半角逗号分隔
        String subject = "阿里|早上好"; // 主题
        // 附件的路径是文件在你电脑上的绝对路径,多个附件以逗号分开
        List<String> attachments = Arrays.asList("");
        Email email = Email.entity(smtpHost, userName, password, to, cc, subject, body, attachments,fromName);
        email.send(); // 发送！
    }

    public static void main(String[] args) throws Exception {
        //邮件发送组件
        SendTest emailTest=new SendTest();
        //使用图片验证码生成的随机文本作为验证邮箱的文本，拼合字符发送邮件
        String ddUserNickName="abc";//这个是对方名称
        String boby="阿里，早上好，美好的一天开始了！";//正文内容
        String ddUserEmail="1637465932@qq.com";//对方邮箱
        //发送邮件
        emailTest.sendEmail(ddUserNickName,boby,ddUserEmail);
    }

    public static class Email{
        private static String defaultSenderName = "";// 默认的发件人用户名，defaultEntity用得到
        private static String defaultSenderPass = "";// 默认的发件人密码，defaultEntity用得到
        private static String defaultSmtpHost = "";// 默认的邮件服务器地址，defaultEntity用得到
        private String smtpHost; // 邮件服务器地址
        private String sendUserName; // 发件人的用户名
        private String sendUserPass; // 发件人密码
        private MimeMessage mimeMsg; // 邮件对象
        private Session session;
        private Properties props;
        private Multipart mp;// 附件添加的组件
        private List<FileDataSource> files = new LinkedList<FileDataSource>();// 存放附件文件

        private void init() {
            if (props == null) {
                props = System.getProperties();
            }
            props.put("mail.transport.protocol", "smtp");// 连接协议
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", "465");// 端口号
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true"); // 需要身份验证
            props.put("mail.smtp.ssl.enable", "true");//设置是否使用ssl安全连接  ---一般都使用
            props.put("mail.debug", "true");//设置是否显示debug信息  true 会在控制台显
            session = Session.getDefaultInstance(props, null);
            // 置true可以在控制台（console)上看到发送邮件的过程
            session.setDebug(true);
            // 用session对象来创建并初始化邮件对象
            mimeMsg = new MimeMessage(session);
            // 生成附件组件的实例
            mp = new MimeMultipart();
        }

        private Email(String smtpHost, String sendUserName, String sendUserPass, String to, String cc, String mailSubject, String mailBody,
                      List<String> attachments, String formName) {
            this.smtpHost = smtpHost;
            this.sendUserName = sendUserName;
            this.sendUserPass = sendUserPass;
            init();
            setFrom(sendUserName, formName);  //设置发送人邮箱账号和发送人名
            setTo(to);  //设置收件人邮箱
            setCC(cc);  //设置抄送
            setBody(mailBody);  //设置内容
            setSubject(mailSubject);  //设置标题
            if (attachments != null) {  //循环添加附件
                for (String attachment : attachments) {
                    addFileAffix(attachment);
                }
            }
        }

        /**
         * 邮件实体
         *
         * @param smtpHost     邮件服务器地址
         * @param sendUserName 发件邮件地址
         * @param sendUserPass 发件邮箱密码
         * @param to           收件人，多个邮箱地址以半角逗号分隔
         * @param cc           抄送，多个邮箱地址以半角逗号分隔
         * @param mailSubject  邮件主题
         * @param mailBody     邮件正文
         * @return
         * @paramattachmentPath 附件路径
         */

        public static Email entity(String smtpHost, String sendUserName, String sendUserPass, String to, String cc, String mailSubject, String mailBody,
                                      List<String> attachments, String fromName) {

            return new Email(smtpHost, sendUserName, sendUserPass, to, cc, mailSubject, mailBody, attachments, fromName);

        }

        /**
         * 默认邮件实体，用了默认的发送帐号和邮件服务器
         *
         * @param to      收件人，多个邮箱地址以半角逗号分隔
         * @param cc      抄送，多个邮箱地址以半角逗号分隔
         * @param subject 邮件主题
         * @param body    邮件正文
         * @return
         */

        public static Email defaultEntity(String to, String cc, String subject, String body, List<String> attachments, String fromName) {
            return new Email(defaultSmtpHost, defaultSenderName, defaultSenderPass, to, cc, subject, body, attachments, fromName);
        }

        /**
         * 设置邮件主题
         *
         * @param mailSubject
         * @return
         */
        private boolean setSubject(String mailSubject) {
            try {
                mimeMsg.setSubject(mailSubject);
            } catch (Exception e) {
                return false;
            }
            return true;
        }


        /**
         * 设置邮件内容,并设置其为文本格式或HTML文件格式，编码方式为UTF-8
         *
         * @param mailBody
         * @return
         */

        private boolean setBody(String mailBody) {
            try {
                BodyPart bp = new MimeBodyPart();
                bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + mailBody, "text/html;charset=UTF-8");
                // 在组件上添加邮件文本
                mp.addBodyPart(bp);
            } catch (Exception e) {
                System.err.println("设置邮件正文时发生错误！" + e);
                return false;
            }
            return true;
        }

        /**
         * 添加一个附件
         *
         * @param filename 邮件附件的地址，只能是本机地址而不能是网络地址，否则抛出异常
         * @return
         */

        public boolean addFileAffix(String filename) {
            try {
                if (filename != null && filename.length() > 0) {
                    BodyPart bp = new MimeBodyPart();
                    FileDataSource fileds = new FileDataSource(filename);
                    bp.setDataHandler(new DataHandler(fileds));
                    bp.setFileName(MimeUtility.encodeText(fileds.getName(), "utf-8", null)); // 解决附件名称乱码
                    mp.addBodyPart(bp);// 添加附件
                    files.add(fileds);
                }
            } catch (Exception e) {
                System.err.println("增加邮件附件：" + filename + "发生错误！" + e);
                return false;
            }
            return true;
        }

        /**
         * 删除所有附件
         *
         * @return
         */

        public boolean delFileAffix() {
            try {
                FileDataSource fileds = null;
                for (Iterator<FileDataSource> it = files.iterator(); it.hasNext(); ) {
                    fileds = it.next();
                    if (fileds != null && fileds.getFile() != null) {
                        fileds.getFile().delete();
                    }
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }


        /**
         * 设置发件人地址
         *
         * @param from 发件人地址
         * @return
         */

        private boolean setFrom(String from, String name) {
            try {
                mimeMsg.setFrom(new InternetAddress(from, name));
            } catch (Exception e) {
                return false;
            }
            return true;
        }


        /**
         * 设置收件人地址
         *
         * @return
         * @paramto收件人的地址
         */

        private boolean setTo(String to) {
            if (to == null)
                return false;
            try {
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        /**
         * 设置抄送
         *
         * @param cc
         * @return
         */

        private boolean setCC(String cc) {
            if (cc == null) {
                return false;
            }
            try {
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        /**
         * 发送邮件
         *
         * @return
         */
        public boolean send() throws Exception {
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            System.out.println("正在发送邮件....");
            Transport transport = session.getTransport("smtp");
            // 连接邮件服务器并进行身份验证
            transport.connect(smtpHost, sendUserName, sendUserPass);
            // 发送邮件
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
            System.out.println("发送邮件成功！");
            transport.close();
            return true;
        }
    }
}


