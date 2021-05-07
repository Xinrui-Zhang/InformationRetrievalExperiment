import java.util.TreeMap;

/**
 * @author 张心睿
 * @date 2021/5/7
 * @description
 */
public class Term {
    private char word;
    private TreeMap<String, Integer> docs= new TreeMap<>();
    private int collFreq = 0;
    private int docNum = 0;

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
            this.docs.put(docID, this.docs.get(docID)+1);
        }else {
            this.docs.put(docID, 1);
            addDocNum();
        }
    }

    /**
     * @author 张心睿
     * @description
     * @date 19:30 2021/5/7
     * @param docID
     * @return void
     **/
    public void setDoc(TreeMap<String, Integer> docID) {
        this.docs = docID;
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
    public TreeMap<String, Integer> getDoc() {
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
}

