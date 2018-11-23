package ota.monitoring.backend.esb.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import ota.monitoring.backend.esb.events.PackageReleaseEvent;

@Service
public class JmsSender {

	@Autowired
	@Qualifier("jmsTemplateQueue")
	private JmsTemplate jmsTemplateQueue;

	@Autowired
	@Qualifier("jmsTemplateTopic")
	private JmsTemplate jmsTemplateTopic;

	public void notifyVehicle(PackageReleaseEvent event) {
		jmsTemplateTopic.convertAndSend("topic.vehicle.package.release.destination", event);
	}
}