package com.asxms.customer;

import org.springframework.aot.generate.Generated;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * Bean definitions for {@link CustomerListDataAccessService}.
 */
@Generated
public class CustomerListDataAccessService__BeanDefinitions {
  /**
   * Get the bean definition for 'list'.
   */
  public static BeanDefinition getListBeanDefinition() {
    RootBeanDefinition beanDefinition = new RootBeanDefinition(CustomerListDataAccessService.class);
    beanDefinition.setInstanceSupplier(CustomerListDataAccessService::new);
    return beanDefinition;
  }
}
