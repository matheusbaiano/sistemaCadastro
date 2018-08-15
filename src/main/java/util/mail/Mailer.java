package util.mail;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.outjected.email.api.MailMessage;
import com.outjected.email.api.SessionConfig;
import com.outjected.email.impl.MailMessageImpl;
import util.mail.MailConfigProducer;

@RequestScoped
public class Mailer implements Serializable {

	private static final long serialVersionUID = 1L;

	public MailMessage novaMensagem() {
		MailConfigProducer config = new MailConfigProducer();
		return new MailMessageImpl(config.getMailConfig());
	}
	
}
