package com.okky.restserver.config;

import com.okky.restserver.aop.DataSourceEnum;

/**
 * @author taekwon
 * @description ThreadLocal context for dynamic datasource
 * 
 */

public class DataSourceContext {
	
    private static final ThreadLocal<DataSourceEnum> CONTEXT = new ThreadLocal<DataSourceEnum>() {

        @Override
        protected DataSourceEnum initialValue() {
            return DataSourceEnum.primary;
        }
        
    };

    public static void setDataSourceType(DataSourceEnum type) {
    	CONTEXT.set(type);
    }

    public static DataSourceEnum getDataSourceType() {
        return CONTEXT.get();
    }

    public static void resetDataSourceType() {
    	CONTEXT.set(DataSourceEnum.primary);
    }
    
    public static void clear() {
        CONTEXT.remove();
    }
}