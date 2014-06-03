package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreProcessor {

	private File cflowFolder;
	private String importString = "import main.Interface;";
	private String functionString = "Interface.next";
	private String startString = "Interface.init";
	private final String regexString = "//@cflow [a-zA-Z0-9]*";
	private final String regexStartString = "//@cflow start .*";
	Pattern regex = Pattern.compile(regexString);
	Pattern regexStart = Pattern.compile(regexStartString);
	private String folder;


	public static void main(String[] args){
		/*
		System.out.println("Whats the folder you want to process to control the flow?");
		Scanner scan = new Scanner(System.in);
		String folder = scan.next();
		scan.close();
		*/

		PreProcessor p = new PreProcessor(args[0]);			
	}

	public PreProcessor(String folder){
		this.folder = folder;
		
		createTempFolder(folder);
		scanFiles(folder);
	}

	public void scanFiles(String folder){

		listFiles(folder, cflowFolder.getAbsolutePath());
	}

	public void createTempFolder(String folder){
		String path = "";

		System.out.println(folder);
		int i = folder.lastIndexOf('\\');

		if (i > 0) {
			path = folder.substring(0, i+1);
		}
		System.out.println(path);

		File cflowFolder= new File(path + "cflow");
		cflowFolder.mkdir();
		this.cflowFolder = cflowFolder;
	}

	public void listFiles(String folder, String output){

		File directory = new File(folder);
		if(directory.isFile()){
			String extension = "";

			int i = directory.getName().lastIndexOf('.');
			if (i > 0) {
				extension = directory.getName().substring(i+1);
			}
			if(extension.equals("java")){
				//files.add(file);
				try {
					//System.out.println("PROCESSAR " + directory);
					process(directory, output);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				Path FROM = Paths.get(directory.getAbsolutePath());
				Path TO = Paths.get(output + File.separator + directory.getName());

				CopyOption[] options = new CopyOption[]{
						StandardCopyOption.REPLACE_EXISTING,
						StandardCopyOption.COPY_ATTRIBUTES
				};

				try {
					Files.copy(FROM, TO, options);
				} catch (IOException e) {
					System.out.println("ERROR COPYING FILE");
					e.printStackTrace();
				}
			}
		}
		else{
			// get all the files from a directory
			File[] fList = directory.listFiles();
			for (File file : fList) {			
				if (file.isFile()) {

					String extension = "";

					int i = file.getName().lastIndexOf('.');
					if (i > 0) {
						extension = file.getName().substring(i+1);
					}
					if(extension.equals("java")){
						//files.add(file);
						try {
							process(file, output);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					else{
						Path FROM = Paths.get(file.getAbsolutePath());
						Path TO = Paths.get(output + File.separator + file.getName());

						CopyOption[] options = new CopyOption[]{
								StandardCopyOption.REPLACE_EXISTING,
								StandardCopyOption.COPY_ATTRIBUTES
						};

						try {
							Files.copy(FROM, TO, options);
						} catch (IOException e) {
							System.out.println("ERROR COPYING FILE");
							e.printStackTrace();
						}
					}

				} else if (file.isDirectory()) {
					//criar pasta
					String newFolderName = output + File.separator + file.getName();
					File newFolder = new File(newFolderName);
					newFolder.mkdir();

					listFiles(file.getAbsolutePath(), newFolderName);			
				}
			}
		}

	}

	public void process(File file, String output) throws IOException{

		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		String outputFile = new String();

		while ((line = br.readLine()) != null) {
			
			Matcher matcher = regexStart.matcher(line);
			if(matcher.find()){
				System.out.println("Encontrei cflow start");
				System.out.println("LINE: " + line);
				
				int indexOfCflow = line.indexOf("//@cflow start");
				String name = line.substring(indexOfCflow + 15);
				System.out.println("name1:" + name);
				name = name.replaceAll("\\\"", "\\\\\\\\\"");
				System.out.println("name2:" + name);

				String funcString = startString + "(\""; 
				funcString += name;
				funcString += "\\\\n\");";
				
				//System.out.println("REGEXSTARTSTRING: " + regexStartString);
				//System.out.println("FUNCSTRING: " + funcString);
				//System.out.println("OLD LINE: " + line);
				line = line.replaceAll(regexStartString, funcString);
				
				//System.out.println("NEW LINE: " + line);
				
			}else{
				
				matcher = regex.matcher(line);
				if(matcher.find()){
	
					//System.out.println("Encontrei cflow");
					int indexOfCflow = line.indexOf("//@cflow");
					String name = line.substring(indexOfCflow + 9);
					
					System.out.println("FIND CFLOW:" + name);
	
					String funcString = functionString + "(\""; 
					funcString += name;
					funcString += "\");";
	
					//System.out.println("OLD LINE: " + line);
	
					line = line.replaceAll(regexString, funcString);
	
					//System.out.println("NEW LINE: " + line);
				}	
			}

			if(line.indexOf("package") != -1)
			{
				line += "\n";
				line +=	importString;
			}	

			line += "\n";
			outputFile += line; 

		}
		br.close();

		File file2 = new File(output + File.separator + file.getName());
		FileWriter fileWriter = new FileWriter(file2, false); // true to append
		// false to overwrite.
		fileWriter.write(outputFile);
		fileWriter.close();
		//System.out.println(outputFile);
	}


	/*
	public void execute() throws IOException{

		for(String file: files){
			String outputFile = new String();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {

				Matcher matcher = regex.matcher(line);
				if(matcher.find()){
					System.out.println("Encontrei cflow");
					int indexOfCflow = line.indexOf("//@cflow");
					String name = line.substring(indexOfCflow + 9);
					System.out.println(name);

					String funcString = "functionName("; 
					funcString += name;
					funcString += ");\n";

					//System.out.println("OLD LINE: " + line);

					line = line.replaceAll(regexString, funcString);

					//System.out.println("NEW LINE: " + line);
				}

				line += "\n";
				outputFile += line; 
			}
			br.close();


			File file2 = new File(file);
			FileWriter fileWriter = new FileWriter(file2, false); // true to append
			// false to overwrite.
			fileWriter.write(outputFile);
			fileWriter.close();
			//System.out.println(outputFile);
		}
	}
	 */
}