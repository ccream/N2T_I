// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.
	
	@RESTART
	0;JMP

(KEYCHECK)
	@KBD
	D=M
	@BLACKEN
	D;JGT
	@WHITEN
	D;JEQ
	@KEYCHECK
	0;JEQ


(BLACKEN)
	@row
	M=0
	@32
	D=A
	@nc
	M=D
	@256
	D=A
	@nr
	M=D
	@color
	M=-1
	@OUTLOOP
	0;JMP

(WHITEN)
	@row
	M=0
	@32
	D=A
	@nc
	M=D
	@256
	D=A
	@nr
	M=D
	@color
	M=0
	@OUTLOOP
	0;JMP

(OUTLOOP)
	@row
	D=M
	@nr
	D=D-M
	@RESTART
	D;JEQ
	@i
	M=0
	@INLOOP
	0;JMP

(INLOOP)
	@i
	D=M
	@nc
	D=D-M
	@OUTLOOPC
	D;JEQ
	@color
	D=M
	@current
	A=M
	M=D
	@current
	M=M+1
	@i
	M=M+1
	@INLOOP
	0;JEQ

(OUTLOOPC)
	@row
	M=M+1
	@OUTLOOP
	0;JMP


(RESTART)
	@SCREEN
	D=A
	@current
	M=D
	@KEYCHECK
	0;JMP
	

(END)
	@END
	0;JMP
	
