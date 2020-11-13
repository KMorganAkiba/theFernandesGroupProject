package Main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class Logs {

    private static String successfuls = "successfulLogins.txt";
    private static String failures = "failedLogins.txt";

    public static void logSuccess(String username) {
        //appends the successfulLogins.txt file with all subsequent successful logins.
        try (
                FileWriter fileWriter = new FileWriter(successfuls,true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter printWriter = new PrintWriter(bufferedWriter)){
                printWriter.println(LocalDateTime.now() + " " + username + ": Successful login");
                }
                catch (IOException e){
                    System.out.println("Log Error: " + e.getMessage());
        }
    }
    public static void logFailure(String username) {
        //appends the failedLogins.txt file with all subsequent failed logins.
        try (
                FileWriter fileWriter = new FileWriter(failures,true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PrintWriter printWriter = new PrintWriter(bufferedWriter)){
                printWriter.println(LocalDateTime.now() + " " + username + ": Failed login");
        }
        catch (IOException e){
            System.out.println("Log Error: " + e.getMessage());
        }
    }
}