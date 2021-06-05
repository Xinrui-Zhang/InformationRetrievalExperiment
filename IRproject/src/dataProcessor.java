import org.json.*;
import java.io.*;
import java.lang.reflect.Array;
import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * @author 张心睿
 * @date 2021/5/6
 * @description
 */

public class dataProcessor {

    private static TreeMap<String, Poet> poets = new TreeMap<String, Poet>(new Comparator() {
        public int compare(Object o1, Object o2) {
            //如果有空值，直接返回0
            if (o1 == null || o2 == null)
                return 0;

            Collator collator =  Collator.getInstance();
            CollationKey ck1 = collator.getCollationKey(String.valueOf(o1));
            CollationKey ck2 = collator.getCollationKey(String.valueOf(o2));
            return ck1.compareTo(ck2);
        }
    });
    private static TreeMap<Character,Term> poetTerm = new TreeMap<Character, Term>(new Comparator() {
        public int compare(Object o1, Object o2) {
            //如果有空值，直接返回0
            if (o1 == null || o2 == null)
                return 0;

            Collator collator =  Collator.getInstance();
            CollationKey ck1 = collator.getCollationKey(String.valueOf(o1));
            CollationKey ck2 = collator.getCollationKey(String.valueOf(o2));
            return ck1.compareTo(ck2);
        }
    });

    private static TreeMap<String,TreeMap<Character,Double>> dictionarys = new TreeMap<>();
    private static TreeMap<String,TreeMap<Character,Double>> dictionarysMax = new TreeMap<>();

    /**
     * @author 张心睿
     * @description 读取诗词文件并存入Java对象Poet
     * @date 19:42 2021/5/6
     * @param path
     * @return void
     **/
    public static void readFile(String path) throws IOException {
        char[] cbuf = new char[1000000000];
        InputStreamReader input =new InputStreamReader(new FileInputStream(new File(path)));
        int len =input.read(cbuf);
        String text =new String(cbuf,0,len);
        //构建json对象
        JSONObject obj = new JSONObject(text.substring(text.indexOf("{")));
        System.out.println("groupID"+obj.getString("groupID"));
        //获取古诗数组
        JSONArray arr = obj.getJSONArray("poets");
        for(int i=0;i<arr.length();i++)
        {
            JSONObject subObj = arr.getJSONObject(i);
            JSONArray subArr = subObj.getJSONArray("paragraphs");
            StringBuilder sentence = new StringBuilder();
            //获取诗词内容句子数组
            for(int j=0;j< subArr.length();j++){
                sentence.append(subArr.getString(j));
            }
            poets.put(subObj.getString("id"), new Poet(subObj.getString("title"),subObj.getString("author"),String.valueOf(sentence),subObj.getString("id")));
        }
        for (Poet poet : poets.values()) {
            char[] content = poet.getContent().toCharArray();
            for (char c : content) {
                if(poetTerm.containsKey(c)){
                    poetTerm.get(c).addDoc(poet.getPid());
                    poetTerm.get(c).addCollFreq();
                }else {
                    Term t = new Term();
                    t.setWord(c);
                    t.addDoc(poet.getPid());
                    t.addCollFreq();
                    poetTerm.put(c,t);
                }

            }
        }
        for (Term term : poetTerm.values()){
            TreeMap<String, Double> tfidf = new TreeMap<>();
            TreeMap<String, Double> wfidf = new TreeMap<>();
            TreeMap<String, ArrayList<Double>> docs = new TreeMap<>();
            ArrayList<Double> nums;
            for(String docID : term.getDoc().keySet()){
                Poet temp = poets.get(docID);
                nums = new ArrayList<>();
                //double tf = (double)term.getDoc().get(docID).get(0) / (double)temp.getWordNum();
                double tf = (double)term.getDoc().get(docID).get(0);
                double wf = tf==0? 0 : (Math.log(tf)/Math.log(10)+1);
                double idf = Math.log(1000 / (double)(term.getDocNum()+1))/Math.log(10);
                double tf_idf = tf * idf;
                double wf_idf = wf * idf;
                nums.add(tf);
                nums.add(tf_idf);
                nums.add(wf_idf);
                nums.add(idf);
                docs.put(docID,nums);
            }
            term.setDocs(docs);
        }


    }

