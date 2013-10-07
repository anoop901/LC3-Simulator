/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lc3simulator;

/**
 *
 * @author anoop
 */
public enum Interpretation {
	BINARY, HEXADECIMAL, DECIMAL, INSTRUCTION;
	
	public String toString() {
		switch (this) {
			case BINARY:
				return "Binary";
			case HEXADECIMAL:
				return "Hex";
			case DECIMAL:
				return "Decimal";
			case INSTRUCTION:
				return "Instruction";
		}
		return null;
	}
	
	public String interpret(short data) {
		switch (this) {
			case BINARY:
				return Helper.shortToBinString(data);
			case HEXADECIMAL:
				return Helper.shortToHexString(data);
			case DECIMAL:
				return Short.toString(data);
			case INSTRUCTION:
				return "[instruction]";
		}
		return null;
	}
}
