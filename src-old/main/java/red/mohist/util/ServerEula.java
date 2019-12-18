package red.mohist.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import red.mohist.util.i18n.Message;
import red.mohist.util.i18n.UTF8Properties;

public class ServerEula
{
    private final File eulaFile;
    private final boolean acceptedEULA;

    public ServerEula(File eulaFile)
    {
        this.eulaFile = eulaFile;
        this.acceptedEULA = this.loadEULAFile(eulaFile);
    }

    private boolean loadEULAFile(File inFile)
    {
        FileInputStream fileinputstream = null;
        boolean flag = false;

        try
        {
            UTF8Properties properties = new UTF8Properties();
            fileinputstream = new FileInputStream(inFile);
            properties.load(new InputStreamReader(fileinputstream, StandardCharsets.UTF_8));
            flag = Boolean.parseBoolean(properties.getProperty("eula", "false"));
        }
        catch (Exception var8)
        {
            this.createEULAFile();
        }
        finally
        {
            if (fileinputstream != null)
            {
                try
                {
                    fileinputstream.close();
                }
                catch (IOException var11)
                {
                    ;
                }
            }
        }

        return flag;
    }

    public boolean hasAcceptedEULA()
    {
        return this.acceptedEULA;
    }

    public void createEULAFile()
    {
        FileOutputStream fileoutputstream = null;

        try
        {
            UTF8Properties properties = new UTF8Properties();
            fileoutputstream = new FileOutputStream(this.eulaFile);
            properties.put("eula", "false", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            Object[] p = {"https://account.mojang.com/documents/minecraft_eula"};
            properties.orderStore(new OutputStreamWriter(fileoutputstream, StandardCharsets.UTF_8), Message.getFormatString("eula.text",p));
        }
        catch (Exception exception)
        {
        }
        finally
        {
            if (fileoutputstream != null)
            {
                try
                {
                    fileoutputstream.close();
                }
                catch (IOException var11)
                {
                    ;
                }
            }
        }
    }
}