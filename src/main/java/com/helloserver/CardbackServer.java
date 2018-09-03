package com.helloserver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// [START example]
@SuppressWarnings("serial")
@WebServlet(name = "helloworld", value = "/")
public class CardbackServer extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    out.println("Hello Elsie");
  }

  public static String getInfo() {
    return "Version: " + System.getProperty("java.version")
        + " OS: " + System.getProperty("os.name")
        + " User: " + System.getProperty("user.name");
  }
}