package Assembler;

import java.util.*;
import java.io.*;


public class HackAssembler {
	
	public static void main(String[] args)  {
		Parser parser = new Parser();
		Scanner sc = new Scanner(System.in);
		String nextName = sc.nextLine();
		SymbolTable st = new SymbolTable();
		String fileName = System.getProperty("user.dir")+"/"+nextName+".asm";
		File file = new File(fileName);
		BufferedReader br;
		int L = 0;
		try  {
			br = new BufferedReader(new FileReader(fileName));
			String next = br.readLine();
			while(next != null)  {
				parser.first(st, fileName, next);
				next = br.readLine();
				L++;
			}
		}
		catch(IOException e)  {
			e.printStackTrace();
		}
		
		L = 0;
		
		try  {
			br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/"+"temp.asm"));
			String next = br.readLine();
			while(next != null)  {
				parser.writeLine(next, L, nextName);
				next = br.readLine();
				L++;
			}
			br.close();
		}
		catch (IOException e) {
            e.printStackTrace();
        }
		sc.close();
	}
}
