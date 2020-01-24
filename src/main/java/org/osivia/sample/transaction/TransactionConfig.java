package org.osivia.sample.transaction;


import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.tasks.ITasksService;
import org.osivia.portal.api.transaction.ITransactionService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.ldap.transaction.compensating.manager.ContextSourceTransactionManagerDelegate;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoServiceFactory;

/**
 * @author Jean-Sébastien Steux
 *
 */
@Configuration
@ComponentScan(basePackages = {"org.osivia.sample.transaction"})
public class TransactionConfig {



    /**
     * Get view resolver.
     *
     * @return view resolver
     */
    @Bean
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setCache(true);
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }


    /**
     * Get message source.
     *
     * @return message source
     */
    @Bean(name = "messageSource")
    public ResourceBundleMessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("Resource");
        return messageSource;
    }

    
    /**
     * Get bundle factory.
     *
     * @return bundle factory
     */
    @Bean
    public IBundleFactory getBundleFactory() {
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        return internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }

    /**
     * Get notifications service.
     * 
     * @return notification service
     */
    @Bean
    public INotificationsService getNotificationService() {
        return Locator.findMBean(INotificationsService.class, INotificationsService.MBEAN_NAME);
    }
    
    
    /**
     * Get notifications service.
     * 
     * @return notification service
     */
    @Bean
    public ITransactionService getTransactionService() {
        return Locator.findMBean(ITransactionService.class, ITransactionService.MBEAN_NAME);
    }


    /**
     * Get forms service.
     * 
     * @return forms service
     */
    @Bean
    public IFormsService getFormsService() {
        return NuxeoServiceFactory.getFormsService();
    }

    /**
     * Get person service.
     *
     * @return person service
     */
    @Bean(name = "personUpdateService")
    public PersonUpdateService getPersonService() {
        return DirServiceFactory.getService(PersonUpdateService.class);
    }
    
    
    /**
     * Get person service.
     *
     * @return person service
     */
    @Bean(name = "ldapTxManager")
    public ContextSourceTransactionManagerDelegate getTxManager() {
        return (ContextSourceTransactionManagerDelegate) DirServiceFactory.getDirectoryTxManagerDelegate();
    }
    
    

    /**
     * Get tasks service.
     * 
     * @return tasks service
     */
    @Bean
    public ITasksService getTasksService() {
        return Locator.findMBean(ITasksService.class, ITasksService.MBEAN_NAME);
    }


    
    
}
