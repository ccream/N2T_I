package Assembler;

import java.io.*;

public class Parser {
	
	public boolean isComment(String line)  {
		return type(line).equals("Comment");
	}
	
	public String type(String line)  {
		for(int i = 0; i < line.length(); i++)  {
			if(line.charAt(i) == '@')  {
				if(Character.isDigit(line.charAt(i+1)))  {
					return "A";
				}
				return "@L";
			}
			if(line.charAt(i) == '(')  {
				return "L";
			}
			if(line.charAt(i) == '/' && i+1 < line.length() && line.charAt(i+1) == '/')  {
				return "Comment";
			}
			if(line.charAt(i) != ' ')  {
				return "C";
			}
		}
		return "Comment";
	}
	
	public String clean(String line)  {
		if(isComment(line))  {
			return "";
		}
		String result = "";
		for(int i = 0; i < line.length(); i++)  {
			if(line.charAt(i) == '@')  {
				for(int j = i+1; j < line.length(); j++)  {
					if(line.charAt(j) == ' ')  {
						return result;
					}
					result += line.charAt(j);
				}
			}
			else if(line.charAt(i) == '(')  {
				for(int j = i+1; j < line.length(); j++)  {
					if(line.charAt(j) == ')')  {
						return result;
					}
					result += line.charAt(j);
				}
			}
		}
		return result;
	}
	
	public String dest(String line)  {
		for(int i = 0; i < line.length(); i++)  {
			if(line.charAt(i) == ';')  {
				return "null";
			}
			if(line.charAt(i) == 'n')  {
				return "null";
			}
			else if(line.charAt(i) == 'D')  {
				if(i+1 == line.length() || line.charAt(i+1) == '=')  {
					return "D";
				}
			}
			else if(line.charAt(i) == 'M')  {
				if(i+1 == line.length() || line.charAt(i+1) == '=')  {
					return "M";
				}
				return "MD";
			}
			else if(line.charAt(i) == 'A')  {
				if(i+1 == line.length() || line.charAt(i+1) == '=')  {
					return "A";
				}
				if(i+2 == line.length() || line.charAt(i+2) == '=')  {
					if(line.charAt(i+1) == 'M')  {
						return "AM";
					}
					else  {
						return "AD";
					}
				}
				return "AMD";
			}
		}
		return "null";
	}
	
	public String comp(String line)  {
		String result = "";
		for(int i = 0; i < line.length(); i++)  {
			if(line.charAt(i) == '0')  {
				if(i+1 >= line.length() || line.charAt(i+1) == ';')  {
					return "0";
				}
			}
			else if(line.charAt(i) == '1')  {
				if(i+1 >= line.length() || line.charAt(i+1) == ';')  {
					return "1";
				}
			}
			else if(line.charAt(i) == 'D')  {
				if(i+1 >= line.length() || line.charAt(i+1) == ';')  {
					return "D";
				}
			}
			else if(line.charAt(i) == 'A')  {
				if(i+1 >= line.length() || line.charAt(i+1) == ';')  {
					return "A";
				}
			}
			else if(line.charAt(i) == 'M')  {
				if(i+1 >= line.length() || line.charAt(i+1) == ';')  {
					return "M";
				}
			}
			if(line.charAt(i) == '=')  {
				if(line.charAt(i+1) == '-')  {
					result = "-"+line.charAt(i+2);
				}
				else if(line.charAt(i+1) == '!')  {
					result = "-"+line.charAt(i+2);
				}
				else if(i+2 >= line.length() || line.charAt(i+2) == ' ')  {
					result += line.charAt(i+1);
				}
				else  {
					result += line.charAt(i+1);
					result += line.charAt(i+2);
					result += line.charAt(i+3);
				}
				return result;
			}
		}
		return result;
	}
	
	public String jump(String line)  {
		String result = "";
		for(int i = 0; i < line.length(); i++)  {
			if(line.charAt(i) == ';')  {
				if(line.charAt(i+1) == 'n')  {
					return "null";
				}
				else  {
					result += line.charAt(i+1);
					result += line.charAt(i+2);
					result += line.charAt(i+3);
					break;
				}
			}
			if(i == line.length()-1 && result.equals(""))  {
				return "null";
			}
		}
		return result;
	}

	
	public void first(SymbolTable table, String FileName, String line) throws IOException  {
		if(type(line).equals("Comment"))  {
			return;
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter("temp.asm", true));
		String result = line;
		int next = -1;
		String symbol = "";
		if(type(line).equals("L"))  {
			result = "";
		}
		else if(type(line).equals("@L"))  {
			symbol = clean(line);
			if(!table.contains(symbol))  {
				next = findLine(symbol, FileName);
				if(next == -1)  {
					table.addEntry(symbol);
					next = table.getAddress(symbol);
				}
				else  {
					next++;
				}
			}
			else  {
				next = table.getAddress(symbol);
			}
			result = "@"+Integer.toString(next);
		}
		if(result != null)  {
			writer.write(result);
			if(!type(line).equals("(L)"))  {
				writer.newLine();
			}
			writer.close();
		}
		
	}
	
	
	public String tobin(String line)  {
		String binary = Integer.toBinaryString(Integer.parseInt(line));
		while(binary.length() < 16)  {
			binary = "0"+binary;
		}
		return binary;
	}
	
	
	private int findLine(String symbol, String FileName) throws IOException  {
		BufferedReader br = new BufferedReader(new FileReader(FileName));
		int prev = 0;
		int current = 0;
		String line = br.readLine();
		while(line != null)  {
			if(clean(line).equals(symbol) && type(line).equals("L"))  {
				br.close();
				return current-prev-1;
			}
			if(type(line).equals("L"))  {
				prev++;
			}
			if(!type(line).equals("Comment"))  {
				current++;
			}
			line = br.readLine();
		}
		br.close();
		return -1;
	}
	
	
	public void writeLine(String line, int index, String fileName) throws IOException {
		if(isComment(line))  {
			return;
		}
		String result = "";
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName+".hack", true));
		if(type(line).equals("A"))  {
			result = tobin(clean(line));
		}
		else  {
			result += "111";
			result += Code.comp(comp(line));
			result += Code.dest(dest(line));
			result += Code.jump(jump(line));
			if(result.length() < 16)  {
				System.out.println(line);
				System.out.println(comp(line));
			}
		}
		writer.write(result);
		writer.newLine();
		writer.close();
		return;
	}
}
