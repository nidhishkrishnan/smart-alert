package com.codechef.smartalert.config;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.codechef.smartalert.notification.service.FileProcessor;
import com.codechef.smartalert.notification.service.NotificationCondition;
import com.codechef.smartalert.notification.service.NotificationSystem;
import com.codechef.smartalert.notification.service.Subscriber;

public class AppInitializer implements WebApplicationInitializer {

	public void onStartup(ServletContext container) throws ServletException {

		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(AppConfig.class);
		ctx.setServletContext(container);

		ServletRegistration.Dynamic servlet = container.addServlet(
				"dispatcher", new DispatcherServlet(ctx));

		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
		
		FileProcessor processor = new FileProcessor("D:/Amazing Hack/Files");
		processor.doProcess();

		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				NotificationSystem.getInstance().addSubscriber(1);
				NotificationCondition condition = new NotificationCondition();
				condition.setProductId(13579);
				condition.setAttributName("price");
				System.out.println("b: "+NotificationSystem.getInstance().registerSubscriberForNotification(condition, 1));
			}
		}, 10000);
		NotificationSystem.getInstance().startNotify();
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				//System.out.println(NotificationSystem.getInstance().getNoti(1));
			}
		}, 10000);
	}

}
