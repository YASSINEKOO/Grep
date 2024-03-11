/**
* 
*
*************
*  Utilisation:
* <p>
*   java Regex monexpression mesfichiers
* <p>
*  par exemple: 
* <p>
*         java Regex '".*"' toto.txt 
* <p>
*  donne tout texte entre parantheses dans le fichier toto.txt. Le
*  traitement se fait ligne par ligne. Pour avoir le texte sans les 
*  parantheses, faire 
* <p>
*         java Regex '"(.*)"' toto.txt
* <p>
*  Pour avoir plus d'information, vous pouvez utiliser le drapeau "-v"
*  comme ceci : 
* <p>
*         java Regex -v '".*"' toto.txt
*
**/
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.io.*;

public class Regex {
  static int inc = 0;

  public static void main(String[] args) throws IOException {
    String currentDirectory = System.getProperty("user.dir");

    // Print the current working directory
    System.out.println("Current Directory: " + currentDirectory);
    boolean optionTop = true;
    int pos = 0;
    if(args[pos].equals("grep")) {
      ++pos;
      System.out.println("Mode OPTIONS");
      System.out.println("Expression regulieres: "+args[pos]);
      //Pattern RegexCompile = Pattern.compile(args[pos]);
      if(args[pos].equals("-h") || args[pos].equals("--hepl")){
        System.out.println("""
                Usage: grep [OPTION]... PATTERN [FILE]...
                Search for PATTERN in each FILE or standard input.

                Options:
                  -V, --version             Print the version information
                  -e, --regexp=PATTERN      use PATTERN for matching
                  -f, --file=FILE           obtain PATTERN from FILE
                  -i,-y --ignore-case       ignore case distinctions
                  -c, --count               print only a count of matching lines
                  -ci                       Case-insensitive count the number of lines containing the string
                  -v, --invert-match        Invert the sense of matching, to select non-matching lines
                  -w, --word-regexp         Select only lines containing matches that form whole words
                  -x, --line-regexp         Select only matches that exactly match the whole line
                  -n, --line-number         print line numbers with output lines
                  -l, --files-with-matches  Display filenames that contain the string
                  -r, --recursive           read all files under each directory recursively
                  -h, --help                display this help and exit
                """);
      }
      else if (args[pos].equals("-V") || args[pos].equals("--version")) {
        System.out.println("""
                MyCustomGrep 1.0 (Windows Build)
                Copyright (C) 2024 Y.Kacimi
                This is a custom build of grep for Windows.
                """);
      }
      // && args[pos].startsWith("-")
      else if (args[pos].equals("-e") || args[pos].equals("--regexp=")){
        ++pos;
        String combinedPattern=args[pos];
        int i=0;
        ++pos;
        while (args[pos].equals("-e") ){
          ++pos;
          combinedPattern += "|" + args[pos];
          i++;
          System.out.println("option mode N`"+i+": -e " + args[pos]);
          ++pos;
        }
        matchTraiter(pos,combinedPattern,args, ' ');
      }
      else if (args[pos].equals("-f") || args[pos].equals("--file=")){
        ++pos;
        int i=0;
        List<String> ligners;
//      while (!args[pos].startsWith("-") || !args[pos].startsWith("--") || args[pos]!=null){
//        File file = new File(args[pos]);
//        ligners.addAll(readerLignes(file));
//        i++;
//        System.out.println("file N`"+i+": " + args[pos]);
//        ++pos;
//      }
        File file = new File(args[pos]);
        ligners=readerLignes(file);
        ++pos;
        String combinedPattern= ligners.remove(0);
        for (String ligne : ligners
        ) {
          if(!ligne.isEmpty())
            combinedPattern += "|" + ligne;
        }
        if (args[pos].equals("-e") || args[pos].equals("--regexp=")){
          ++pos;
          combinedPattern += "|" + args[pos];
          ++pos;
          while (args[pos].equals("-e") ){
            ++pos;
            combinedPattern += "|" + args[pos];
            ++pos;
          }
        }
        matchTraiter(pos,combinedPattern,args, ' ');
      }
      else if (args[pos].equals("-i") || args[pos].equals("ignore-case") || args[pos].equals("-y")) {
        ++pos;
        args[pos] = args[pos].toLowerCase();
        matchTraiter(pos,args, 'i');
      }
      else if (args[pos].equals("-v") || args[pos].equals("--invert-match")) {
        ++pos;
        args[pos] = args[pos].toLowerCase();
        matchTraiter(pos,args, 'v');
      }
      else if (args[pos].equals("-w") || args[pos].equals("--word-regexp=")) {
        ++pos;
        args[pos] = args[pos].toLowerCase();
        matchTraiter(pos,args, 'w');
      }
      else if (args[pos].equals("-x") || args[pos].equals("--line-regexp=")) {
        ++pos;
        args[pos] = args[pos].toLowerCase();
        matchTraiter(pos,args, 'x');
      }
      else if (args[pos].equals("-c") ) {
        ++pos;
        matchTraiter(pos, args, 'c');
        System.out.println(inc);
      }
      else if (args[pos].equals("-ci") ) {
        ++pos;
        args[pos] = args[pos].toLowerCase();
        matchTraiter(pos, args, 'q');
        System.out.println(inc);
      }
      else if (args[pos].equals("-n") ) {
        ++pos;
        args[pos] = args[pos].toLowerCase();
        matchTraiter(pos, args, 'n');
      }
      else if (args[pos].equals("-l") || args[pos].equals("--files-with-matches")) {
        ++pos;
        args[pos] = args[pos].toLowerCase();
        matchTraiter(pos, args, 'l');
      }

      else if (!args[pos].startsWith("-")) {
        matchTraiter(pos, args, ' ');
      }
      else
        System.err.println("they're no option handle " + args[pos]);

//    if (args[pos].startsWith("-",1))
//      matchTraiter(pos,args, ' ');

    }
  }

