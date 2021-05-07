import org.json.*;
import java.io.*;
import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * @author 张心睿
 * @date 2021/5/6
 * @description
 */

public class dataProcessor {

    /**
     * @author 张心睿
     * @description 读取诗词文件并存入Java对象Poet
     * @date 19:42 2021/5/6
     * @param path
     * @return void
     **/
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

    }

    public static void writeFile() throws IOException {
        for (Poet poet : poets.values()) {
            char[] content = poet.getContent().toCharArray();
            for (char c : content) {
                if(poetTerm.containsKey(c)){
                    poetTerm.get(c).addDocID(poet.getPid());
                    poetTerm.get(c).addCollFreq();
                }else {
                    Term t = new Term();
                    t.setWord(c);
                    t.addDocID(poet.getPid());
                    t.addCollFreq();
                    poetTerm.put(c,t);
                }

            }
        }
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(".\\dataset\\dictionary.json"),"UTF-8");

        JSONObject obj=new JSONObject();//创建JSONObject对象

        for(Term term : poetTerm.values())
        {
            JSONObject subObj=new JSONObject();//创建对象数组里的子对象
            subObj.put("Word",String.valueOf(term.getWord()));
            for(String docID : term.getDocID().keySet()){
                JSONObject subSubObj=new JSONObject();//创建对象数组里的子对象
                subSubObj.put("docID",docID);
                subSubObj.put("TermFreq",term.getDocID().get(docID));
                subObj.accumulate("Docs", subSubObj);
            }
            subObj.put("CollFreq",term.getCollFreq());
            subObj.put("DocNum",term.getDocNum());
            obj.accumulate("Terms",subObj);
        }
        osw.write(obj.toString());
        osw.flush();//清空缓冲区，强制输出数据
        osw.close();//关闭输出流
    }


    public static void main(String[] args) throws IOException {
        String path =".\\dataset\\poet.tang.1000.json";
        readFile(path);
        writeFile();
    }
}
