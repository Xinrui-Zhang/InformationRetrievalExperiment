import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;
public class searchMethod {
    public void query(ArrayList<Character> input, LinkedList<Term> dictionary) {


    }
  private TreeMap<String, Integer> getID(Term term)
  {
      return term.getDoc();
  }

    private TreeMap<String, Integer> AND(TreeMap<String, Integer> result, TreeMap<String, Integer> next) {
        TreeMap<String, Integer> docId = new TreeMap<String, Integer>();
        Iterator i = result.entrySet().iterator();
        Iterator j = next.entrySet().iterator();
        while (i.hasNext() && j.hasNext()) {
            Map.Entry rentry = ( Map.Entry)i.next();
            Map.Entry nentry = (Map.Entry)j.next();
            String resultID=(String)rentry.getKey();
            String nextID=(String)nentry.getKey();
            int val = resultID.compareTo(nextID);
            switch (val) {
                case 0: //相等
                    docId.put((String)nentry.getKey(),(Integer)nentry.getValue());
                    i.next();
                    j.next();
                    break;
                case -1: //result[i] < next[j]
                    i.next();
                    break;
                case 1: //result[i] > next[j]
                    j.next();
                    break;
            }
        }
        return docId;
    }

    private TreeMap<String, Integer> ANDNOT(TreeMap<String, Integer> result, TreeMap<String, Integer> next) {

        TreeMap<String, Integer> docId = new TreeMap<String, Integer>();
        Iterator i = result.entrySet().iterator();
        Iterator j = next.entrySet().iterator();
        while (i.hasNext() && j.hasNext()) {
            Map.Entry rentry = (Map.Entry)i.next();
            Map.Entry nentry = (Map.Entry)j.next();
            String resultID=(String)rentry.getKey();
            String nextID=(String)nentry.getKey();
            int val = resultID.compareTo(nextID);
            switch (val) {
                case 0: //相等
                    i.next();
                    j.next();
                    break;
                case -1: //result[i] < next[j]
                case 1: //result[i] > next[j]
                    docId.put((String)rentry.getKey(),(Integer)rentry.getValue());
                    docId.put((String)nentry.getKey(),(Integer)nentry.getValue());
                    i.next();
                    j.next();
                    break;
            }
        }

        return docId;
    }

    private TreeMap<String, Integer> AndAll(TreeMap<String, Integer> result, ArrayList<TreeMap<String, Integer>> next)
    {   int i=0;
        while(i<next.size())
        {
            result=AND(result,next.get(i));
        }
        return result;
    }


    
    private ArrayList<String> OR(ArrayList<String> p1, ArrayList<String> p2){
        ArrayList<String> docId = new ArrayList<>();
        int i = 0, j = 0;
        while (i < p1.size() && j < p2.size()) {
            int val = p1.get(i).compareTo(p2.get(j));
            switch (val) {
                //相等
                case 0 -> {
                    docId.add(p1.get(i));
                    i++;
                    j++;
                }
                //result[i] < next[j]
                case -1 -> {
                    docId.add(p1.get(i));
                    i++;
                }
                //result[i] > next[j]
                case 1 -> {
                    docId.add(p2.get(i));
                    j++;
                }
            }
        }
        while (i < p1.size()) {
            docId.add(p1.get(i++));
        }
        while (j < p2.size()) {
            docId.add(p2.get(i++));
        }
        return docId;
    }

};
