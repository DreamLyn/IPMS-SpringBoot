package com.jktd.ipms.tools;

import com.jktd.ipms.beans.net.SyncDataBean;
import io.netty.buffer.ByteBuf;

public class CommonFun {

//	/*
//	 * CRCУ��
//	 */
//	public static int getCRC(byte[] buffer, int length) {
//		int crc = 0x0;//
//		int polynomial = 0x1021;
//		for (int j = 0; j < length; j++) {
//			char b = (char) buffer[j];
//			for (int i = 0; i < 8; i++) {
//				boolean bit = ((b >> (7 - i) & 1) == 1);
//				boolean c15 = ((crc >> 15 & 1) == 1);
//				crc <<= 1;
//				if (c15 ^ bit) {
//					crc ^= polynomial;
//				}
//			}
//		}
//		crc &= 0xffff;
//		return crc;
//	}
//

    /**
     * 将Short转换为字节流
     * @param data
     * @return
     */
    public static byte[] getByteFromShort(int data) {
        byte[] temp = new byte[2];
        temp[0] = (byte) (0xFF & (data));
        temp[1] = (byte) (0xFF & (data >> 8));
        return temp;
    }

    /**
     * 将Int转换为字节流
     * @param data
     * @return
     */
    public static byte[] getByteFromInt(int data) {
        byte[] temp = new byte[4];
        temp[0] = (byte) (0xFF & (data));
        temp[1] = (byte) (0xFF & (data >> 8));
        temp[2] = (byte) (0xFF & (data >> 16));
        temp[3] = (byte) (0xFF & (data >> 24));
        return temp;
    }

    /**
     * 将Long转换为字节流
     * @param data
     * @return
     */
    public static byte[] getByteFromLong(long data) {
        byte[] temp = new byte[8];
        temp[0] = (byte) (0xFF & (data));
        temp[1] = (byte) (0xFF & (data >> 8));
        temp[2] = (byte) (0xFF & (data >> 16));
        temp[3] = (byte) (0xFF & (data >> 24));
        temp[4] = (byte) (0xFF & (data >> 32));
        temp[5] = (byte) (0xFF & (data >> 40));
        temp[6] = (byte) (0xFF & (data >> 48));
        temp[7] = (byte) (0xFF & (data >> 56));
        return temp;
    }

    /**
     * 将Double转换为字节流
     * @param data
     * @return
     */
    public static byte[] getByteFromDouble(double data) {
        byte[] temp = new byte[8];
        long tempLong = Double.doubleToLongBits(data);
        temp[0] = (byte) (0xFF & (tempLong));
        temp[1] = (byte) (0xFF & (tempLong >> 8));
        temp[2] = (byte) (0xFF & (tempLong >> 16));
        temp[3] = (byte) (0xFF & (tempLong >> 24));
        temp[4] = (byte) (0xFF & (tempLong >> 32));
        temp[5] = (byte) (0xFF & (tempLong >> 40));
        temp[6] = (byte) (0xFF & (tempLong >> 48));
        temp[7] = (byte) (0xFF & (tempLong >> 56));
        return temp;
    }

