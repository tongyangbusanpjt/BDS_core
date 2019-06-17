package com.bnf.bds.config.resolver;

import org.springframework.core.io.Resource;

/**
 * 
 * @author SJH
 */
public interface LocationResolver {
	
	/**
	 * 
	 * @return
	 */
	Resource[] resolve();
}
