package org.cit.mcaleerj.thesis.uiservice.controller;


import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@org.springframework.context.annotation.Configuration

public class Configuration implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/environment/graph.js").setViewName("forward:/graph.js");
    registry.addViewController("/environment/styles.css").setViewName("forward:/styles.css");
    registry.addViewController("/environment/*").setViewName("forward:/index.html");
    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
  }


}