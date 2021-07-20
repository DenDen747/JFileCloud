import com.denesgarda.JFileCloud.Client;
import com.denesgarda.JFileCloud.Credentials;

public class Main extends Client {
    public Main() {
        super("localhost", 7900);
        //boolean result = this.fetch(new Credentials("admin", "admin"), "files");
        //System.out.println(result);
        boolean result = this.push(new Credentials("admin", "admin"), "files");
        System.out.println(result);
    }

    public static void main(String[] args) {
        new Main();
    }
}
