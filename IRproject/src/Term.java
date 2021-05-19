import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author 张心睿
 * @date 2021/5/7
 * @description
 */
public class Term {
    private char word;
    private TreeMap<String, Integer> tf = new TreeMap<>();
    private int collFreq = 0;
    private int docNum = 0;
    private TreeMap<String, Double> tfidf = new TreeMap<>();
    private TreeMap<String, Double> wfidf = new TreeMap<>();
    private TreeMap<String, ArrayList<Double>> docs = new TreeMap<>();

    /**
     * @author 张心睿
     * @description
     * @date 19:28 2021/5/7
     * @param word
     * @return void
     **/
    public void setWord(char word) {
        this.word = word;
    }

    /**
     * @author 张心睿
     * @description 添加Term项所在文档，当该文档已含该Term项时，只termFreq加一，docNum不变，且不加入docID.
     * @date 19:28 2021/5/7
     * @param docID
     * @return void
     **/
    public void addDoc(String docID) {
        if(this.docs.containsKey(docID)){
            ArrayList<Double> temp = this.docs.get(docID);
            temp.set(0,temp.get(0)+1);
            this.docs.put(docID, temp);
        }else {
            ArrayList<Double> temp = new ArrayList<>();
            temp.add(0,1.0);
            this.docs.put(docID, temp);
            addDocNum();
        }
    }

    public void setDocs(TreeMap<String, ArrayList<Double>> docs) {
        this.docs = docs;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:33 2021/5/7
     * @param collFreq
     * @return void
     **/
    public void setCollFreq(int collFreq) {
        this.collFreq = collFreq;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:33 2021/5/7
     * @param
     * @return void
     **/
    public void addCollFreq(){
        this.collFreq++;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:33 2021/5/7
     * @param docNum
     * @return void
     **/
    public void setDocNum(int docNum) {
        this.docNum = docNum;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:33 2021/5/7
     * @param
     * @return void
     **/
    public void addDocNum(){
        this.docNum++;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:33 2021/5/7
     * @param
     * @return char
     **/
    public char getWord() {
        return word;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:33 2021/5/7
     * @param
     * @return java.util.TreeMap<java.lang.String,java.lang.Integer>
     **/
    public TreeMap<String, ArrayList<Double>> getDoc() {
        return docs;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:34 2021/5/7
     * @param
     * @return int
     **/
    public int getCollFreq() {
        return collFreq;
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:34 2021/5/7
     * @param
     * @return int
     **/
    public int getDocNum() {
        return docNum;
    }

    public TreeMap<String, ArrayList<Double>> getDocs() {
        return docs;
    }
}

