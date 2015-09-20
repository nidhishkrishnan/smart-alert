package configurableNotificationSystem;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

    String t = null, t1 = null, d_host, d_port, d_email1, d_email, d_password1;
    static StringBuilder msg;

    public String sendMail(final String mailTo, final String mailFrom, final String password, final String mailSubject, final String mailMessage) {
    	System.out.println("**********************************************");
    	System.out.println("mailTO "+mailTo);
    	System.out.println("mailFrom "+mailFrom);
    	System.out.println("password "+password);
    	System.out.println("mailMessage "+mailMessage);
    	System.out.println("**********************************************");
    	String str="";
        StringTokenizer st = new StringTokenizer(mailFrom, "@");
        while (st.hasMoreTokens()) {
            t = st.nextToken();
        }
        StringTokenizer st1 = new StringTokenizer(t, ".");
        t1 = st1.nextToken();
        if (t1.equals("yahoo") || t1.equals("ymail")) {
            d_host = "smtp.mail.yahoo.com";
            d_port = "465";
        } else {
            d_host = "smtp.gmail.com";
            d_port = "465";
        }

        d_email1 = mailFrom;
        d_password1 = password;
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.user", mailFrom);
        props.put("mail.smtp.host", d_host);
        props.put("mail.smtp.port", d_port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", d_port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        try {
            SMTPAuthenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);
            msg.setContent(mailMessage.toString(), "text/html");
            msg.setSubject(mailSubject);
            msg.setFrom(new InternetAddress(mailFrom));
           msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
            Transport.send(msg);
        } catch (Exception mex) {
            mex.getMessage();
            mex.printStackTrace();
            str=""+mex;
            
        }

        return str;
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(d_email1, d_password1);
        }
    }
 
	public void sendCredentialsAsEmail(final String email, final String userName, final String password) {
		final String message = getEmailMessage(userName, password);
		new MailUtil().sendMail(email, "smartbanking.codechef@gmail.com", "try123456", "Congratulations! Your Smart Bank account has been activated.", message);
	}

	private String getEmailMessage(final String userName, final String password) {
		final StringBuilder stringBuilder = new StringBuilder();
	  	  stringBuilder.append("<div id=':1uc' class='ii gt m14d60c5befacf000 adP adO'>")
	  	  .append("<div id=':20h' class='a3s' style='overflow: hidden;'>")
	  	  .append("<p><b>Welcome to Smart Bank! </b></p>")
	  	  .append("<p>You can login to your account at <a href='http://smartbanking-codechef.rhcloud.com/Smart' target='_blank'><span class='il'>Smart Bank</span></a> as the Account Holder using the below credentials.</p>")
	  	  .append("<ul>")
	  	  .append("<li><b>Username:</b>"+userName)
	  	  .append("</li>")
	  	  .append("<li><b>Password:</b>"+password+"</li>")
	  	  .append("</ul>")
	  	  .append("<p>Please dont share you username or password, Smart Bank wont ask any password in any case,  <a href='http://smartbanking-codechef.rhcloud.com/Smart' target='_blank'>login</a> and start using all of the great <span class='il'>Smart Bank</span> features!</p>")
	  	  .append("<p>Thanks,")
	  	  .append("<br><b><span class='il'>Smart Bank</span></b>")
	  	  .append("</p>")
	  	  .append("<p>If you need further assistance, please contact us at <span class='il'>Smart Bank</span> Support &lt;<a href='mailto:smartbanking.codechef@gmail.com' target='_blank'>smartbanking.codechef@<span class='il'>gmail</span>.com</a>&gt;")
	  	  .append("<br><a href='mailto:SmartBank+Support+%3Csmartbanking.codechef@gmail.com%3E?subject=Support+Request' target='_blank'>Send Mail Now</a>")
	  	  .append("</p>")
	  	  .append("<div class='yj6qo'></div>")
	  	  .append("<div class='adL'></div>")
	  	  .append("</div>")
	  	  .append(" </div>");
		return stringBuilder.toString();
	}

}