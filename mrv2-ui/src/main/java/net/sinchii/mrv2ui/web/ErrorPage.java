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
