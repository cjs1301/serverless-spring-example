package com.example.NoMoney.config;

import java.sql.Connection;


import javax.sql.DataSource;

import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;



@Configuration
public class SnapStartConfiguration implements Resource {
    LocalContainerEntityManagerFactoryBean _dataSourceBean;

    public SnapStartConfiguration(LocalContainerEntityManagerFactoryBean dataSourceBean)
    {


        Core.getGlobalContext().register(SnapStartConfiguration.this);

        _dataSourceBean = dataSourceBean;
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        // 체크포인트 전에 필요한 작업 수행
        // 예: 데이터베이스 연결 닫기, 캐시 비우기 등
        DataSource dataSource = _dataSourceBean.getDataSource();
        Connection databaseConnection = dataSource.getConnection();

        if (!databaseConnection.isClosed())
        {
            databaseConnection.close();
        }
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        // 복원 후 필요한 작업 수행
        // 예: 데이터베이스 연결 재설정, 캐시 재구축 등
    }
}
