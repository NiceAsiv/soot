package TestCaseDroid.utils;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

public class SootInfoUtils {
    public static Boolean isApplicationClass(String tgtClass){
        // 判断被分析的类是否为应用类，并统计类中的方法数量
        System.out.println("--------------------------------");
        SootClass sc = Scene.v().getSootClass(tgtClass);
        System.out.printf("The target class %s is an %s class, loaded with %d methods! %n",
                sc.getName(), sc.isApplicationClass() ? "Application" : "Library", sc.getMethodCount());
        System.out.println("--------------------------------");
        return sc.isApplicationClass();
    }

    public static void reportSootApplicationClassInfo(){
        //遍历当前分析场景中的应用类，并输出其方法
        int classIndex = 0;
        System.out.println("--------------------------------");
        for(SootClass sc : Scene.v().getApplicationClasses()){
            classIndex++;
            int methodIndex = 0;
            System.out.printf("[%d] The class %s is an application class, loaded with %d methods! Methods are as followed:%n",classIndex,sc.getName(),sc.getMethodCount());
            for(SootMethod m:sc.getMethods()){
                methodIndex++;
                System.out.printf("    Method %d: %s%n",methodIndex,m.getSignature());
            }
        }
        System.out.println("--------------------------------");
    }

    public static void reportSootEntryPointsInfo(){
        //设置文本恢复为黑色
        System.out.print(SootVisualizeUtils.TextColor.RESET.getCode());
        int index=0;
        System.out.println("--------------------------------");
        if(Scene.v().getEntryPoints().size()==1){
            System.out.println("Current entrypoint is:");
        }else {
            System.out.println("Current entrypoint are:");
        }

        for(SootMethod m:Scene.v().getEntryPoints()){
            index++;
            System.out.printf("[%d] %s\n",index,m.getSignature());
        }
        System.out.println("--------------------------------");
    }
}
