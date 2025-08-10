package com.mykare.usermanagement.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.mykare.usermanagement.cucumber",
        plugin = {"pretty", "json:target/cucumber-report.json"},
        publish = true
)
public class CucumberTestRunner {
}
