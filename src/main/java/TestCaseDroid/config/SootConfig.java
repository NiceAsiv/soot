package TestCaseDroid.config;

import soot.*;
import soot.options.Options;

import java.io.File;

import static TestCaseDroid.utils.SootUtils.excludeClassesList;


/**
 * Soot configuration
 */
public class SootConfig {
    /**
     * javaPath collects all dependency libraries in the project.
     * jreDir is path to rt.jar
     * sootClassPath combines javaPath and jreDir to form the analysis environment.
     */
    private  static  final String  javaPath = System.getProperty("java.class.path");
    private  static  final String  jreDir = System.getProperty("java.home")+"/lib/rt.jar";
    public  static  final String  sootClassPath = javaPath + File.pathSeparator +  jreDir;
    private String callGraphAlgorithm = "Spark";

    public void setCallGraphAlgorithm(String callGraphAlgorithm) {
        this.callGraphAlgorithm = callGraphAlgorithm;
    }

    public String getCallGraphAlgorithm() {
        return callGraphAlgorithm;
    }


    /**
     * Soot configuration
     * @param ClassName the main class name e.g. "TestCaseDroid.tests.CallGraph"
     * @param constructCallGraph whether to construct call graph
     */
    public void setupSoot(String ClassName, Boolean constructCallGraph)
    {
        //clear all the previous cached values of soot
        G.reset();
        //set soot class path
        Options.v().set_soot_classpath(sootClassPath);
//        Scene.v().setSootClassPath(sootClassPath);
        //whole program analysis
        Options.v().set_whole_program(true);
        //set application class to analyze application class only
        Options.v().set_app(true);
        // exclude jdk and other libraries
        excludeJDKLibrary();

        //load and set main class
        SootClass appClass = Scene.v().loadClassAndSupport(ClassName);

        Scene.v().setMainClass(appClass);
        Scene.v().loadNecessaryClasses();

        // set to keep line number
        Options.v().set_keep_line_number(true);
        // set output format
        Options.v().set_output_format(Options.output_format_jimple);
        // set to see  verbose information
        Options.v().set_verbose(true);

        //set to keep variable names
        Options.v().setPhaseOption("jb","use-original-names:true");
//        Options.v().setPhaseOption("jb.dae","only-stack-locals:true"); // 不去优化b = $stack5;的语句，保持原汁原味
//        Options.v().setPhaseOption("jb.cp", "enabled:false");
//        Options.v().setPhaseOption("jb.ls","enabled:false");
//        Options.v().setPhaseOption("jb.dae","enabled:false");
//        Options.v().setPhaseOption("jb.ulp","unsplit-original-locals:false");
//        Options.v().setPhaseOption("jb.a","enabled:false");
//        Options.v().setPhaseOption("jb.cp","enabled:false");



//        PackManager.v().runPacks();//run soot

        //构建控制流图选项，默认是SPARK
        if (constructCallGraph) {
          switch (new SootConfig().callGraphAlgorithm) {
            case "CHA":
                Options.v().setPhaseOption("cg.cha", "on");
                break;
            case "Spark":
                Options.v().setPhaseOption("cg.spark", "on");
                Options.v().setPhaseOption("cg.spark","enabled:true");
                Options.v().setPhaseOption("cg.spark","verbose:true");
                Options.v().setPhaseOption("cg.spark","on-fly-cg:true");
                break;
            case "VTA":
                Options.v().setPhaseOption("cg.spark", "on");
                Options.v().setPhaseOption("cg.spark", "vta:true");
                break;
            case "RTA":
                Options.v().setPhaseOption("cg.spark", "on");
                Options.v().setPhaseOption("cg.spark", "rta:true");
                Options.v().setPhaseOption("cg.spark", "on-fly-cg:false");
                break;
            default:
                throw new RuntimeException("Unknown call graph algorithm: " + new SootConfig().callGraphAlgorithm);
          }
        }

    }

    private static void excludeJDKLibrary()
    {
        //exclude jdk classes
        Options.v().set_exclude(excludeClassesList);
        //this option must be disabled for a sound call graph
        Options.v().set_no_bodies_for_excluded(true);
        Options.v().set_allow_phantom_refs(true);
    }


}