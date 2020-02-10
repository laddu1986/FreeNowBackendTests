package com.freenow.global.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * This class provides Extent Helper methods
 *
 * @author Kushal Bhalaik
 *
 */
public class ExtentHelper {

   private static ConfigReader configReader = ConfigReader.getInstance();

   /**
    * This method copies logs from logs folder and attached to Final Extent Report
    */
   public static List<String> getFinalLogs() throws IOException {
      BufferedReader bis = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/output/"
            + (null != System.getProperty("env") ? System.getProperty("env")
                  : configReader.getRunnerConfigProperty("ENV"))
            + "/logs/" + "TestLog_" + System.getProperty("TEST_RUN_TIMESTAMP") + ".log"));

      String line = null;

      List<String> logList = new ArrayList<>(0);

      while ((line = bis.readLine()) != null) {
         if (line.contains("START ")) {
            logList.add("<button class=\"test-start\">" + line +"</button><br>\n");
         } else if (line.contains("ERROR ")) {
            logList.add("<span ><mark style=\"color:red;\">" + line + "</mark></span><br>");
         } else if (line.contains("PASS ")) {
            logList.add("<span style=\"color:green;\">" + line + "</span><br>");
         } else if (line.contains("FAIL ")) {
            logList.add("<button class=\"collapsible\">Failure Stacktrace (click to see)</button>\n" +
                    "<pre class=\"content\">\n" + line + "<br>");
         }  else if(line.contains("FAIL-END")) {
            logList.add("</p></pre>");
         }else if (line.contains("CODE ")) {
            logList.add("<button class=\"collapsible\">Actual Response (click to see)</button>\n" +
                    "<pre class=\"content\">\n" + line + "<br>");
         }  else if(line.contains("CODE-END")) {
            logList.add("</p></pre>");
         } else {
            logList.add(line + "<br>");
         }
      }

      bis.close();

      return logList;

   }

   /**
    * This method copies org_logo from "assets" folder and copies it to Test Run
    * folder
    */
   public static void createAssetsDirectory() {

      File source = new File("src/test/resources/assets");
      File destination = null;

      destination = new File(System.getProperty("user.dir") + "/output/"
            + (null != System.getProperty("env") ? System.getProperty("env")
                  : configReader.getRunnerConfigProperty("ENV")));

      try {
         FileUtils.copyDirectory(source, destination);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }


   /**
    * This method removes embedded links for resources and
    * replaces with them local resources
    */
   public static void replaceEmbeddedLinks(String message) {
      File sourceFile = new File(System.getProperty("user.dir") + "/output/"
            + (null != System.getProperty("env") ? System.getProperty("env")
                  : configReader.getRunnerConfigProperty("ENV"))
            + "/Test_Report.html");

      File targetFile = new File(System.getProperty("user.dir") + "/output/"
            + (null != System.getProperty("env") ? System.getProperty("env")
                  : configReader.getRunnerConfigProperty("ENV"))
            + "/Test_Report_final.html");

      String searchString1 = "http://fonts.googleapis.com/icon?family=Material+Icons";
      String searchString2 = "<link href='http://cdn.rawgit.com/anshooarora/extentreports-java/6d3843c29c354373fafad29c03a1a89b7e06c374/dist/css/extent.css' type='text/css' rel='stylesheet' />";
      String searchString3 = "http://cdn.rawgit.com/anshooarora/extentreports-java/5ed7b0c339b51aa347b44ccaa2e2ff29905b2f9f/dist/js/extent.js";
      String searchString4 = "<a href=\"#!\" class=\"brand-logo blue darken-3\">Extent</a>";
      String searchString5 = "</body>";

      String replaceString1 = "MaterialIcons.css";
      String replaceString2 = "<link href='MaterialIcons.css' rel='stylesheet' /> <link href='style.css' type='text/css' rel='stylesheet'> <link href='extent.css' type='text/css' rel='stylesheet' />";
      String replaceString3 = "extent.js";
      String replaceString4 = "<a href=\"#!\" class=\"brand-logo blue darken-3\" style=\"padding-left: 5px;\">Test Report</a>";
      String replaceString5 = "<script > var coll =  document.getElementsByClassName(\"collapsible\"); var i; for (i = 0; i < coll.length; i++) { coll[i].addEventListener(\"click\", function() { this.classList.toggle(\"active\"); var content = this.nextElementSibling; if (content.style.maxHeight){ content.style.maxHeight = null; } else { content.style.maxHeight = content.scrollHeight + \"px\"; } }); } </script></body>";
      try {
         //Copy to report file before making modifications
         FileUtils.copyFile(sourceFile, targetFile);

         FileReader fr = new FileReader(targetFile);
         String s;
         String totalStr = "";
         try (BufferedReader br = new BufferedReader(fr)) {

            while ((s = br.readLine()) != null) {
               totalStr += s;
            }
            totalStr = totalStr.replaceAll(searchString1, replaceString1);
            totalStr = totalStr.replaceAll(searchString2, replaceString2);
            totalStr = totalStr.replaceAll(searchString3, replaceString3);
            totalStr = totalStr.replaceAll(searchString4, replaceString4);
            totalStr = totalStr.replaceAll(searchString5, replaceString5);

            FileWriter fw = new FileWriter(targetFile);
            fw.write(totalStr);
            fw.close();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

}
