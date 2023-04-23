package org.example;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import io.dropwizard.db.DataSourceFactory;

import org.example.objects.PersonMapper;
import org.example.resources.MyResource;
import org.jdbi.v3.core.Jdbi;

public class MyApplication extends Application<MyConfiguration> {
    public static void main(String[] args) throws Exception {
        new MyApplication().run(args);
    }

    @Override
    public void run(MyConfiguration configuration, Environment environment) throws Exception {
        // Database connection
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        jdbi.registerRowMapper(new PersonMapper());
        // Endpoint & Functions
        final MyResource myResource = new MyResource(jdbi);
        // Start the server
        environment.jersey().register(myResource);
    }
}
