import java.io.IOException;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.*;

public class DataCatcher {

	public static void main(String[] args) {
		final NetworkInterface[] devices = JpcapCaptor.getDeviceList(); // 获取设备列表
		// for(NetworkInterface i: devices) {
		// System.out.println(i.description);
		// NetworkInterfaceAddress[] addresses = i.addresses;
		// for(NetworkInterfaceAddress a : addresses) {
		// System.out.println(a.address.getHostAddress());
		// }
		// System.out.println("");
		// }
		int nd = Integer.parseInt(args[0]);
		try {
			JpcapCaptor captor = JpcapCaptor.openDevice(devices[nd], 1024, true, 10000);
			captor.setFilter("tcp", true);

			// ICMPPacket icmp = new ICMPPacket();
			// icmp.type = 8;
			// icmp.data = new byte[32];
			// icmp.setIPv4Parameter(0, false, false, false, 0, false, true, false, 0, 0,
			// 64, 1,
			// InetAddress.getByName("192.168.155.2"),
			// InetAddress.getByName("www.baidu.com"));
			// JpcapSender sender = JpcapSender.openRawSocket();
			// sender.sendPacket(icmp);
			while (true) {
				IPPacket p = (IPPacket) captor.getPacket();
				if (p == null)
					continue;
				byte[] data = p.header;
				System.out.print("Source: " + p.src_ip.getHostAddress());
				System.out.print("  Destination: " + p.dst_ip.getHostAddress() + "  ");
				for (byte i : data) {
					System.out.print(Integer.toHexString(i & 0xFF) + " ");
				}
				System.out.println("");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class Receiver implements PacketReceiver {

		@Override
		public void receivePacket(Packet arg0) {

		}

	}

}
