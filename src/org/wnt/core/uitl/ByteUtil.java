package org.wnt.core.uitl;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理byte相关
 * 
 * @author 赵俊鹏
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class ByteUtil {

	/**
	 * 初始化一个新的byteList
	 * 
	 * @return
	 */
	public static List<Byte> initList() {
		return new ArrayList<Byte>();
	}

	/**
	 * 追加byte数组
	 * 
	 * @param byteList
	 * @param bytes
	 * @return
	 */
	public static List<Byte> addBytes(List<Byte> byteList, byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			byteList.add(bytes[i]);
		}
		return byteList;
	}

	/**
	 * 追加byte数组
	 * 
	 * @param byteList
	 * @param bytes
	 * @return
	 */
	public static List<Byte> addBytes(List<Byte> byteList, byte[] bytes,int length) {
		for (int i = 0; i < length; i++) {
			byteList.add(bytes[i]);
		}
		return byteList;
	}
	
	/**
	 * 追加byte数组
	 * 
	 * @param byteList
	 * @param bytes
	 * @return
	 */
	public static List<Byte> addBytes(List<Byte> byteList, byte[] bytes,
			int nStart, int nLength) {
		for (int i = 0; i < nLength; i++) {
			byteList.add(bytes[i + nStart]);
		}
		return byteList;
	}

	/**
	 * byteList集合转换成byte []数组
	 * 
	 * @param byteList
	 * @return
	 */
	public static byte[] listToBytes(List<Byte> byteList) {
		byte[] bytes = new byte[byteList.size()];
		for (int i = 0; i < byteList.size(); i++) {
			bytes[i] = byteList.get(i);
		}
		return bytes;
	}
	
	/**
	 * byteList集合转换成byte []数组
	 * 
	 * @param byteList
	 * @return
	 */
	public static byte[] listToBytes(List<Byte> byteList,int begin,int end) {
		byte[] bytes = new byte[end-begin];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = byteList.get(begin + i);
		}
		return bytes;
	}
	
	/**
	 * 字节重组
	 * @param byteList
	 * @param index
	 * @param length
	 * @return
	 */
	public static byte[] subBytes(byte [] byteList ,int index,int length){
		byte[] bytes = new byte[length];
		int j = 0;
		for (int i = index; i < length; i++,j++) {
			bytes[j] = byteList[i];
		}
		return bytes;
	}
	/**
	 * 测试用，打印
	 * 
	 * @param byteList
	 */
	public static void printListByte(List<Byte> byteList) {
		for (int i = 0; i < byteList.size(); i++) {
			//System.out.print(byteList.get(i) + " ");
		}
		//System.out.println();
	}

	/**
	 * 测试用，打印
	 * 
	 * @param bytes
	 */
	public static void printBytes(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			//System.out.print(Integer.toHexString(SocketEntityUtil.byteToInt(bytes, i, 1)) +" ");
//			Integer.toHexString(SocketEntityUtil.byteToInt(bytes, i, 1));
		}
		//System.out.println();
	}
}
