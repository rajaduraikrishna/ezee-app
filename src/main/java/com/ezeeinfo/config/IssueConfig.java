package com.ezeeinfo.config;

import com.ezeeinfo.issuemanager.IssueManagerManager;
import com.ezeeinfo.issuemanager.service.IssueService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class IssueConfig {
    @Bean
    IssueManagerManager issueManager(final DataSource dataSource) {
        return IssueManagerManager.getManager(dataSource);
    }

    @Bean
    IssueService issueService(IssueManagerManager manager) {
        return new IssueService(manager);
    }
}
