package ru.otus.pro.hw.jee.web.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

public class SubtractServlet extends CalculatorServlet {
    private static Logger logger = LoggerFactory.getLogger(SubtractServlet.class);

    @Override
    public void init() throws ServletException {
        logger.debug("SubtractServlet initialized");
    }
}
