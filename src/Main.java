import Client.USCPClient;
import Server.USCPServer;
import Utils.DPRunnable;
import Utils.DataPackage;
import Utils.Sender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        USCPServer server = new USCPServer(59689, new MyRunnableServer());
        new Thread(server::StartListen).start();
        USCPClient client = new USCPClient("localhost", 59689, new MyDpRunnable());

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in,
                StandardCharsets.UTF_8));
        String temp;
        try {
            while ((temp = bufferedReader.readLine()) != null) {
                if (temp.equals("exit 0"))
                    break;
                client.Send(temp.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        server.StopServer();
        client.Close();
    }
}

class MyDpRunnable implements DPRunnable {
    private DataPackage dataPackage;

    @Override
    public void SetDp(DataPackage dataPackage) {
        this.dataPackage = dataPackage;
    }

    @Override
    public void run() {
        System.out.println("收到消息:" + this.dataPackage.originalStringValue);
    }
}

class MyRunnableServer implements DPRunnable {
    private DataPackage dataPackage;

    @Override
    public void SetDp(DataPackage dataPackage) {
        this.dataPackage = dataPackage;
    }

    @Override
    public void run() {
        Sender.SendMesage(this.dataPackage.socket, "<服务端回复>:" + this.dataPackage.originalStringValue);
    }
}