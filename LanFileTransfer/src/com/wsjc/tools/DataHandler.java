package com.wsjc.tools;

import com.wsjc.data.*;

/**
 * This tool is used to exact a required Data object or a byte array to be send
 * 
 * @author WSJohnCai
 */
public class DataHandler {
	private Data data;
	private byte[] rdata, rawData, type;

	public DataHandler(Data data) {
		this.data = data;
		type = Integer.toString(data.getType()).getBytes();
		if (data.getData() != null)
			rawData = data.getData().getBytes();
	}

	/**
	 * This constructor is used to return the Data object;
	 * 
	 * @param raw
	 *            The binary data that is received from the sender
	 * @return a {@link Data} object instance
	 */
	public DataHandler(byte[] raw) {
		rdata = raw;
	}

	public byte[] getSendData() {
		if (rawData == null) {
			rdata = new byte[4];
			int i = 0;
			for (byte b : type) {
				rdata[i++] = b;
			}
			return rdata;
		} else {
			rdata = new byte[rawData.length + 4];
			int i = 0;
			for (byte b : type) {
				rdata[i++] = b;
			}
			i = 4;
			for (byte b : rawData) {
				rdata[i++] = b;
			}
			return rdata;
		}
	}

	public Data getDataObject() {
		int len1 = 0, len2 = 0;
		type = new byte[4];
		rawData = new byte[rdata.length - 4];
		for (int j = 0; j < 4; j++) {
			byte b = rdata[j];
			if (b != 0) {
				type[len1++] = b;
			} else
				break;
		}
		for (int j = 4; j < rdata.length; j++) {
			byte b = rdata[j];
			if (b != 0) {
				rawData[len2++] = b;
			} else
				break;
		}
		String a = new String(type, 0, len1);
		int t = Integer.parseInt(a);
		String b = new String(rawData, 0, len2);
		if (b.length() == 0)
			data = new Data(t);
		else
			data = new Data(t, b);

		return data;
	}
}
