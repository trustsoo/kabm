package com.kabm.svl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.IOFileUploadException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.io.FileDeleteStrategy;
import org.imgscalr.Scalr;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kabm.util.NetworkUtil;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.http.WebController;
import jdf.framework.core.log.Logger;
import jdf.framework.core.util.DateTime;
import jdf.framework.core.util.SmartStringArray;
import jdf.framework.core.util.Utility;

public class UploadServlet extends HttpServlet implements Servlet {
    private static final long serialVersionUID = 8662476226643944154L;

    Config conf = null;
    String tempPath = null;
    String charEncoder = null;
    String dir = null;
    String publicDir = null;
    String publicUrl = null;
    int maxUploadFileKilloBytes = 0;
    String excludExtNames = null;
    String includExtNames = null;


    private final String CLASS_NAME = "<at:UploadServlet> ";

    public UploadServlet() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        
        try {
            conf = Configuration.lookup("/MultipartAttributes");
            charEncoder = conf.getString("encoding");
            tempPath = conf.getString("tempPath");
            File tempPaths = new File(tempPath);
            if (!tempPaths.exists()){
                tempPaths.mkdirs();
            }
            dir = conf.getString("dir");
            
            publicDir = conf.getString("publicDir");
            publicUrl = conf.getString("publicUrl");
            maxUploadFileKilloBytes = conf.getInt("maxUploadFileKilloBytes");
            excludExtNames = conf.getString("excludExtNames");
            includExtNames = conf.getString("includExtNames");
        } catch (ConfigurationException ce) {
            Logger.warn.println(CLASS_NAME + " config.xml read fail." + Utility.getStackTrace(ce));
        }

    }

    public void destroy() {
        super.destroy();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        if (request.getParameter("getfile") != null && !request.getParameter("getfile").isEmpty()) {
            File file =
                    new File(request.getServletContext().getRealPath("/") + "repository/"
                            + request.getParameter("getfile"));
            if (file.exists()) {
                int bytes = 0;
                ServletOutputStream op = response.getOutputStream();

                response.setContentType(getMimeType(file));
                response.setContentLength((int) file.length());
                response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

                byte[] bbuf = new byte[1024];
                DataInputStream in = new DataInputStream(new FileInputStream(file));

                while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
                    op.write(bbuf, 0, bytes);
                }

                in.close();
                op.flush();
                op.close();
            }
        } else if (request.getParameter("delfile") != null && !request.getParameter("delfile").isEmpty()) {
            doDelete(request, response);
        } else if (request.getParameter("getthumb") != null && !request.getParameter("getthumb").isEmpty()) {
            File file =
                    new File(request.getServletContext().getRealPath("/") + "repository/"
                            + request.getParameter("getthumb"));
            if (file.exists()) {
                String mimetype = getMimeType(file);
                if (mimetype.endsWith("png") || mimetype.endsWith("jpeg") || mimetype.endsWith("jpg")
                        || mimetype.endsWith("gif")) {
                    BufferedImage im = ImageIO.read(file);
                    if (im != null) {
                        BufferedImage thumb = Scalr.resize(im, 75);
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        if (mimetype.endsWith("png")) {
                            ImageIO.write(thumb, "PNG", os);
                            response.setContentType("image/png");
                        } else if (mimetype.endsWith("jpeg")) {
                            ImageIO.write(thumb, "jpg", os);
                            response.setContentType("image/jpeg");
                        } else if (mimetype.endsWith("jpg")) {
                            ImageIO.write(thumb, "jpg", os);
                            response.setContentType("image/jpeg");
                        } else {
                            ImageIO.write(thumb, "GIF", os);
                            response.setContentType("image/gif");
                        }
                        ServletOutputStream srvos = response.getOutputStream();
                        response.setContentLength(os.size());
                        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
                        os.writeTo(srvos);
                        srvos.flush();
                        srvos.close();
                    }
                }
            }
        } else {
            PrintWriter writer = response.getWriter();
            writer.write("call POST with multipart form data");
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new IllegalArgumentException(
                    "Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }
        
        DiskFileItemFactory factory = null;
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        response.setContentType("application/json");
        try {
            jsonObject.put("files", jsonArray);

            factory = new DiskFileItemFactory();
            factory.setSizeThreshold(20 * 1024 * 1024);
            factory.setRepository(new File(tempPath));
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding(charEncoder);
            //upload.setSizeMax(maxUploadFileKilloBytes * 1024);
            upload.setSizeMax(4513291179L);

            /*Upload progress monitor....*/
            FileUploadProgressListener listener = new FileUploadProgressListener();
            listener.setClientIP(NetworkUtil.getRemoteAddr(request));
            upload.setProgressListener(listener);

            DeleteFilesOnEndUploadCleaningTracker tracker = new DeleteFilesOnEndUploadCleaningTracker();
            factory.setFileCleaningTracker(tracker);

            List<FileItem> items = upload.parseRequest(request);

            Iterator<FileItem> iter = items.iterator();
            FileItem fileItem = null;
            String orgfileName = null;
            String extName = "";
            long fileSize = 0;
            String fileName = null;
            
            
            File uploadedFile = null;
            String absDir = null;
            String currMM = DateTime.getFormatString("yyyyMM");
            String publicUrl = this.publicUrl + "/" + currMM + "/";
            File _dir = new File(dir + java.io.File.separator + currMM);
            if (!_dir.exists()) {
                _dir.mkdirs();
            }
            absDir = _dir.getAbsoluteFile().toString();

            while (iter.hasNext()) {
                fileItem = iter.next();
                if (!fileItem.isFormField()) {
                    JSONObject fileItemObject = new JSONObject();
                    jsonArray.put(fileItemObject);

                    orgfileName = fileItem.getName();
                    extName = getSuffix(orgfileName).toLowerCase();
                    fileSize = fileItem.getSize();
                    
                    if (!isAllowFile(excludExtNames, includExtNames, extName)) {
                        tracker.deleteTemporaryFiles();
                        fileItemObject.put("name", orgfileName);
                        fileItemObject.put("size", fileSize);
                        fileItemObject.put("error", "Filetype not allowed");
                    } else {
                        fileName = UUID.randomUUID().toString() + "." + extName;
                        
                        uploadedFile = new File(_dir.getAbsoluteFile(), fileName);
                        fileItem.write(uploadedFile);
                        fileItem.delete();
                        fileSize = (int) uploadedFile.length();
                        Logger.info.println(CLASS_NAME + uploadedFile.getAbsolutePath() + " upload complete.");
                        
                        //String encrypt_key = SitePropertyManager.getString("COOKIE_USER_ID_KEY");
                        //byte[] encryptValue = CipherUtil.encode(encrypt_key.getBytes(), fileName.getBytes());
                        //String encFileName = CipherUtil.hexToString(encryptValue);

                        orgfileName = orgfileName.replaceAll(",", "_");
                        
                        //for jQuery File Upload
                        fileItemObject.put("name", orgfileName);
                        fileItemObject.put("size", fileSize);
                        fileItemObject.put("url", "/upload?getfile=" + fileName);
                        //fileItemObject.put("thumbnailUrl", "/upload?getthumb=" + encFileName);
                        fileItemObject.put("deleteUrl", "/upload?delfile=" + fileName);
                        fileItemObject.put("deleteType", "DELETE");
                        
                        //for Ubicus File Upload
                        fileItemObject.put("file_nm", fileName);
                        fileItemObject.put("file_path", currMM);
                        fileItemObject.put("file_size", fileSize);//증복 size
                        fileItemObject.put("ext_nm", extName);
                        fileItemObject.put("user_file_nm", orgfileName);//중복 name
                        
                        doInsTmpl(request, response, orgfileName, fileSize, fileName, extName, publicUrl, absDir);
                        
                        Logger.info.println(CLASS_NAME + jsonObject.toString() );
                    }
                }
            }
        } catch (IOFileUploadException e) {
            Logger.warn.println("File Uploade request canceled");
        } catch (FileUploadException e) {
            Logger.err.println("File Uploade request fail", e);
        } catch (Exception e) {
            Logger.err.println("File Uploade request fail", e);
        } finally {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), charEncoder));
            writer.write(jsonObject.toString());
            writer.close();
        }
    }
    
    /**
     * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        request.setCharacterEncoding("UTF-8");
        
        JSONObject fileObject = new JSONObject();
        JSONArray fileArray = new JSONArray();
        
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        try {
            fileObject.put("files", fileArray);
            String[] delfiles = request.getParameterValues("delfile");
            if (delfiles != null) {
                for (int i = 0, ic = delfiles.length; i < ic; i++) {
                    File file = new File(request.getServletContext().getRealPath("/") + "repository/" + delfiles[i]);
                    String fileName = file.getName();
                    JSONObject fileJSON = new JSONObject();
                    if (file.exists()) {
                        fileJSON.put(fileName, file.delete());
                    } else {
                        fileJSON.put(fileName, false);
                    }
                    fileArray.put(fileJSON);
                }
            }
        } catch (JSONException e) {
            Logger.err.println("UploadServlet doDelete Error : " + Utility.getStackTrace(e));
        } finally {
            writer.print(fileObject.toString());
            writer.close();
        }
    }

    private boolean isAllowFile(String excludExtNames, String includExtnames, String extName) {
        String[] excludExtNamesArray = SmartStringArray.split(",", excludExtNames);
        String[] includExtNamesArray = SmartStringArray.split(",", includExtnames);

        List<String> list = null;

        if (excludExtNamesArray.length > 0) {
            list = Arrays.asList(excludExtNamesArray);
            return !list.contains(extName);
        } else {
            list = Arrays.asList(includExtNamesArray);
            return list.contains(extName);
        }
    }

    private String doInsTmpl(HttpServletRequest request, HttpServletResponse response, String orgFileName,
            long fileSize, String fileName, String ext_name, String publicUrl, String fileDir) {
        jdf.framework.core.data.DataSet input = new jdf.framework.core.data.DataSet();
        jdf.framework.core.data.DataSet output = new jdf.framework.core.data.DataSet();
        WebController.bind(request, input);
        InteractionBean interact = new InteractionBean();
        String val = null;

        try {
            input.put("cmd", "createTmpl");
            input.put("file_nm", fileName);
            input.put("file_path", fileDir);
            input.put("file_size", fileSize);
            input.put("ext_nm", ext_name);

            try {
                orgFileName = orgFileName.replaceAll(",", "_");
            } catch (Exception ex) {
                Logger.warn.println(CLASS_NAME + ex.toString());
            }

            input.put("user_file_nm", orgFileName);

            output = interact.execute("common/Attach", input);
            val = output.getText("file_no");
        } catch (ResourceException ex) {
            Logger.err.print(Utility.getStackTrace(ex));
        }
        Logger.info.println("file_no: " + val);
        return val;
    }



    private String getMimeType(File file) {
        String mimetype = "";
        if (file.exists()) {
            String ext = getSuffix(file.getName());
            if (ext.equalsIgnoreCase("png")) {
                mimetype = "image/png";
            } else if (ext.equalsIgnoreCase("jpg")) {
                mimetype = "image/jpg";
            } else if (ext.equalsIgnoreCase("jpeg")) {
                mimetype = "image/jpeg";
            } else if (ext.equalsIgnoreCase("gif")) {
                mimetype = "image/gif";
            } else {
                javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
                mimetype = mtMap.getContentType(file);
            }
        }
        return mimetype;
    }

    private String getSuffix(String filename) {
        String suffix = "";
        int pos = filename.lastIndexOf('.');
        if (pos > 0 && pos < filename.length() - 1) {
            suffix = filename.substring(pos + 1);
        }
        return suffix;
    }
    
    private class FileUploadProgressListener implements ProgressListener {
        private String ip = null;
        private long megaBytes = -1;

        public void setClientIP(String ip) {
            this.ip = ip;
        }

        public void update(long pBytesRead, long pContentLength, int pItems) {
            long mBytes = pBytesRead / 1000000;
            if (megaBytes == mBytes) {
                return;
            }
            megaBytes = mBytes;

            //Logger.info.println(CLASS_NAME + ip + " reading item " + pItems);
            if (pContentLength == -1) {
                Logger.info.println(CLASS_NAME + ip + " " + pBytesRead + " bytes have been read.");
            } else {
                //Logger.debug.println(CLASS_NAME + ip + "(" + pItems + "): " + pBytesRead + " of " + pContentLength);
            }
        }
    }

    private class DeleteFilesOnEndUploadCleaningTracker extends FileCleaningTracker {
        private List<String> filesToDelete = new ArrayList();

        public void deleteTemporaryFiles() {
            for (String file : filesToDelete) {
                Logger.debug.println("<DeleteFilesOnEndUploadCleaningTracker>" + file);
                new File(file).delete();
            }
            filesToDelete.clear();
        }

        @Override
        public synchronized void exitWhenFinished() {
            deleteTemporaryFiles();
        }

        @Override
        public int getTrackCount() {
            return filesToDelete.size();
        }

        @Override
        public void track(File file, Object marker) {
            filesToDelete.add(file.getAbsolutePath());
        }

        @Override
        public void track(File file, Object marker, FileDeleteStrategy deleteStrategy) {
            filesToDelete.add(file.getAbsolutePath());
        }

        @Override
        public void track(String path, Object marker) {
            filesToDelete.add(path);
        }

        @Override
        public void track(String path, Object marker, FileDeleteStrategy deleteStrategy) {
            filesToDelete.add(path);
        }
    }
}