    /**
     * @author 张心睿
     * @description 写入字典文件
     * @date 19:27 2021/5/7
     * @param
     * @return void
     **/
    public static void writeFile() throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(".\\dataset\\dictionary.json"),"UTF-8");

        JSONObject obj=new JSONObject();//创建JSONObject对象

        for(Term term : poetTerm.values())
        {
            JSONObject subObj=new JSONObject();//创建对象数组里的子对象
            subObj.put("Word",String.valueOf(term.getWord()));
            for(String docID : term.getDoc().keySet()){
                JSONObject subSubObj=new JSONObject();//创建对象数组里的子对象
                subSubObj.put("docID",docID);
                subSubObj.put("TermFreq",term.getDoc().get(docID).get(0));
                subSubObj.put("tfidf",term.getDoc().get(docID).get(1));
                subSubObj.put("wfidf",term.getDoc().get(docID).get(2));
                subObj.accumulate("Docs", subSubObj);
            }
            subObj.put("CollFreq",term.getCollFreq());
            subObj.put("DocNum",term.getDocNum());
            obj.accumulate("Terms",subObj);
        }
        osw.write(obj.toString());
        osw.flush();//清空缓冲区，强制输出数据
        osw.close();//关闭输出流

        int totalNum=0;
        for(Poet poet : poets.values()){
            totalNum+=poet.getWordNum();
            File f = new File("./dataset/poets/"+poet.getPid()+".json");
            if(!f.exists())
                f.createNewFile();
            OutputStreamWriter oswdic = new OutputStreamWriter(new FileOutputStream(".\\dataset\\poets\\"+poet.getPid()+".json"),"UTF-8");

            JSONObject dicobj=new JSONObject();//创建JSONObject对象

            TreeMap<Character, Double> dic = new TreeMap<>();

            File fmax = new File("./dataset/poets/max"+poet.getPid()+".json");
            if(!fmax.exists())
                fmax.createNewFile();
            OutputStreamWriter oswdicMax = new OutputStreamWriter(new FileOutputStream(".\\dataset\\poets\\max"+poet.getPid()+".json"),"UTF-8");

            JSONObject dicobjMax=new JSONObject();//创建JSONObject对象

            TreeMap<Character, Double> dicMax = new TreeMap<>();

            for(Term term : poetTerm.values()){
                if(poet.getContent().indexOf(term.getWord())!=-1){
                    ArrayList<Double> t = term.getDocs().get(poet.getPid());
                    dic.put(term.getWord(),t.get(0)/ poet.getWordNum() * t.get(3));
                    dicMax.put(term.getWord(),(0.5+0.5*t.get(0)/term.getMaxTf()) * t.get(3));
                    dicobj.put(String.valueOf(term.getWord()),t.get(0)/ poet.getWordNum() * t.get(3));
                    dicobjMax.put(String.valueOf(term.getWord()),(0.5+0.5*t.get(0)/term.getMaxTf()) * t.get(3));
                }else{
                    dic.put(term.getWord(),0.0);
                    dicMax.put(term.getWord(),0.0);
                    dicobj.put(String.valueOf(term.getWord()),0);
                    dicobjMax.put(String.valueOf(term.getWord()),0);
                }
            }
            dictionarys.put(poet.getPid(),dic);
            dictionarysMax.put(poet.getPid(),dicMax);
            oswdic.write(dicobj.toString());
            oswdicMax.write(dicobjMax.toString());
            oswdic.flush();//清空缓冲区，强制输出数据
            oswdic.close();//关闭输出流
            oswdicMax.flush();//清空缓冲区，强制输出数据
            oswdicMax.close();//关闭输出流
        }
        System.out.println(totalNum);

    }

    public static TreeMap<String, Poet> getPoets() {
        return poets;
    }

    public static TreeMap<Character, Term> getPoetTerm() {
        return poetTerm;
    }

    public static TreeMap<String, TreeMap<Character, Double>> getDictionarys() {
        return dictionarys;
    }

    public static TreeMap<String, TreeMap<Character, Double>> getDictionarysMax() {
        return dictionarysMax;
    }

    public static void main(String[] args) throws IOException {
        String path =".\\dataset\\poet.tang.1000.json";
        readFile(path);
        writeFile();
    }
}
