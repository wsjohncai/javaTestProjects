import java.io.*;
import java.util.Scanner;

public class FileEncryption {

    private int KEY = 0x99;
    private int[] POI = {9, 12, 25, 38, 50, 68, 74, 89, 91, 98, 105, 113};

    public static void main(String[] args) {
        FileEncryption fileEncryption = new FileEncryption();
        fileEncryption.fromConsole();
    }

    private void fromConsole() {
        System.out.println("Choose a tool to continue:\n");
        System.out.println("1.Encrypt file    2.Decrypt file: \n");
        Scanner sc = new Scanner(System.in);
        int chosen = sc.nextInt();
        sc.nextLine();
        System.out.println("Please enter the file path:\n");
        String path = sc.nextLine();
        operate(chosen, path);
        sc.close();
    }

    private void operate(int op, String path) {
        File origin = new File(path);
        if (!origin.exists()) {
            System.out.println("File not exists.");
            return;
        }
        String fileName = origin.getName();
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        if(name.contains("_encrypt"))
            name = name.substring(0, name.lastIndexOf("_encrypt"));
        else if(name.contains("_decrypt"))
            name = name.substring(0, name.lastIndexOf("_decrypt"));
        String postfix = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
        //Construct the new name of the file
        String newName = name + (op == 1 ? "_encrypt" : op == 2 ? "_decrypt" : "_null") + postfix;
        String oldPath = origin.getPath();
        //Construct the new path of the file
        String newPath = oldPath.substring(0, oldPath.lastIndexOf('\\') + 1) + newName;
        if (newName.contains("_null")) {
            System.out.println("Wrong operation.");
            return;
        }

        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(origin));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newPath));
            byte[] bytes = new byte[128];
            int len;
            while ((len = bis.read(bytes, 0, bytes.length)) != -1) {
                for (int p : POI) {
                    if (p >= len)
                        break;
                    bytes[p] ^= KEY;
                }
                bos.write(bytes);
                bos.flush();
            }
            bis.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
