/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lc3simulator;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author anoop
 */
public class MemoryTableModel implements TableModel {
	
	private LC3 lc3;
	
	public MemoryTableModel(LC3 lc3) {
		this.lc3 = lc3;
	}

	@Override
	public int getRowCount() {
		return 1 << 16;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 1) {
			return "Address";
		} else if (columnIndex == 2) {
			return "Bin";
		} else if (columnIndex == 3) {
			return "Hex";
		} else if (columnIndex == 4) {
			return "Instruction";
		}
		return "";
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 2 || columnIndex == 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			if (lc3.getPC() == rowIndex) {
				return "PC";
			} else {
				return "";
			}
		} else if (columnIndex == 1) {
			return Helper.shortToHexString((short) rowIndex);
		} else if (columnIndex == 2) {
			return Helper.shortToBinString(lc3.getMem((short) rowIndex));
		} else if (columnIndex == 3) {
			return Helper.shortToHexString(lc3.getMem((short) rowIndex));
		} else if (columnIndex == 4) {
			return Helper.shortToInstruction(lc3.getMem((short) rowIndex), (short) (rowIndex + 1));
		}
		
		return "";
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		String in = (String) aValue;
		try {
			if (columnIndex == 2)
				lc3.setMem((short) rowIndex, (short) Integer.parseInt(in, 2));
			else if (columnIndex == 3) {
				if (!in.isEmpty() && in.charAt(0) != 'x')
					lc3.setMem((short) rowIndex, (short) Integer.parseInt(in, 16));
				else
					lc3.setMem((short) rowIndex, (short) Integer.parseInt(in.substring(1), 16));
			}
		} catch (NumberFormatException nfe) {
			
		}
	}

	@Override
	public void addTableModelListener(TableModelListener tl) {
	}

	@Override
	public void removeTableModelListener(TableModelListener tl) {
	}
}
