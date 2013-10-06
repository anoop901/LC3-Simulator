/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lc3simulator;

/**
 *
 * @author anoop
 */
public class LC3Simulator {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		LC3 lc3 = new LC3();
		lc3.setReg((short) 1, (short) 0xCAA0);
		lc3.setReg((short) 3, (short) 0x460C);
		
		lc3.setMem((short) 0x3000, (short) 0x1CC1);	// 0001 110 011 0 00 001
		lc3.instructionCycle();
		lc3.printRegisters();
	}
}
