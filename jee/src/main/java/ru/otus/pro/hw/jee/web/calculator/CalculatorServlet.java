package ru.otus.pro.hw.jee.web.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.OperationNotSupportedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CalculatorServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(CalculatorServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer result = null;
        String error = null;
        try {
            Integer left = Integer.valueOf(request.getParameter("left"));
            Integer right = Integer.valueOf(request.getParameter("right"));
            String operator = request.getParameter("operator");
            logger.debug("left " + left);
            logger.debug("right " + right);
            logger.debug("operator " + operator);
            result = Calculator.calculate(Calculator.Operator.valueOf(operator.toUpperCase()), left, right);
            logger.debug("result " + result);
        } catch (OperationNotSupportedException | IllegalArgumentException e) {
            logger.error("error " + e.getMessage());
            error = e.getMessage();
            if (e instanceof NumberFormatException) {
                error = "left or right is null or empty";
            }
        }

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            // Ajax request.
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            if (error == null) {  // Normal request.
                response.getWriter().write(String.valueOf(result));
            } else {
                response.getWriter().write(String.valueOf(error));
            }
        } else {
            if (error == null) {  // Normal request.
                request.setAttribute("result", result);
            } else {
                request.setAttribute("error", error);
            }
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

}
