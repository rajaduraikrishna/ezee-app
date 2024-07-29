

package com.ezeeinfo.config;


import com.ezeeinfo.issuemanager.IssueManagerManager;
import com.ezeeinfo.issuemanager.service.IssueService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

/**
 * IssueConfig class.
 */
@Configuration
public class IssueConfig {

    /**
     *The issuseManager.
     * @param dataSource
     * @return dataSource
     */
    @Bean
    IssueManagerManager issueManager(final DataSource dataSource) {
        return IssueManagerManager.getManager(dataSource);
    }
    /**
     * The issueService.
     * @param manager
     * @return manager
     */
    @Bean
    IssueService issueService(final IssueManagerManager manager) {
        return new IssueService(manager);
    }
}
