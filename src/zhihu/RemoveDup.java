package zhihu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class RemoveDup {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner read = new Scanner(new BufferedReader(new FileReader("user.in")));
		Set name = new HashSet();
		while(read.hasNext()){
			name.add(read.nextLine());
		}
		read.close();
		PrintWriter pw = new PrintWriter(new File("new_user.in"));
		Iterator it = name.iterator();
		while(it.hasNext()){
			pw.println((String)it.next());
		}
		pw.close();
	}

}