    /**
     * 字节流转Long
     * @param bytes
     * @return
     */
    public static long getLong(byte[] bytes) {
        return ((0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8)) | (0xff0000L & ((long) bytes[2] << 16))
                | (0xff000000L & ((long) bytes[3] << 24)) | (0xff00000000L & ((long) bytes[4] << 32))
                | (0xff0000000000L & ((long) bytes[5] << 40)) | (0xff000000000000L & ((long) bytes[6] << 48))
                | (0xff00000000000000L & ((long) bytes[7] << 56)));
    }

    /**
     * 从字节流转化为Short
     * @param bytes
     * @param offset
     * @return
     */
    public static int getShortWithOffset(byte[] bytes, int offset) {
        return ((0xff & bytes[0 + offset]) | (0xff00 & (bytes[1 + offset] << 8)));
    }

    /**
     * 从字节流转化为Int
     * @param bytes
     * @param offset
     * @return
     */
    public static int getIntWithOffset(byte[] bytes, int offset) {
        return ((0xff & bytes[0 + offset]) | (0xff00 & (bytes[1 + offset] << 8))
                | (0xff0000 & (bytes[2 + offset] << 16)) | (0xff000000 & (bytes[3 + offset] << 24)));
    }

    /**
     * @param bytes
     * @param offset
     * @return
     */
    public static long getLongWithOffset(byte[] bytes, int offset) {
        return ((0xffL & (long) bytes[0 + offset]) | (0xff00L & ((long) bytes[1 + offset] << 8))
                | (0xff0000L & ((long) bytes[2 + offset] << 16)) | (0xff000000L & ((long) bytes[3 + offset] << 24))
                | (0xff00000000L & ((long) bytes[4 + offset] << 32))
                | (0xff0000000000L & ((long) bytes[5 + offset] << 40))
                | (0xff000000000000L & ((long) bytes[6 + offset] << 48))
                | (0xff00000000000000L & ((long) bytes[7 + offset] << 56)));
    }

	/**
	 * 解析同步数据
	 *
	 * @param byteSyncData
	 * @return
	 */
    public static SyncDataBean getSyncBean(byte[] byteSyncData) {
        SyncDataBean syncDataBean = new SyncDataBean();
        syncDataBean.setPackageIndex((short) (byteSyncData[7] & 0xFF));
        syncDataBean.setDeviceId(getLongWithOffset(byteSyncData,8));
        syncDataBean.setSyncIndex((short) (byteSyncData[16] & 0xFF));
        syncDataBean.setUpBaseTimestamp(getLongWithOffset(byteSyncData,20));
        syncDataBean.setSlaverTimestamp(getLongWithOffset(byteSyncData,28));
        syncDataBean.setMasterSlaverDiff(getLongWithOffset(byteSyncData,36));
        syncDataBean.setOlsCoefficient(Double.longBitsToDouble(getLongWithOffset(byteSyncData,44)));
        syncDataBean.setFirstPathRssi(Float.intBitsToFloat(getIntWithOffset(byteSyncData,68)));
        syncDataBean.setRssi(Float.intBitsToFloat(getIntWithOffset(byteSyncData,72)));
        return syncDataBean;
    }

    /**
     * 根据byteBuf获取同步bean
     * @param byteBuf
     * @return
     */
    public static SyncDataBean getSyncBean(ByteBuf byteBuf) {
        SyncDataBean syncDataBean = new SyncDataBean();
        syncDataBean.setPackageIndex((short) byteBuf.getByte(7));
        syncDataBean.setDeviceId(byteBuf.getLongLE(8));
        syncDataBean.setSyncIndex((short) byteBuf.getByte(16));
        syncDataBean.setUpBaseTimestamp(byteBuf.getLongLE(20));
        syncDataBean.setSlaverTimestamp(byteBuf.getLongLE(28));
        syncDataBean.setMasterSlaverDiff(byteBuf.getLongLE(36));
        syncDataBean.setOlsCoefficient(Double.longBitsToDouble(byteBuf.getLongLE(44)));
        syncDataBean.setFirstPathRssi(Float.intBitsToFloat(byteBuf.getIntLE(68)));
        syncDataBean.setRssi(Float.intBitsToFloat(byteBuf.getIntLE(72)));
        return syncDataBean;
    }

    /**
     * Float转字节流
     * @param data
     * @return
     */
    public static byte[] getByteFromFloat(float data){
        byte[] temp=new byte[4];
        int tempInt=Float.floatToIntBits(data);
        temp[0]=(byte)(0xFF&(tempInt));
        temp[1]=(byte)(0xFF&(tempInt>>8));
        temp[2]=(byte)(0xFF&(tempInt>>16));
        temp[3]=(byte)(0xFF&(tempInt>>24));
        return temp;
    }

