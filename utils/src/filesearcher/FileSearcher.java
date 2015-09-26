package filesearcher;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility makes a search for given part of file name and outputs results to
 * console
 * 
 * @author Artyom Tsanda
 * @version 1.0
 */
public class FileSearcher {
	/**
	 * Makes deep file search by given part of file name
	 * 
	 * @param fileNamePart
	 * @return File[]
	 */
	public File[] serchFiles(final String fileNamePart) {

		File curDir = new File(System.getProperty("user.dir"));

		List<String> fileList = new LinkedList<String>();

		FilenameFilter fileNameFilter;
		if (fileNamePart.contains("*") || fileNamePart.contains("*")) {

			fileNameFilter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.matches(fileNamePart.replace("?", ".?")
							.replace("*", ".*?"));
				}
			};

		} else {

			fileNameFilter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.contains(fileNamePart);
				}
			};
		}

		this.addAllMatchingFileNames(fileList, curDir, fileNamePart,
				fileNameFilter);

		File[] retFiles = new File[fileList.size()];
		int i = 0;
		for (String tmpFileName : fileList) {
			retFiles[i] = new File(tmpFileName);
			++i;
		}

		return retFiles;
	}

	/**
	 * Adds all files which satisfy given filter from current and sub-
	 * directories to given List
	 * 
	 * @param fileList
	 * @param dir
	 * @param fileNamePart
	 * @param fileNameFilter
	 */
	private void addAllMatchingFileNames(List<String> fileList, File dir,
			final String fileNamePart, FilenameFilter fileNameFilter) {

		File[] allFiles = dir.listFiles();

		this.addMatchingFileNames(fileList, dir, fileNamePart, fileNameFilter);

		for (File tmpFile : allFiles)
			if (tmpFile.isDirectory())
				this.addAllMatchingFileNames(fileList, tmpFile, fileNamePart,
						fileNameFilter);
	}

	/**
	 * Adds all files which satisfy given filter from current directory to given
	 * List
	 * 
	 * @param fileList
	 * @param dir
	 * @param fileNamePart
	 * @param fileNameFilter
	 */
	private void addMatchingFileNames(List<String> fileList, File dir,
			final String fileNamePart, FilenameFilter fileNameFilter) {

		String[] fileNames = dir.list(fileNameFilter);

		for (String tmpFileName : fileNames)
			fileList.add(dir.getPath() + File.separator + tmpFileName);
	}

	/**
	 * Prints to console information about found files<br/>
	 * Basic information: Dir/File, Name, Path<br/>
	 * Could be added: Size, Last change, isHidden, Access rights
	 * 
	 * @param files
	 * @param additionalInf
	 */
	public void printFileInformation(File[] files, String additionalInf) {

		if (files.length == 0) {
			System.out.println("No mathces found");
			return;
		}

		String format = "|%-8s|%-40s|%-49s|";
		boolean expanded = false;
		boolean nameFieldExpanded = false;
		boolean pathFieldExpanded = false;
		System.out.format("|%-8s|%-40s|%-49s|", "Dir/File", "Name", "Path");

		boolean sizeFieldFlag = false;
		if (additionalInf != null && additionalInf.contains("s")) {
			sizeFieldFlag = true;
			format += "%-10s|";
			System.out.format("%-10s|", "Size,bytes");
		}

		boolean dateFieldFlag = false;
		if (additionalInf != null && additionalInf.contains("d")) {
			dateFieldFlag = true;
			format += "%-16s|";
			System.out.format("%-16s|", "Last change");
		}

		boolean isHiddenFieldFlag = false;
		if (additionalInf != null && additionalInf.contains("h")) {
			isHiddenFieldFlag = true;
			format += "%-10s|";
			System.out.format("%-10s|", "isHidden");
		}

		boolean accesFieldFlag = false;
		if (additionalInf != null && additionalInf.contains("a")) {
			accesFieldFlag = true;
			format += "%-10s|";
			System.out.format("%-10s|", "Access");
		}

		System.out.println("");

		for (File tmpFile : files) {
			expanded = false;
			nameFieldExpanded = false;
			pathFieldExpanded = false;

			String name;
			if (tmpFile.getName().length() <= 40) {
				name = tmpFile.getName();
			} else {
				expanded = true;
				nameFieldExpanded = true;
				name = tmpFile.getName().substring(0, 40);
			}

			String path;
			if (tmpFile.getParent().length() <= 49) {
				path = tmpFile.getParent();
			} else {
				expanded = true;
				pathFieldExpanded = true;
				path = tmpFile.getParent().substring(0, 49);
			}
			// standart output
			System.out.format("|%-8s|%-40s|%-49s|",
					this.getDirOrFileStatus(tmpFile), name, path);

			if (sizeFieldFlag)
				System.out.format("%-10s|", tmpFile.length());
			if (dateFieldFlag) {
				Date d = new Date(tmpFile.lastModified());
				SimpleDateFormat dateformat = new SimpleDateFormat(
						"dd.MM.yyyy hh:mm");
				System.out.format("%-16s|", dateformat.format(d));
			}
			if (isHiddenFieldFlag)
				System.out.format("%-10s|", tmpFile.isHidden());
			if (accesFieldFlag)
				System.out.format("%-10s|", this.getAccessStatus(tmpFile));

			System.out.println("");

			if (expanded) {

				String[] tmp = new String[format.lastIndexOf('s') + 1];
				for (int i = 0; i < tmp.length; i++)
					tmp[i] = "";

				String nameToPrint = tmp[1];
				if (nameFieldExpanded)
					nameToPrint = tmpFile.getName().substring(40);

				String pathToPrint = tmp[2];
				if (pathFieldExpanded)
					pathToPrint = tmpFile.getPath().substring(49);

				while (nameFieldExpanded || pathFieldExpanded) {
					if (nameFieldExpanded) {
						if (nameToPrint.length() <= 40) {
							tmp[1] = nameToPrint;
							System.out.format(format, tmp);
							nameFieldExpanded = false;
						} else {
							tmp[1] = nameToPrint.substring(0, 40);
							System.out.format(format, tmp);
							nameToPrint = nameToPrint.substring(40);
						}
					}
					if (pathFieldExpanded) {
						if (pathToPrint.length() <= 40) {
							tmp[2] = pathToPrint;
							System.out.format(format, tmp);
							pathFieldExpanded = false;
						} else {
							tmp[2] = pathToPrint.substring(0, 40);
							System.out.format(format, tmp);
							pathToPrint = pathToPrint.substring(40);
						}
					}
					System.out.println("");
				}
			}
		}
	}

	/**
	 * Returns whether File directory or file(traditional)
	 * 
	 * @param File
	 * @return String
	 */
	private String getDirOrFileStatus(File f) {
		return (f.isDirectory()) ? "dir" : "file";
	}

	/**
	 * Returns access rights to given file<br/>
	 * w - could be written<br/>
	 * r - could be read<br/>
	 * - no rights
	 * 
	 * @param File
	 * @return
	 */
	private String getAccessStatus(File f) {
		String ret;
		ret = (f.canRead()) ? "r" : "-";
		ret += (f.canWrite()) ? "w" : "-";
		return ret;
	}

	public void printHelp() {
		System.out.println("------------------------------------HELP-------------------------------------");
		System.out.println("[FileNamePart] -[modifiers] ... - command to search files");
		System.out.println("By default programm prints whether File directory or not, name and path");
		System.out.println("These modifiers could be used to get more information: ");
		System.out.format("%-10s - %-45s\n", "-s","prints size of file in bytes");
		System.out.format("%-10s - %-45s\n", "-d","prints date of last change");
		System.out.format("%-10s - %-45s\n", "-h","prints whether file is hidden or not");
		System.out.format("%-10s - %-45s\n", "-a","prints access rights to the given file");
		System.out.format("%-10s - %-45s\n", "-v", "equals to -s -d -h -a -v");
	}

	public static void main(String[] args) {
		FileSearcher a = new FileSearcher();
		if (args.length == 0) {
			a.printHelp();
			return;
		}
		String flags = "";
		List<String> argsList = Arrays.asList(args);
		try {
			boolean SIZEFLAG = argsList.contains("-s");
			boolean DATEFLAG = argsList.contains("-d");
			boolean ISHIDDENFLAG = argsList.contains("-h");
			boolean ACCESSFLAG = argsList.contains("-a");
			boolean VERBOSEFLAG = argsList.contains("-v");
			if (argsList.contains("-help")) {
				a.printHelp();
				return;
			}
			if (SIZEFLAG) { // size
				flags += "s";
			}
			if (DATEFLAG) { // date
				flags += "d";
			}
			if (ISHIDDENFLAG) { // is hidden
				flags += "h";
			}
			if (ACCESSFLAG) { // access
				flags += "a";
			}
			if (VERBOSEFLAG) { // verbose
				flags = "ahds";
			}
			if (args.length > 1
					&& !(SIZEFLAG || DATEFLAG || ISHIDDENFLAG || ACCESSFLAG || VERBOSEFLAG)) {
				System.out.println("Invalid command");
				a.printHelp();
				return;
			}
			a.printFileInformation(a.serchFiles(args[0]), flags);
		} catch (Exception e) {
			System.out.println(e);
			a.printHelp();
			return;
		}
	}
}
