package ota.monitoring.backend.config;

import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ErrorHandler;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;

@Configuration
@EnableScheduling
@EnableJms
public class JmsMainConfiguration implements JmsListenerConfigurer {

	@Value("${activemq.brokerUrl}")
	private String brokerURL;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public CachingConnectionFactory cachedConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(brokerURL);

		RedeliveryPolicy policy = new RedeliveryPolicy();
		policy.setMaximumRedeliveries(RedeliveryPolicy.DEFAULT_MAXIMUM_REDELIVERIES);
		activeMQConnectionFactory.setRedeliveryPolicy(policy);
		activeMQConnectionFactory.setTrustAllPackages(true);
		CachingConnectionFactory cachedConnectionFactory = new CachingConnectionFactory(activeMQConnectionFactory);
		return cachedConnectionFactory;
	}

	public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
		registrar.setMessageHandlerMethodFactory(jmsHandlerMethodFactory());
		registrar.setContainerFactory(queueListenerContainerFactory());
	}

	// Queue listener factory
	@Bean
	public JmsListenerContainerFactory queueListenerContainerFactory() {
		DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
		defaultJmsListenerContainerFactory.setConnectionFactory(cachedConnectionFactory());
		defaultJmsListenerContainerFactory.setErrorHandler(new LoggingErrorHandler());
		defaultJmsListenerContainerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

		return defaultJmsListenerContainerFactory;
	}

	// Topic listener factory
	@Bean
	public JmsListenerContainerFactory topicListenerContainerFactory() {
		DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
		defaultJmsListenerContainerFactory.setConnectionFactory(cachedConnectionFactory());
		defaultJmsListenerContainerFactory.setPubSubDomain(true);
		defaultJmsListenerContainerFactory.setErrorHandler(new LoggingErrorHandler());
		return defaultJmsListenerContainerFactory;
	}

	@Bean
	public MessageHandlerMethodFactory jmsHandlerMethodFactory() {
		DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
		factory.setMessageConverter(messageConverter());
		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplateQueue() {
		JmsTemplate jmsTemplate = new JmsTemplate(cachedConnectionFactory());
		jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
		return jmsTemplate;
	}

	@Bean
	public JmsTemplate jmsTemplateTopic() {
		JmsTemplate jmsTemplate = new JmsTemplate(cachedConnectionFactory());
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}

	@Bean
	public MessageConverter messageConverter() {
		MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
		mappingJackson2MessageConverter.getObjectMapper().setVisibility(PropertyAccessor.FIELD,
				JsonAutoDetect.Visibility.ANY);
		return mappingJackson2MessageConverter;
	}

	private static class LoggingErrorHandler implements ErrorHandler {

		private static final Logger LOGGER = LoggerFactory.getLogger(LoggingErrorHandler.class);

		public void handleError(Throwable t) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Unexpected error occurred in jms listener.", t);
			}
		}
	}

}
