package jdf.framework.core.mail;

public final class SMTPAuthenticator extends javax.mail.Authenticator 
{

    private String id;
    private String pw;

    public SMTPAuthenticator(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
        return new javax.mail.PasswordAuthentication(id, pw);
    }

}
