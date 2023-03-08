package Assembler;

import java.util.HashMap;

public class SymbolTable {
	private HashMap<String, Integer> symbols = new HashMap();
	private int currentAddress = 16;
	public SymbolTable()  {
	    for (int i = 0; i <= 15; i++) {
	        final String key = "R" + i;
	        symbols.put(key, i);
	    }
	    symbols.put("SCREEN", 16384);
	    symbols.put("KBD", 24576);
	    symbols.put("SP", 0);
	    symbols.put("LCL", 1);
	    symbols.put("ARG", 2);
	    symbols.put("THIS", 3);
	    symbols.put("THAT", 4);
	    
	}
	public void addEntry(String symbol) {
		if(symbols.containsKey(symbol))  {
			return;
		}
		symbols.put(symbol, currentAddress);
		currentAddress++;
	}
	public boolean contains(String symbol)  {
		return symbols.containsKey(symbol);
	}
	public int getAddress(String symbol)  {
		if(!symbols.containsKey(symbol))  {
		    addEntry(symbol);
			return currentAddress;
		}
		return symbols.get(symbol);
	}
	
	public void print()  {
		for(String next: symbols.keySet())  {
			System.out.print(next+" "+getAddress(next));
			System.out.println();
		}
	}
}
