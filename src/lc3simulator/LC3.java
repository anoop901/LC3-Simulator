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
		conditionCode = 0;
	}
	
	public void setMem(short address, short data) {
		memory[address & 0x0000ffff] = data;
	}
	
	public short getMem(short address) {
		return memory[address & 0x0000ffff];
	}
	
	public void setReg(short reg, short data) {
		registers[reg] = data;
	}
	
	public short getReg(short reg) {
		return registers[reg];
	}

	public void setPC(short pc) {
		this.pc = pc;
	}
	
	public short getPC() {
		return pc;
	}
	
	public short getIR() {
		return ir;
	}
	
	public int getConditionCode() {
		return conditionCode;
	}
	
	public void instructionCycle() {
		// fetch
		ir = memory[pc];
		pc++;
		// decode
		short opcode = Helper.zeroExt(ir, 15, 12);
		
		switch (opcode) {
			case 0x0: // 0000 BR
				conditionalBranch(
						Helper.zeroExt(ir, 11, 11) == 1,
						Helper.zeroExt(ir, 10, 10) == 1,
						Helper.zeroExt(ir, 9, 9) == 1,
						Helper.signExt(ir, 8, 0));
				break;
			case 0x1: // 0001 ADD
				if (Helper.zeroExt(ir, 5, 5) == 0) { // use register
					addReg(Helper.zeroExt(ir, 11, 9),
							Helper.zeroExt(ir, 8, 6),
							Helper.zeroExt(ir, 2, 0));
				} else { // use immediate
					addImm(Helper.zeroExt(ir, 11, 9),
							Helper.zeroExt(ir, 8, 6),
							Helper.signExt(ir, 4, 0));
				}
				break;
			case 0x2: // 0010 LD
				loadPCRel(Helper.zeroExt(ir, 11, 9),
						Helper.signExt(ir, 8, 0));
				break;
			case 0x3: // 0011 ST
				storePCRel(Helper.zeroExt(ir, 11, 9),
						Helper.signExt(ir, 8, 0));
				break;
			case 0x4: // 0100 JSR, JSRR
				if (Helper.zeroExt(ir, 11, 11) == 0) { // PC relative mode
					jumpToSubroutinePCRel(Helper.zeroExt(ir, 10, 0));
				} else { // base mode
					jumpToSubroutineBase(Helper.zeroExt(ir, 8, 6));
				}
				break;
			case 0x5: // 0101 AND
				if (Helper.zeroExt(ir, 5, 5) == 0) { // use register
					andReg(Helper.zeroExt(ir, 11, 9),
							Helper.zeroExt(ir, 8, 6),
							Helper.zeroExt(ir, 2, 0));
				} else { // use immediate
					andImm(Helper.zeroExt(ir, 11, 9),
							Helper.zeroExt(ir, 8, 6),
							Helper.signExt(ir, 4, 0));
				}
				break;
			case 0x6: // 0110 LDR
				loadBaseOffset(Helper.zeroExt(ir, 11, 9),
						Helper.zeroExt(ir, 8, 6),
						Helper.signExt(ir, 5, 0));
				break;
			case 0x7: // 0111 STR
				storeBaseOffset(Helper.zeroExt(ir, 11, 9),
						Helper.zeroExt(ir, 8, 6),
						Helper.signExt(ir, 5, 0));
				break;
			case 0x8: // 1000 RTI
				break;
			case 0x9: // 1001 NOT
				not(Helper.zeroExt(ir, 11, 9),
						Helper.zeroExt(ir, 8, 6));
				break;
			case 0xA: // 1010 LDI
				loadIndirect(Helper.zeroExt(ir, 11, 9),
						Helper.signExt(ir, 8, 0));
				break;
			case 0xB: // 1011 STI
				storeIndirect(Helper.zeroExt(ir, 11, 9),
						Helper.signExt(ir, 8, 0));
				break;
			case 0xC: // 1100 JMP, RET
				jump(Helper.zeroExt(ir, 8, 6));
				break;
			case 0xE: // 1110 LEA
				loadEffectiveAddress(Helper.zeroExt(ir, 11, 9),
						Helper.signExt(ir, 8, 0));
				break;
			case 0xF: // 1111 TRAP
				break;
			default:  // unused opcode
				break;
		}
	}
	
	// OPERATION INSTRUCTIONS
	
	public void addReg(short dr, short sr1, short sr2) {
		registers[dr] = (short) (registers[sr1] + registers[sr2]);
		updateConditionCode(registers[dr]);
	}
	
	public void addImm(short dr, short sr1, short imm) {
		registers[dr] = (short) (registers[sr1] + imm);
		updateConditionCode(registers[dr]);
	}
	
	public void andReg(short dr, short sr1, short sr2) {
		short data = (short) (registers[sr1] & registers[sr2]);
		registers[dr] = data;
		updateConditionCode(data);
	}
	
	public void andImm(short dr, short sr1, short imm) {
		short data = (short) (registers[sr1] & imm);
		registers[dr] = data;
		updateConditionCode(data);
	}
	
	public void not(short dr, short sr) {
		short data = (short) (~registers[sr]);
		registers[dr] = data;
		updateConditionCode(data);
	}
	
	// DATA MOVEMENT INSTRUCTIONS
	
	public void loadPCRel(short dr, short pcOffset) {
		short data = memory[pc + pcOffset];
		registers[dr] = data;
		updateConditionCode(data);
	}
	
	public void loadIndirect(short dr, short pcOffset) {
		short address = memory[pc + pcOffset];
		short data = memory[address];
		registers[dr] = data;
		updateConditionCode(data);
	}
	
	public void loadBaseOffset(short dr, short baseR, short offset) {
		short data = memory[registers[baseR] + offset];
		registers[dr] = data;
		updateConditionCode(data);
	}
	
	public void loadEffectiveAddress(short dr, short pcOffset) {
		short data = (short) (pc + pcOffset);
		registers[dr] = data;
		updateConditionCode(data);
	}
	
	public void storePCRel(short sr, short pcOffset) {
		short data = registers[sr];
		memory[pc + pcOffset] = data;
	}
	
	public void storeIndirect(short sr, short pcOffset) {
		short data = registers[sr];
		short address = memory[pc + pcOffset];
		memory[address] = data;
	}
	
	public void storeBaseOffset(short sr, short baseR, short offset) {
		short data = registers[sr];
		memory[registers[baseR] + offset] = data;
	}
	
	// CONTROL INSTRUCTIONS
	
	public void conditionalBranch(boolean n, boolean z, boolean p, short pcOffset) {
		if ((n && conditionCode == N)
				|| (z && conditionCode == Z)
				|| (p && conditionCode == P)) {
			pc += pcOffset;
		}
	}
	
	public void jump(short reg) {
		pc = registers[reg];
	}
	
	public void jumpToSubroutinePCRel(short pcOffset) {
		short temp = pc;
		pc += pcOffset;
		registers[7] = temp;
	}
	
	public void jumpToSubroutineBase(short baseR) {
		short temp = pc;
		pc = registers[baseR];
		registers[7] = temp;
	}
	
	// OTHER METHODS
	
	public void updateConditionCode(short x) {
		if (x > 0) {
			conditionCode = P;
		} else if (x == 0) {
			conditionCode = Z;
		} else {
			conditionCode = N;
		}
	}
}
