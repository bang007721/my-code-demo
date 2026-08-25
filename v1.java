import java.util.Scanner;
public class v1 {
    float c;
    float k;
    float g;
    float V1;
    public float getv1(float c,float k,float g) {
        this.c = c;
        this.k = k;
        this.g = g;
        this.V1 = c * k * g;
        return V1;
    }

public static void main(String[] args){
        v1 person = new v1();
        System.out.println("请输入c:");
        Scanner sc =new Scanner(System.in);
        float c = sc.nextFloat();
        System.out.println("请输入k:");
        float k = sc.nextFloat();
        System.out.println("请输入g:");
        float g = sc.nextFloat();
        float v1 = person.getv1(c, k, g);
        System.out.println("v1=" + v1);
    }
}