  public static void matchReader(File f, Pattern RegexCompile, char optionTop) throws IOException {
    if (!f.isFile() || !f.exists()) {
      throw new RuntimeException("File does not exist or is not a regular file: " + f.getAbsolutePath());
    }

    try {
      BufferedReader br = new BufferedReader(new FileReader(f));
      String ligne;
      Matcher m;
      while ((ligne = br.readLine()) != null) {
        if(optionTop=='i' || optionTop=='x' || optionTop=='q'){
          String ligneLower = ligne.toLowerCase();
           m = RegexCompile.matcher(ligneLower);}
        else {
           m = RegexCompile.matcher(ligne);
        }
        if (m.find()) {
          inc++;
          //String correspond = m.group();
          if(!(optionTop=='c' || optionTop=='q')) {
            if (optionTop=='n') System.out.print(inc+" : ");
            if (optionTop=='l'){
              System.out.println(f.getPath());
              break;
            }else {
              System.out.println(f.getPath() + " : " + ligne);
            }          }
          if(optionTop=='x') break;
        }
      }

      br.close();
    } catch (FileNotFoundException e) {
      System.out.println("File not found: " + f.getAbsolutePath());
      e.printStackTrace();
    } catch (IOException ioe) {
      System.out.println("Error while reading the file: " + f.getAbsolutePath());
      ioe.printStackTrace();
    }
  }
  public static List<String> readerLignes(File f) throws IOException {
    if (!f.isFile() || !f.exists()) {
      throw new RuntimeException("File does not exist or is not a regular file: " + f.getAbsolutePath());
    }

    try {
      BufferedReader br = new BufferedReader(new FileReader(f));
      List<String> lignes = new ArrayList<String>();
      String ligne;
      while ((ligne = br.readLine()) != null) {
        lignes.add(ligne);
      }
      br.close();
      return lignes;
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + f.getAbsolutePath());
      e.printStackTrace();
      return null;
    } catch (IOException ioe) {
      System.err.println("Error while reading the file: " + f.getAbsolutePath());
      ioe.printStackTrace();
      return null;
    }
  }
  public static void matchTraiter(int pos,String[] args,char optionTop) {
    long debut = System.currentTimeMillis();
    Pattern RegexCompile=Pattern.compile(args[pos]);
    if (optionTop=='w' | optionTop=='x'){
      RegexCompile = Pattern.compile("\\b" + args[pos] + "\\b");
    }
    if(optionTop=='v'){
    String invertedPatternString = "(?!" + RegexCompile.pattern() + ").*";
    RegexCompile=Pattern.compile(invertedPatternString);}
    ++pos;
    for (; pos < args.length; ++pos) {
      try {
        //System.out.println("Je traite le fichier: " + args[pos]);
        File file = new File(args[pos]);
        matchReader(file, RegexCompile, optionTop);
        //System.out.println("J'ai traite le fichier: " + args[pos]);
      } catch (IOException ioe) {
        System.err.println("Impossible de traiter le fichier: " + args[pos]);
        ioe.printStackTrace();
      }
    }
    long fin = System.currentTimeMillis();
    System.out.println("Temps ecoule: " + ((fin - debut) / 1000.0) + " s");
  }

  public static void matchTraiter(int pos,String arg,String[] args,char optionTop) {
    long debut = System.currentTimeMillis();
    Pattern RegexCompile=Pattern.compile(arg);
    for (; pos < args.length; ++pos) {
      try {
        System.out.println("Je traite le fichier: " + args[pos]);
        File file = new File(args[pos]);
        matchReader(file, RegexCompile, optionTop);
        System.out.println("J'ai traite le fichier: " + args[pos]);
      } catch (IOException ioe) {
        System.out.println("Impossible de traiter le fichier: " + args[pos]);
        ioe.printStackTrace();
      }
    }
    long fin = System.currentTimeMillis();
    System.out.println("Temps ecoule: " + ((fin - debut) / 1000.0) + " s");
  }
}
// solution du devoir
//http://\w+@\w+   exempleURLExtractor.txt
//http://\w+:\w+@.+:\d+/\w+/\w+        exempleURLExtractor.txt