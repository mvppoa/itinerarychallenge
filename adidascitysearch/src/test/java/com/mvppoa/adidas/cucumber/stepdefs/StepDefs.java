package com.mvppoa.adidas.cucumber.stepdefs;

import com.mvppoa.adidas.AdidascitysearchApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = AdidascitysearchApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
