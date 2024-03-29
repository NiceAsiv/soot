package TestCaseDroid.graph;

import TestCaseDroid.config.SootConfig;
import TestCaseDroid.utils.DotGraphWrapper;
import heros.InterproceduralCFG;
import lombok.Setter;
import soot.*;
import soot.jimple.toolkits.ide.icfg.JimpleBasedInterproceduralCFG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Setter
public class BuildICFG extends SceneTransformer {

    private static String targetClassName = "TestCaseDroid.test.FastJsonTest";
    private static String targetMethodName = "testFastJson";
    private static String targetPackageName = "TestCaseDroid";
    private static DotGraphWrapper dotGraph = new DotGraphWrapper("interproceduralCFG");
    private ArrayList<Unit> visited;

    public static void main(String[] args) {
        BuildICFGForClass();
    }

    public static void BuildICFGForClass() {
        SootConfig sootConfig = new SootConfig();
        sootConfig.setupSoot(targetClassName, true);
        sootConfig.setCallGraphAlgorithm("Spark");

        //add an intra-procedural analysis phase to Soot
        BuildICFG analysis = new BuildICFG();
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.ifds", analysis));
        PackManager.v().runPacks();
    }
    @Override
    protected void internalTransform(String phaseName, Map<String, String> options) {
        if (Scene.v().hasMainClass()) {
            SootMethod mainMethod = Scene.v().getMainMethod();
            Scene.v().setEntryPoints(Collections.singletonList(mainMethod));
            JimpleBasedInterproceduralCFG icfg = new JimpleBasedInterproceduralCFG();
            visited = new ArrayList<>();
            if (mainMethod.hasActiveBody()) {
                Unit startPoint = mainMethod.getActiveBody().getUnits().getFirst();
                graphTraverse(startPoint, icfg);
            }
        }else {
            System.out.println("No main method found!");
            return;
        }
        dotGraph.plot("icfg", targetClassName, targetMethodName);
    }

    public void graphTraverse(final Unit startPoint, final InterproceduralCFG<Unit, SootMethod> icfg) {
        final List<Unit> currentSuccessors = icfg.getSuccsOf(startPoint);
        if (currentSuccessors.isEmpty()) {
            System.out.println("Traversal complete");
            return;
        }
        for (final Unit succ : currentSuccessors) {
            if (!this.visited.contains(succ)) {
                dotGraph.drawEdge(startPoint.toString(), succ.toString());
                System.out.println(startPoint + " may reach " + succ);
                this.visited.add(succ);
                this.graphTraverse(succ, icfg);
            }
            else {
                dotGraph.drawEdge(startPoint.toString(), succ.toString());
                System.out.println(startPoint +"may reach " + succ);
            }
        }
    }
}
