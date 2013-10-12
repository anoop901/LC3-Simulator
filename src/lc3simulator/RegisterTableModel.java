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
public class RegisterTableModel implements TableModel {
	
	private LC3 lc3;
	
	public RegisterTableModel(LC3 lc3) {
		this.lc3 = lc3;
	}

	@Override
	public int getRowCount() {
		return 11;
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "Register";
		} else if (columnIndex == 1) {
			return "Hex";
		} else if (columnIndex == 2) {
			return "Dec";
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
			if (rowIndex < 8) {
				return "R" + rowIndex;
			} else if (rowIndex == 8) {
				return "PC";
			} else if (rowIndex == 9) {
				return "IR";
			} else if (rowIndex == 10) {
				return "CC";
			} else {
				return "";
			}
		} else if (columnIndex == 1) {
			if (rowIndex < 8) {
				return Helper.shortToHexString(lc3.getReg((short) rowIndex));
			} else if (rowIndex == 8) {
				return Helper.shortToHexString(lc3.getPC());
			} else if (rowIndex == 9) {
				return Helper.shortToHexString(lc3.getIR());
			} else if (rowIndex == 10) {
				switch (lc3.getConditionCode()) {
					case -1:
						return "N";
					case 0:
						return "Z";
					case 1:
						return "P";
				}
			} else {
				return "";
			}
		} else if (columnIndex == 2) {
			if (rowIndex < 8) {
				return "#" + Short.toString(lc3.getReg((short) rowIndex));
			} else if (rowIndex == 8) {
				return "#" + Short.toString(lc3.getPC());
			} else if (rowIndex == 9) {
				return "#" + Short.toString(lc3.getIR());
			} else {
				return "";
			}
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
