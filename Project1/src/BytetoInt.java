
public class BytetoInt {
	public int transtoByte(String s) {
		int i = 7, mul = 1, temp;
		int[] num = new int[8];
		int result = 0;
		while (i >= 0) {
			temp = Integer.parseInt(s.substring(i, i + 1));
			num[i] = temp;
			i--;
		}

		// 转换为补码形式
		if (num[0] == 1) {
			int j = 7;
			while (j > 0) {
				if(num[j]==0) num[j]=1;
				else num[j] = 0;
				j--;
			}
			if (num[7] == 1) {
				num[7] = 0;
				j = 6;
				while (j > 0) {
					if (num[j] == 1) {
						num[j] = 0;
					} else {
						num[j] = 1;
						break;
					}
					j--;
				}
			} else num[7]=1;
		}

		// 计算值
		i = 7;
		while (i > 0) {
			temp = num[i];
			result += mul * temp;
			mul *= 2;
			i--;
		}
		if (num[0] == 1)
			result = -result;
		return result;
	}
}
