import org.json.*;
import java.io.*;

/**
 * @author 张心睿
 * @date 2021/5/6
 * @description
 */

public class dataProcess {

    /**
     * @author 张心睿
     * @description 读取诗词文件并存入Java对象Poet
     * @date 19:42 2021/5/6
     * @param path
     * @return void
     **/
    private static Poet[] poets;
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
        poets = new Poet[arr.length()];
        for(int i=0;i<arr.length();i++)
        {
            JSONObject subObj = arr.getJSONObject(i);
            JSONArray subArr = subObj.getJSONArray("paragraphs");
            StringBuilder sentence = new StringBuilder();
            //获取诗词内容句子数组
            for(int j=0;j< subArr.length();j++){
                sentence.append(subArr.getString(j));
            }
            poets[i] = new Poet(subObj.getString("title"),subObj.getString("author"),String.valueOf(sentence),subObj.getString("id"));
            poets[i].print();
        }

    }

    public static void writeFile() throws IOException {
        for (Poet poet : poets) {
            File newFile = new File(".\\dataset\\poets\\" + poet.getTitle()+".txt");
            if (!newFile.exists())
                newFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(poet.getContent().getBytes());
            fos.close();
        }
    }
    public static void main(String[] args) throws IOException {
        String path =".\\dataset\\poet.tang.1000.json";
        readFile(path);
        writeFile();
    }
}
