package org.cit.mcaleerj.thesis.springkafka.headers.configuration;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.stream.messaging.DirectWithAttributesChannel;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;

@org.springframework.context.annotation.Configuration
public class Configuration {

  private static final String ATTRIB_CHANNEL_TYPE = "type";
  private static final String HEADER_APPLICATION_NAME = "HEADER_APP_NAME";
  private static final String CHANNEL_TYPE_OUTPUT = "output";

  @Value("${spring.application.name}")
  private String applicationName;

  @Bean
  public BeanPostProcessor messageChannelConfigurer() {
    return new BeanPostProcessor() {

      @Override
      public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {

        if (bean instanceof DirectWithAttributesChannel) {
          final DirectWithAttributesChannel channel = (DirectWithAttributesChannel) bean;
          final Object channelType = channel.getAttribute(ATTRIB_CHANNEL_TYPE);
          if (CHANNEL_TYPE_OUTPUT.equals(channelType)) {
            channel.addInterceptor(
                    new ChannelInterceptor() {

                      @Override
                      public Message<?> preSend(Message<?> message, MessageChannel channel) {
                        final Message<?> annotatedMessage = MessageBuilder.withPayload(message.getPayload()).
                                copyHeaders(message.getHeaders()).setHeader(HEADER_APPLICATION_NAME, applicationName).build();
                        return annotatedMessage;
                      }

                    });


          }        }
        return bean;
      }

    };

  }
}
