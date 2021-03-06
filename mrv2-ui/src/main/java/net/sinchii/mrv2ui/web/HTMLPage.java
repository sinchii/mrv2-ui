/**
 * Copyright (c) 2015 Shinichi YAMASHITA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sinchii.mrv2ui.web;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HTMLPage {

  private PrintWriter out;
  
  public static final String DOCTYPE =
      "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\""
          + " \"http://www.w3.org/TR/html4/strict.dtd\">";
  public static final String HOME = "/mrv2-ui/m/";
  public static final String JQMINJS =
      "/mrv2-ui/static/jquery/jquery-1.11.3.min.js";
  public static final String JQDTMINJS =
      "/mrv2-ui/static/jquery/jquery.dataTables.min.js";
  public static final String DTJQMINJS =
      "/mrv2-ui/static/jquery/dataTables.jqueryui.min.js";
  public static final String JQUICSS =
      "/mrv2-ui/static/jquery/jquery-ui.css";
  public static final String DTJQUIMINCSS =
      "/mrv2-ui/static/jquery/dataTables.jqueryui.min.css";
  public static final String MRV2UICSS = "/mrv2-ui/static/mrv2ui.css";
  
  public HTMLPage() {
    
  }
  
  public HTMLPage(PrintWriter writer) {
    out = writer;
  }
  
  public void output(String str) {
    out.println(str);
  }
  
  public HTMLPage html() {
    out.println("<html>");
    return this;
  }
  
  public HTMLPage head() {
    out.println("<head>");
    return this;
  }
  
  public HTMLPage link(String rel, String href) {
    out.println("<link rel=\"" + rel + "\" href=\"" + href + "\">");
    return this;
  }
  
  public HTMLPage link(String rel, String type, String href) {
    out.println("<link rel=\"" + rel + "\" type=\"" 
        + type + "\" href=\"" + href + "\">");
    return this;
  }
  
  public HTMLPage title(String title) {
    out.println("  <title>" + title + "</title>");
    return this;
  }
  
  public HTMLPage meta_http(String header, String content) {
    out.println("<meta http-equiv=\"" + header + "\" content=\""
        + content + "\">");
    return this;
  }
  
  public HTMLPage script() {
    out.println("<script>");
    return this;
  }
  
  public HTMLPage script(String type) {
    out.println("<script type=\"" + type + "\">");
    return this;
  }
  
  public HTMLPage script(String type, String src) {
    out.println("<script type=\"" + type + "\" src=\"" + src + "\">");
    return this;
  }
  
  public HTMLPage body() {
    out.println("<body>");
    return this;
  }
  
  public HTMLPage table() {
    out.println("<table>");
    return this;
  }
  public HTMLPage table(String tableId) {
    out.println("<table id=\"" + tableId + "\">");
    return this;
  }
  
  public HTMLPage table(String tableId, String clazz) {
    out.println("<table id=\"" + tableId + "\" class=\"" + clazz +"\">");
    return this;
  }
  
  public HTMLPage thead() {
    out.println("<thead>");
    return this;
  }
  
  public HTMLPage tbody() {
    out.println("<tbody>");
    return this;
  }
  
  public HTMLPage tr() {
    out.println("<tr>");
    return this;
  }
  
  public HTMLPage th(String clazz, String name) {
    out.println("<th class=\"" + clazz + "\">" + name + "</th>");
    return this;
  }
  
  public HTMLPage th(String name) {
    out.println("<th>" + name + "</th>");
    return this;
  }
  
  public HTMLPage td(String clazz, String name) {
    out.println("<td class=\"" + clazz + "\">" + name + "</td>");
    return this;
  }

  public HTMLPage td(String name) {
    out.println("<td>" + name + "</td>");
    return this;
  }

  public HTMLPage td() {
    out.println("<td>");
    return this;
  }
  
  public HTMLPage div(String clazz) {
    out.println("<div class=\"" + clazz + "\">");
    return this;
  }
  
  public HTMLPage div(String id, String clazz) {
    out.println("<div id=\"" + clazz + "\" class=\"" + clazz + "\">");
    return this;
  }

  public HTMLPage h1(String h1) {
    out.println("<h1>" + h1 + "</h1>");
    return this;
  }
  
  public HTMLPage h2(String h2) {
    out.println("<h2>" + h2 + "</h2>");
    return this;
  }
  
  public HTMLPage h3(String h3) {
    out.println("<h3>" + h3 + "</h3>");
    return this;
  }
  
  public HTMLPage a(String href, String name) {
    out.println("<a href=\"" + href + "\">" + name + "</a>");
    return this;
  }
  
  public HTMLPage br() {
    out.println("<br />");
    return this;
  }
  
  public HTMLPage _(String tag) {
    out.println("</" + tag + ">");
    return this;
  }
  
  public HTMLPage dataTable(String tableId) {
    String script = "$(document).ready(function() {\n"
        + "  $('#" + tableId + "').DataTable();\n"
        + "} );";
    out.println(script);
    return this;
  }
  
  public static String getDisplayDate(long time) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd E HH:mm:ss z");
    return sdf.format(new Date(time));
  }
  
  public static String getElapsedTime(long time) {
    long sec10 = (time / 100) % 10;
    long sec = (time / 1000) % 60;
    long min = (time / 60 / 1000);
    long hour = (time / 60 / 60 / 1000);
    String timeStr =
        ((hour > 0) ? hour + " hour ": "")
        + ((min > 0) ? min + " min " : "")
        + ((sec > 0) ? sec + "." + sec10 + " sec" : "");
    return timeStr;
  }
}
