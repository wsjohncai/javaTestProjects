import java.util.Iterator;
import java.util.Vector;

public class InnerClassTest {

	public static void main(String[] args) {
		InnerClassTest i = new InnerClassTest();
		i.work();
		Iterator<String> it = i.v.iterator();
		while (it.hasNext()) {
			i.print(it.next());
		}
	}

	private int a = 2;
	private Vector<String> v = null;

	private void work() {
		v = new Vector();
		int i = 3;
//		while (i > 0) {
			InCla in = new InCla("da" + i);
			in.change2();
//			i--;
//		}
	}

	private void print(String x) {
		System.out.println(x);
	}

	class InCla {
		String x;

		public InCla(String s) {
			x = s;
		}

		public InCla() {

		}

		public void change() {
			a = 3;
			String temp = x;
			for (int i = 0; i < 3; i++)
				if (i == 2)
					v.addElement(temp);
		}

		public void change2() {
			InCla in = new InCla(x);
			in.change();
		}

	}

}
