import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


public class TakeFileServlet extends HttpServlet {

    static final int fileMaxSize = 500000 * 1024;
    static final int memMaxSize = 500 * 1024;
    static int lastByte;
    ZipOutputStream zout = new ZipOutputStream(new FileOutputStream("/home/maxvkamkin/prog/java/javapro/PRO3/src/main/webapp/output.zip"));



    public TakeFileServlet() throws FileNotFoundException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        PrintWriter writer = response.getWriter();


        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        String filePath = "/home/maxvkamkin/test/test2/";
        diskFileItemFactory.setRepository(new File(filePath));
        diskFileItemFactory.setSizeThreshold(memMaxSize);

        ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);
        upload.setSizeMax(fileMaxSize);



        try {
            List<FileItem> fileItems = upload.parseRequest(request);

            Iterator<FileItem> iterator = fileItems.iterator();

            writer.println("<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<title>" +
                    "TakeFileServlet" +
                    "</title>" +
                    "</head>" +
                    "<body>");

            while (iterator.hasNext()) {
                FileItem fileItem = iterator.next();
                if (!fileItem.isFormField()) {

                    String fileName = fileItem.getName();

                    if (fileName.equals(""))
                        continue;

                    File file = new File(filePath + fileName);

                    fileItem.write(file);
                    zip(filePath, fileName, zout);
                    file.delete();


                }
            }
            zout.close();
            writer.println("<h1><a href=\"output.zip\">Take your arch.</a></h1>");
            writer.println("</body>" +
                    "</html>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void zip(String path, String name, ZipOutputStream zout) {
        String filename = path + name;
        try
        {
            File f = new File(filename);
            zout.putNextEntry(new ZipEntry(name));
            write(new FileInputStream(f), zout);
        }
        catch(Exception ex){

            System.out.println(ex.getMessage());
        }
    }

    private void write(InputStream in, ZipOutputStream zout) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0)
            zout.write(buffer, 0, len);
        in.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
