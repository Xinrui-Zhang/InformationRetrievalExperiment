import java.util.TreeMap;

/**
 * @author 张心睿
 * @date 2021/5/7
 * @description
 */
public class Term {
    private char word;
    private TreeMap<String, Integer> docID= new TreeMap<>();
    private int collFreq = 0;
    private int docNum = 0;

    public void setWord(char word) {
        this.word = word;
    }

    public void addDocID(String docID) {
        if(this.docID.containsKey(docID)){
            this.docID.put(docID, this.docID.get(docID)+1);
        }else {
            this.docID.put(docID, 1);
            this.docNum++;
        }
    }

    public void setDocID(TreeMap<String, Integer> docID) {
        this.docID = docID;
    }

    public void setCollFreq(int collFreq) {
        this.collFreq = collFreq;
    }

    public void addCollFreq(){
        this.collFreq++;
    }

    public void setDocNum(int docNum) {
        this.docNum = docNum;
    }

    public void addDocNum(){
        this.docNum++;
    }

    public char getWord() {
        return word;
    }

    public TreeMap<String, Integer> getDocID() {
        return docID;
    }

    public int getCollFreq() {
        return collFreq;
    }

    public int getDocNum() {
        return docNum;
    }
}

