/**
 * 
 */
package archiver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
/**
 * This class contains a number of methods for manipulating zip archives (such as creating and unpacking an so one)
 * @version 1.0  
 * @author Tsanda Artyom
 *
 */
public class Archiver {
	/**
	 * Adds to already existing zip archive new files
	 * @param zipfilename
	 * @param filenames
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void addToZipArchive(String zipfilename, String[] filenames)
			throws IllegalArgumentException, IOException {
		this.addToZipArchive(zipfilename, filenames, null);
	}

	/**
	 * Adds to already existing zip archive new comment<br/>
	 * Old comment will be deleted after executing
	 * @param zipfilename
	 * @param comment
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void addCommentToZipArchive(String zipfilename, String comment)
			throws IllegalArgumentException, IOException {
		this.addToZipArchive(zipfilename, null, comment);
	}

	/**
	 * Adds to already existing zip archive new files and sets new comment<br/>
	 * In case of null comment function just adds to already existing zip archive new files.
	 * @param zipfilename
	 * @param filenames
	 * @param comment
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void addToZipArchive(String zipfilename, String[] filenames,
			String comment) throws IOException, IllegalArgumentException {
		// checking arguments
		if (zipfilename == null)
			throw new IllegalArgumentException("Invalid name of file to unzip");

		ZipFile sourcezipfile = new ZipFile(zipfilename);
		Enumeration<?> enu = sourcezipfile.entries();

		File destzipfile = new File("tmp" + zipfilename);
		try (FileOutputStream fos = new FileOutputStream(destzipfile);
				ZipOutputStream zos = new ZipOutputStream(fos)) {
			// coping soursezip to new destzip
			while (enu.hasMoreElements()) {
				ZipEntry zipentry = (ZipEntry) enu.nextElement();

				try (InputStream is = sourcezipfile.getInputStream(zipentry)) {
					zos.putNextEntry(zipentry);

					byte[] buffer = new byte[1024];
					int readbytes;
					while ((readbytes = is.read(buffer)) >= 0)
						zos.write(buffer, 0, readbytes);
				}
			}
			// addition new files to destzip
			if (filenames != null) {
				for (String fn : filenames) {
					File filetozip = new File(fn);
					if (filetozip.isDirectory())
						addToZipDirectory(zos, filetozip);
					else
						addToZipFile(zos, filetozip);
				}
			}

			if (comment != null)
				zos.setComment(comment);

		} finally {
			sourcezipfile.close();
		}
		File todeletezip = new File(zipfilename);
		todeletezip.delete();
		destzipfile.renameTo(new File(zipfilename));
	}

	/**
	 * Unpacks zip archive to selected directory<br/>
	 * In case of null directory unpacks into current directory.<br/>
	 * In case of names conflict unpacked files remains in directory. 
	 * @param zipfilename
	 * @param dir
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void unpackZipArchive(String zipfilename, String dir)
			throws IOException, IllegalArgumentException {
		// checking arguments
		if (zipfilename == null)
			throw new IllegalArgumentException("Invalid name of file to unzip");

		ZipFile zipfile = new ZipFile(zipfilename);
		Enumeration<?> enu = zipfile.entries();

		while (enu.hasMoreElements()) {
			ZipEntry zipentry = (ZipEntry) enu.nextElement();
			
			String name = zipentry.getName();
			if(name.startsWith(File.separator))
				name = name.substring(1);
			
			File file = (dir == null) ? new File(name) : new File(dir + "/"
					+ name);
			
			if (file.isDirectory()) {
				file.mkdirs();
				continue;
			}

			File parent = file.getParentFile();
			if (parent != null && !parent.exists())
				parent.mkdirs();

			if (file.exists()) {
				zipfile.close();
				throw new IllegalArgumentException("Namespace conflict");
			}

			try (InputStream is = zipfile.getInputStream(zipentry);
					FileOutputStream fos = new FileOutputStream(
							file.getAbsoluteFile())) {

				byte[] buffer = new byte[1024];
				int readbytes;
				while ((readbytes = is.read(buffer)) >= 0)
					fos.write(buffer, 0, readbytes);

				buffer = null;
			}

		}
		zipfile.close();
	}

	/**
	 * Creates new zip archive with given name and comment.<br/>
	 * In case of null comment just creates new zip archive with given name.
	 * @param zipfilename
	 * @param filenames
	 * @param comment
	 * @throws IOException
	 * @throws IllegalArgumentException in case of null arguments or attemt to create empty archieve
	 */
	public void createZipArchive(String zipfilename, String[] filenames, String comment) 
			throws IOException, IllegalArgumentException {
		// checking arguments
		if (filenames == null || filenames.length == 0)
			throw new IllegalArgumentException(
					"Can't create empty zip archieve");
		if (zipfilename == null)
			throw new IllegalArgumentException("Invalid name of file to zip");
		if (!zipfilename.endsWith(".zip")) 
			zipfilename += ".zip";

		File zipfile = new File(zipfilename);
		try (FileOutputStream fos = new FileOutputStream(zipfile);
				ZipOutputStream zos = new ZipOutputStream(fos);) {

			if (comment != null)
				zos.setComment(comment);

			for (String fn : filenames) {
				File filetozip = new File(fn);
				if (filetozip.isDirectory())
					addToZipDirectory(zos, filetozip);
				else
					addToZipFile(zos, filetozip);
			}
		}

		ZipFile tmp = new ZipFile(zipfilename);
		if (!tmp.entries().hasMoreElements()) {
			tmp.close();
			throw new IllegalArgumentException(
					"Can't create empty zip archieve");
		}
		tmp.close();
	}

