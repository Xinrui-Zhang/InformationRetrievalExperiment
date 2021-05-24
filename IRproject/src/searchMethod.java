/**
 * @创建人 柳靖洋
 * @创建时间 2021/5/10
 * @描述
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;
import java.util.Map.Entry;
public class searchMethod {
    public TreeMap<String,TreeMap<Character,Double>> docVector;

    public TreeMap<String, ArrayList<Double>> query(String q, TreeMap<Character, Term> terms){
        TreeMap<String, ArrayList<Double>> result = new TreeMap<>();
        for(int i = 0; i < q.length(); i++){
            switch (q.charAt(i)){
                case '+':
                    if(terms.containsKey(q.charAt(i+1)))
                        result = OR(result,terms.get(q.charAt(i+1)).getDoc());
                    i++;
                    break;
                case '*':
                    if(terms.containsKey(q.charAt(i+1)))
                        result = AND(result,terms.get(q.charAt(i+1)).getDoc());
                    i++;
                    break;
                case '-':
                    if(terms.containsKey(q.charAt(i+1)))
                        result = ANDNOT(result,terms.get(q.charAt(i+1)).getDoc());
                    i++;
                    break;
                default:
                    if(terms.containsKey(q.charAt(i)))
                        result = terms.get(q.charAt(i)).getDoc();
                    break;

            }
        }

        return result;
    }

    public TreeMap<String,Double> CosinSimilarity(String qvector)
    {
        TreeMap<String,Double> result =new TreeMap<String,Double>();//返回一个文档和对应相关性的集合
        TreeMap<Character,Double> dvector;//文档向量
        Character query;
        int i = 0;
        while(i<qvector.length())
        {   int j=0;
            query=(Character)qvector.charAt(i);
            while(j<docVector.keySet().size())//全部文档向量的集合
            {   String Docid=(String)docVector.keySet().toArray()[i];
                dvector=(TreeMap<Character,Double>)docVector.get(Docid);
                double dw=dvector.get(query);
                if(result.containsKey(Docid))//如果计算过这个文档
                {  double oldwf=result.get(Docid);
                    result.replace(Docid,oldwf+dw);
                }else
                {
                    result.put(Docid,dw);
                }
                j++;
            }
            i++;
        }
        return result;
    }
    


    private TreeMap<String, ArrayList<Double>> AND(TreeMap<String, ArrayList<Double>> p1, TreeMap<String, ArrayList<Double>> p2) {
        TreeMap<String, ArrayList<Double>> docId = new TreeMap<String, ArrayList<Double>>();
        int i = 0, j = 0;
        String p1ID="",p2ID="";
        while (i<p1.keySet().size() && j<p2.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            p2ID=(String)p2.keySet().toArray()[j];
            int val = p1ID.compareTo(p2ID);
            //相等
            if (val == 0) {
                ArrayList<Double> temp = new ArrayList<>();
                temp.add( (p1.get(p1ID).get(0)+p1.get(p1ID).get(0))/2.0);
                temp.add( (p1.get(p1ID).get(1)+p1.get(p1ID).get(1))/2.0);
                temp.add( (p1.get(p1ID).get(2)+p1.get(p1ID).get(2))/2.0);
                docId.put((String) p1ID, temp);
                i++;
                j++;
            } else if (val < 0) {
                i++;

            } else if (val > 0) {
                j++;
            }
        }
        return docId;
    }

    private TreeMap<String, ArrayList<Double>> ANDNOT(TreeMap<String, ArrayList<Double>> p1, TreeMap<String, ArrayList<Double>> p2) {

        TreeMap<String, ArrayList<Double>> docId = new TreeMap<String, ArrayList<Double>>();
        int i = 0, j = 0;
        String p1ID="",p2ID="";
        while (i<p1.keySet().size() && j<p2.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            p2ID=(String)p2.keySet().toArray()[j];
            int val = p1ID.compareTo(p2ID);
            //相等
            if (val == 0) {
                i++;
                j++;
            } else if (val < 0) {
                docId.put((String) p1ID, p1.get(p1ID));
                i++;
            } else if (val > 0) {
                j++;
            }
        }
        while(i<p1.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            docId.put((String) p1ID,p1.get(p1ID));
            i++;
        }
        return docId;
    }

    private TreeMap<String, ArrayList<Double>> AndAll( TreeMap<String, ArrayList<Double>> result,ArrayList<TreeMap<String, ArrayList<Double>>> next)
    {

       next.sort(new Comparator<TreeMap<String, ArrayList<Double>>>() {
           @Override
           public int compare(TreeMap<String, ArrayList<Double>> o1, TreeMap<String, ArrayList<Double>> o2) {
               int dif=o1.size()-o2.size();
               if(dif>0){
                   return 1;
               }else if(dif<0){
                   return -1;
               }
               return 0;
           }
       });


        int i=0;
        while(i<next.size())
        {
            result=AND(result,next.get(i));
        }
        return result;
    }
    
    private TreeMap<String, ArrayList<Double>> OR(TreeMap<String, ArrayList<Double>> p1, TreeMap<String, ArrayList<Double>> p2){
        TreeMap<String, ArrayList<Double>> docId = new TreeMap<String, ArrayList<Double>>();
        int i = 0, j = 0;
        String p1ID="",p2ID="";
        while (i<p1.keySet().size() && j<p2.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            p2ID=(String)p2.keySet().toArray()[j];
            int val = p1ID.compareTo(p2ID);
            //相等
            if (val == 0) {
                ArrayList<Double> temp = new ArrayList<>();
                temp.add( (p1.get(p1ID).get(0)+p1.get(p1ID).get(0))/2.0);
                temp.add( (p1.get(p1ID).get(1)+p1.get(p1ID).get(1))/2.0);
                temp.add( (p1.get(p1ID).get(2)+p1.get(p1ID).get(2))/2.0);
                docId.put((String) p1ID, temp);
                i++;
                j++;
            } else if (val < 0) {
                docId.put((String) p1ID, p1.get(p1ID));
                i++;

            } else if (val > 0) {
                docId.put((String) p2ID, p2.get(p2ID));
                j++;
            }
        }

        while(i<p1.keySet().size()){
            p1ID=(String)p1.keySet().toArray()[i];
            docId.put((String) p1ID, p1.get(p1ID));
            i++;
        }
        while(j<p2.keySet().size()){
            p2ID=(String)p2.keySet().toArray()[j];
            docId.put((String) p2ID, p2.get(p2ID));
            j++;
        }
        return docId;
    }

    private TreeMap<String, ArrayList<Double>> OrAll( TreeMap<String, ArrayList<Double>> result,ArrayList<TreeMap<String, ArrayList<Double>>> next)
    {

        next.sort(new Comparator<TreeMap<String, ArrayList<Double>>>() {
            @Override
            public int compare(TreeMap<String, ArrayList<Double>> o1, TreeMap<String, ArrayList<Double>> o2) {
                int dif=o1.size()-o2.size();
                if(dif<0){
                    return 1;
                }else if(dif>0){
                    return -1;
                }
                return 0;
            }
        });


        int i=0;
        while(i<next.size())
        {
            result=OR(result,next.get(i));
        }
        return result;
    }

};
