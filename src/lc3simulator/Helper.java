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
}
