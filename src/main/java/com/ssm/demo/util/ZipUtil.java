package com.ssm.demo.util;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Properties;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

/**
 * 文件夹压缩，支持win和linux
 * @author wlzhang
 *
 */
public class ZipUtil
{
	/**
	 * @param inputFileName
	 *            输入一个文件夹
	 * @param zipFileName
	 *            输出一个压缩文件夹，打包后文件名字
	 * @throws Exception
	 */
	public static OutputStream zip(String inputFileName, String zipFileName) throws Exception
	{
		return zip(zipFileName, new File(inputFileName));
	}

	private static OutputStream zip(String zipFileName, File inputFile) throws Exception
	{
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		out.setEncoding("UTF-8");//解决linux乱码  根据linux系统的实际编码设置,可以使用命令 vi/etc/sysconfig/i18n 查看linux的系统编码
		zip(out, inputFile, "");
		out.close();
		return out;
	}

	private static void zip(ZipOutputStream out, File f, String base) throws Exception
	{
		if (f.isDirectory())
		{ // 判断是否为目录
			File[] fl = f.listFiles();
			// out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
			//            out.putNextEntry(new ZipEntry(base + "/"));
			ZipEntry zipEntry=new ZipEntry(base + System.getProperties().getProperty("file.separator"));
			zipEntry.setUnixMode(755);//解决linux乱码
			out.putNextEntry(zipEntry);
			//            base = base.length() == 0 ? "" : base + "/";
			base = base.length() == 0 ? "" : base + System.getProperties().getProperty("file.separator");
			for (int i = 0; i < fl.length; i++)
			{
				zip(out, fl[i], base + fl[i].getName());
			}
		}
		else
		{ // 压缩目录中的所有文件
			// out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
			ZipEntry zipEntry=new ZipEntry(base);
			zipEntry.setUnixMode(644);//解决linux乱码
			out.putNextEntry(zipEntry);
			FileInputStream in = new FileInputStream(f);
			int b;
			while ((b = in.read()) != -1)
			{
				out.write(b);
			}
			in.close();
		}
	}

	private static void unzip(String sourceZip,String destDir) throws Exception{  

		try{  

			Project p = new Project();  

			Expand e = new Expand();  

			e.setProject(p);  

			e.setSrc(new File(sourceZip));  

			e.setOverwrite(false);  

			e.setDest(new File(destDir));  

			/* 

            ant下的zip工具默认压缩编码为UTF-8编码， 

            而winRAR软件压缩是用的windows默认的GBK或者GB2312编码 

            所以解压缩时要制定编码格式 

			 */ 

			e.setEncoding("UTF-8");  //根据linux系统的实际编码设置

			e.execute();  

		}catch(Exception e){  

			throw e;  

		}  

	}  


	public static void zip2(String srcPathName, String zipFileName)  
	{  
		File file = new File(srcPathName);  
		File zipFile = new File(zipFileName);  
		if (!file.exists()) throw new RuntimeException(srcPathName + "不存在！");
		try  
		{  
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);  
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());  
			ZipOutputStream out = new ZipOutputStream(cos);  
			Properties pro=System.getProperties();
			String osName=pro.getProperty("os.name");
			if("Linux".equals(osName)||"linux".equals(osName)){
				out.setEncoding("GBK");//设置文件名编码方式
			}else{
				String property = System.getProperty("sun.jnu.encoding");
				out.setEncoding(property);//设置文件名编码方式
			}

			String basedir = "";  
			compress(file, out, basedir);  
			out.close();  
		}  
		catch (Exception e)  
		{  
			throw new RuntimeException(e);  
		}  
	}

	private static void compress(File file, ZipOutputStream out, String basedir)  
	{  
		/* 判断是目录还是文件 */  
		if (file.isDirectory())  
		{  
			// System.out.println("压缩：" + basedir + file.getName());  
			compressDirectory(file, out, basedir);  
		}  
		else  
		{  
			// System.out.println("压缩：" + basedir + file.getName());  
			compressFile(file, out, basedir);  
		}  
	}  

	/** 压缩一个目录 */  
	private static void compressDirectory(File dir, ZipOutputStream out, String basedir)  
	{  
		if (!dir.exists()) return;

		File[] files = dir.listFiles();  
		for (int i = 0; i < files.length; i++)  
		{  
			/* 递归 */  
			compress(files[i], out, basedir);  
		}  
	}  

	/** 压缩一个文件 */  
	private static void compressFile(File file, ZipOutputStream out, String basedir)  
	{  
		if (!file.exists())  
		{  
			return;  
		}  
		try  
		{  
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));  
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			Properties pro=System.getProperties();
			String osName=pro.getProperty("os.name");
			if("Linux".equals(osName)||"linux".equals(osName)){
				entry.setUnixMode(644);
			}
			out.putNextEntry(entry);  
			int count;  
			byte data[] = new byte[8192];  
			while ((count = bis.read(data, 0, 8192)) != -1)  
			{  
				out.write(data, 0, count);  
			}  
			bis.close();  
		}  
		catch (Exception e)  
		{  
			throw new RuntimeException(e);  
		}  
	}  



}