	/**
	 * Creates new zip archive with given name.
	 * @param zipfilename
	 * @param filenames
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void createZipArchive(String zipfilename, String[] filenames)
			throws IOException, IllegalArgumentException {
		this.createZipArchive(zipfilename, filenames, null);
	}

	/**
	 * Outputs in the console comment to the given zip archive.<br/>
	 * If there is no comment, "No comment" will be printed.
	 * @param zipfilename
	 * @throws IOException
	 */
	public void printComment(String zipfilename) throws IOException {
		ZipFile zipfile = new ZipFile(zipfilename);
		String comment = zipfile.getComment();
		if (comment == null)
			System.out.println("No comment");
		else
			System.out.println(comment);
		zipfile.close();
	}

	/**
	 * Adds file to zip archive through its stream. 
	 * @param zos
	 * @param filetozip
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	private void addToZipFile(ZipOutputStream zos, File filetozip)
			throws IOException, IllegalArgumentException {
		// checking arguments
		if (filetozip == null)
			throw new IllegalArgumentException("Invalid file to zip");
		if (zos == null)
			throw new IllegalArgumentException(
					"Zip file was closed or can't be created");

		try (FileInputStream fis = new FileInputStream(filetozip)) {

			ZipEntry entry = new ZipEntry(filetozip.getName());
			zos.putNextEntry(entry);

			byte[] buffer = new byte[1024];
			int readbytes;
			while ((readbytes = fis.read(buffer)) >= 0)
				zos.write(buffer, 0, readbytes);

			buffer = null;
		}
	}

	/**
	 * Adds directory to zip archive through its stream. 
	 * @param zos
	 * @param directorytozip
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	private void addToZipDirectory(ZipOutputStream zos, File directorytozip)
			throws IOException, IllegalArgumentException {
		// checking arguments
		if (directorytozip == null)
			throw new IllegalArgumentException("Invalid directory to zip");
		if (zos == null)
			throw new IllegalArgumentException(
					"Zip file was closed or can't be created");

		List<File> fileList = new ArrayList<File>();
		addAllFiles(directorytozip, fileList);

		for (File file : fileList) {
			try (FileInputStream fis = new FileInputStream(file)) {
				// make zipEntry's path relative path
				String zipfilepath = "/"
						+ directorytozip.getName()
						+ "/"
						+ directorytozip.toURI().relativize(file.toURI())
								.getPath();

				ZipEntry zipEntry = new ZipEntry(zipfilepath);
				zos.putNextEntry(zipEntry);

				byte[] buffer = new byte[1024];
				int readbytes;
				while ((readbytes = fis.read(buffer)) >= 0)
					zos.write(buffer, 0, readbytes);

				buffer = null;
			}
		}
	}

	/**
	 * Puts all files which directory contains into given list.
	 * @param dir
	 * @param filelist
	 */
	private void addAllFiles(File dir, List<File> filelist) {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				addAllFiles(file, filelist);
			} else {
				filelist.add(file);
			}
		}
	}
	
	/**
	 * Outputs in the console help list to the program .
	 */
	public void printHelp() {
		System.out.println("");
		System.out.println("==================================================ARCHIVER_HELP=================================================================");
		System.out.println("Command list:");
		System.out.println("To archive files or folders:			{c|create}   archive_name 	[file|folder] [file|folder] ...");
		System.out.println("To archive files or folders and add comment: 	{c|create}   archive_name 	[file|folder] [file|folder] ... -c \"Commenttext\"");
		System.out.println("To unpack archive: 				{u|unpack}   [directory]");
		System.out.println("To add files to existing archieve:		{a|add}      archieve_name 	[file|folder] [file|folder] ...");
		System.out.println("To get archive comment:				{cm|comment} -g  		archieve_name ");
		System.out.println("To set archive comment:				{cm|comment} -s  		archieve_name 	\"Comment\"");
		System.out.println("================================================================================================================================");
	}
	
	public static void main(String[] args){
		Archiver a = new Archiver();
		try {
			if ((args[0].equals("c") || args[0].equals("create"))
					&& (args[args.length - 2].equals("-c") || args[args.length - 2].equals("-с"))) {
				String[] filelist = new String[args.length - 4];
				System.arraycopy(args, 2, filelist, 0, filelist.length);
				a.createZipArchive(args[1], filelist, args[args.length - 1]);
				return;
			} else if (args[0].equals("c") || args[0].equals("create")) {
				String[] filelist = new String[args.length - 2];
				System.arraycopy(args, 2, filelist, 0, filelist.length);
				a.createZipArchive(args[1], filelist);
				return;
			} else if (args[0].equals("u") || args[0].equals("unpack")) {
				if(args.length == 3)
					a.unpackZipArchive(args[1], args[2]);
				else
					a.unpackZipArchive(args[1], null);
				return;
			} else if (args[0].equals("a") || args[0].equals("add")) {
				String[] filelist = new String[args.length - 2];
				System.arraycopy(args, 2, filelist, 0, filelist.length);
				a.addToZipArchive(args[1], filelist);
				return;
			} else if ((args[0].equals("cm") || args[0].equals("comment"))
					&& Arrays.binarySearch(args, "-g") == 1) {
				a.printComment(args[2]);
				return;
			} else if ((args[0].equals("cm") || args[0].equals("comment"))
					&& Arrays.binarySearch(args, "-s") == 1) {
				a.addCommentToZipArchive(args[2], args[3]);
				return;
			} else {
				a.printHelp();
				return;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			a.printHelp();
		}
		
	}
}