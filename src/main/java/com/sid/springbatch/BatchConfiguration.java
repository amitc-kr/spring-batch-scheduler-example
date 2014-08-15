package com.sid.springbatch;

import com.sid.springbatch.model.Person;
import com.sid.springbatch.model.Account;
import com.sid.springbatch.processor.MyItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableBatchProcessing
@ComponentScan
//spring boot configuration
@EnableAutoConfiguration
// file that contains the properties
@PropertySource("classpath:application.properties")
public class BatchConfiguration {

    /*
        Load the properties
     */
    @Value("${database.driver}")
    private String databaseDriver;
    @Value("${database.url}")
    private String databaseUrl;
    @Value("${database.username}")
    private String databaseUsername;
    @Value("${database.password}")
    private String databasePassword;


    /**
     * JPAPagingItemReader
     *
     * @return
     */
    @Bean
    public ItemReader<Account> reader() throws Exception {
        String jpqlQuery = "select a from Account a";

    		JpaPagingItemReader<Account> reader = new JpaPagingItemReader<Account>();
    		reader.setQueryString(jpqlQuery);
    		reader.setEntityManagerFactory(entityManagerFactory().getObject());
    		reader.setPageSize(3);
    		reader.afterPropertiesSet();
    		reader.setSaveState(true);

    		return reader;
    }

    /**
     * The ItemProcessor is called after a new record is read and it allows the developer
     * to transform the data read
     * In our example it simply read and write the data
     *
     * @return
     */
    @Bean
    public ItemProcessor<Account, Person> processor() {
        return new MyItemProcessor();
    }

    /**
     * Nothing special here a simple JpaItemWriter
     * @return
     */
    @Bean
    public ItemWriter<Person> writer() {
        JpaItemWriter writer = new JpaItemWriter<Person>();
        writer.setEntityManagerFactory(entityManagerFactory().getObject());

        return writer;
    }

    /**
     * This method declare the steps that the batch has to follow
     *
     * @param jobs
     * @param s1
     * @return
     */
    @Bean
    public Job importPerson(JobBuilderFactory jobs, Step s1) {

        return jobs.get("import")
                .incrementer(new RunIdIncrementer()) // because a spring config bug, this incrementer is not really useful
                .flow(s1)
                .end()
                .build();
    }


    /**
     * Step
     * We declare that every 1000 lines processed the data has to be committed
     *
     * @param stepBuilderFactory
     * @param reader
     * @param writer
     * @param processor
     * @return
     */

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Account> reader,
                      ItemWriter<Person> writer, ItemProcessor<Account, Person> processor) {
        return stepBuilderFactory.get("step1")
                .<Account, Person>chunk(1000)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    /**
     * As data source we use an external database
     *
     * @return
     */

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(databaseDriver);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(databaseUsername);
        dataSource.setPassword(databasePassword);
        return dataSource;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setPackagesToScan("com.sid.springbatch");
        lef.setDataSource(dataSource());
        lef.setJpaVendorAdapter(jpaVendorAdapter());
        lef.setJpaProperties(new Properties());
        return lef;
    }


    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setShowSql(false);

        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        return jpaVendorAdapter;
    }

}
