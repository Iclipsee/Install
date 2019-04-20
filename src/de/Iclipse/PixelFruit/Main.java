package de.Iclipse.PixelFruit;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

/**
 * Created by Yannick who could get really angry if somebody steal his code!
 * ~Yannick on 17.04.2019 at 14:04 o´ clock
 */
public class Main {
    public static void main(String[] args){
        File libs = new File("PFLibs");
        File cloud = new File("PFLibs/PFCloud.jar");
        File api = new File("PFLibs/PFAPI.jar");
            System.out.println("PixelFruits System wird installiert!");
            System.out.println("Libraries werden heruntergeladen!");
            libs.mkdir();
            downloadFileFromURL("https://cdn.getbukkit.org/spigot/spigot-1.13.2.jar", new File("PFLibs/Spigot.jar"));
            downloadFileFromURL("https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar", new File("PFLibs/Bungee.jar"));
            downloadCloud(cloud);
            System.out.println("Libraries wurden heruntergeladen!");
            System.out.println("PixelFruits System wurde installiert!");
        System.exit(0);
    }

    public static void downloadCloud(File cloud){
        String output = callURL("https://api.github.com/repos/Iclipsee/PFCloud/releases/latest");

        downloadFileFromURL(output.split("\"browser_download_url\":\"")[1].split("\"")[0], cloud);

    }


    public static void downloadAPI(File api){
        String output = callURL("https://api.github.com/repos/Iclipsee/PFAPI/releases/latest");
        StringBuilder result = new StringBuilder();

        readData(output, result);

        String u = result.toString();

        downloadFileFromURL(u.split("\"browser_download_url\": \"")[1].split("\"")[0], api);

    }

    public static void downloadFileFromURL(String urlString, File destination) {
        try {
            URL website = new URL(urlString);
            System.out.println("Verbindung zu " + urlString.split("/")[2] + " wird hergestellt!");
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void readData(String toRead, StringBuilder result) {
        int i = 7;

        while(i < 200) {
            if(!String.valueOf(toRead.charAt(i)).equalsIgnoreCase("\"")) {

                result.append(String.valueOf(toRead.charAt(i)));

            } else {
                break;
            }

            i++;
        }
    }


    private static String callURL(String URL) {
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(URL);
            urlConn = url.openConnection();

            if (urlConn != null) urlConn.setReadTimeout(60 * 1000);

            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);

                if (bufferedReader != null) {
                    int cp;

                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }

                    bufferedReader.close();
                }
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
