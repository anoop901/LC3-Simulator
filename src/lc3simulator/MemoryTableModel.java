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
		return 4;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "Address";
		} else if (columnIndex == 1) {
			return "Bin";
		} else if (columnIndex == 2) {
			return "Hex";
		} else if (columnIndex == 3) {
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
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return Helper.shortToHexString((short) rowIndex);
		} else if (columnIndex == 1) {
			return Helper.shortToBinString(lc3.getMem((short) rowIndex));
		} else if (columnIndex == 2) {
			return Helper.shortToHexString(lc3.getMem((short) rowIndex));
		} else if (columnIndex == 3) {
			return Helper.shortToInstruction(lc3.getMem((short) rowIndex), (short) (rowIndex + 1));
		}
		
		return "";
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener tl) {
		//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void removeTableModelListener(TableModelListener tl) {
		//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
