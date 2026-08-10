import java.util.Scanner;
public class BMI{
    float weight;
    float height;
    float bmi;
    public float getBMI(float weight, float height) {
        this.weight = weight;
        this.height = height;
        this.bmi = weight / (height * height);
        return bmi;
    }
    public static void main(String[] args){
        BMI person = new BMI();
        System.out.println("请输入体重(kg):");
        Scanner sc =new Scanner(System.in);
        float weight = sc.nextFloat();
        System.out.println("请输入升高(m):");
        float height = sc.nextFloat();
        float bmi = person.getBMI(weight, height);
        System.out.println("BMI=" + bmi);
        if(bmi < 18.5){
            System.out.println("体重过轻");
        }else if(bmi >= 18.5 && bmi < 24){
            System.out.println("体重正常");
        }else if(bmi >= 24 && bmi < 28){
            System.out.println("体重过重");
        }else{
            System.out.println("肥胖");
        }
    }
}