//
//	/**
//	 * 解析定位数据
//	 *
//	 * @param byteLocData
//	 * @return
//	 */
//	public static TDOADataBean getTDOABean(byte[] byteLocData) {
//		TDOADataBean locDataBean = new TDOADataBean();
//
//		locDataBean.setDataIndex((byteLocData[7]));
//
//		locDataBean.setDeviceID((0xffL & (long) byteLocData[8]) | (0xff00L & ((long) byteLocData[9] << 8))
//				| (0xff0000L & ((long) byteLocData[10] << 16)) | (0xff000000L & ((long) byteLocData[11] << 24))
//				| (0xff00000000L & ((long) byteLocData[12] << 32)) | (0xff0000000000L & ((long) byteLocData[13] << 40))
//				| (0xff000000000000L & ((long) byteLocData[14] << 48))
//				| (0xff00000000000000L & ((long) byteLocData[15] << 56)));
//
//		locDataBean.setTagId((0xffL & (long) byteLocData[16]) | (0xff00L & ((long) byteLocData[17] << 8))
//				| (0xff0000L & ((long) byteLocData[18] << 16)) | (0xff000000L & ((long) byteLocData[19] << 24))
//				| (0xff00000000L & ((long) byteLocData[20] << 32)) | (0xff0000000000L & ((long) byteLocData[21] << 40))
//				| (0xff000000000000L & ((long) byteLocData[22] << 48))
//				| (0xff00000000000000L & ((long) byteLocData[23] << 56)));
//
//		locDataBean.setLocIndex((byteLocData[24]));
//
//		locDataBean.setTimeStamp((0xffL & (long) byteLocData[28]) | (0xff00L & ((long) byteLocData[29] << 8))
//				| (0xff0000L & ((long) byteLocData[30] << 16)) | (0xff000000L & ((long) byteLocData[31] << 24))
//				| (0xff00000000L & ((long) byteLocData[32] << 32)) | (0xff0000000000L & ((long) byteLocData[33] << 40))
//				| (0xff000000000000L & ((long) byteLocData[34] << 48))
//				| (0xff00000000000000L & ((long) byteLocData[35] << 56)));
//
//		locDataBean.setSyncIndex((byteLocData[36]));
//		locDataBean.setStatus(byteLocData[39]);
//		byte[] tempByte = new byte[4];
//		System.arraycopy(byteLocData, 40, tempByte, 0, 4);
//		locDataBean.setFirstPathRssi(Float.intBitsToFloat(getInt(tempByte)));
//		System.arraycopy(byteLocData, 44, tempByte, 0, 4);
//		locDataBean.setRssi(Float.intBitsToFloat(getInt(tempByte)));
//
//		return locDataBean;
//	}
//
//	/**
//	 *
//	 * @param byteLocData
//	 * @return
//	 */
//	public static TDOADataBean getTDOABean(ByteBuf byteBuf, HashMap<Long, DevicePos> deviceHashMap) {
//
//		TDOADataBean locDataBean = new TDOADataBean();
//
//		locDataBean.setDataIndex((byteBuf.getByte(7)));///// 包序号
//
//		Long deviceID = byteBuf.getLongLE(8); ////
//
//		locDataBean.setDeviceID(deviceID);
//
//		locDataBean.setTagId(byteBuf.getLongLE(16));
//
//		locDataBean.setLocIndex((byteBuf.getByte(24)));
//
//		locDataBean.setTagType((byteBuf.getByte(25))); //// 标签类型
//
//		locDataBean.setTagState((byteBuf.getShortLE(26)));
//
//		locDataBean.setTimeStamp(byteBuf.getLongLE(28));//// 时间戳
//
//		locDataBean.setSyncIndex((byteBuf.getByte(36)));
//
//		locDataBean.setSlotIndex(byteBuf.getShortLE(37));
//
//		locDataBean.setStatus(byteBuf.getByte(39));
//
//		byte[] tempByte = new byte[4];
//		byteBuf.getBytes(40, tempByte);
//		locDataBean.setFirstPathRssi(Float.intBitsToFloat(CommonFun.getInt(tempByte)));
//
//		byteBuf.getBytes(44, tempByte);
//		locDataBean.setRssi(Float.intBitsToFloat(CommonFun.getInt(tempByte)));
//		if (deviceHashMap.containsKey(deviceID)) {
//			DevicePos devicePos = deviceHashMap.get(deviceID);
//			locDataBean.setX(devicePos.getX());
//			locDataBean.setY(devicePos.getY());
//			locDataBean.setZ(devicePos.getZ());
//		}
//
//		return locDataBean;
//
//	}
//
//	/**
//	 * post
//	 **/
//	public static String sendPost(String url, String param) {
//		PrintWriter out = null;
//		BufferedReader in = null;
//		String result = "";
//		try {
//			URL realUrl = new URL(url);
//			// 打开和URL之间的连接
//			URLConnection conn = realUrl.openConnection();
//			// 设置通用的请求属性
//			conn.setRequestProperty("accept", "*/*");
//			conn.setRequestProperty("connection", "Keep-Alive");
//			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//			// 发送POST请求必须设置如下两行
//			conn.setDoOutput(true);
//			conn.setDoInput(true);
//			// 获取URLConnection对象对应的输出流
//			out = new PrintWriter(conn.getOutputStream());
//			// 发送请求参数
//			out.print(param);
//			// flush输出流的缓冲
//			out.flush();
//			// 定义BufferedReader输入流来读取URL的响应
//			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String line;
//			while ((line = in.readLine()) != null) {
//				result += line;
//			}
//		} catch (Exception e) {
//			System.out.println("发送 POST 请求出现异常！" + e);
//			e.printStackTrace();
//		}
//		// 使用finally块来关闭输出流、输入流
//		finally {
//			try {
//				if (out != null) {
//					out.close();
//				}
//				if (in != null) {
//					in.close();
//				}
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//		return result;
//	}
//
//	public static Long ipToLong(String ip) {
//		Long ips = 0L;
//		String[] numbers = ip.split("\\.");
//		// 等价上面
//		for (int i = 0; i < 4; ++i) {
//			ips = ips << 8 | Integer.parseInt(numbers[i]);
//		}
//		return ips;
//	}
//
//	public static String longToIp(long ipaddress) {
//		StringBuffer sb = new StringBuffer("");
//		sb.append(String.valueOf((ipaddress >>> 24)));
//		sb.append(".");
//		sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
//		sb.append(".");
//		sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
//		sb.append(".");
//		sb.append(String.valueOf((ipaddress & 0x000000FF)));
//		return sb.toString();
//	}
//	public static String longToIpR(long ipaddress) {
//		StringBuffer sb = new StringBuffer("");
//		sb.append(String.valueOf((ipaddress & 0x000000FF)));
//		sb.append(".");
//		sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
//		sb.append(".");
//		sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
//		sb.append(".");
//		sb.append(String.valueOf(((ipaddress&0xFFFFFFFF) >>> 24)&0xFF));
//		return sb.toString();
//	}
//	public static void sendOData(Destination destinationM, byte[] sendData) {
//		// 开始发送数据
//		DatagramSocket ds;
//		try {
//			ds = new DatagramSocket();
//			InetAddress destination;
//			DatagramPacket dp;
//			destination = InetAddress.getByName(CommonFun.longToIp(destinationM.getDistIP()));
//			dp = new DatagramPacket(sendData, sendData.length, destination, destinationM.getDistPort());
//			ds.send(dp);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 向目标地址发送定位数据
//	 *
//	 * @param distList
//	 * @param position
//	 * @param tagID
//	 */
//	public static void sendPosition(ArrayList<Destination> distList, Position position, long tagID,short tagState) {
//		// 准备发送数据
////		System.out.println(position.getZoneid());
//		byte[] sendData = new byte[46];
//		long tmp = tagID & 0xFFFFFFFF;
//		sendData[0] = 'L';
//		sendData[1] = 'O';
//		sendData[2] = 'C';
//		sendData[3] = 'L';
//		sendData[6] = 1;
//		sendData[8] = (byte) (0xff & position.getZoneid());
//		sendData[9] = (byte) ((0xff00 & position.getZoneid()) >> 8);
//		sendData[10] = (byte) (0xff & tmp);
//		sendData[11] = (byte) ((0xff00 & tmp) >> 8);
//		sendData[12] = (byte) ((0xff0000 & tmp) >> 16);
//		sendData[13] = (byte) ((0xff000000 & tmp) >> 24);
//		long l = Double.doubleToLongBits(position.getX() * 100);
//		for (int i = 0; i < 8; i++) {
//			sendData[14 + i] = new Long(l).byteValue();
//			l = l >> 8;
//		}
//		l = Double.doubleToLongBits(position.getY() * 100);
//		for (int i = 0; i < 8; i++) {
//			sendData[22 + i] = new Long(l).byteValue();
//			l = l >> 8;
//		}
//		System.out.println("标签状态信息："+tagState);
//		int sos,exercise,quantity;
//
//		byte temp = (byte) ((tagState >> 9) & 0x03);
//		exercise=temp;
//
//		temp = (byte) ((tagState >> 11) & 0x07);
//		if (temp==0x04) {
//			sos=1;
//		}else{
//			sos=0;
//		}
//		sendData[38]=(byte)(sendData[38]&0xFE);
//		sendData[38]|=(sos);
//		sendData[38]=(byte)(sendData[38]&0xF3);
//		sendData[38]|=(exercise<<2);
//
//		System.out.println("ID:"+Long.toHexString(tagID)+",SOS:"+sos);
//		temp = (byte) (tagState & 0x7F);
//		sendData[39]=(byte) temp;
//
//		// 开始发送数据
//		DatagramSocket ds;
//		try {
//			ds = new DatagramSocket();
//			InetAddress destination;
//			DatagramPacket dp;
//			if (distList != null) {
//				for (int count = 0; count < distList.size(); count++) {
//					Destination destination2 = distList.get(count);
//					destination = InetAddress.getByName(CommonFun.longToIp(destination2.getDistIP()));
//					dp = new DatagramPacket(sendData, sendData.length, destination, destination2.getDistPort());
//					ds.send(dp);
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 判断点是否在多边形内
//	 * @param point
//	 * @param points
//	 * @return
//	 */
//	public static boolean IsPointInPoly(ZonePoint point, ArrayList<ZonePoint> points,int zoneid){
//		if(zoneid==1){
//			return true;
//		}
//	    int N = points.size();
//	    boolean boundOrVertex = true; //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
//	    int intersectCount = 0;//cross points count of x
//	    double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
//	    ZonePoint p1, p2;//neighbour bound vertices
//	    ZonePoint p = point; //当前点
//
//	    p1 = points.get(0);//left vertex
//	    for(int i = 1; i <= N; ++i){//check all rays
//	        if(p.equals(p1)){
//	            return boundOrVertex;//p is an vertex
//	        }
//
//	        p2 = points.get(i % N);//right vertex
//	        if(p.getPointx() < Math.min(p1.getPointx(), p2.getPointx()) || p.getPointx() > Math.max(p1.getPointx(), p2.getPointx())){//ray is outside of our interests
//	            p1 = p2;
//	            continue;//next ray left point
//	        }
//
//	        if(p.getPointx() > Math.min(p1.getPointx(), p2.getPointx()) && p.getPointx() < Math.max(p1.getPointx(), p2.getPointx())){//ray is crossing over by the algorithm (common part of)
//	            if(p.getPointy() <= Math.max(p1.getPointy(), p2.getPointy())){//x is before of ray
//	                if(p1.getPointx() == p2.getPointx() && p.getPointy() >= Math.min(p1.getPointy(), p2.getPointy())){//overlies on a horizontal ray
//	                    return boundOrVertex;
//	                }
//
//	                if(p1.getPointy() == p2.getPointy()){//ray is vertical
//	                    if(p1.getPointy() == p.getPointy()){//overlies on a vertical ray
//	                        return boundOrVertex;
//	                    }else{//before ray
//	                        ++intersectCount;
//	                    }
//	                }else{//cross point on the left side
//	                    double xinters = (p.getPointx() - p1.getPointx()) * (p2.getPointy() - p1.getPointy()) / (p2.getPointx() - p1.getPointx()) + p1.getPointy();//cross point of y
//	                    if(Math.abs(p.getPointy() - xinters) < precision){//overlies on a ray
//	                        return boundOrVertex;
//	                    }
//
//	                    if(p.getPointy() < xinters){//before ray
//	                        ++intersectCount;
//	                    }
//	                }
//	            }
//	        }else{//special case when ray is crossing through the vertex
//	            if(p.getPointx() == p2.getPointx() && p.getPointy() <= p2.getPointy()){//p crossing over p2
//	                ZonePoint p3 = points.get((i+1) % N); //next vertex
//	                if(p.getPointx() >= Math.min(p1.getPointx(), p3.getPointx()) && p.getPointx() <= Math.max(p1.getPointx(), p3.getPointx())){//p.getPointx() lies between p1.getPointx() & p3.getPointx()
//	                    ++intersectCount;
//	                }else{
//	                    intersectCount += 2;
//	                }
//	            }
//	        }
//	        p1 = p2;//next ray left point
//	    }
//
//	    if(intersectCount % 2 == 0){//偶数在多边形外
//	        return false;
//	    } else { //奇数在多边形内
//	        return true;
//	    }
//
//	}
}
