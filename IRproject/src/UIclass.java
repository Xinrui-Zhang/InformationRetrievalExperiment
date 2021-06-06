/**
 * @创建人 柳靖洋
 * @创建时间 2021/5/10
 * @描述
 */
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultTreeCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import org.json.*;
import java.io.*;
import java.text.CollationKey;
import java.text.Collator;
import java.util.List;

public class UIclass extends JFrame{

    public JTextField searchText;
    public JTextField lambdaText;
    public JButton boolSearch;
    public JButton vectorSearch;
    public JButton vectorMaxSearch;
    public JButton MLESearch;
    public JButton PossbilitySearch;
    public JSlider jSlider;
    public static JTextArea showArea;
    public static JScrollPane scroll;
    public JRadioButton tf;
    public JRadioButton wf;
    public ButtonGroup bgroup = new ButtonGroup();
    public ArrayList<Character> text;
    private searchMethod sm;
    public TreeMap<Character,Term> getDictionary;
    public dataProcessor data;
    private final TreeMap<Character,Term> terms;
    private final TreeMap<String, Poet> poets;
    private Color backColor;
    private Color contentColor;
    private Color textColor;
    private Color boderColor;
    public UIclass() throws IOException {
        super();
        textColor=new Color(228,223,215);
        contentColor=new Color(113,54,29);
        boderColor=new Color(217,145,86);
        this.setSize(1000, 800);     //窗口大小
        this.getContentPane().setLayout(null);
        this.add(getJTextField(), null);
        this.add(getJButton(), null);
        this.add(getVectorSearch(), null);
        //this.add(getVectorMaxSearch(), null);
        this.add(getMLESearch(),null);
        this.add(getLambdaText(), null);
        this.add(getPossbilityMethod(), null);
        this.add(getScrollPane(),null);
        //this.add(getJTextArea(),null);
        this.setTitle("信息检索");

        this.getContentPane().setBackground(new Color(255,255,255));
        //String path="IRproject/dataset/poet.tang.1000.json";
        String path =".\\dataset\\poet.tang.1000.json";
        dataProcessor.readFile(path);
        //dataProcessor.writeFile();
        poets = dataProcessor.getPoets();
        terms = dataProcessor.getPoetTerm();

    }



    private JTextField getJTextField()
    {
        if(searchText == null) {
            searchText = new javax.swing.JTextField();
            searchText.setBounds(250, 50, 500, 20);
        }
        return searchText;
    }

    private JTextField getLambdaText(){
        if(lambdaText == null) {
            lambdaText = new javax.swing.JTextField();
            lambdaText.setBounds(590, 110, 40, 20);
        }
        return lambdaText;
    }

