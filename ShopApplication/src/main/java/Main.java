import java.util.ArrayList;

public class Main {
    private static ArrayList<String> a = new ArrayList<>();
    public static void main(String[] args) {
        a.add("salam");
        Test.test();
        System.out.println(a);
    }

    public static ArrayList<String> getA() {
        return a;
    }
}


class Test {
    public static void test() {
        ArrayList a = Main.getA();
        a.clear();
    }
}
