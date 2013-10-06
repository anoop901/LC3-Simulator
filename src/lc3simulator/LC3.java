/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lc3simulator;

/**
 *
 * @author anoop
 */
public class LC3 {
	
	private short[] memory;		// stores contents of memory
	private short[] registers;	// stores contents of general-purpose registers
	private int conditionCode;	// -1 if N, 0 if Z, 1 if P
	private short pc;
	private short ir;
	
	public static final int N = -1;
	public static final int Z = 0;
	public static final int P = 1;
	
	public LC3() {
		memory = new short[1 << 16];
		registers = new short[8];
		pc = 0x3000;
		ir = 0x0000;
	}
	
	public void setMem(short address, short data) {
		memory[address] = data;
	}
	
	public short getMem(short address) {
		return memory[address];
	}
	
	public void setReg(short reg, short data) {
		registers[reg] = data;
	}
	
	public short getReg(short reg) {
		return registers[reg];
	}
	
	public void instructionCycle() {
		// fetch
		ir = memory[pc];
		pc++;
		// decode
		short opcode = Helper.bitSubstrUnsigned(ir, 15, 12);
		
		short sr1;
		short sr2;
		short sr3;
		switch (opcode) {
			case 0x0: // 0000 BR
				conditionalBranch(
						Helper.bitSubstrUnsigned(ir, 11, 11) == 1,
						Helper.bitSubstrUnsigned(ir, 10, 10) == 1,
						Helper.bitSubstrUnsigned(ir, 9, 9) == 1,
						Helper.bitSubstrSigned(ir, 8, 0));
				break;
			case 0x1: // 0001 ADD
				if (Helper.bitSubstrUnsigned(ir, 5, 5) == 0) { // use register
					addReg(Helper.bitSubstrUnsigned(ir, 11, 9),
							Helper.bitSubstrUnsigned(ir, 8, 6),
							Helper.bitSubstrUnsigned(ir, 2, 0));
				} else { // use immediate
					addImm(Helper.bitSubstrUnsigned(ir, 11, 9),
							Helper.bitSubstrUnsigned(ir, 8, 6),
							Helper.bitSubstrSigned(ir, 4, 0));
				}
				break;
			case 0x2: // 0010 LD
				loadPCRel(Helper.bitSubstrUnsigned(ir, 11, 9),
						Helper.bitSubstrSigned(ir, 8, 0));
				break;
			case 0x3: // 0011 ST
				break;
			case 0x4: // 0100 JSR, JSRR
				break;
			case 0x5: // 0101 AND
				if (Helper.bitSubstrUnsigned(ir, 5, 5) == 0) { // use register
					andReg(Helper.bitSubstrUnsigned(ir, 11, 9),
							Helper.bitSubstrUnsigned(ir, 8, 6),
							Helper.bitSubstrUnsigned(ir, 2, 0));
				} else { // use immediate
					andImm(Helper.bitSubstrUnsigned(ir, 11, 9),
							Helper.bitSubstrUnsigned(ir, 8, 6),
							Helper.bitSubstrSigned(ir, 4, 0));
				}
				break;
			case 0x6: // 0110 LDR
				break;
			case 0x7: // 0111 STR
				break;
			case 0x8: // 1000 RTI
				break;
			case 0x9: // 1001 NOT
				break;
			case 0xA: // 1010 LDI
				
				break;
			case 0xB: // 1011 STI
				break;
			case 0xC: // 1100 JMP, RET
				jump(Helper.bitSubstrUnsigned(ir, 8, 6));
				break;
			case 0xE: // 1110 LEA
				break;
			case 0xF: // 1111 TRAP
				break;
			default:  // unused opcode
				break;
		}
	}
	
	public void addReg(short dr, short sr1, short sr2) {
		registers[dr] = (short) (registers[sr1] + registers[sr2]);
		updateConditionCode(registers[dr]);
	}
	
	public void addImm(short dr, short sr1, short imm) {
		registers[dr] = (short) (registers[sr1] + imm);
		updateConditionCode(registers[dr]);
	}
	
	public void andReg(short dr, short sr1, short sr2) {
		registers[dr] = (short) (registers[sr1] & registers[sr2]);
		updateConditionCode(registers[dr]);
	}
	
	public void andImm(short dr, short sr1, short imm) {
		registers[dr] = (short) (registers[sr1] & imm);
		updateConditionCode(registers[dr]);
	}
	
	public void conditionalBranch(boolean n, boolean z, boolean p, short pcOffset) {
		if ((n && conditionCode == N)
				|| (z && conditionCode == Z)
				|| (p && conditionCode == P)) {
			pc += pcOffset;
		}
	}
	
	public void loadPCRel(short dr, short pcOffset) {
		registers[dr] = memory[pc + pcOffset];
		updateConditionCode(registers[dr]);
	}
	
	public void loadIndirect(short dr, short pcOffset) {
		registers[dr] = memory[memory[pc + pcOffset]];
	}
	
	public void updateConditionCode(short x) {
		if (x > 0) {
			conditionCode = P;
		} else if (x == 0) {
			conditionCode = Z;
		} else {
			conditionCode = N;
		}
	}
	
	public void jump(short reg) {
		pc = registers[reg];
	}
	
	public void printRegisters() {
		for (int i = 0; i < 8; i++) {
			System.out.println("R" + i + "\t" + Helper.shortToHexString(registers[i]));
		}
	}
}