    public JButton getMLESearch(){
        if(MLESearch == null) {
            MLESearch = new javax.swing.JButton();
            MLESearch.setBounds(590, 80, 140, 20);
            MLESearch.setText("语言模型(MLE)");
        }
        MLESearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showArea.setText("");
                searchMethod s = new searchMethod();
                String text;
                text=searchText.getText();
                String lam = lambdaText.getText();
                Double lambda = lam.isEmpty() ? 0.5 : Double.parseDouble(lam);
                TreeMap<String, Double> result = s.MLEcal(text, lambda);
                List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(result.entrySet());

                list.sort(new Comparator<Map.Entry<String, Double>>() {
                    //升序排序
                    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                        //若为tfidf排序则get(1)，wfidf则为get(2)
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                if(list.isEmpty()){
                    showArea.append("无结果");
                }else{
                    list = list.subList(0, Math.min(list.size(), 20));
                    for(Map.Entry<String,Double> d : list) {
                        Poet t = poets.get(d.getKey());
                        showArea.append(t.getTitle()+"\n"+t.getAuthor()+"\n"+t.getParagraphs()+"\n"+"similarity:"+result.get(d.getKey())+"\n");
                    }
                }
            }
        });
        return MLESearch;

    }
    public JButton getPossbilityMethod()
    {
        if(PossbilitySearch == null) {
            PossbilitySearch = new javax.swing.JButton();
            PossbilitySearch.setBounds(740, 80, 140, 20);
            PossbilitySearch.setText("概率模型");
        }
        PossbilitySearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showArea.setText("");
                searchMethod s = new searchMethod();
                String text;
                text=searchText.getText();

                TreeMap<String, Double> result = s.possbilityMethod(text);
                List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(result.entrySet());

                list.sort(new Comparator<Map.Entry<String, Double>>() {
                    //升序排序
                    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {

                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                if(list.isEmpty()){
                    showArea.append("无结果");
                }else{
                    list = list.subList(0, Math.min(list.size(), 20));
                    for(Map.Entry<String,Double> d : list) {
                        Poet t = poets.get(d.getKey());
                        showArea.append(t.getTitle()+"\n"+t.getAuthor()+"\n"+t.getParagraphs()+"\n"+"weight:"+result.get(d.getKey())+"\n");
                    }
                }
            }
        });
        return PossbilitySearch;
    }


    public JButton getVectorMaxSearch() {
        if(vectorMaxSearch == null) {
            vectorMaxSearch = new javax.swing.JButton();
            vectorMaxSearch.setBounds(590, 80, 140, 20);
            vectorMaxSearch.setText("向量模型(max)");
        }
        vectorMaxSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showArea.setText("");
                searchMethod s = new searchMethod();
                String text;
                text=searchText.getText();
                TreeMap<String, Double> result = s.MLEcal(text,0.5);
                List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(result.entrySet());

                list.sort(new Comparator<Map.Entry<String, Double>>() {
                    //升序排序
                    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                if(list.isEmpty()){
                    showArea.append("无结果");
                }else{
                    list = list.subList(0, Math.min(list.size(), 20));
                    for(Map.Entry<String,Double> d : list) {
                        Poet t = poets.get(d.getKey());
                        showArea.append(t.getTitle()+"\n"+t.getAuthor()+"\n"+t.getParagraphs()+"\n"+"weight:"+result.get(d.getKey())+"\n");
                    }
                }
            }
        });
        return vectorMaxSearch;
    }

    private JButton getVectorSearch(){
        if(vectorSearch == null) {
            vectorSearch = new javax.swing.JButton();
            vectorSearch.setBounds(425, 80, 140, 20);
            vectorSearch.setText("向量模型");
        }
        vectorSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showArea.setText("");
                searchMethod s = new searchMethod();
                String text;
                text=searchText.getText();
                TreeMap<String, Double> result = s.CosinSimilarity(text);
                List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(result.entrySet());

                list.sort(new Comparator<Map.Entry<String, Double>>() {
                    //升序排序
                    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                if(list.isEmpty()){
                    showArea.append("无结果");
                }else{
                    list = list.subList(0, Math.min(list.size(), 20));
                    for(Map.Entry<String,Double> d : list) {
                        Poet t = poets.get(d.getKey());
                        showArea.append(t.getTitle()+"\n"+t.getAuthor()+"\n"+t.getParagraphs()+"\n"+"weight:"+result.get(d.getKey())+"\n");
                    }
                }
            }
        });
        return vectorSearch;
    }

    private JButton getJButton()
    {
        if(boolSearch == null) {
            boolSearch = new javax.swing.JButton();
            boolSearch.setBounds(260, 80, 140, 20);
            boolSearch.setText("布尔模型");
        }
        boolSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showArea.setText("");
                searchMethod s = new searchMethod();
                String text;
                text=searchText.getText();
                TreeMap<String, ArrayList<Double>> result = s.query(text,terms);
                List<Map.Entry<String, ArrayList<Double>>> list = new ArrayList<Map.Entry<String, ArrayList<Double>>>(result.entrySet());

                list.sort(new Comparator<Map.Entry<String, ArrayList<Double>>>() {
                    //升序排序
                    public int compare(Map.Entry<String, ArrayList<Double>> o1, Map.Entry<String, ArrayList<Double>> o2) {
                        //若为tfidf排序则get(1)，wfidf则为get(2)
                        return o2.getValue().get(1).compareTo(o1.getValue().get(1));
                    }
                });
                if(list.isEmpty()){
                    showArea.append("无结果");
                }else{
                    for(Map.Entry<String,ArrayList<Double>> d : list) {
                        Poet t = poets.get(d.getKey());
                        showArea.append(t.getTitle()+"\n"+t.getAuthor()+"\n"+t.getParagraphs()+"\n"+"tfidf:"+result.get(d.getKey()).get(1)+"\n"+"wfidf:"+result.get(d.getKey()).get(2)+"\n");
                    }
                }
            }
        });
        return boolSearch;

    }

    public JRadioButton getTf() {
        if(tf==null) {
            tf = new JRadioButton("tfidf",true);
            tf.setLocation(300,75);
        }
        return tf;
    }

    public JRadioButton getWf() {
        if(wf==null) {
            wf = new JRadioButton("wfidf");
            wf.setLocation(400, 75);
        }
        return wf;
    }

    private JTextArea getJTextArea()
    {
        if(showArea == null)
        {
            showArea =new JTextArea();
            showArea.setBounds(300,100,380,500);
            showArea.setLineWrap(true);
            showArea.setEditable(false);
            showArea.setBackground(contentColor);
            showArea.setForeground(textColor);

        }
        return showArea;
    }
    private JScrollPane getScrollPane()
    {
        if(scroll == null)
        {
            scroll =new JScrollPane();
            scroll.setBounds(0,150,1000,500);
            scroll.setViewportView(getJTextArea());
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setBorder(BorderFactory.createMatteBorder(50,220,50,220,boderColor));
        }
        return scroll;
    }
    public static void main(String[] args) throws IOException {

         UIclass w = new UIclass();
         w.setVisible(true);                         //设为可见
    }
}
