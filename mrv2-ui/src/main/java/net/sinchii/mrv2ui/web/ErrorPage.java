package net.sinchii.mrv2ui.web;

import java.io.PrintWriter;

public class ErrorPage {

  private final static String TITLE = "Error";
  
  private HTMLPage page;
  private int statusCode;
  
  public ErrorPage(PrintWriter writer, int code) {
    page = new HTMLPage(writer);
    statusCode = code;
    render();
  }
  
  public void render() {
    page.output(HTMLPage.DOCTYPE);
    page.html().head().meta_http("X-UA-Compatible", "IE=8")
      .meta_http("Content-type", "text/html; charset=UTF-8")
      .title(TITLE)._("head").body();
    
    page.output(Integer.toString(statusCode));
    
    page._("body")._("html");
  }
}
