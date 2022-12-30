/**
 * Created by 86157 on 2022/10/8.
 */


import aa.Email;

import java.util.*;
import java.util.List;


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

}


