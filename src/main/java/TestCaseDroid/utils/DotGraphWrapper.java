package TestCaseDroid.utils;

import lombok.extern.slf4j.Slf4j;
import soot.util.dot.DotGraph;

import java.io.File;

import static TestCaseDroid.utils.SootDataProcessUtils.folderExistenceTest;

/**
 * Wrapper for the DotGraph class
 */
@Slf4j
public class DotGraphWrapper {
    private final DotGraph dotGraph;

    /**
     * Constructor
     *
     * @param graphName the name of the graph
     */
    public DotGraphWrapper(String graphName) {
        this.dotGraph = new DotGraph(graphName);
        //设置节点的形状
        this.dotGraph.setNodeShape("box");
//        this.dotGraph.setGraphAttribute("fontname", "Helvetica");
//        this.dotGraph.setGraphAttribute("fontsize", "12");

    }

    public void drawEdge(String src, String tgt) {
        this.dotGraph.drawEdge(src, tgt);
    }

    public void drawNode(String node) {
        this.dotGraph.drawNode(node);
    }

    /**
     * Plot the graph
     * @param graphType the type of graph including "cg", "cfg", "icfg"
     * @param targetClass the target class
     * @param targetMethod the target method (optional) but required for "cfg" and "icfg"
     * @see DotGraphWrapper#convertDotToPng(String, String)
     */
    public void plot(String graphType,String targetClass,String ...targetMethod) {
        if (dotGraph == null) {
            log.error("DotGraph is null");
            return;
        }
        switch (graphType) {
            case "cg":
                String callGraphPath = "./sootOutput/dot/" + targetClass + ".cg.dot";
                String outputPath = "./sootOutput/pic/" + targetClass+ ".cg.png";
                folderExistenceTest(callGraphPath);
                this.dotGraph.plot(callGraphPath);
                try {
                    convertDotToPng(callGraphPath, outputPath);
                } catch (Exception e) {
                    log.error("Error in converting dot to png", e);
                }
                break;
            case "cfg":
                String cfgPath = "./sootOutput/dot/" + targetClass + "." + targetMethod[0] + ".cfg.dot";
                String cfgOutputPath = "./sootOutput/pic/" + targetClass + "." + targetMethod[0] + ".cfg.png";
                folderExistenceTest(cfgPath);
                this.dotGraph.plot(cfgPath);
                try {
                    convertDotToPng(cfgPath, cfgOutputPath);
                } catch (Exception e) {
                    log.error("Error in converting dot to png",e);
                }
                break;

            case "icfg":
                String icfgPath = "./sootOutput/dot/" + targetClass + ".icfg.dot";
                String icfgOutputPath = "./sootOutput/pic/" + targetClass + ".icfg.png";
                folderExistenceTest(icfgPath);
                this.dotGraph.plot(icfgPath);
                try {
                    convertDotToPng(icfgPath, icfgOutputPath);
                } catch (Exception e) {
                    log.error("Error in converting dot to png",e);
                }
                break;
            default:
                log.error("Invalid graph type");
                break;
        }
    }


    /**
     * Convert a dot file to a png file
     * @param dotFilePath the dot file path
     * @param outputFilePath the output png file path
     */
    public static void convertDotToPng(String dotFilePath, String outputFilePath) {
        try {
            String graphvizFilePath = System.getenv("GRAPHVIZ");
            String graphvizPath = getString(graphvizFilePath);
            // Check if pic output folder exist
            folderExistenceTest(outputFilePath);
//            File folder = new File(outputFilePath.substring(0, outputFilePath.lastIndexOf("/")));
//            if (!folder.exists()) {
//                if (folder.mkdirs()) {
//                    System.out.println("Create pic output folder：" + folder.getAbsolutePath());
//                } else {
//                    System.err.println("Unable to create pic output folder：" + folder.getAbsolutePath());
//                }
//            } else {
//                System.out.println("Pic output folder exist in：" + folder.getAbsolutePath());
//            }

            String[] cmd = new String[]{graphvizPath, "-Tpng",dotFilePath,"-Gdpi=300","-Gfontname=Arial","-o",outputFilePath };
            Runtime rt = Runtime.getRuntime();
            rt.exec(cmd);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private static String getString(String graphvizFilePath) {
        String graphvizPath;
        if (graphvizFilePath == null) {
            throw new RuntimeException("\nPlease set the installation folder for graphviz as an environment variable and name it \"GRAPHVIZ\".\n" +
                    "The graphviz folder is like this: \"D:\\APPdata\\Graphviz-10.0.1-win64\".\n" +
                    "You can download graphviz at https://graphviz.org/download/.\n" +
                    "When you finish that, please restart your IDE.\n");
        } else {
            graphvizPath = graphvizFilePath + File.separator + "bin" + File.separator + "dot.exe";
        }
        return graphvizPath;
    }

}
