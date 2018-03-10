import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import graph.Edge;
import graph.Graph;

/// The File class is responsible for reading and writing into files.
public class File {
	
	public int[][] query;
	public int l;
	Graph G;
	
	public File(String path) {
		ReadFile(path);
	}
	
	//Take the output
	public void ReadFile(String path) {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			
			String sCurrentLine;
			sCurrentLine = br.readLine();
			String[] line = sCurrentLine.split(" ");
			int n = Integer.parseInt(line[0]);
			int m = Integer.parseInt(line[1]);
			Edge[] E = new Edge[m];
			for(int j = 0; j < m; j++){
				
				sCurrentLine = br.readLine();
				line = sCurrentLine.split(" ");
				E[j] = new Edge(Integer.parseInt(line[0]) - 1,Integer.parseInt(line[1]) - 1,Integer.parseInt(line[2])); // Subtract 1 because inputs starts from 1 and not from 0.
		
			}
			G = new Graph(E); G.n = n;
			sCurrentLine = br.readLine();
			line = sCurrentLine.split(" ");
			l = Integer.parseInt(line[0]);
			query = new int[l][2];
			for(int j = 0; j < l; j++){
				sCurrentLine = br.readLine();
				line = sCurrentLine.split(" ");
				query[j][0] = Integer.parseInt(line[0]) - 1;
				query[j][1] = Integer.parseInt(line[1]) - 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	};
	
	static void Save(String file, int[] noise) {
		BufferedWriter writer = null;
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(file)));
		    for(int i = 0; i < noise.length; i++) {
				writer.write("" + noise[i]);
				writer.newLine();
				writer.flush();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
	}
	
}
