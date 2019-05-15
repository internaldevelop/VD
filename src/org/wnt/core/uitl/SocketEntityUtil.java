package org.wnt.core.uitl;

import java.util.ArrayList;
import java.util.List;
import com.google.common.primitives.UnsignedBytes;
/**
 * 处理字节的相关帮助类
 * 
 * @author 赵俊鹏
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class SocketEntityUtil {

	/**
	 * IP转换为数值
	 * 
	 * @param ip
	 * @return
	 */
	public static long ipToLong(String ip) {
		String[] split = new String [4];
			
		split = ip.split("\\.");
		byte ipByte[] = new byte[4];
		if (ip != null && !"".equals(ip) && split.length == 4) {
			ipByte[0] = (byte) (0xff & Integer.parseInt(split[0]));
			ipByte[1] = (byte) (0xff & Integer.parseInt(split[1]));
			ipByte[2] = (byte) (0xff & Integer.parseInt(split[2]));
			ipByte[3] = (byte) (0xff & Integer.parseInt(split[3]));
		}

		return byteToLong(ipByte, 0, 4);
	}
	
	public static byte[] ipTobyte(String ip) {
		String[] split = new String [4];			
		split = ip.split("\\.");
		byte ipByte[] = new byte[4];
		if (ip != null && !"".equals(ip) && split.length == 4) {
			ipByte[0] = (byte) (0xff & Integer.parseInt(split[0]));
			ipByte[1] = (byte) (0xff & Integer.parseInt(split[1]));
			ipByte[2] = (byte) (0xff & Integer.parseInt(split[2]));
			ipByte[3] = (byte) (0xff & Integer.parseInt(split[3]));
		}

		return ipByte;
	}

	/**
	 * byteToInt
	 * 
	 * @param data
	 * @param index
	 * @param length
	 * @return
	 */
	public static int byteToInt(byte[] data, int index, int length) {
		byte bs[] = new byte[length];
		int value = 0;
		int k = 0;
		for (int i = 0; i < length; i++) {
			byte bv = data[index + i];
			bs[i] = bv;
			int v = UnsignedBytes.toInt(bv);
			if (length > 1) {
				value += v << ((length - ++k) * 8);
			} else {
				value += v;

			}
		}
		return value;
	}

	/**
	 * 
	 * @param data
	 * @param index
	 * @param length
	 * @return
	 */
	public static String byteToUUID(byte[] data, int index, int length) {
		String value = "";
		for (int i = 0; i < length; i++) {
			String hexString = Integer.toHexString(UnsignedBytes
					.toInt(data[index + i]));
			if (hexString.length() == 1) {
				value += 0;
			}
			value += hexString;
		}
		return value;
	}

	/**
	 * byteToInt
	 * 
	 * @param data
	 * @param index
	 * @param length
	 * @return
	 */
	public static long byteToLong(byte[] data, int index, int length) {
		long value = 0;
		int k = 0;
		for (int i = 0; i < length; i++) {
			byte bv = data[index + i];
			long longValue = UnsignedBytes.toInt(bv);
			if (length > 1) {
				value += longValue << ((length - ++k) * 8);
			} else {
				value += longValue;

			}
		}
		return value;
	}

	/**
	 * byteToString
	 * 
	 * @param data
	 * @param index
	 * @param length
	 * @return
	 */
	public static String byteToString(byte[] data, int index, int length) {
		String value = "";
		for (int i = 0; i < length; i++) {

			value += (char) (data[index + i]);
		}
		return value;
	}

	public static String ipString(long ip) {
		return ipString(ip, 4);
	}

	/**
	 * ip数值转换成显示的正常IP串
	 * 
	 * @param ip
	 * @param length
	 * @return
	 */
	private static String ipString(long ip, int length) {
		String value = "";
		long i = ip >> 24;
		value += i;
		value += ".";
		ip = ip - (i << 24);

		i = ip >> 16;
		value += i;
		value += ".";
		ip = ip - (i << 16);

		i = ip >> 8;
		value += i;
		value += ".";
		ip = ip - (i << 8);

		value += ip;
		return value;
	}

	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。 和bytesToInt2（）配套使用
	 */
	public static byte[] intToBytes(long value, int length) {
		byte[] bytes = new byte[length];

		for (int i = 0; i < length; i++) {
			int num = length - 1 - i;
			if (num > 0) {
			//	System.out.println(value >> (num * 8));
				bytes[i] = (byte) ((value >> (num * 8)) & 0xFF);
				
			} else {
				bytes[i] = (byte) (value & 0xFF);
			}
		}

		return bytes;
	}
	
	
	public static byte[] StringToBytes(String value, int length) {
		char[] cdata=value.toCharArray();
		byte[] b = new byte[length];
		for (int j = 0; j < length; j++) {
			int num = length - 1 - j;
			if (num > 0) {
			for(int i=0;i<cdata.length;i++){
			char c= cdata[i];
				b[i] = (byte) ((c & 0xFF00) >> 8);
			}
			}else{
			for(int i=0;i<cdata.length;i++){
				char c= cdata[i];
				b[i] = (byte) (c & 0xFF);
			}
			}
		}
	return b;
	}
	
	/**
	 * IP模糊查询使用转换*
	 * 
	 * @param srcIp
	 * @return
	 */
	public static List<String> getCondtionIps(String srcIp) {
		String[] ips = srcIp.split("\\.");
		String ip = "";
		for (int i = 0; i < ips.length; i++) {
			if ("*".equals(ips[i])) {
				break;
			}
			if (i != 0) {
				ip += ".";
			}
			ip += ips[i];
		}

		srcIp.trim();
		List<String> list = new ArrayList<String>();
		String ip2 = "";
		int length = ip.split("\\.").length;
		if (length < 4) {
			ip2 = ip;
			for (int i = 0; i < 4 - length; i++) {
				ip += ".0";
				ip2 += ".255";
			}
		}
		list.add(ip);
		if (!"".equals(ip2)) {
			list.add(ip2);
		}
		return list;
	}

	public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }
	
	public static String bytesTo16Str(){
		
		return "";
	}
	
	/**
	 * 转MAC
	 * @param data
	 * @param index
	 * @param length
	 * @return
	 */
	public static String bytesToMac(byte[] data,int index,int length){ 
		String str = "";
		for (int i = 0; i < length; i++) {
			byte bv = data[index + i];
			String hexString = Integer.toHexString(UnsignedBytes.toInt(bv));
			if(i!=0){
				str+=":";
			}
			if(hexString.length()!=2){
				str+="0";
			}
			str+=hexString;
		}
		
		return str;
	}
	
	 /** 
     * float转换byte,zll 
     * @param x 
     * @param index 
     */  
    public static byte[] FloatToByteArray( float x, int length) {  
      
    	byte[] bb=new byte[length];
        int l = Float.floatToIntBits(x); 
        for (int j = 0; j <length-4; j++){
        	bb[j]=0;
        }
       for (int i = 0; i <4; i++) {  
            bb[ length-i-1] = new Integer(l).byteValue();  
            l = l >> 8;  
        }
        return bb;
    }  
    /**
	 * 通过byte数组取得float
	 *
	 * @param bb
	 * @param index
	 * @return
	 */ 
	public static float getFloat(byte[] b, int index) { 
	    int l; 
	    l = b[index + 0]; 
	    l &= 0xff; 
	    l |= ((long) b[index - 1] << 8); 
	    l &= 0xffff; 
	    l |= ((long) b[index - 2] << 16); 
	    l &= 0xffffff; 
	    l |= ((long) b[index - 3] << 24); 
	    return Float.intBitsToFloat(l); 
	}
    
 
    
    /** 
     * 64位double（正负均可）转换byte ,zll
     *  
     * @param x 
     * @param index 
     */  
    public static byte[] DoubleToByteArray( double x, int length) {  
        byte[] bb = new byte[length];  
        long l = Double.doubleToLongBits(x);  
        for (int i = 0; i <  length ; i++) {  
            bb[length-i-1] = new Long(l).byteValue();  
            l = l >> 8;  
        }
        return bb;
    }  
    /**
	 * 通过byte数组取得double
	 *
	 * @param bb
	 * @param index
	 * @return
	 */ 
    public static double getDouble(byte[] b, int index) { 
	    long l; 
	    l = b[index-1]; 
	    l &= 0xff; 
	    l |= ((long) b[index-2] << 8); 
	    l &= 0xffff; 
	    l |= ((long) b[index-3] << 16); 
	    l &= 0xffffff; 
	    l |= ((long) b[index-4] << 24); 
	    l &= 0xffffffffl; 
	    l |= ((long) b[index-5] << 32); 
	    l &= 0xffffffffffl; 
	    l |= ((long) b[index-6] << 40); 
	    l &= 0xffffffffffffl; 
	    l |= ((long) b[index-7] << 48); 
	    l &= 0xffffffffffffffl; 
	    l |= ((long) b[index-8] << 56); 
	    return Double.longBitsToDouble(l); 
	}
    /**
     * 转换32位short（正负均可）为byte,zll
     *
     * @param s 需要转换的short
     * @param index
     */ 
     public static byte[] ShortToByteArray( short s, int length) { 
    	  byte[] bb = new byte[length]; 
    	  for(int i = 0; i < length-2 ; i++)
    	  {
    		   bb[i]=0;
    	  }
          bb[length-2] = (byte)( (s >> 8) &0xff); 
          bb[length-1] = (byte)( (s >> 0) &0xff); 
          return bb;
     } 
     /**
      *将32位的int（正负均可）值放到4字节的byte数组,转为长度为8的数组，zll
     * @param num
      * @return
      */ 
      public static byte[] int4ToByteArray(long num ,int length) { 
         byte[] result = new byte[length]; 
         for(int i = 0; i < length-4; i++)
   	  {
        	 result[i]=0;
   	  }
         result[length-4] = (byte)(  (num >> 24)&0xff  );     //取最高8位放到0下标  
         result[length-3] = (byte)(    (num >> 16) &0xff        );            //取次高8为放到1下标  
         result[length-2] = (byte)(   (num >> 8)&0xff );             //取次低8位放到2下标  
         result[length-1] = (byte)(   (num ) &0xff );                  //取最低8位放到3下标  
         return result; 
      } 
      /**
       * 将4字节的byte数组转成一个int值
       * @param b
       * @return
       */
       public static int get4byteArray(byte[] b,int length){
           byte[] a = new byte[4];
           int i = a.length - 1,j = length - 1;
           for (; i >= 0 ; i--,j--) {//从b的尾部(即int值的低位)开始copy数据
               if(j >= 0)
                   a[i] = b[j];
               else
                   a[i] = 0;//如果b.length不足4,则将高位补0
           }
           int v0 = (a[0] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
           int v1 = (a[1] & 0xff) << 16;
           int v2 = (a[2] & 0xff) << 8;
           int v3 = (a[3] & 0xff) ;
           return v0 + v1 + v2 + v3;
       }
       /**
        * 将4字节的byte数组转成一个int值,32无符号
        * @param b
        * @return
        */
        public static long get4byteArray2(byte[] b,int length){
           
        	 byte[] a = new byte[4];
             int i = a.length - 1,j = length - 1;
             for (; i >= 0 ; i--,j--) {//从b的尾部(即int值的低位)开始copy数据
                 if(j >= 0)
                     a[i] = b[j];
                 else
                     a[i] = 0;//如果b.length不足4,则将高位补0
             }
             int v0 = (a[0] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
             int v1 = (a[1] & 0xff) << 16;
             int v2 = (a[2] & 0xff) << 8;
             int v3 = (a[3] & 0xff) ;
             long result = v0 | v1 | v2 | v3;
            
           
            return result&0x0FFFFFFFFl;
        }
      /**
       *将16位的int（均可）值放到2字节的byte数组,转为长度为8的数组，zll
      * @param num
       * @return
       */ 
       public static byte[] int2ToByteArray( long num ,int length) { 
          byte[] result = new byte[length]; 
          for(int i = 0; i < length-2; i++)
    	  {
         	 result[i]=0;
    	  }
          result[length-2] = (byte)(   (num >> 8)&0xff );             //取最高8位放到0下标
          result[length-1] = (byte)(   (num ) &0xff );                  //取最低8位放到3下标  
          return result; 
       } 
       /**
   	 * 通过byte数组取到short
   	 *
   	 * @param b
   	 * @param index  第几位开始取
   	 * @return
   	 */
   	 public static short getShort(byte[] b, int index) {
   	       return (short) (((b[index-2] << 8) | b[index-1] & 0xff));
   	 }
     /**
    	 * 通过byte数组取到short
    	 *
    	 * @param b
    	 * @param index  第几位开始取
    	 * @return
    	 */
   	 public static int getWord(byte[] b, int index) {
		 
		 short qq=(short) (((b[index-2] << 8) | b[index-1] & 0xff));
	       return qq&0x0FFFF;
	      
	 }
       /**
        *将8位的int（正负均可）值放到1字节的byte数组,转为长度为8的数组，zll
       * @param num
        * @return
        */ 
        public static byte[] int1ToByteArray(  int num ,int length) { 
           byte[] result = new byte[length]; 
           for(int i = 0; i < length-1; i++)
     	  {
          	 result[i]=0;
     	  }
                      //取最高8位放到0下标
           result[length-1] = (byte)(   (num ) &0xff );                  //取最低8位放到3下标  
           return result; 
        }
        /**
         * 获取有符号8位字符
         * @param b
         * @param index
         * @return
         */
        public static int getchar(byte[] b, int index) {
      	  int  y = (int) ( b[index-1] & 0xff);
      	  if(y>127){
      		    return y-(1<<8);
      	  }else{
      		  return (int) ( b[index-1] & 0xff); 
      	  }
  	 }
        /**
         * 获取8位无符号位
         * @param b
         * @param index
         * @return
         */
        public static int getin1t(byte[] b, int index) {
 	       return (int) (b[index-1] & 0xff);//((b[index-2] << 8) | b[index-1] & 0xff)
 	 }
	
}
