/* Copyright (C) 2003-2016 Patrick G. Durand
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
package com.plealog.genericapp.api.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import com.plealog.genericapp.api.log.EZLogger;

public class EZFileUtils {

  /**
   * Check whether or not a path terminates with a separator. If not, this
   * method returns a path ending with such separator.
   */
  public static String terminatePath(String path) {
    if (path == null || path.isEmpty())
      return path;
    if (path.charAt(path.length() - 1) == File.separatorChar)
      return path;
    return (path + File.separator);
  }

  /**
   * Check whether or not a path terminates with a separator. If not, this
   * method returns a path ending with such separator.
   */
  public static String terminateURL(String url) {
    if (url.charAt(url.length() - 1) == '/')
      return url;
    return (url + "/");
  }

  /**
   * Utility method to delete a file from one location
   */
  public static void deleteFile(String path) throws IOException {
    File f = null;
    f = new File(path);
    if (f.exists()) {
      f.delete();
    }
  }

  /**
   * Utilitary method to delete the directory corresponding to the given path
   * 
   * @param dir_path
   *          - the directory path
   * @return boolean - true if directory was succesfully deleted
   */
  static public boolean deleteDirectory(String dir_path) {
    return deleteDirectory(new File(dir_path));
  }

  /**
   * Utilitary method to delete the directory given as parameter
   * 
   * @param dir
   *          - the directory to delete
   * @return boolean - true if directory was succesfully deleted
   */
  public static boolean deleteDirectory(File dir) {
    if (dir.exists()) {
      File[] files = dir.listFiles();
      for (int i = 0; i < files.length; i++) {
        if (files[i].isDirectory()) {
          deleteDirectory(files[i]);
        } else {
          files[i].delete();
        }
      }
    }
    return (dir.delete());
  }

  /**
   * Binary copy of a file from source to target.
   */
  public static void copyFile(File source, File target) throws IOException {
    FileOutputStream fos = null;
    FileInputStream fis = null;
    BufferedInputStream bis;
    int n;
    byte[] buf = new byte[2048];

    try {
      fos = new FileOutputStream(target);
      fis = new FileInputStream(source);
      bis = new BufferedInputStream(fis);
      while ((n = bis.read(buf)) != -1) {
        fos.write(buf, 0, n);
      }
      fos.flush();
    } catch (IOException e) {
      throw e;
    } finally {
      try {
        if (fos != null)
          fos.close();
      } catch (Exception ex) {
      }
      try {
        if (fis != null)
          fis.close();
      } catch (Exception ex) {
      }
    }
  }

  /**
   * Returns the content of a file as text. This method should be used only with
   * text files.
   */
  public static String getFileContent(File file) throws IOException {
    BufferedReader in = null;
    StringBuffer szBuf;
    String line;

    try {
      in = new BufferedReader(new FileReader(file));
      szBuf = new StringBuffer();
      while ((line = in.readLine()) != null) {
        szBuf.append(line);
        szBuf.append("\n");
      }
    } catch (IOException e) {
      throw e;
    } finally {
      try {
        if (in != null)
          in.close();
      } catch (Exception ex) {
      }
    }
    return szBuf.toString();
  }

  /**
   * Returns the file name (without extension) given a absolute path. If path
   * denotes a directory, then the last element of the absolute path is
   * returned. It path denotes a file, then the last element of the absolute
   * path is returned but without its extension (if any). The file extension is
   * recognized by looking for the last occurrence of a dot. Note that the
   * method may return null if no file name can be found.
   */
  public static String getFileName(File path) {
    String fName;
    int idx;

    fName = path.getName();
    if (fName.length() == 0)
      return null;
    idx = fName.lastIndexOf('.');
    if (idx < 0)
      return fName;
    return fName.substring(0, idx);
  }

  /**
   * Forces a file name to terminate with a particular file extension.
   * 
   * @param f
   *          the file to check
   * @param ext
   *          the file extension (do not provide the prefix dot).
   */
  public static File forceFileExtension(File f, String ext) {
    String path;

    path = f.getAbsolutePath();
    if (path.endsWith(ext) == false)
      path += "." + ext;
    return new File(path);
  }

  /**
   * Uncompress a gzipped file. A file is considered gzipped if its extension
   * matches '.gz'. If it is not true, then the zipFile param is returned as is.
   * 
   * @param gzFile the gzipped file
   * 
   * @return the uncompressed file path or null if failure.
   */
  public static String gunzipFile(String gzFile) {
    String zipname, source;
    byte[] buffer;
    int length, bufSize = 8192;

    if (gzFile.endsWith(".gz")) {
      zipname = gzFile;
      source = gzFile.substring(0, gzFile.length() - 3);
    } else {
      return gzFile;
    }

    try (GZIPInputStream zipin = new GZIPInputStream(new FileInputStream(zipname));) {
      buffer = new byte[bufSize];
      try (FileOutputStream out = new FileOutputStream(source);) {
        while ((length = zipin.read(buffer, 0, bufSize)) != -1) {
          out.write(buffer, 0, length);
        }
        out.flush();
      } catch (IOException e) {
        EZLogger.warn("Couldn't decompress " + gzFile + ".");
        source = null;
      }

    } catch (IOException e) {
      EZLogger.warn("Couldn't open " + zipname + ".");
      return null;
    }

    return source;
  }

  /**
   * Uncompress a gzipped file. 
   * 
   * @param gzFile the gzipped file
   * @param ungzFile where to uncompress the gzipped file
   * 
   * @return false if failure, true otherwise.
   */
  public static boolean gunzipFile(String gzFile, String ungzFile) {
    byte[] buffer;
    int length, bufSize = 8192;

    try (GZIPInputStream zipin = new GZIPInputStream(new FileInputStream(gzFile));) {
      buffer = new byte[bufSize];
      try (FileOutputStream out = new FileOutputStream(ungzFile);) {
        while ((length = zipin.read(buffer, 0, bufSize)) != -1) {
          out.write(buffer, 0, length);
        }
        out.flush();
      } catch (IOException e) {
        EZLogger.warn("Couldn't decompress " + ungzFile + ".");
        return false;
      }
    } catch (IOException e) {
      EZLogger.warn("Couldn't open " + gzFile + ".");
      return false;
    }
    return true;
  }
}
