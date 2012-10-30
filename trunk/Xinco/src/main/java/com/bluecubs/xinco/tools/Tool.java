package com.bluecubs.xinco.tools;

import com.bluecubs.xinco.core.server.service.XincoAddAttribute;
import com.bluecubs.xinco.core.server.service.XincoCoreDataTypeAttribute;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class Tool {

    public static int MIN_PORT_NUMBER = 1;
    public static int MAX_PORT_NUMBER = 65535;
    private static final Logger LOG =
            Logger.getLogger(Tool.class.getName());

    private Tool() {
    }

    /**
     * Compare two number strings. For example: 2.1.0 == 2.01.00
     *
     * @param first first string to compare
     * @param second second string to compare
     * @return
     */
    public static boolean compareNumberStrings(String first, String second) {
        return compareNumberStrings(first, second, ".");
    }

    /**
     * Compare two number strings. For example: 2.1.0 == 2.01.00
     *
     * @param first first string to compare
     * @param second second string to compare
     * @param separator separator of fields (i.e. for 2.1.0 is '.')
     * @return
     */
    public static boolean compareNumberStrings(String first, String second,
            String separator) {
        StringTokenizer firstST = new StringTokenizer(first, separator);
        StringTokenizer secondST = new StringTokenizer(second, separator);
        if (firstST.countTokens() != secondST.countTokens()) {
            //Different amount of fields, not equal. (i.e. 2.1 and 2.1.1
            return false;
        } else {
            try {
                while (firstST.hasMoreTokens()) {
                    int firstInt = Integer.parseInt(firstST.nextToken());
                    int secondInt = Integer.parseInt(secondST.nextToken());
                    //Both numbers let's continue
                    if (firstInt != secondInt) {
                        return false;
                    }
                }
                //Everything the same
            } catch (java.lang.NumberFormatException e) {
                //Is not a number
                return false;
            }
        }
        return true;
    }

    public static boolean isPortAvaialble(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            LOG.log(Level.FINE, null, e);
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /*
                     * should not be thrown
                     */
                    LOG.log(Level.SEVERE, null, e);
                }
            }
        }

        return false;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
                source.close();
                destination.close();
        }
    }

    /**
     * Validate the form of an email address.
     *
     * <P>Return <tt>true</tt> only if <ul> <li> <tt>aEmailAddress</tt> can
     * successfully construct an {@link javax.mail.internet.InternetAddress}
     * <li> when parsed with "
     *
     * @" as delimiter, <tt>aEmailAddress</tt> contains two tokens which satisfy
     * {@link hirondelle.web4j.util.Util#textHasContent}. </ul>
     *
     * <P> The second condition arises since local email addresses, simply of
     * the form "<tt>albert</tt>", for example, are valid for
     * {@link javax.mail.internet.InternetAddress}, but almost always undesired.
     * @param aEmailAddress
     * @return
     */
    public static boolean isValidEmailAddress(String aEmailAddress) {
        if (aEmailAddress == null) {
            return false;
        }
        boolean result = true;
        try {
            InternetAddress internetAddress = new InternetAddress(aEmailAddress);
            if (!hasNameAndDomain(aEmailAddress)) {
                result = false;
            }
        } catch (AddressException ex) {
            LOG.log(Level.FINE, null, ex);
            result = false;
        }
        return result;
    }

    private static boolean hasNameAndDomain(String aEmailAddress) {
        String[] tokens = aEmailAddress.split("@");
        return tokens.length == 2
                && textHasContent(tokens[0])
                && textHasContent(tokens[1]);
    }

    /**
     * Return <tt>true</tt> only if <tt>aText</tt> is not null, and is not empty
     * after trimming. (Trimming removes both leading/trailing whitespace and
     * ASCII control characters.)
     *
     * <P> For checking argument validity, {@link Args#checkForContent} should
     * be used instead of this method.
     *
     * <P>This method is particularly useful, since it is very commonly
     * required.
     *
     * @param aText possibly-null.
     * @return true if has content
     */
    public static boolean textHasContent(String aText) {
        return (aText != null) && (aText.trim().length() > 0);
    }

    public static Dimension getImageDim(final String path) {
        Dimension result = null;
        String suffix = getFileSuffix(path);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if (iter.hasNext()) {
            ImageReader reader = iter.next();
            try {
                ImageInputStream stream = 
                        new FileImageInputStream(new File(path));
                reader.setInput(stream);
                int width = reader.getWidth(reader.getMinIndex());
                int height = reader.getHeight(reader.getMinIndex());
                result = new Dimension(width, height);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, null, e);
            } finally {
                reader.dispose();
            }
        }
        return result;
    }

    public static String getFileSuffix(final String path) {
        String result = null;
        if (path != null) {
            result = "";
            if (path.lastIndexOf('.') != -1) {
                result = path.substring(path.lastIndexOf('.'));
                if (result.startsWith(".")) {
                    result = result.substring(1);
                }
            }
        }
        return result;
    }

    public static void addDefaultAddAttributes(
            com.bluecubs.xinco.core.server.service.XincoCoreData data) {
        //add specific attributes
        data.getXincoAddAttributes().clear();
        XincoAddAttribute xaa;
        for (Iterator<XincoCoreDataTypeAttribute> it =
                data.getXincoCoreDataType().getXincoCoreDataTypeAttributes()
                .iterator(); it.hasNext();) {
            XincoCoreDataTypeAttribute attr = it.next();
            try {
                xaa = new XincoAddAttribute();
                xaa.setAttributeId(attr.getAttributeId());
                xaa.setAttribVarchar("");
                xaa.setAttribText("");
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
                xaa.setAttribDatetime(DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(calendar));
                data.getXincoAddAttributes().add(xaa);
            } catch (DatatypeConfigurationException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        LOG.log(Level.FINE,
                "Added {0} attributes!", data.getXincoAddAttributes().size());
    }
}