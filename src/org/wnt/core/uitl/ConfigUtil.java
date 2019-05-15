package org.wnt.core.uitl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Properties;


public class ConfigUtil {
    
    public static String getFileIO(String name,String fileURL){
         Properties prop = new Properties();
         InputStream in = ConfigUtil.class.getResourceAsStream(fileURL);
         try {
            prop.load(in);
            return prop.getProperty(name);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
    public static void writeData(String key, String value,String fileURL) {
        Properties prop = new Properties();
        InputStream fis = null;
        OutputStream fos = null;
        try {
              java.net.URL  url = ConfigUtil.class.getResource(fileURL);
            File file = new File(url.toURI());
            if (!file.exists())
                file.createNewFile();
            fis = new FileInputStream(file);
            prop.load(fis);
            fis.close();//一定要在修改值之前关闭fis
            fos = new FileOutputStream(file);
            prop.setProperty(key, value);
            prop.store(fos, "Update '" + key + "' value");
            fos.close();
            
        } catch (IOException e) {
            System.err.println("Visit " + fileURL + " for updating "
            + value + " value error");
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            try {
                fos.close();
                fis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    } 
    
    public static void main(String[] args) {
        //ConfigUtil.getFileIO("name", "gxyTest.properties");
        
        ConfigUtil.writeData("name", "microsoft", "sysConfig.properties");
        System.out.println(ConfigUtil.getFileIO("name", "sysConfig.properties"));
        
        ConfigUtil.writeData("name", "microsoft12", "sysConfig.properties");
        System.out.println(ConfigUtil.getFileIO("name", "sysConfig.properties"));
    }

}
