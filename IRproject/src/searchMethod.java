import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;
public class searchMethod {
    public void query(String input,LinkedList<Term> dictionary) {

    }
    private ArrayList<String> AND(ArrayList<String> result, ArrayList<String> next) {
        ArrayList<String> docId = new ArrayList<>();
        int i = 0, j = 0;
        while (i < result.size() && j < next.size()) {
            int val = result.get(i).compareTo(next.get(j));
            switch (val) {
                case 0: //相等
                    docId.add(result.get(i));
                    i++;
                    j++;
                    break;
                case -1: //result[i] < next[j]
                    i++;
                    break;
                case 1: //result[i] > next[j]
                    j++;
                    break;
            }
        }
        return docId;
    }

    private ArrayList<String> ANDNOT(ArrayList<String> result, ArrayList<String> next) {
        ArrayList<String> docId = new ArrayList<>();
        int i = 0, j = 0;
        while (i < result.size() && j < next.size()) {
            int val = result.get(i).compareTo(next.get(j));
            switch (val) {
                case 0: //相等
                    i++;
                    j++;
                    break;
                case -1: //result[i] < next[j]
                    docId.add(result.get(i));
                    i++;
                    break;
                case 1: //result[i] > next[j]
                    j++;
                    break;
            }
        }
        while (i < result.size()) {
            docId.add(result.get(i++));
        }
        return docId;
    }

    
}
