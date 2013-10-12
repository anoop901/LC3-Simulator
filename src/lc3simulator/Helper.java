/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lc3simulator;

/**
 *
 * @author anoop
 */
public class Helper {
	
	public static short bitSubstrSigned(short str, int left, int right) {
		return (short) (str << (31 - left) >> (31 - left + right));
	}
	
	public static short bitSubstrUnsigned(short str, int left, int right) {
		return (short) (str << (31 - left) >>> (31 - left + right));
	}
	
	public static String shortToBinString(short n) {
		String res = "";
		for (int i = 0; i < 16; i++) {
			int bit = (n & 1);
			res = (char) ('0' + bit) + res;
			n = (short) (n >> 1);
		}
		return res;
	}
	
	public static String shortToHexString(short n) {
		String res = "";
		for (int i = 0; i < 4; i++) {
			int bit = (n & 0xF);
			res = (char) ((bit < 10) ? ('0' + bit) : ('A') + bit - 10) + res;
			n = (short) (n >> 4);
		}
		return "x" + res;
	}
	
	public static String shortToInstruction(short data, short address) {
		
		short opcode = Helper.bitSubstrUnsigned(data, 15, 12);
		
		switch (opcode) {
			case 0x0: // 0000 BR
				boolean n = Helper.bitSubstrUnsigned(data, 11, 11) == 1;
				boolean z = Helper.bitSubstrUnsigned(data, 10, 10) == 1;
				boolean p = Helper.bitSubstrUnsigned(data, 9, 9) == 1;
				if (n || z || p) {
					return "BR" +
							(n ? "n" : "") +
							(z ? "z" : "") +
							(p ? "p" : "") +
							" " + Helper.shortToHexString((short) (address + Helper.bitSubstrSigned(data, 8, 0)));
				} else {
					return "NOP";
				}
			case 0x1: // 0001 ADD
				if (Helper.bitSubstrUnsigned(data, 5, 5) == 0) { // use register
					return "ADD R" + Helper.bitSubstrUnsigned(data, 11, 9) + 
							", R" + Helper.bitSubstrUnsigned(data, 8, 6) + 
							", R" + Helper.bitSubstrUnsigned(data, 2, 0);
				} else { // use immediate
					return "ADD R" + Helper.bitSubstrUnsigned(data, 11, 9) +
							", R" + Helper.bitSubstrUnsigned(data, 8, 6) +
							", #" + Short.toString(Helper.bitSubstrSigned(data, 4, 0));
				}
			case 0x2: // 0010 LD
				return "LD R" + Helper.bitSubstrUnsigned(data, 11, 9) +
						", " + Helper.shortToHexString((short) (address + Helper.bitSubstrSigned(data, 8, 0)));
			case 0x3: // 0011 ST
				return "ST R" + Helper.bitSubstrUnsigned(data, 11, 9) +
						", " + Helper.shortToHexString((short) (address + Helper.bitSubstrSigned(data, 8, 0)));
			case 0x4: // 0100 JSR, JSRR
				if (Helper.bitSubstrUnsigned(data, 11, 11) == 0) { // PC relative mode
					return "JSR " + Helper.shortToHexString((short) (address + Helper.bitSubstrSigned(data, 10, 0)));
				} else { // base mode
					return "JSRR R" + Helper.bitSubstrUnsigned(data, 8, 6);
				}
			case 0x5: // 0101 AND
				if (Helper.bitSubstrUnsigned(data, 5, 5) == 0) { // use register
					return "AND R" + Helper.bitSubstrUnsigned(data, 11, 9) + 
							", R" + Helper.bitSubstrUnsigned(data, 8, 6) + 
							", R" + Helper.bitSubstrUnsigned(data, 2, 0);
				} else { // use immediate
					return "AND R" + Helper.bitSubstrUnsigned(data, 11, 9) +
							", R" + Helper.bitSubstrUnsigned(data, 8, 6) +
							", #" + Short.toString(Helper.bitSubstrSigned(data, 4, 0));
				}
			case 0x6: // 0110 LDR
				return "LDR R" + Helper.bitSubstrUnsigned(data, 11, 9) +
						", R" + Helper.bitSubstrUnsigned(data, 8, 6) +
						", #" + Short.toString(Helper.bitSubstrSigned(data, 5, 0));
			case 0x7: // 0111 STR
				return "STR R" + Helper.bitSubstrUnsigned(data, 11, 9) +
						", R" + Helper.bitSubstrUnsigned(data, 8, 6) +
						", #" + Short.toString(Helper.bitSubstrSigned(data, 5, 0));
			case 0x8: // 1000 RTI
				return "RTI";
			case 0x9: // 1001 NOT
				return "NOT R" + Helper.bitSubstrUnsigned(data, 11, 9) +
						" R" + Helper.bitSubstrUnsigned(data, 8, 6);
			case 0xA: // 1010 LDI
				return "LDI R" + Helper.bitSubstrUnsigned(data, 11, 9) +
						", " + Helper.shortToHexString((short) (address + Helper.bitSubstrSigned(data, 8, 0)));
			case 0xB: // 1011 STI
				return "STI R" + Helper.bitSubstrUnsigned(data, 11, 9) +
						", " + Helper.shortToHexString((short) (address + Helper.bitSubstrSigned(data, 8, 0)));
			case 0xC: // 1100 JMP, RET
				short reg = Helper.bitSubstrUnsigned(data, 8, 6);
				if (reg != 7)
					return "JMP R" + Helper.bitSubstrUnsigned(data, 8, 6);
				else
					return "RET";
			case 0xE: // 1110 LEA
				return "LEA R" + Helper.bitSubstrUnsigned(data, 11, 9) +
						", " + Helper.bitSubstrSigned(data, 8, 0);
			case 0xF: // 1111 TRAP
				return "TRAP " + Helper.shortToHexString(Helper.bitSubstrUnsigned(data, 7, 0));
			default:  // unused opcode
				return "reserved opcode";
		}
	}
}
