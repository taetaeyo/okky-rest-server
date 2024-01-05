package com.okky.restserver.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author taekwon
 * @description override datasourceLookUp for dynamic datasource
 */

public class DynamicDataSource extends AbstractRoutingDataSource {
	
	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContext.getDataSourceType();
	}
}
